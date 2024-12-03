package com.demo.impl.matrix;

import com.Bean.GroupLocationRelationship;
import com.Bean.RealNodeInfo;
import com.Util.location.OrientationSimilarity;
import com.demo.overall.NewDemoRun.locationRelation.Demo7;
import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Util.matrix.PrintMatrix.printMatrix;

/**
 * 存储6个区域之间的方位关系矩阵
 比较矩阵 111-112:
 0.25	0.5	0.0	0.0	0.0	0.0	0.25	0.0	0.25	0.25	0.0
 1.0	0.75	0.75	1.0	0.0	0.75	1.0	0.0	0.25	0.75	0.5
 0.5	0.75	0.25	1.0	0.25	0.0	0.5	0.25	0.25	0.25	0.0
 0.0	0.5	0.0	0.75	0.0	0.5	0.0	0.0	0.0	0.0	0.0
 0.75	0.75	0.5	0.75	0.25	0.75	1.0	0.25	0.0	0.0	0.25
 0.0	0.75	0.25	0.75	0.0	0.5	1.0	0.0	0.25	0.25	0.25
 0.25	0.0	0.25	0.5	0.25	0.25	0.5	0.25	0.25	0.25	0.25
 0.75	0.75	0.0	1.0	0.25	0.75	1.0	0.0	0.25	0.5	0.0
 1.0	0.75	1.0	1.0	1.0	0.75	1.0	0.0	0.5	0.75	0.75
 0.75	0.75	1.0	1.0	0.5	1.0	1.0	0.25	0.25	0.75	0.0
 0.75	0.75	0.75	0.75	0.75	0.75	1.0	0.5	0.0	1.0	0.75

 比较矩阵 111-113:
 0.5	0.5	0.0	0.25	0.25	0.25	0.25	0.25	0.5	0.25	0.5
 1.0	0.75	0.75	0.0	0.5	0.5	0.5	0.25	0.75	0.25	0.5
 0.5	0.75	0.75	0.5	0.0	0.5	0.5	0.25	0.75	0.5	0.5
 0.25	0.0	0.5	0.25	0.25	0.25	0.25	0.25	0.25	0.25	0.25
 0.0	0.75	0.5	0.0	0.5	0.5	0.25	0.0	0.5	0.0	0.25
 0.5	0.75	0.5	0.25	0.25	0.5	0.0	0.0	0.0	0.25	0.5
 0.5	0.75	0.75	0.5	0.5	0.0	0.5	0.5	0.5	0.5	0.5
 0.75	0.75	0.75	0.5	0.5	0.5	0.0	0.25	1.0	0.25	0.5
 1.0	0.75	0.75	0.75	0.5	0.5	0.75	0.5	0.75	0.0	0.75
 1.0	0.75	0.75	0.75	0.75	0.5	0.75	0.0	1.0	0.5	0.5
 0.75	0.75	0.5	0.5	0.5	0.5	0.5	0.5	0.5	0.25	0.0

 比较矩阵 110-111:
 0.75	1.0	1.0	0.75	1.0	1.0	0.0	1.0	1.0	1.0	1.0
 0.25	1.0	0.0	0.25	0.75	0.5	0.0	0.0	0.75	0.75	1.0
 0.25	0.75	0.25	0.25	0.5	0.5	0.0	0.25	1.0	0.0	1.0
 0.0	0.75	0.25	0.25	1.0	0.0	0.0	0.5	0.75	0.5	1.0
 0.25	0.25	0.0	0.25	0.5	0.25	0.0	0.25	0.0	0.0	0.75
 0.0	0.0	0.0	0.25	0.25	0.0	0.0	0.0	0.25	0.0	0.0
 0.0	0.5	0.0	0.25	0.0	0.0	0.0	0.25	0.5	0.5	0.75
 0.0	0.75	0.75	1.0	1.0	1.0	0.25	0.75	0.75	0.75	1.0
 0.25	0.0	0.0	0.25	0.5	0.25	0.0	0.0	0.75	0.25	1.0
 0.25	1.0	0.0	0.75	1.0	0.75	0.0	1.0	1.0	0.75	1.0
 0.0	0.75	0.25	0.0	0.75	0.75	0.25	0.75	0.75	0.75	0.75

 比较矩阵 112-113:
 1.0	0.25	0.0	0.75	0.25	0.0	0.5	0.5	0.0	0.75	1.0
 1.0	0.75	0.75	1.0	1.0	0.0	1.0	1.0	1.0	1.0	1.0
 0.75	0.25	0.25	1.0	0.0	0.0	0.0	0.75	0.5	0.75	1.0
 1.0	1.0	0.0	0.75	0.75	0.25	0.75	0.75	1.0	0.75	1.0
 0.5	0.25	0.25	0.0	0.0	0.0	0.0	0.25	0.25	0.75	1.0
 1.0	0.75	0.25	1.0	0.0	0.0	1.0	0.75	0.75	1.0	1.0
 0.75	0.0	0.0	0.75	0.25	0.25	0.75	0.75	0.75	0.75	0.75
 0.5	0.25	0.25	0.25	0.0	0.0	0.25	0.0	0.25	0.0	0.75
 0.25	0.25	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.25	0.0
 0.0	0.25	0.0	0.5	0.0	0.0	0.25	0.5	0.0	0.5	0.75
 0.5	0.25	0.25	0.75	0.25	0.0	0.25	0.0	0.5	1.0	1.0

 比较矩阵 110-112:
 0.5	0.0	0.5	0.75	0.5	0.5	0.75	0.5	0.5	0.5	0.5
 1.0	0.5	0.0	0.75	0.5	0.5	0.75	0.25	0.5	0.75	0.25
 1.0	0.5	0.75	0.75	0.75	0.75	0.75	0.5	0.5	1.0	0.0
 0.0	0.5	0.0	0.5	0.25	0.25	0.75	0.25	0.5	0.5	0.0
 0.75	0.5	0.75	0.75	0.75	0.5	0.75	0.0	0.75	1.0	0.5
 0.5	0.5	0.5	0.5	0.5	0.5	0.75	0.25	0.0	0.75	0.5
 0.5	0.5	0.25	0.5	0.0	0.5	0.75	0.0	0.25	0.0	0.0
 0.5	0.25	0.25	0.0	0.25	0.25	0.5	0.25	0.5	0.5	0.25
 0.75	0.5	0.5	0.75	0.0	0.5	0.75	0.25	0.5	1.0	0.25
 0.75	0.5	0.5	0.75	0.5	0.0	0.75	0.5	0.5	0.5	0.25
 0.25	0.25	0.25	0.5	0.25	0.25	0.0	0.25	0.25	0.25	0.25

 比较矩阵 110-113:
 0.75	1.0	1.0	0.75	0.75	0.0	0.75	0.75	0.75	0.75	0.75
 1.0	0.5	0.5	0.75	0.25	0.25	0.0	0.5	0.75	0.5	0.75
 0.75	0.5	0.5	1.0	0.5	0.25	0.5	0.0	0.75	0.75	0.75
 0.75	0.5	0.25	0.5	0.0	0.25	0.25	0.25	0.0	0.5	0.75
 0.75	0.5	0.5	0.5	0.25	0.25	0.5	0.25	0.5	0.0	1.0
 0.5	0.5	0.25	0.25	0.25	0.25	0.25	0.25	0.25	0.0	0.0
 0.0	0.5	0.25	0.25	0.25	0.25	0.0	0.25	0.25	0.25	0.5
 0.75	0.75	0.0	0.5	0.5	0.0	0.5	0.5	0.75	0.5	0.75
 0.75	0.5	0.5	0.0	0.25	0.25	0.25	0.0	0.5	0.5	0.75
 0.75	1.0	0.5	0.75	0.0	0.25	0.75	0.5	1.0	0.75	0.75
 0.5	0.0	0.25	0.5	0.0	0.0	0.5	0.5	0.5	0.5	0.5

 * @author JXS
 */
