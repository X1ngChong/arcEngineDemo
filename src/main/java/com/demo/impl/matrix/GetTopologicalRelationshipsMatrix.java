package com.demo.impl.matrix;

import com.Bean.GroupRelationship;
import com.Bean.RealNodeInfo;
import com.demo.NewDemoRun.meetRelation.CalculateGroupSim;
import com.demo.NewDemoRun.meetRelation.Demo6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Util.matrix.PrintMatrix.printMatrix;

/**
 * 存储6个区域之间的临近关系矩阵
 * 比较矩阵 111-112:
 * 1	0	1	1	1	1	0	1	1	1	1
 * 1	1	0	1	1	1	1	1	0	0	0
 * 0	0	0	1	1	1	0	1	1	1	1
 * 1	1	1	0	1	0	1	1	1	1	1
 * 0	1	1	1	0	1	1	1	1	1	1
 * 1	1	0	1	1	0	1	1	1	0	1
 * 1	1	1	0	1	0	1	1	1	1	1
 * 0	1	1	1	0	0	1	1	1	1	0
 * 1	1	1	1	1	1	1	1	0	1	0
 * 1	1	0	1	0	1	1	0	1	1	1
 * 1	1	1	1	0	1	1	0	1	1	1
 *
 * 比较矩阵 111-113:
 * 0	1	0	0	0	1	0	0	0	0	0
 * 1	0	0	0	0	0	1	1	0	0	1
 * 0	1	0	0	0	1	1	0	1	0	0
 * 0	0	1	0	1	0	0	0	0	0	0
 * 0	0	0	1	0	0	0	0	1	0	0
 * 1	0	0	0	1	0	1	0	0	0	0
 * 0	0	1	0	1	0	0	0	0	0	0
 * 0	0	0	1	1	0	0	1	1	0	0
 * 0	0	0	0	0	0	0	1	0	0	1
 * 0	0	0	1	0	0	1	0	0	1	0
 * 0	0	0	1	0	0	0	0	0	1	0
 *
 * 比较矩阵 110-111:
 * 1	0	1	0	0	0	0	0	0	0	0
 * 0	1	1	0	0	1	0	0	0	1	0
 * 0	1	0	0	0	0	0	1	1	0	0
 * 0	0	1	0	1	0	0	1	0	0	0
 * 0	0	0	0	0	0	0	0	0	1	1
 * 0	1	0	0	0	0	0	0	1	0	0
 * 0	1	0	0	0	1	0	0	0	0	0
 * 0	0	0	1	0	0	1	0	0	0	0
 * 0	0	0	0	1	0	0	1	0	1	1
 * 0	0	0	1	0	1	1	1	0	0	0
 * 1	0	1	0	0	0	0	0	0	0	0
 *
 * 比较矩阵 112-113:
 * 1	0	0	0	1	0	1	0	0	0	0
 * 0	0	1	0	1	0	0	0	0	0	0
 * 0	0	0	1	1	0	0	1	1	0	0
 * 0	1	0	0	0	1	0	0	0	0	0
 * 1	0	0	0	0	0	1	1	0	0	1
 * 0	1	0	0	0	1	1	0	1	0	0
 * 0	0	1	0	1	0	0	0	0	0	0
 * 0	0	0	0	0	0	0	1	0	0	1
 * 0	0	0	1	0	0	0	0	0	1	0
 * 0	0	0	1	0	0	0	0	1	0	0
 * 0	0	0	1	0	0	1	0	0	1	0
 *
 * 比较矩阵 110-112:
 * 0	0	0	1	0	1	0	0	0	0	0
 * 1	0	0	0	1	1	0	0	0	0	1
 * 0	0	1	0	1	0	0	1	0	0	0
 * 0	0	1	0	0	1	0	0	0	1	0
 * 0	0	0	0	0	0	0	0	1	0	1
 * 0	0	0	0	1	0	0	1	0	0	0
 * 1	0	0	0	1	0	0	0	0	0	0
 * 0	1	0	0	0	0	1	0	0	0	0
 * 0	0	1	0	0	0	0	0	1	1	1
 * 1	1	1	0	0	0	1	0	0	0	0
 * 0	0	0	1	0	1	0	0	0	0	0
 *
 * 比较矩阵 110-113:
 * 1	1	0	1	0	1	1	1	1	1	1
 * 1	1	1	0	0	1	1	0	0	1	1
 * 1	1	1	0	1	1	0	1	1	0	1
 * 0	1	1	1	0	1	0	1	1	1	1
 * 1	1	1	1	1	1	1	0	1	1	0
 * 1	1	1	0	1	1	1	1	1	0	1
 * 1	1	1	0	1	1	1	1	0	1	1
 * 1	0	1	1	1	0	1	1	1	1	1
 * 0	1	1	1	1	1	0	0	1	1	0
 * 1	0	1	1	1	0	0	1	0	1	1
 * 1	1	0	1	0	1	1	1	1	1	1
 *
 *
 * 进程已结束,退出代码0
 * @author JXS
 */
