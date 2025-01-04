package com.demo.overall.NewDemoRun;

import com.Common.InfoCommon;
import com.demo.overall.NewDemoRun.meetRelation.NodeData;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo1 {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()) {

            // 收集真实数据
            Map<Integer, NodeData> realData = new HashMap<>();
            collectDataFromDb(session, "MATCH p=()-[r:Have]->() RETURN p", realData);
            System.out.println("真实数据: " + realData);

            // 收集草图数据
            Map<Integer, NodeData> sketchData = new HashMap<>();
            collectDataFromDb(session, "MATCH p=()-[r:CONTAINS]->() RETURN p", sketchData);
            System.out.println("草图数据: " + sketchData);

            // 比较数据
            compareData(realData, sketchData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void collectDataFromDb(Session session, String query, Map<Integer, NodeData> dataMap) {
        session.run(query).forEachRemaining(record -> {
            Path path = record.get("p").asPath();
            int parentId = (int) path.start().id();

            if (path.end().containsKey("type")) {
                String type = path.end().get("type").asString();
                addNodeData(dataMap, parentId, type);
            }
        });
    }

    private static void addNodeData(Map<Integer, NodeData> data, int nodeId, String type) {
        data.putIfAbsent(nodeId, new NodeData());
        NodeData nodeData = data.get(nodeId);
        int currentCount = nodeData.typesCount.getOrDefault(type, 0) + 1;
        nodeData.addType(type, currentCount);
    }

    private static void compareData(Map<Integer, NodeData> realData, Map<Integer, NodeData> sketchData) {
        for (Map.Entry<Integer, NodeData> entry : sketchData.entrySet()) {
            int sketchNodeId = entry.getKey();
            NodeData sketchNodeData = entry.getValue();

            List<Map.Entry<Integer, NodeData>> matchingRealEntries = findMatchingRealNodes(realData, sketchNodeData);

            if (!matchingRealEntries.isEmpty()) {
                for (Map.Entry<Integer, NodeData> matchingRealEntry : matchingRealEntries) {
                    int realNodeId = matchingRealEntry.getKey();
                    NodeData realNodeData = matchingRealEntry.getValue();

                    for (Map.Entry<String, Integer> typeEntry : sketchNodeData.typesCount.entrySet()) {
                        String type = typeEntry.getKey();
                        int sketchCount = typeEntry.getValue();
                        int realCount = realNodeData.typesCount.getOrDefault(type, 0);

                        System.out.println("匹配: 草图节点 ID = " + sketchNodeId +
                                ", 真实节点 ID = " + realNodeId +
                                ", 类型 = " + type +
                                ", 草图数量 = " + sketchCount +
                                ", 真实数量 = " + realCount);
                    }
                }
            } else {
                System.out.println("未找到匹配: 草图节点 ID = " + sketchNodeId);
            }
        }
    }

    private static List<Map.Entry<Integer, NodeData>> findMatchingRealNodes(Map<Integer, NodeData> realData, NodeData sketchNodeData) {
        List<Map.Entry<Integer, NodeData>> matchingEntries = new ArrayList<>();
        for (Map.Entry<Integer, NodeData> realEntry : realData.entrySet()) {
            if (isMatching(realEntry.getValue(), sketchNodeData)) {
                matchingEntries.add(realEntry);
            }
        }
        return matchingEntries;
    }

    private static boolean isMatching(NodeData realNodeData, NodeData sketchNodeData) {
        for (Map.Entry<String, Integer> entry : sketchNodeData.typesCount.entrySet()) {
            String type = entry.getKey();
            int sketchCount = entry.getValue();
            if (!realNodeData.typesCount.containsKey(type) || realNodeData.typesCount.get(type) != sketchCount) {
                return false;
            }
        }
        return true;
    }
}