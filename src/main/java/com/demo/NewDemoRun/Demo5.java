package com.demo.NewDemoRun;

import com.Bean.RealNodeInfo;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Path;

import java.util.*;



/**
 * 计算Group的相似度
 */
public class Demo5 {
    private static final double OMEGA1 = 0.5;
    private static final double OMEGA2 = 0.5;
    private static final double SIM_VALUE = 0.3;


    public static void main(String[] args) {
        Demo5 d = new Demo5();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = d.firstFilter();
        // 输出结果
        sketchToRealMap.forEach((sketchId, realNodeInfos) -> {
                    System.out.println("草图节点 ID: " + sketchId + ", 真实节点 ID: " + realNodeInfos);
        });
       // System.out.println("草图节点与真实节点的映射: " + d.firstFilter());
    }
    public Map<Integer, List<RealNodeInfo>> firstFilter() {
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = new HashMap<>();
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
             Session session = driver.session()) {

            Map<Integer, NodeData> realData = new HashMap<>();
            collectDataFromDb(session, "MATCH p=()-[r:Have]->() RETURN p", realData);

            Map<Integer, NodeData> sketchData = new HashMap<>();
            collectDataFromDb(session, "MATCH p=()-[r:CONTAINS]->() RETURN p", sketchData);

            compareData(realData, sketchData, sketchToRealMap);

            // 对每个草图ID的真实节点进行排序
            for (List<RealNodeInfo> realNodeInfos : sketchToRealMap.values()) {
                realNodeInfos.sort(new Comparator<RealNodeInfo>() {
                    @Override
                    public int compare(RealNodeInfo o1, RealNodeInfo o2) {
                        // 按照相似度从大到小排序
                        return Double.compare(o2.similarity1, o1.similarity1);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sketchToRealMap;
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

    private static void compareData(Map<Integer, NodeData> realData, Map<Integer, NodeData> sketchData, Map<Integer, List<RealNodeInfo>> sketchToRealMap) {
        for (Map.Entry<Integer, NodeData> sketchEntry : sketchData.entrySet()) {
            int sketchNodeId = sketchEntry.getKey();
            NodeData sketchNodeData = sketchEntry.getValue();
            boolean matched = false;

            List<RealNodeInfo> realNodeInfos = new ArrayList<>();

            for (Map.Entry<Integer, NodeData> realEntry : realData.entrySet()) {
                int realNodeId = realEntry.getKey();
                NodeData realNodeData = realEntry.getValue();

                double sim = calculateSimilarity(sketchNodeData, realNodeData);

                if (sim > SIM_VALUE) {
//                    System.out.println("匹配: 草图节点 ID = " + sketchNodeId +
//                            ", 真实节点 ID = " + realNodeId +
//                            ", 相似度 = " + sim);
                    realNodeInfos.add(new RealNodeInfo(realNodeId, sim)); // 添加匹配的真实节点信息
                    matched = true; // 标记已匹配
                }
            }
            if (matched) {
                sketchToRealMap.put(sketchNodeId, realNodeInfos); // 存储草图节点 ID 和对应的真实节点信息列表
            } else {
                System.out.println("未找到匹配: 草图节点 ID = " + sketchNodeId);
            }
        }
    }

    private static double calculateSimilarity(NodeData sketchNodeData, NodeData realNodeData) {
        int NS = sketchNodeData.getTotalCount();
        int N0 = realNodeData.getTotalCount();
        int NT = getSameTypeCount(sketchNodeData, realNodeData);

        double sim1 = OMEGA1 * (Math.min(NS, N0) / (double) Math.max(NS, N0)) +
                OMEGA2 * (NT / (double) Math.min(NS, N0));
        return sim1;
    }

    private static int getSameTypeCount(NodeData sketchNodeData, NodeData realNodeData) {
        int count = 0;
        for (String type : sketchNodeData.typesCount.keySet()) {
            count += Math.min(sketchNodeData.typesCount.get(type), realNodeData.typesCount.getOrDefault(type, 0));
        }
        return count;
    }
}