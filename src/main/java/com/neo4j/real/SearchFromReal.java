package com.neo4j.real;

import com.Common.DriverCommon;
import com.Common.PathCommon;
import com.Util.CalculateLocation;

import com.Util.list.ListToMap;
import com.Util.list.ListUtils2;
import com.Util.filter.RoadFilter;
import com.neo4j.sketch.SearchFromSketch;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.neo4j.driver.Values.parameters;

/**
 * 问题草图转换的数据太大
 * 1.解决方法一 取出三个标志性地物然后按数量大小进行倒叙排序然后进行地物的 类型关系
 * @author JXS
 */
@Slf4j
public class SearchFromReal {

    private  Node firstNode;
    private  int all = 0;

    private int place = 0;
    private  ArrayList<String[]> list = new ArrayList<>();//把StringBuilder变成String[]

    private  Map<String[], Double> map = new HashMap<>();//使用这个去计算权值 前面是区域的osm_id集合,后面是权值

    public   ArrayList<String[]> searchDemo(String labelName) {
        SearchFromSketch searchResult = new SearchFromSketch(labelName);
        String[] searches = searchResult.getSearches().toArray(new String[0]);//交替查询可以减少很多输出结果
        String[] positions = searchResult.getPositions().toArray(new String[0]);
        Boolean[] roadRelation = searchResult.getRoadRelation().toArray(new Boolean[0]);
        ArrayList<String> geometryList = searchResult.getGeometryList();

         log.info("查询的地物:{}",Arrays.toString(searches));   //[ pitch, pitch,building]
         log.info("查询的方位:{}",Arrays.toString(positions)); //[159.32594448949595, 210.3595750793219, 186.86210905224578]
        log.info("查询的道路关系:{}",Arrays.toString(roadRelation)); //[false, true, false]

        /**
         * 开始进行从真实草图查询
         */
        try (DriverCommon driverCommon = new DriverCommon();
             Driver driver = driverCommon.getGraphDatabase();
             Session session = driver.session();) {
            try (Transaction tx = session.beginTransaction()) {
                long startTime = System.currentTimeMillis();
                StringBuilder initialResult = new StringBuilder();

                runRecursiveQuery(tx, searches, positions, null, 0,initialResult);

                long endTime = System.currentTimeMillis();
                log.info("程序运行时间：{} 毫秒", (endTime - startTime));
                log.info("原始的结果集长度:{}",list.size());

                /**
                 * 填充权值集合
                 */
                ListToMap.populateMap(list, map);

              //  ListToMap.printMap(map);//Key: ['265948702', '156128805', '155915364'], Value: 1.0


                /**
                 * 如果不进行道路二次查询的话 那么结果集排行第二  如果进行道路二次查询的话就排行第三  原因就是这边的权值变化
                 */
                RoadFilter.roadFilter(driver, roadRelation, list,map);
               // ListToMap.printMap(map);//Key: ['156128802', '475184565', '156127005'], Value: 2.0
                log.info("筛选后的结果集长度:{}",list.size());


                /**
                 * 根据权值去排序然后输出
                 */
                ListUtils2 sorter = new ListUtils2();
                list = sorter.sortAndFilterList(list,map,geometryList);//权值map集合

                // 提交事务,这是筛选
                tx.commit();
            }catch (Exception ex) {
                log.error("数据筛选失败", ex);
            }
            finally {
                // 在这里关闭Driver，即使发生异常也会执行
                try {
                    session.close();
                    driver.close();
                    driverCommon.close();
                } catch (Exception ex) {
                    log.error("neo4j驱动关闭失败", ex);
                }
            }
        }
        catch (Exception ex) {
            log.error("neo4j驱动关闭失败", ex);
        }
        return list;
    }

