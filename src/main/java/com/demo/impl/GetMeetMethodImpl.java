package com.demo.impl;

import com.Bean.GroupRelationship;
import com.Bean.RealNodeInfo;
import com.demo.NewDemoRun.meetRelation.CalculateGroupSim;
import com.demo.NewDemoRun.meetRelation.Demo6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 这里是通过邻接矩阵去返回相似的集合
 * [96, 94, 100, 98]
 * [98, 100, 94, 96]
 * [98, 94, 100, 96]
 * [95, 93, 99, 92]
 * [95, 99, 93, 92]
 * [97, 93, 101, 91]
 * [97, 101, 93, 91]
 * [99, 92, 95, 93]
 * [99, 95, 92, 93]
 * [101, 97, 91, 93]
 * [101, 91, 97, 93]
 * [91, 93, 101, 97]
 * [91, 101, 93, 97]
 * [92, 93, 99, 95]
 * [92, 99, 93, 95]
 * [100, 96, 98, 94]
 * [100, 98, 96, 94]
 * [93, 92, 95, 99]
 * [93, 95, 92, 99]
 * [93, 97, 91, 101]
 * [93, 91, 97, 101]
 * [94, 96, 98, 100]
 * [94, 98, 96, 100]
 * [96, 100, 94, 98]
 * [96, 94, 100, 98]
 * List<Integer[]> realResultList = new ArrayList<>();
 * @author JXS
 */
public class GetMeetMethodImpl {
    public static void main(String[] args) {
        GetMeetMethodImpl g = new GetMeetMethodImpl();
        List<Integer[]> realResultList = g.getRealResultList();

        for (Integer[] i : realResultList){
            System.out.println(Arrays.toString(i));
        }


    }

    static int count = 0;

    public  List<Integer[]>  getRealResultList() {
        Demo6 demo6 = new Demo6();
        List<GroupRelationship> caoTuMeetsList = demo6.getMeetList("Group");
        List<GroupRelationship> realMeetsList = demo6.getMeetList("xianLinGroup");
        List<Integer[]> realResultList = new ArrayList<>();

        // 块 ID
        Integer[] blockIds = {110, 111, 112, 113}; // 这里的顺序随意
        int n = blockIds.length;

        // 创建邻接矩阵
        int[][] adjacencyMatrix = createAdjacencyMatrix(caoTuMeetsList, blockIds, n);

        CalculateGroupSim demo5 = new CalculateGroupSim();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = demo5.firstFilter();

        // 处理每个真实节点 ID 的组合
        findRealBlockIds(realMeetsList, sketchToRealMap, blockIds, adjacencyMatrix, realResultList, n);

        return realResultList;
    }

    // 从关系中创建邻接矩阵
    private static int[][] createAdjacencyMatrix(List<GroupRelationship> relationships, Integer[] blockIds, int n) {
        int[][] adjacencyMatrix = new int[n][n];
        for (GroupRelationship relationship : relationships) {
            Integer block1Id = relationship.getBlock1Id();
            Integer block2Id = relationship.getBlock2Id();
            int block1Index = getIndex(blockIds, block1Id);
            int block2Index = getIndex(blockIds, block2Id);
            if (block1Index != -1 && block2Index != -1) {
                adjacencyMatrix[block1Index][block2Index] = 1;
                adjacencyMatrix[block2Index][block1Index] = 1;
            }
        }
        return adjacencyMatrix;
    }

    // 查找所有真实块 ID 的组合
    private static void findRealBlockIds(List<GroupRelationship> realMeetsList, Map<Integer, List<RealNodeInfo>> sketchToRealMap,
                                         Integer[] blockIds, int[][] adjacencyMatrix, List<Integer[]> realResultList, int n) {
        Integer[] realBlockIds = new Integer[n];
        generateCombinations(realMeetsList, sketchToRealMap, blockIds, adjacencyMatrix, realResultList, realBlockIds, 0);
    }

    // 递归生成所有组合
    private static void generateCombinations(List<GroupRelationship> realMeetsList, Map<Integer, List<RealNodeInfo>> sketchToRealMap,
                                             Integer[] blockIds, int[][] adjacencyMatrix, List<Integer[]> realResultList,
                                             Integer[] realBlockIds, int index) {
        if (index == blockIds.length) {
            // 当组合完成时，创建临时邻接矩阵并进行比较
            int[][] tempMatrix = createTempAdjacencyMatrix(realMeetsList, realBlockIds, realBlockIds.length);
            if (areMatricesEqual(adjacencyMatrix, tempMatrix)) {
//                System.out.println("第" + count++ + "次打印");
                 printMatrix(tempMatrix);
                realResultList.add(realBlockIds.clone());
                System.out.println(Arrays.toString(realBlockIds));
            }
            return;
        }

        // 遍历当前块 ID 的所有真实节点
        for (RealNodeInfo realNodeInfo : sketchToRealMap.get(blockIds[index])) {
            realBlockIds[index] = realNodeInfo.getRealNodeId();
            // 递归到下一个块 ID
            generateCombinations(realMeetsList, sketchToRealMap, blockIds, adjacencyMatrix, realResultList, realBlockIds, index + 1);
        }
    }

    // 根据真实块 ID 创建临时邻接矩阵
    private static int[][] createTempAdjacencyMatrix(List<GroupRelationship> relationships, Integer[] blockIds, int n) {
        int[][] tempMatrix = new int[n][n];
        for (GroupRelationship relationship : relationships) {
            Integer block1Id = relationship.getBlock1Id();
            Integer block2Id = relationship.getBlock2Id();
            int block1Index = getIndex(blockIds, block1Id);
            int block2Index = getIndex(blockIds, block2Id);
            if (block1Index != -1 && block2Index != -1) {
                tempMatrix[block1Index][block2Index] = 1;
                tempMatrix[block2Index][block1Index] = 1;
            }
        }
        return tempMatrix;
    }

    // 获取块 ID 在数组中的索引
    private static int getIndex(Integer[] blockIds, Integer blockId) {
        for (int i = 0; i < blockIds.length; i++) {
            if (blockIds[i].equals(blockId)) {
                return i;
            }
        }
        return -1; // 未找到
    }

    // 打印邻接矩阵
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    // 比较两个邻接矩阵是否相同
    private static boolean areMatricesEqual(int[][] matrix1, int[][] matrix2) {
        return Arrays.deepEquals(matrix1, matrix2);
    }
}