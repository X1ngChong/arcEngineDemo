package com.demo.NewDemo;

import com.Common.InfoCommon;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

/**
 * 添加草图的roadIDs
 */
public class Test3 {

    public static final String LAYER_NAME = "xianLinTest"; //设置道路节点所在图层的名称
    public static List<Integer> roadIDs = new ArrayList<>(); // 初始化roadIDs

    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()) {

            // 查找所有的building节点
            List<Integer> buildingIds = fetchBuildingIds(session);

            // 地图用于跟踪每栋建筑所属的组
            Map<Integer, Integer> buildingGroupMap = new HashMap<>(); // <Building ID, Group ID>

            for (Integer buildingId : buildingIds) {
                // 跳过已经分组的建筑
                if (buildingGroupMap.containsKey(buildingId)) continue;

                // 如果没有组 那么创建新的组
                Integer groupId = createGroup(session);
                buildingGroupMap.put(buildingId, groupId); // 记录组之间的关系

                //将非道路交叉的建筑与该组关联
                associateBuildingsToGroup(session, buildingIds, buildingGroupMap, groupId, buildingId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> fetchBuildingIds(Session session) {
        String fetchBuildingsQuery = "MATCH (b:building) WHERE b.ID IS NOT NULL RETURN b.ID AS id";
        List<Integer> buildingIds = new ArrayList<>();
        Result result = session.run(fetchBuildingsQuery);
        while (result.hasNext()) {
            Record record = result.next();
            buildingIds.add(record.get("id").asInt());
        }
        return buildingIds;
    }

    private static boolean areBuildingsConnected(Session session, Integer buildingId1, Integer buildingId2) {
        String cypherQuery =
                "MATCH (b1:building {ID: $buildingId1}), (b2:building {ID: $buildingId2}) " +
                        "WHERE b1.ID <> b2.ID " +
                        "WITH b1, b2, " +
                        "((b1.bbox[0] + b1.bbox[2]) / 2) AS centerX1, " +
                        "((b1.bbox[1] + b1.bbox[3]) / 2) AS centerY1, " +
                        "((b2.bbox[0] + b2.bbox[2]) / 2) AS centerX2, " +
                        "((b2.bbox[1] + b2.bbox[3]) / 2) AS centerY2 " +
                        "WITH 'LINESTRING(' + " +
                        "toString(centerX1) + ' ' + toString(centerY1) + ', ' + " +
                        "toString(centerX2) + ' ' + toString(centerY2) + ')' AS line " +
                        "CALL spatial.intersects('" + LAYER_NAME + "', line) YIELD node " +
                        "WHERE 'road' IN labels(node)  " +
                        "RETURN id(node) as ID";

        System.out.println(cypherQuery);
        Result result = session.run(cypherQuery, parameters("buildingId1", buildingId1, "buildingId2", buildingId2));
        boolean hasNode = !result.hasNext();

        /**
         * 添加道路ID  roadIds: 24,24,24,24,24,22,22,24,22,24,22,24,22,22,24,22,24,22,24,24,22,24,22,22,22,22,24,22,22,22 下次添加只添加为拥有的道路
         */
            while (result.hasNext()) {
                Record record = result.next();
                System.out.println(record.get("ID").asInt());
                roadIDs.add(record.get("ID").asInt());

            }

        return hasNode; // 如果建筑之间没有道路交叉，则为真
    }

    private static Integer createGroup(Session session) {
        String createGroupQuery = "CREATE (g:Group) RETURN id(g) AS id";
        Record groupRecord = session.run(createGroupQuery).single();
        return groupRecord.get("id").asInt();
    }

    private static void associateBuildingsToGroup(Session session, List<Integer> buildingIds,
                                                  Map<Integer, Integer> buildingGroupMap, Integer groupId, Integer startingBuildingId) {
        // 添加组和节点之间的关系
        String addNodeToGroupQuery =
                "MATCH (g:Group), (b:building) WHERE id(g) = $groupNodeId AND b.ID = $buildingId " +
                        "CREATE (g)-[:CONTAINS]->(b)";

        // 将起始建筑添加到组中
        session.run(addNodeToGroupQuery, parameters("groupNodeId", groupId, "buildingId", startingBuildingId));

        // 如果连接，则将其他建筑分配到同一组
        for (Integer otherBuildingId : buildingIds) {
            if (!otherBuildingId.equals(startingBuildingId) && !buildingGroupMap.containsKey(otherBuildingId)) {
                if (areBuildingsConnected(session, startingBuildingId, otherBuildingId)) {
                    session.run(addNodeToGroupQuery, parameters("groupNodeId", groupId, "buildingId", otherBuildingId));
                    buildingGroupMap.put(otherBuildingId, groupId);
                    System.out.println("Building ID: " + otherBuildingId + " 添加到 Group的ID: " + groupId);
                    // 添加连接的道路ID到 Group 节点
                    addRoadIdsToGroup(session, groupId);
                }
            }
        }
    }

    private static void addRoadIdsToGroup(Session session, Integer groupId) {
        if (roadIDs != null && !roadIDs.isEmpty()) {
            String addRoadIdsQuery = "MATCH (g:Group) WHERE id(g) = $groupNodeId " +
                    "SET g.roadIds = "+roadIDs;

            // 将道路ID作为参数传递
            session.run(addRoadIdsQuery, parameters("groupNodeId", groupId));
            roadIDs.clear();
        }
    }
}