    /**
     * 执行查询方法
     */
    private  void runRecursiveQuery(Transaction tx, String[] searches, String[] positions, Node beginNode, int currentIndex,StringBuilder currentResult) {
        Result result;
        if (currentIndex==0){
            // 如果是第一次查询走这个方法
            result= runQuery(tx, searches[currentIndex], searches[currentIndex + 1], positions[currentIndex], beginNode);

            while (result.hasNext()) {
                place++;
//                System.out.println(place);
                StringBuilder newResult = new StringBuilder("");
                // 递归调用下一次查询
                Record record = result.next();
                firstNode = record.get("beginNode").asNode();//获取第一次查询结果的beginNode
                newResult.append("'").append(firstNode.get(PathCommon.OSMID).asString()).append("',");//储存第一个节点

                Node endNode = record.get("endNode").asNode();
                newResult.append("'").append(endNode.get(PathCommon.OSMID).asString()).append("',");//储存第二个节点

                CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                        runRecursiveQuery(tx, searches, positions, endNode, currentIndex+1,newResult)//以第二个节点为头进行下一步查询
                );
                future.join();  // 等待

            }
        } else if(currentIndex < searches.length - 2 && currentIndex >=1){
            //大于第一次并且不是最后一次查询
            result= runQuery(tx, null, searches[currentIndex + 1], positions[currentIndex], beginNode);
            while (result.hasNext()) {
                // 递归调用下一次查询
                Record record = result.next();
                Node endNode = record.get("endNode").asNode();
                if(!currentResult.toString().contains(endNode.get(PathCommon.OSMID).asString())){//如果当前的结果集中没有这个数据才执行第二次查询,如果有就跳到下一个节点
                    /**
                     * 将newResult加入异步,避免newResult的竞争态!!!!
                     */
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
                                StringBuilder newResult = new StringBuilder(currentResult);
                                newResult.append("'").append(endNode.get(PathCommon.OSMID).asString()).append("',");
                                runRecursiveQuery(tx, searches, positions, endNode, currentIndex + 1, newResult);
                            }
                    );
                    future.join();  // Wait for the completion of the asynchronous task
                }
            }
        } else {
            // 最后一次查询，直接执行
            result = runQuery(tx, null, searches[searches.length-1], positions[positions.length-1], beginNode);
            while (result.hasNext()) {
                List<Object> beginBbox = firstNode.get("bbox").asList();
                Record record = result.next();
                Node endNode = record.get("endNode").asNode();
                if(!currentResult.toString().contains(endNode.get(PathCommon.OSMID).asString())){ //排除为空和重复的ID
                    //把后面的positions中存放的应该变成纯数字
                    if (CalculateLocation.compareAndCalculate(CalculateLocation.GetDirectionNew(beginBbox, endNode.get("bbox").asList()),positions[positions.length-1])) {
                        int length = currentResult.length();
                        currentResult.append("'").append(endNode.get(PathCommon.OSMID).asString()).append("'");
//                        if(currentResult.toString().contains("265949063")){//看是否可以查到
//                            System.out.println(currentResult.toString());
//                        }
                        String[] resultArray = currentResult.toString().split(",");
                        list.add(resultArray);
//                    System.out.println(list.get(0)[0]);
                        all++;
                        currentResult.delete(length,currentResult.length());  // 移除最后一个节点
                    }
                }

            }
        }
    }

    /**
     * 构建查询语句
     */
    private  Result runQuery(Transaction tx, String search, String search2, String position, Node beginNode) {
        StringBuilder cypherQuery = new StringBuilder();
        if(search == null){
            cypherQuery.append("MATCH (beginNode :"+PathCommon.caoTuLabel+")");
        }else{
            cypherQuery.append("MATCH (beginNode :"+PathCommon.caoTuLabel+" {"+PathCommon.TypeName+": '" + search + "'})");
        }


        if (search2 != null) {
            cypherQuery.append("-[r:NEAR]->(endNode {"+PathCommon.TypeName+": '" + search2 + "'})");
        }


//        if(beginNode != null){
//            //cypherQuery.append(" WHERE ID(beginNode) = $beginNodeId AND r.location CONTAINS '"+position+"' ");//判断方位
//            cypherQuery.append(" WHERE ID(beginNode) = $beginNodeId AND ").append(Double.parseDouble(position) - PathCommon.ANGLE_RANGE).append(" < toFloat(r.location) < ").append(Double.parseDouble(position) + PathCommon.ANGLE_RANGE).append(" ");//修改后的方位
//           // System.out.println(cypherQuery);
//        }else{
//            cypherQuery.append(" WHERE ").append(Double.parseDouble(position) - PathCommon.ANGLE_RANGE).append(" < toFloat(r.location) < ").append(Double.parseDouble(position) + PathCommon.ANGLE_RANGE).append(" ");//判断方位
//           // System.out.println(cypherQuery);
//        }

        cypherQuery.append(" RETURN ");

        cypherQuery.append(search != null ? "beginNode,endNode" : "endNode");

        Result result;

       // System.out.println(cypherQuery);

//        if (beginNode != null) {
//            System.out.println(beginNode.id());
//        }

        if(beginNode != null){
            result = tx.run(cypherQuery.toString(), parameters("beginNodeId",beginNode.id()));
        }else{
            result = tx.run(cypherQuery.toString());
        }

        return result;
    }


}
