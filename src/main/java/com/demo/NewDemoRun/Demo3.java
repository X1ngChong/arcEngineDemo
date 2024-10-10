package com.demo.NewDemoRun;

import java.util.*;

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
        addNodeData(realData, 95, new String[]{"resident", "1", "university", "1", "college", "1"});
        addNodeData(realData, 96, new String[]{"resident", "3", "mall", "1", "park", "1"});
        addNodeData(realData, 97, new String[]{"resident", "3", "mall", "1", "park", "1"});
        addNodeData(realData, 98, new String[]{"square", "1", "school", "2", "resident", "2"});
        addNodeData(realData, 99, new String[]{"school", "2", "resident", "5"});
        addNodeData(realData, 100, new String[]{"resident", "4", "school", "2"});
        addNodeData(realData, 101, new String[]{"resident", "5", "school", "1", "square", "1"});
        addNodeData(realData, 102, new String[]{"hospital", "1", "school", "1", "park", "1", "resident", "1"});
        addNodeData(realData, 103, new String[]{"resident", "3"});
        addNodeData(realData, 104, new String[]{"school", "2", "resident", "4"});
        addNodeData(realData, 105, new String[]{"resident", "1"});
    }

    private static void initializeSketchData(Map<Integer, NodeData2> sketchData) {
        addNodeData(sketchData, 28, new String[]{"resident", "1", "mall", "1", "park", "1"});
        addNodeData(sketchData, 29, new String[]{"resident", "5", "school", "2"});
        addNodeData(sketchData, 30, new String[]{"resident", "3"});
        addNodeData(sketchData, 31, new String[]{"square", "1", "school", "2", "resident", "2"});
    }

    private static void addNodeData(Map<Integer, NodeData2> data, int nodeId, String[] typeAndCounts) {
        NodeData2 nodeData = new NodeData2();
        for (int i = 0; i < typeAndCounts.length; i += 2) {
            nodeData.addType(typeAndCounts[i], Integer.parseInt(typeAndCounts[i + 1]));
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