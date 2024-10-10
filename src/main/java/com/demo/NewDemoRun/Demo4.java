package com.demo.NewDemoRun;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 真实数据: {96=NodeData{typesCount={mall=1, park=1, resident=3}}, 97=NodeData{typesCount={mall=1, park=1, resident=3}}, 98=NodeData{typesCount={square=1, school=2, resident=2}}, 99=NodeData{typesCount={school=2, resident=4}}, 100=NodeData{typesCount={school=2, resident=4}}, 101=NodeData{typesCount={square=1, school=1, resident=5}}, 102=NodeData{typesCount={school=1, hospital=1, park=1, resident=1}}, 103=NodeData{typesCount={resident=3}}, 104=NodeData{typesCount={school=2, resident=4}}, 105=NodeData{typesCount={resident=1}}, 95=NodeData{typesCount={college=1, university=1, resident=1}}}
 * 草图数据: {28=NodeData{typesCount={mall=1, park=1, resident=1}}, 29=NodeData{typesCount={school=2, resident=4}}, 30=NodeData{typesCount={resident=3}}, 31=NodeData{typesCount={square=1, school=2, resident=2}}}
 * 匹配: 草图节点 ID = 28, 真实节点 ID = 96, 草图总数量 = 3, 真实总数量 = 5
 * 匹配: 草图节点 ID = 28, 真实节点 ID = 97, 草图总数量 = 3, 真实总数量 = 5
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 99, 草图总数量 = 6, 真实总数量 = 6
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 100, 草图总数量 = 6, 真实总数量 = 6
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 104, 草图总数量 = 6, 真实总数量 = 6
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 96, 草图总数量 = 3, 真实总数量 = 5
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 97, 草图总数量 = 3, 真实总数量 = 5
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 99, 草图总数量 = 3, 真实总数量 = 6
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 100, 草图总数量 = 3, 真实总数量 = 6
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 101, 草图总数量 = 3, 真实总数量 = 7
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 103, 草图总数量 = 3, 真实总数量 = 3
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 104, 草图总数量 = 3, 真实总数量 = 6
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 草图总数量 = 5, 真实总数量 = 5
 *
 */
public class Demo4 {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
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
        for (Map.Entry<Integer, NodeData> sketchEntry : sketchData.entrySet()) {
            int sketchNodeId = sketchEntry.getKey();
            NodeData sketchNodeData = sketchEntry.getValue();
            boolean matched = false;

            for (Map.Entry<Integer, NodeData> realEntry : realData.entrySet()) {
                int realNodeId = realEntry.getKey();
                NodeData realNodeData = realEntry.getValue();

                // 检查草图节点所有类型的数量是否都小于等于对应的真实节点数量
                if (isLessThanOrEqual(sketchNodeData, realNodeData)) {
                    System.out.println("匹配: 草图节点 ID = " + sketchNodeId +
                            ", 真实节点 ID = " + realNodeId +
                            ", 草图总数量 = " + sketchNodeData.getTotalCount() +
                            ", 真实总数量 = " + realNodeData.getTotalCount());
                    matched = true; // 标记已匹配
                }
            }
            if (!matched) {
                System.out.println("未找到匹配: 草图节点 ID = " + sketchNodeId);
            }
        }
    }

    private static boolean isLessThanOrEqual(NodeData sketchNodeData, NodeData realNodeData) {
        for (Map.Entry<String, Integer> entry : sketchNodeData.typesCount.entrySet()) {
            String type = entry.getKey();
            int sketchCount = entry.getValue();
            int realCount = realNodeData.typesCount.getOrDefault(type, 0);

            // 如果草图节点的数量大于真实节点的数量，则不符合条件
            if (sketchCount > realCount) {
                return false;
            }
        }
        return true; // 所有类型检查通过
    }
}