package com.demo.NewDemoRun;

import java.util.*;

/**
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 99, 类型 = school, 草图数量 = 2, 真实数量 = 2
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 99, 类型 = resident, 草图数量 = 5, 真实数量 = 5
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 96, 类型 = resident, 草图数量 = 3, 真实数量 = 3
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 97, 类型 = resident, 草图数量 = 3, 真实数量 = 3
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 103, 类型 = resident, 草图数量 = 3, 真实数量 = 3
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 类型 = square, 草图数量 = 1, 真实数量 = 1
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 类型 = school, 草图数量 = 2, 真实数量 = 2
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 类型 = resident, 草图数量 = 2, 真实数量 = 2
 */
class NodeData2 {
    Map<String, Integer> typesCount = new HashMap<>();

    void addType(String type, int count) {
        typesCount.put(type, count);
    }
}

public class Demo3 {
    public static void main(String[] args) {
        Map<Integer, NodeData2> realData = new HashMap<>();
        Map<Integer, NodeData2> sketchData = new HashMap<>();

        initializeRealData(realData);
        initializeSketchData(sketchData);
        compareData(realData, sketchData);
    }

    private static void initializeRealData(Map<Integer, NodeData2> realData) {
        addNodeData(realData, 95, Arrays.asList("resident", 1, "university", 1, "college", 1));
        addNodeData(realData, 96, Arrays.asList("resident", 3, "mall", 1, "park", 1));
        addNodeData(realData, 97, Arrays.asList("resident", 3, "mall", 1, "park", 1));
        addNodeData(realData, 98, Arrays.asList("square", 1, "school", 2, "resident", 2));
        addNodeData(realData, 99, Arrays.asList("school", 2, "resident", 5));
        addNodeData(realData, 100, Arrays.asList("resident", 4, "school", 2));
        addNodeData(realData, 101, Arrays.asList("resident", 5, "school", 1, "square", 1));
        addNodeData(realData, 102, Arrays.asList("hospital", 1, "school", 1, "park", 1, "resident", 1));
        addNodeData(realData, 103, Arrays.asList("resident", 3));
        addNodeData(realData, 104, Arrays.asList("school", 2, "resident", 4));
        addNodeData(realData, 105, Arrays.asList("resident", 1));
    }

    private static void initializeSketchData(Map<Integer, NodeData2> sketchData) {
        addNodeData(sketchData, 28, Arrays.asList("resident", 1, "mall", 1, "park", 1));
        addNodeData(sketchData, 29, Arrays.asList("resident", 5, "school", 2));
        addNodeData(sketchData, 30, Arrays.asList("resident", 3));
        addNodeData(sketchData, 31, Arrays.asList("square", 1, "school", 2, "resident", 2));
    }

    private static void addNodeData(Map<Integer, NodeData2> data, int nodeId, List<Object> typeAndCounts) {
        NodeData2 nodeData = new NodeData2();
        for (int i = 0; i < typeAndCounts.size(); i += 2) {
            String type = (String) typeAndCounts.get(i);
            int count = (int) typeAndCounts.get(i + 1);
            nodeData.addType(type, count);
        }
        data.put(nodeId, nodeData);
    }

    private static void compareData(Map<Integer, NodeData2> realData, Map<Integer, NodeData2> sketchData) {
        for (Map.Entry<Integer, NodeData2> entry : sketchData.entrySet()) {
            int sketchNodeId = entry.getKey();
            NodeData2 sketchNodeData = entry.getValue();

            List<Map.Entry<Integer, NodeData2>> matchingRealEntries = findMatchingRealNodes(realData, sketchNodeData);

            if (!matchingRealEntries.isEmpty()) {
                for (Map.Entry<Integer, NodeData2> matchingRealEntry : matchingRealEntries) {
                    int realNodeId = matchingRealEntry.getKey();
                    NodeData2 realNodeData = matchingRealEntry.getValue();

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

    private static List<Map.Entry<Integer, NodeData2>> findMatchingRealNodes(Map<Integer, NodeData2> realData, NodeData2 sketchNodeData) {
        List<Map.Entry<Integer, NodeData2>> matchingEntries = new ArrayList<>();
        for (Map.Entry<Integer, NodeData2> realEntry : realData.entrySet()) {
            if (isMatching(realEntry.getValue(), sketchNodeData)) {
                matchingEntries.add(realEntry);
            }
        }
        return matchingEntries;
    }

    private static boolean isMatching(NodeData2 realNodeData, NodeData2 sketchNodeData) {
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