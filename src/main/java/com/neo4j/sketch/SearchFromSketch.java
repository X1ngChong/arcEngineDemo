package com.neo4j.sketch;

import com.Bean.Line;
import com.Bean.Point;
import com.Common.DriverCommon;
import com.Common.PathCommon;
import com.Util.CalIntersect;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 将草图转化为标志性地物
 * @author JXS
 */
public class SearchFromSketch {
    private final Integer number = 3;//设置初始的筛选地物数量

    private ArrayList<Boolean> roadRelation = new ArrayList<>();//[false, true, false] 地物之间是否有道路通过 最后一个默认是false

    private  ArrayList<String> searches = new ArrayList<>();//地物类型

    private  ArrayList<List> bbox = new ArrayList<>();//searches的地物坐标

    private  ArrayList<String> positions = new ArrayList<>();//方位关系 修改为数字关系

    private  ArrayList<List> roadBox = new ArrayList<>();//道路坐标

    private List<String> landmarkTypes = new ArrayList<>(); // 标志性地物类型
    private Map<String, Integer> landmarkTypeCount = new HashMap<>(); // 标志性地物类型及其数量

    private  ArrayList<String> geometryList = new ArrayList<>();//geometry数组



    public SearchFromSketch(String labelName) {
        searchDemo(labelName);
    }