public class GetLocationRelationshipsMatrix {
    private Map<String, double[][]> locationMatrixMap = new HashMap<>(); // 使用 Map 存储矩阵

    public static void main(String[] args) {
        GetLocationRelationshipsMatrix g1 = new GetLocationRelationshipsMatrix();
        Map<String, double[][]> topologicalRelationshipsMatrix = g1.getLocationRelationshipsMatrix();

        // 打印所有比较矩阵
        for (Map.Entry<String, double[][]> entry : topologicalRelationshipsMatrix.entrySet()) {
            printMatrix("比较矩阵 " + entry.getKey(), entry.getValue());
        }

    }

    public Map<String, double[][]> getLocationRelationshipsMatrix() {

        CalculateGroupSim demo5 = new CalculateGroupSim();
        Demo7 demo7 = new Demo7();

        // 获取草图到真实节点的映射
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = demo5.firstFilter();

        // 假设我们要比较的草图节点
        int[] sketchNodes = {110, 111, 112, 113};//这边先写死

        List<RealNodeInfo>[] similarNodes = new List[sketchNodes.length];

        // 获取相似节点数组
        for (int i = 0; i < sketchNodes.length; i++) {
            similarNodes[i] = sketchToRealMap.get(sketchNodes[i]);
        }

        // 获取真实的方位关系
        List<GroupLocationRelationship> realLocationList = demo7.getLocationList("xianLinGroup");
        Map<Integer, List<GroupLocationRelationship>> realRelationships = createRealRelationships(realLocationList);

        // 获取草图的方位
        List<GroupLocationRelationship> caoTuLocationList = demo7.getLocationList("Group");
        Map<Integer, List<GroupLocationRelationship>>sketchRelationships = createSketchRelationships(caoTuLocationList);

        // 创建和填充比较矩阵
        for (int i = 0; i < sketchNodes.length; i++) {
            for (int j = i + 1; j < sketchNodes.length; j++) {
               // if(sketchNodes[i] == 112 && sketchNodes[j] ==113){
                String key = sketchNodes[i] + "-" + sketchNodes[j]; // 生成双键
                double[][] comparisonMatrix = createComparisonMatrix(similarNodes[i], similarNodes[j],
                        sketchNodes[i], sketchNodes[j],
                        realRelationships, sketchRelationships);
                locationMatrixMap.put(key, comparisonMatrix); // 存储矩阵
           //     }
            }
        }
        return locationMatrixMap;
    }

