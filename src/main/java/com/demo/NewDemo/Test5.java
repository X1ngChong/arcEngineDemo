package com.demo.NewDemo;

import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

/**
 * 为组节点创建next_to关系 添加了双向关系
 */
public class Test5 {

   // public static final String LAYER_NAME = "xianLinTest"; // 设置道路节点所在图层的名称
   public static final String LAYER_NAME = "xianLinRoad"; // 设置道路节点所在图层的名称

    //public final static String Relationship = "CONTAINS";//草图的关系
    public final static String Relationship = "Have";//真实图谱的关系
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
             Session session = driver.session()) {

            // 查找所有的building节点
            List<Integer> fatherIds = fetchFatherIds(session);

            // 检查每一对节点之间的相邻关系
            for (int i = 0; i < fatherIds.size(); i++) {
                for (int j = i + 1; j < fatherIds.size(); j++) {
                    Integer group1 = fatherIds.get(i);
                    Integer group2 = fatherIds.get(j);

                    if (areFatherNextTo(session, group1, group2)) {
                        // 为两个建筑物添加nextTo关系
                        createNextToRelationship(session, group1, group2);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> fetchFatherIds(Session session) {
        String fetchFatherIdQuery = "MATCH p=(start)-[r:"+Relationship+"]->(end)  " +
                "WITH start, COLLECT(p) AS paths  " +
                "RETURN id(start) AS startId, HEAD(paths) AS firstPath  " +
                "LIMIT 25";
        List<Integer> fatherIds = new ArrayList<>();
        Result result = session.run(fetchFatherIdQuery);
        while (result.hasNext()) {
            Record record = result.next();
            fatherIds.add(record.get("startId").asInt());
        }
        return fatherIds;
    }

    private static boolean areFatherNextTo(Session session, Integer group1, Integer group2) {
        String cypherQuery =
                "MATCH (b1), (b2) " +
                        "WHERE id(b1) = $buildingId1 AND id(b2) = $buildingId2 " +
                        "WITH b1, b2, " +
                        "((b1.bbox[0] + b1.bbox[2]) / 2) AS centerX1, " +
                        "((b1.bbox[1] + b1.bbox[3]) / 2) AS centerY1, " +
                        "((b2.bbox[0] + b2.bbox[2]) / 2) AS centerX2, " +
                        "((b2.bbox[1] + b2.bbox[3]) / 2) AS centerY2 " +
                        "WITH 'LINESTRING(' + " +
                        "toString(centerX1) + ' ' + toString(centerY1) + ', ' + " +
                        "toString(centerX2) + ' ' + toString(centerY2) + ')' AS line " +
                        "CALL spatial.intersects('" + LAYER_NAME + "', line) YIELD node " +
                      //  "WHERE 'road' IN labels(node) " +  //计算草图的时候给他放开
                        " RETURN COUNT(node) AS count ";

        Result result = session.run(cypherQuery, parameters("buildingId1", group1, "buildingId2", group2));

        if (result.hasNext()) {
            Record record = result.next();
            int count = record.get("count").asInt();
            // 如果返回的计数等于 1，代表两个区域是相邻的
            return count == 1;
        }

        return false;
    }

    private static void createNextToRelationship(Session session, Integer group1, Integer group2) {
        String createRelationshipQuery =
                "MATCH (b1), (b2) " +
                        "WHERE id(b1) = $buildingId1 AND id(b2) = $buildingId2 " +
                        "MERGE (b1)-[:NEXT_TO]->(b2) " +
                        "MERGE (b2)-[:NEXT_TO]->(b1)";

        session.run(createRelationshipQuery, parameters("buildingId1", group1, "buildingId2", group2));
        System.out.println("创建next_to关系 " + group1 + " and " + group2);
    }
}