public class GetTopologicalRelationshipsMatrix {
     private Map<String, Integer[][]> topologicalMatrixMap = new HashMap<>(); // 使用 Map 存储矩阵
    public static void main(String[] args) {
        GetTopologicalRelationshipsMatrix g1 = new GetTopologicalRelationshipsMatrix();
        Map<String, Integer[][]> topologicalRelationshipsMatrix = g1.getTopologicalRelationshipsMatrix();

        // 打印所有比较矩阵
        for (Map.Entry<String, Integer[][]> entry : topologicalRelationshipsMatrix.entrySet()) {
            printMatrix("比较矩阵 " + entry.getKey(), entry.getValue());
        }

    }
    public Map<String, Integer[][]>  getTopologicalRelationshipsMatrix() {

        CalculateGroupSim demo5 = new CalculateGroupSim();
        Demo6 demo6 = new Demo6();

        // 获取草图到真实节点的映射
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = demo5.firstFilter();

        // 假设我们要比较的草图节点
        int[] sketchNodes = {110, 111, 112, 113};//这边先写死

        List<RealNodeInfo>[] similarNodes = new List[sketchNodes.length];

        // 获取相似节点数组
        for (int i = 0; i < sketchNodes.length; i++) {
            similarNodes[i] = sketchToRealMap.get(sketchNodes[i]);
        }

        // 获取真实的相邻关系
        List<GroupRelationship> realMeetsList = demo6.getMeetList("xianLinGroup");
        Map<Integer, List<Integer>> realRelationships = createRealRelationships(realMeetsList);

        // 获取草图的相邻关系
        List<GroupRelationship> caoTuMeetsList = demo6.getMeetList("Group");
        Map<Integer, List<Integer>> sketchRelationships = createSketchRelationships(caoTuMeetsList);

        // 创建和填充比较矩阵
        for (int i = 0; i < sketchNodes.length; i++) {
            for (int j = i + 1; j < sketchNodes.length; j++) {
                String key = sketchNodes[i] + "-" + sketchNodes[j]; // 生成双键
                Integer[][] comparisonMatrix = createComparisonMatrix(similarNodes[i], similarNodes[j],
                        sketchNodes[i], sketchNodes[j],
                        realRelationships, sketchRelationships);
                topologicalMatrixMap.put(key, comparisonMatrix); // 存储矩阵
            }
        }
        return topologicalMatrixMap;
    }

    // 创建邻接关系映射的方法
    private static Map<Integer, List<Integer>> createRealRelationships(List<GroupRelationship> realMeetsList) {
        Map<Integer, List<Integer>> realRelationships = new HashMap<>();
        for (GroupRelationship relationship : realMeetsList) {
            realRelationships.computeIfAbsent(relationship.getBlock1Id(), k -> new ArrayList<>())
                    .add(relationship.getBlock2Id());
        }
        return realRelationships;
    }

    // 创建草图邻接关系映射的方法
    private static Map<Integer, List<Integer>> createSketchRelationships(List<GroupRelationship> caoTuMeetsList) {
        Map<Integer, List<Integer>> sketchRelationships = new HashMap<>();
        for (GroupRelationship relationship : caoTuMeetsList) {
            sketchRelationships.computeIfAbsent(relationship.getBlock1Id(), k -> new ArrayList<>())
                    .add(relationship.getBlock2Id());
        }
        return sketchRelationships;
    }

    // 创建比较矩阵的方法
    private static Integer[][] createComparisonMatrix(List<RealNodeInfo> nodes1, List<RealNodeInfo> nodes2,
                                                      int sketchId1, int sketchId2,
                                                      Map<Integer, List<Integer>> realRelationships,
                                                      Map<Integer, List<Integer>> sketchRelationships) {
        int size1 = nodes1.size();
        int size2 = nodes2.size();
        Integer[][] comparisonMatrix = new Integer[size1][size2];

        // 填充比较矩阵
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                int realNodeId1 = nodes1.get(i).getRealNodeId();
                int realNodeId2 = nodes2.get(j).getRealNodeId();

                // 检查草图是否相邻
                boolean areSketchNodesAdjacent = sketchRelationships.containsKey(sketchId1) &&
                        sketchRelationships.get(sketchId1).contains(sketchId2);
                boolean areRealNodesAdjacent = realRelationships.containsKey(realNodeId1) &&
                        realRelationships.get(realNodeId1).contains(realNodeId2);

                if (areSketchNodesAdjacent) {
                    comparisonMatrix[i][j] = (areRealNodesAdjacent) ? 1 : 0; // 草图相邻且真实相邻
                } else {
                    comparisonMatrix[i][j] = (areRealNodesAdjacent) ? 0 : 1; // 草图不相邻且真实不相邻
                }
            }
        }
        return comparisonMatrix;
    }

}