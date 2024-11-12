package com.demo.impl;

import com.Bean.GroupRelationship;
import com.Bean.RealNodeInfo;
import com.demo.NewDemoRun.meetRelation.CalculateGroupSim;
import com.demo.NewDemoRun.meetRelation.Demo6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        int count = 0;
        Demo6 d6 = new Demo6();
        List<GroupRelationship> caoTuMeetsList = d6.getMeetList("Group");
        List<GroupRelationship> realMeetsList = d6.getMeetList("xianLinGroup");
        List<int[]> realResultList = new ArrayList<>();

        // 定义块 ID
        int[] blockIds = {110, 111, 113, 112};//获取所有的草图ID然后填充
        int n = blockIds.length;

        // 创建邻接矩阵
        int[][] adjacencyMatrix = new int[n][n];
        int[][] tempMatrix = new int[n][n];

        // 填充邻接矩阵
        for (GroupRelationship relationship : caoTuMeetsList) {
            int block1Index = getIndex(blockIds, relationship.getBlock1Id());
            int block2Index = getIndex(blockIds, relationship.getBlock2Id());
            if (block1Index != -1 && block2Index != -1) {
                adjacencyMatrix[block1Index][block2Index] = 1; // 1 表示相邻
                adjacencyMatrix[block2Index][block1Index] = 1; // 由于是无向图，反向也设置为 1
            }
        }



        CalculateGroupSim d = new CalculateGroupSim();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = d.firstFilter();
        // 输出结果
        
            List<RealNodeInfo> temp1 =  sketchToRealMap.get(blockIds[0]);
            for ( RealNodeInfo realNodeInfo :temp1){
                int id1 = realNodeInfo.getRealNodeId();
                List<RealNodeInfo> temp2 =  sketchToRealMap.get(blockIds[1]);
                for ( RealNodeInfo realNodeInfo2 :temp2){
                    int id2 =  realNodeInfo2.getRealNodeId();
                    List<RealNodeInfo> temp3 =  sketchToRealMap.get(blockIds[2]);
                    for ( RealNodeInfo realNodeInfo3 :temp3){
                        int id3 =  realNodeInfo3.getRealNodeId();
                        List<RealNodeInfo> temp4 =  sketchToRealMap.get(blockIds[3]);
                        for ( RealNodeInfo realNodeInfo4 :temp4){
                            int id4 =  realNodeInfo4.getRealNodeId();
                            int[] realBlockIds = {id1, id2, id3, id4};//获取所有的草图ID然后填充
                            for (GroupRelationship relationship : realMeetsList) {
                                int block1Index = getIndex(realBlockIds, relationship.getBlock1Id());
                                int block2Index = getIndex(realBlockIds, relationship.getBlock2Id());
                                if (block1Index != -1 && block2Index != -1) {
                                    tempMatrix[block1Index][block2Index] = 1; // 1 表示相邻
                                    tempMatrix[block2Index][block1Index] = 1; // 由于是无向图，反向也设置为 1
                                }
                            }
                            if(areMatricesEqual(adjacencyMatrix,tempMatrix)){
                                System.out.println("第"+ count++ +"次打印");
                                printMatrix(tempMatrix);
                                realResultList.add(realBlockIds);
                                System.out.println(Arrays.toString(realBlockIds));
                            }
                            tempMatrix = new int[n][n];//清空
                        }
                    }
                }
            }
        System.out.println(realResultList.toString());
    }

    // 获取块 ID 在数组中的索引
    private static int getIndex(int[] blockIds, int blockId) {
        for (int i = 0; i < blockIds.length; i++) {
            if (blockIds[i] == blockId) {
                return i;
            }
        }
        return -1; // 如果未找到，返回 -1
    }
    // 打印邻接矩阵
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
    // 比较两个邻接矩阵是否相同
    private static boolean areMatricesEqual(int[][] matrix1, int[][] matrix2) {
        // 检查行数和列数是否相同
        if (matrix1.length != matrix2.length) {
            return false;
        }
        for (int i = 0; i < matrix1.length; i++) {
            if (matrix1[i].length != matrix2[i].length) {
                return false;
            }
            // 比较每一行的元素
            for (int j = 0; j < matrix1[i].length; j++) {
                if (matrix1[i][j] != matrix2[i][j]) {
                    return false; // 如果发现不同的元素，返回 false
                }
            }
        }
        return true; // 所有元素都相同，返回 true
    }
}
