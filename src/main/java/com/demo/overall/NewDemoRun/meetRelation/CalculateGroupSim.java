package com.demo.overall.NewDemoRun.meetRelation;

import com.Bean.RealNodeInfo;
import com.Common.DriverCommon;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Path;

import java.util.*;



/**
 * 计算Group的相似度
 * 草图节点 ID: 112, 真实节点 ID: [RealNodeInfo{realNodeId=99, similarity=1.0}, RealNodeInfo{realNodeId=98, similarity=0.8}, RealNodeInfo{realNodeId=95, similarity=0.8}, RealNodeInfo{realNodeId=100, similarity=0.75}, RealNodeInfo{realNodeId=93, similarity=0.75}, RealNodeInfo{realNodeId=94, similarity=0.75}, RealNodeInfo{realNodeId=96, similarity=0.7142857142857143}, RealNodeInfo{realNodeId=101, similarity=0.6666666666666666}, RealNodeInfo{realNodeId=91, similarity=0.6666666666666666}, RealNodeInfo{realNodeId=92, similarity=0.6333333333333333}, RealNodeInfo{realNodeId=97, similarity=0.5416666666666666}]
 * 草图节点 ID: 113, 真实节点 ID: [RealNodeInfo{realNodeId=92, similarity=1.0}, RealNodeInfo{realNodeId=96, similarity=0.8571428571428572}, RealNodeInfo{realNodeId=100, similarity=0.8166666666666667}, RealNodeInfo{realNodeId=93, similarity=0.8166666666666667}, RealNodeInfo{realNodeId=94, similarity=0.8166666666666667}, RealNodeInfo{realNodeId=98, similarity=0.7}, RealNodeInfo{realNodeId=95, similarity=0.7}, RealNodeInfo{realNodeId=97, similarity=0.65}, RealNodeInfo{realNodeId=99, similarity=0.6333333333333333}, RealNodeInfo{realNodeId=101, similarity=0.6}, RealNodeInfo{realNodeId=91, similarity=0.4666666666666667}]
 * 草图节点 ID: 110, 真实节点 ID: [RealNodeInfo{realNodeId=98, similarity=0.8}, RealNodeInfo{realNodeId=95, similarity=0.8}, RealNodeInfo{realNodeId=97, similarity=0.7083333333333333}, RealNodeInfo{realNodeId=99, similarity=0.6666666666666666}, RealNodeInfo{realNodeId=101, similarity=0.6666666666666666}, RealNodeInfo{realNodeId=91, similarity=0.6666666666666666}, RealNodeInfo{realNodeId=92, similarity=0.4666666666666667}, RealNodeInfo{realNodeId=100, similarity=0.41666666666666663}, RealNodeInfo{realNodeId=93, similarity=0.41666666666666663}, RealNodeInfo{realNodeId=94, similarity=0.41666666666666663}, RealNodeInfo{realNodeId=96, similarity=0.38095238095238093}]
 * 草图节点 ID: 111, 真实节点 ID: [RealNodeInfo{realNodeId=100, similarity=1.0}, RealNodeInfo{realNodeId=93, similarity=1.0}, RealNodeInfo{realNodeId=94, similarity=1.0}, RealNodeInfo{realNodeId=96, similarity=0.9285714285714286}, RealNodeInfo{realNodeId=92, similarity=0.8166666666666667}, RealNodeInfo{realNodeId=99, similarity=0.75}, RealNodeInfo{realNodeId=98, similarity=0.7166666666666667}, RealNodeInfo{realNodeId=95, similarity=0.7166666666666667}, RealNodeInfo{realNodeId=101, similarity=0.5833333333333334}, RealNodeInfo{realNodeId=97, similarity=0.5833333333333333}, RealNodeInfo{realNodeId=91, similarity=0.41666666666666663}]
 * @author JXS
 */
public class CalculateGroupSim {
    private static final double OMEGA1 = 0.5;
    private static final double OMEGA2 = 0.5;
    private static final double SIM_VALUE = 0.3;

    public Map<Integer, List<RealNodeInfo>> firstFilter(String caoTuLabel,String realLabel) {
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = new HashMap<>();
        try (DriverCommon driverCommon = new DriverCommon();
             Driver driver = driverCommon.getGraphDatabase();
             Session session = driver.session()) {

            Map<Integer, NodeData> realData = new HashMap<>();
            collectDataFromDb(session, "MATCH p=(:"+realLabel+")-[r:Contain]->() RETURN p", realData);

            Map<Integer, NodeData> sketchData = new HashMap<>();
            collectDataFromDb(session, "MATCH p=(:"+caoTuLabel+")-[r:Contain]->() RETURN p", sketchData);

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

            if (path.end().containsKey("fclass")) {
                String type = path.end().get("fclass").asString();
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
                    realNodeInfos.add(new RealNodeInfo(realNodeId, sim)); // 添加匹配的真实节点信息
                    matched = true; // 标记已匹配
                }
            }
            if (matched) {
                sketchToRealMap.put(sketchNodeId, realNodeInfos); // 存储草图节点 ID 和对应的真实节点信息列表
            } else {
                //System.out.println("未找到匹配: 草图节点 ID = " + sketchNodeId);
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