    public void searchDemo(String labelName) {
        try (DriverCommon driverCommon = new DriverCommon();
             Driver driver = driverCommon.getGraphDatabase()) {
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {

                    // 查询三个最常见的标志性地物类型
                    searchThreePoint(tx,labelName);

                    // 完成方位关系的查找   会重复查找后续优化可以修改
                    findDirectionRelationships(tx,labelName,bbox,searches,number);

                    // 完成草图 俩地物之间是否有道路  未做缓存 后续可以优化   不能使用简单的bbox进行判断 已经更换判断   问题不处在这 应该在实际图谱中查询的时候那俩个地物直接有道路的
                    processRoads(tx, labelName, bbox,roadBox,roadRelation);

                    //查询草图的geometry数组
                    searchGeometryByLandmarkTypeCount(tx,landmarkTypeCount,labelName,geometryList);


                    // 提交事务
                    tx.commit();
                }
                finally {
                    session.close();
                    driver.close();
                    driverCommon.close();
                }
            }
        }
    }

    /**
     * 将{pitch=2, building=11} 变成 [pitch, pitch, building]
     * @param landmarkTypeCount
     * @return
     */
    private  ArrayList<String> convertMapToList(Map<String, Integer> landmarkTypeCount) {
        ArrayList<String> repeatedLandmarks = new ArrayList<>();
        // 遍历映射，将每种地物类型按照数量添加到列表中
        int temp = number;
        for (Map.Entry<String, Integer> entry : landmarkTypeCount.entrySet()) {
            String landmark = entry.getKey();
//             int count = entry.getValue();
//            for (int i = 0; i < count; i++) {
//                if(temp > 0){
//                    repeatedLandmarks.add(landmark);
//                    temp--;
//                }
//            }
                if(temp > 0){
                    repeatedLandmarks.add(landmark);//每个类型选取一个
                    temp--;
            }
        }
        return repeatedLandmarks;
    }

    /**
     *  // 查询三个最常见的标志性地物类型
     * @param tx
     * @param labelName
     */
    public void searchThreePoint(Transaction tx,String labelName){
        // 查询三个最常见的标志性地物类型
        String cypherQuery = "MATCH (n:" + labelName + ") WHERE n."+ PathCommon.TypeName +" <> 'null' RETURN n."+ PathCommon.TypeName +" AS type, COUNT(n) AS count ORDER BY count desc ";
        Result result = tx.run(cypherQuery);//building
        List<Record> landmarkRecords = result.list();
        int temp = number;
        // 将标志性地物类型及其数量还有geometry 存储到地物类型关系的Map中
        for (Record record : landmarkRecords) {
            String type = record.get("type").asString();
//            int count = record.get("count").asInt();

            if(temp > 0){
                searches.add(type);//每个类型选取一个
                temp--;
            }

          //  System.out.println(type);
//            landmarkTypes.add(type);
//            landmarkTypeCount.put(type, count);
        }

//        //不为空代表里面有数据
//        if(landmarkTypeCount != null) {
//            searches = convertMapToList(landmarkTypeCount);
//        }  //完成searches数组的查找[pitch, pitch, building]

    }
    /**
     * 完成方位关系的查找 限制只选择三个标志性地物
     * @param tx
     * @param labelName
     * @param searches
     * @param number
     * @return
     */
    public void findDirectionRelationships(Transaction tx, String labelName, ArrayList<List> bbox ,List<String> searches, int number) {
       // System.out.println(searches.toString());
                int size = searches.size();
            String cypherQuery2 = "MATCH p=(m:"+labelName+"{"+PathCommon.TypeName+":\""+searches.get(size-3)+"\"})-[r:NEAR]->(n:"+labelName+"{"+PathCommon.TypeName+":\""+searches.get(size-2)+"\"})-[z:NEAR]->(k:"+labelName+"{"+PathCommon.TypeName+":\""+searches.get(size-1)+"\"}) RETURN m.bbox as fbbox,n.bbox as sbbox,k.bbox as lbbox LIMIT 25";//换成具有near关系的节点的
            Result result2 = tx.run(cypherQuery2);
            while (result2.hasNext()  && bbox.size() <number) {
                Record record = result2.next();
                List<Object> box1 = record.get("fbbox").asList();
                List<Object> box2 = record.get("sbbox").asList();
                List<Object> box3 = record.get("lbbox").asList();
                bbox.add(box1);
                bbox.add(box2);
                bbox.add(box3);
            }


        for(int i = 0 ; i< searches.size() ; i++){
            if (i+1 < searches.size() ){
                /**
                 * 这里可以优化 TODO:就是直接从关系中获取 r.location 然后直接添加
                 */
                positions.add(CalculateLocation.getDirection2(bbox.get(i),bbox.get(i+1)));//正常比较
            }else{
                positions.add(CalculateLocation.getDirection2(bbox.get(0),bbox.get(searches.size()-1)));//第一个和最后一个比较
            }
        }
    }

    /**
     * 俩地物之间是否有道路
     * @param tx
     * @param labelName
     * @param bbox
     * @return
     */
    public void processRoads(Transaction tx, String labelName, ArrayList<List> bbox, ArrayList<List> roadBox , ArrayList<Boolean> roadRelation) {
        //先获取所有道路数据
        String roadLabel = "road";
        //String cypherQuery3 = "MATCH (n:" + labelName + ") WHERE n.Type = 'road' RETURN n.bbox as box ";
        String cypherQuery3 = "MATCH (n:" + roadLabel + ") WHERE n.type is not null RETURN n.bbox as box ";//测试wandamao的道路

        Result result3 = tx.run(cypherQuery3);
        while (result3.hasNext()) {
            Record record3 = result3.next();
            List box1 = record3.get("box").asList();
            roadBox.add(box1);//所有道路坐标的获取
        }
        //System.out.println(roadBox.size());

        //循环判断 是否有道路通过 如果有就下一个 如果没有就一直循环
        for (int i = 0; i < bbox.size(); i++) {
            Point p1, p2;
            Line line1;
            if (i >= bbox.size() - 1) {
                roadRelation.add(false);//最后一次默认false
                break;
            }

            //这是俩点的线段
            p1 = new Point((Double) bbox.get(i).get(0),(Double) bbox.get(i).get(1));
            p2 = new Point((Double) bbox.get(i+1).get(0),(Double) bbox.get(i+1).get(1));

            line1 = new Line(p1, p2); // 地物之间的线段

            boolean isIntersecting = false; // 标记当前地物是否与任何道路相交
            for (int k = 0; k < roadBox.size(); k++) {
                List road = roadBox.get(k);
                Point r1 = new Point((Double) road.get(0), (Double) road.get(1)); // 修正Point对象的创建
                Point r2 = new Point((Double) road.get(2), (Double) road.get(3)); // 修正Point对象的创建
                Line line2 = new Line(r1, r2);

                isIntersecting = CalIntersect.doLinesIntersect(line1, line2);
                if (isIntersecting) {
                    roadRelation.add(true);
                    break; // 如果有交点，跳出内层循环
                }
            }
            // 检查完所有道路后，如果还没有交点，则添加false
            if (!isIntersecting) {
                roadRelation.add(false);
            }
        }
    }

    /**
     * 根据landmarkTypeCount查询对应的geometry
     * @return
     */
    public void searchGeometryByLandmarkTypeCount(Transaction tx,Map<String, Integer> landmarkTypeCount , String labelName, ArrayList<String> geometryList){

            for (Map.Entry<String, Integer> entry : landmarkTypeCount.entrySet()) {
                String landmarkType = entry.getKey();
                int count = entry.getValue();

                String cypherQuery = "MATCH (n:" + labelName + ") WHERE n.Type = '" + landmarkType + "' RETURN n.geometry as geometry LIMIT " + count;
                Result result = tx.run(cypherQuery);
                while (result.hasNext()) {
                    Record record = result.next();
                    String geometry = record.get("geometry").asString();
                    // 处理返回的几何信息，可以根据需要进行进一步操作
                    if (geometryList.size()<3){
                        geometryList.add(geometry);
                    }else {
                        return;
                    }

                }

        }

    }

    public ArrayList<String> getSearches() {
        return searches;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }


    public ArrayList<Boolean> getRoadRelation() {
        return roadRelation;
    }

    public ArrayList<String> getGeometryList() {
        return geometryList;
    }

}