    // 创建邻接关系映射的方法
    private static Map<Integer, List<GroupLocationRelationship>> createRealRelationships(List<GroupLocationRelationship> realMeetsList) {
        Map<Integer, List<GroupLocationRelationship>> realRelationships = new HashMap<>();
        for (GroupLocationRelationship relationship : realMeetsList) {
            // 获取 block1Id
            int block1Id = relationship.getBlock1Id();

            // 将关系添加到对应的 block1Id 列表中
            realRelationships.computeIfAbsent(block1Id, k -> new ArrayList<>()).add(relationship);
        }
        return realRelationships;
    }



    // 创建草图关系的
    private static Map<Integer, List<GroupLocationRelationship>> createSketchRelationships(List<GroupLocationRelationship> caoTuMeetsList) {
        Map<Integer, List<GroupLocationRelationship>> sketchRelationships = new HashMap<>();
        for (GroupLocationRelationship relationship : caoTuMeetsList) {
            // 获取 block1Id
            int block1Id = relationship.getBlock1Id();

            // 将关系添加到对应的 block1Id 列表中
            sketchRelationships.computeIfAbsent(block1Id, k -> new ArrayList<>()).add(relationship);
        }
        return sketchRelationships;
    }

    private static double[][] createComparisonMatrix(List<RealNodeInfo> nodes1, List<RealNodeInfo> nodes2,
                                                     int sketchId1, int sketchId2,
                                                     Map<Integer, List<GroupLocationRelationship>> realRelationships,
                                                     Map<Integer, List<GroupLocationRelationship>> sketchRelationships) {
        int size1 = nodes1.size();
        int size2 = nodes2.size();
        double[][] comparisonMatrix = new double[size1][size2]; // 使用 double 数组

        // 查询草图地物的方位
        String sketchOrientation = getOrientation(sketchId1, sketchId2, sketchRelationships);
        // 填充比较矩阵
        for (int i = 0; i < size1; i++) {
            int realNodeId1 = nodes1.get(i).realNodeId; // 获取真实节点 ID
            for (int j = 0; j < size2; j++) {
                int realNodeId2 = nodes2.get(j).realNodeId; // 获取真实节点 ID
                // 查询真实地物的方位
                String realOrientation = getOrientation(realNodeId1, realNodeId2, realRelationships);

                // 计算相似度
                double similarity = OrientationSimilarity.calculateSimilarity(realOrientation,sketchOrientation);

                // 根据相似度填充比较矩阵
                comparisonMatrix[i][j] = similarity; // 直接填充相似度值
            }
        }
        return comparisonMatrix;
    }

    // 根据 ID 获取方位的方法
    private static String getOrientation(int id1, int id2, Map<Integer, List<GroupLocationRelationship>> relationships) {
        //if (relationships.containsKey(id1)) {
            for (GroupLocationRelationship relationship : relationships.get(id1)) {
                if (relationship.getBlock2Id() == id2) {
                    return relationship.getLocationRelationship(); // 返回方位
                }
            }
        //}
        return null; // 如果没有找到，返回 null
    }


}