package com.neo4j;



import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

/**
 * 查询四个地物 的demo (能够准确查询并且方向正确)
 */
public class SearchDemo4 {
    public static void main(String[] args) {
        searchDemo();
    }
    public static ArrayList<String[]> searchDemo(){
        ArrayList<String[]> list = new ArrayList<>();
        String search1 = "pitch";
        String search2 = "pitch";
        String search3 = "pitch";
        String search4 = "building";
        String position1 = "北";//下一个在当前的哪个方位
        String position2 = "东";//
        String position3 = "北";//查找不到距离给小了
        String lastposition = "北";//
        try (DriverCommon driverCommon = new DriverCommon()) {
            int all =0;
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    String cypherQuery = "MATCH (n {fclass: '"+search1+"'})-[r:NEAR]->(otherNode {fclass: '"+search2+"'}) return n ,otherNode,r.location AS location";
                    Result result = tx.run(cypherQuery);
                    while (result.hasNext()) {
                        Record record = result.next();

                        //第一个节点相关数据
                        Node firstnode = record.get("n").asNode();
                        List<Object> firstBbox =  firstnode.get("bbox").asList();//获取bbox

                        //第一个结果的关系
                        String  location = record.get("location").asString();

                        //第二个节点
                        Node secondNode = record.get("otherNode").asNode();

                        //判断方位是否符合
                            if(location.contains(position1)){
                                // 构建第二个查询，基于已找到的节点 node2
                                String secondQuery = "MATCH (beginNode)-[r:NEAR]->(endNode {fclass: '"+search3+"'}) WHERE ID(beginNode) = $beginNodeId  RETURN endNode,r.location AS location";
                                Result secondResult = tx.run(secondQuery,parameters("beginNodeId",secondNode.id()));
                                while (secondResult.hasNext()) {
                                    Record secondRecord = secondResult.next();
                                    Node thirdNode = secondRecord.get("endNode").asNode();

                                   String location2 =  secondRecord.get("location").asString();

                                    if (location2.contains(position2) ){
                                        // 构建第三个查询，基于已找到的节点
                                        String thirdQuery = "MATCH (beginNode)-[r:NEAR]->(endNode {fclass: '"+search4+"'}) WHERE ID(beginNode) = $beginNodeId  RETURN endNode,r.location AS location";
                                        Result thirdResult = tx.run(thirdQuery,parameters("beginNodeId",thirdNode.id()));
                                        while (thirdResult.hasNext()) {

                                            Record thirdRecord = thirdResult.next();
                                            Node lastNode = thirdRecord.get("endNode").asNode();
                                            List<Object> lastBbox =  lastNode.get("bbox").asList();;//获取bbox
                                            String location3 =thirdRecord.get("location").asString();
                                            if (location3.contains(position3)){
                                        //最后一步筛选结果 构建最后一个和第一个的方位
                                        if(CalculateLocation.GetDirection(firstBbox,lastBbox).contains(lastposition)){
                                            //TODO 数据'"8704627"'
                                            String [] osmIds = {"'"+firstnode.get("osm_id").asString()+"'","'"+secondNode.get("osm_id").asString()+"'","'"+thirdNode.get("osm_id").asString()+"'","'"+lastNode.get("osm_id").asString()+"'"};
                                            list.add(osmIds);
                                            System.out.println(firstnode.get("name")+location+" "+secondNode.get("name")+location2+" "+thirdNode.get("name")+location3+" "+lastNode.get("name"));
                                            System.out.println(osmIds[0]);
                                            all++;
                                               }
                                            }
                                        }
                                }
                            }
                        }
                }
                    System.out.println(all);
                    // 提交事务
                    tx.commit();
            }
            driver.close();
        }
            return list;
    }
}

}
