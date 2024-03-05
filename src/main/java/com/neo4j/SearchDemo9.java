package com.neo4j;

import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import com.neo4j.caotu.SearchCaoTuDemo;
import com.neo4j.caotu.SearchCaoTuDemo1;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.neo4j.driver.Values.parameters;

/**
 * 问题草图转换的数据太大
 * 1.解决方法一 取出三个标志性地物然后按数量大小进行倒叙排序然后进行地物的 类型关系
 * @author JXS
 */
public class SearchDemo9 {
    private    Node firstNode;
    private  int all = 0;
    private  int place = 0;

    private  ArrayList<String[]> list = new ArrayList<>();//把StringBuilder变成String[]

    public static void main(String[] args) {
        SearchDemo9 searchDemo9 = new SearchDemo9();
        searchDemo9.searchDemo("chenhui");
    }
    public   ArrayList<String[]> searchDemo(String labelName) {
        SearchCaoTuDemo1 searchCaoTuDemo1 = new SearchCaoTuDemo1(labelName);
        String[] searches = searchCaoTuDemo1.getSearches().toArray(new String[0]);//交替查询可以减少很多输出结果
        String[] positions =searchCaoTuDemo1.getPositions().toArray(new String[0]);

        System.out.println(Arrays.toString(searches));
        System.out.println(Arrays.toString(positions));


        try (DriverCommon driverCommon = new DriverCommon()) {

            Driver driver = driverCommon.getGraphDatabase();

            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    long startTime = System.currentTimeMillis();
                    StringBuilder initialResult = new StringBuilder();
                    runRecursiveQuery(tx, searches, positions, null, 0,initialResult);
                    System.out.println(all);
                    long endTime = System.currentTimeMillis();
                    System.out.println("程序运行时间：" + (endTime - startTime) + "毫秒");
                    // 提交事务
                    tx.commit();
                }
                driver.close();
            }
        }
        return list;
    }

    /**
     * 执行查询方法
     * @param tx
     * @param searches
     * @param positions
     * @param beginNode
     * @param currentIndex
     * @param
     * @return
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
                newResult.append("'").append(firstNode.get("osm_id").asString()).append("',");//储存第一个节点

                Node endNode = record.get("endNode").asNode();
                newResult.append("'").append(endNode.get("osm_id").asString()).append("',");//储存第二个节点

                CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                        runRecursiveQuery(tx, searches, positions, endNode, currentIndex+1,newResult)//以第二个节点为头进行下一步查询
                );
                future.join();  // Wait for the completion of the asynchronous task

            }
        } else if(currentIndex < searches.length - 2 && currentIndex >=1){
            //大于第一次并且不是最后一次查询
            result= runQuery(tx, null, searches[currentIndex + 1], positions[currentIndex], beginNode);
            while (result.hasNext()) {
                // 递归调用下一次查询
                Record record = result.next();
                Node endNode = record.get("endNode").asNode();
                if(!currentResult.toString().contains(endNode.get("osm_id").asString())){//如果当前的结果集中没有这个数据才执行第二次查询,如果有就跳到下一个节点
                    /**
                     * 将newResult加入异步,避免newResult的竞争态!!!!
                     */
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
                                StringBuilder newResult = new StringBuilder(currentResult);
                                newResult.append("'").append(endNode.get("osm_id").asString()).append("',");
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
                if(!currentResult.toString().contains(endNode.get("osm_id").asString())){
                    if (CalculateLocation.GetDirection(beginBbox, endNode.get("bbox").asList()).contains(positions[positions.length-1])) {
                        int length = currentResult.length();
                        currentResult.append("'").append(endNode.get("osm_id").asString()).append("'");
                        if(currentResult.toString().contains("265949063")){//看是否可以查到
                            System.out.println(currentResult.toString());
                        }
                        String[] resultArray = currentResult.toString().split(",");
                        list.add(resultArray);
//                    System.out.println(list.get(0)[0]);
                        all++;
                        currentResult.delete(length,currentResult.length());  // Remove the last added node
                    }
                }

            }
        }
    }

    /**
     * 构建查询语句
     * @param tx
     * @param search
     * @param search2
     * @param position
     * @param beginNode
     * @return
     */
    private  Result runQuery(Transaction tx, String search, String search2, String position, Node beginNode) {
        StringBuilder cypherQuery = new StringBuilder();
        if(search == null){
            cypherQuery.append("MATCH (beginNode)");
        }else{
            cypherQuery.append("MATCH (beginNode {fclass: '" + search + "'})");
        }


        if (search2 != null) {
            cypherQuery.append("-[r:NEAR]->(endNode {fclass: '" + search2 + "'})");
        }


        if(beginNode != null){
            cypherQuery.append(" WHERE ID(beginNode) = $beginNodeId AND r.location CONTAINS '"+position+"' ");//判断方位
        }else{
            cypherQuery.append(" WHERE  r.location CONTAINS '"+position+"' ");//判断方位
        }

        cypherQuery.append(" RETURN ");

        cypherQuery.append(search != null ? "beginNode,endNode" : "endNode");

        Result result;

        if(beginNode != null){
            result = tx.run(cypherQuery.toString(), parameters("beginNodeId",beginNode.id()));
        }else{
            result = tx.run(cypherQuery.toString());
        }

//        System.out.println(cypherQuery);
        return result;
    }
}
