package com.demo.impl.matrix;

import com.Bean.PathResult;
import com.Bean.RealNodeInfo;
import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;

import java.util.*;

/**
 * 找出路径 并且按照权值排序
 0:Path: 110 -> 111 -> 112 -> 113 | Indices: [0, 2, 1, 0] | Weight: 1.0
 1:Path: 110 -> 111 -> 112 -> 113 | Indices: [0, 2, 8, 0] | Weight: 1.0
 2:Path: 110 -> 111 -> 113 -> 112 | Indices: [0, 2, 1, 0] | Weight: 1.0
 3:Path: 110 -> 111 -> 112 -> 113 | Indices: [1, 1, 1, 0] | Weight: 1.0
 4:Path: 110 -> 111 -> 112 -> 113 | Indices: [1, 1, 8, 0] | Weight: 1.0
 5:Path: 110 -> 111 -> 113 -> 112 | Indices: [1, 1, 1, 0] | Weight: 1.0
 6:Path: 110 -> 111 -> 112 -> 113 | Indices: [2, 8, 1, 0] | Weight: 1.0
 7:Path: 110 -> 111 -> 112 -> 113 | Indices: [2, 8, 8, 0] | Weight: 1.0
 8:Path: 110 -> 111 -> 113 -> 112 | Indices: [2, 8, 1, 0] | Weight: 1.0
 9:Path: 110 -> 111 -> 112 -> 113 | Indices: [3, 4, 1, 0] | Weight: 1.0
 10:Path: 110 -> 111 -> 112 -> 113 | Indices: [3, 4, 8, 0] | Weight: 1.0
 11:Path: 110 -> 111 -> 113 -> 112 | Indices: [3, 4, 1, 0] | Weight: 1.0
 12:Path: 110 -> 111 -> 112 -> 113 | Indices: [7, 3, 1, 0] | Weight: 1.0
 13:Path: 110 -> 111 -> 112 -> 113 | Indices: [7, 3, 8, 0] | Weight: 1.0
 14:Path: 110 -> 111 -> 113 -> 112 | Indices: [7, 3, 1, 0] | Weight: 1.0
 15:Path: 110 -> 111 -> 112 -> 113 | Indices: [8, 10, 1, 0] | Weight: 1.0
 16:Path: 110 -> 111 -> 112 -> 113 | Indices: [8, 10, 8, 0] | Weight: 1.0
 17:Path: 110 -> 111 -> 113 -> 112 | Indices: [8, 10, 1, 0] | Weight: 1.0
 18:Path: 110 -> 111 -> 112 -> 113 | Indices: [9, 7, 1, 0] | Weight: 1.0
 19:Path: 110 -> 111 -> 112 -> 113 | Indices: [9, 7, 8, 0] | Weight: 1.0
 20:Path: 110 -> 111 -> 113 -> 112 | Indices: [9, 7, 1, 0] | Weight: 1.0
 21:Path: 111 -> 110 -> 112 -> 113 | Indices: [1, 1, 1, 0] | Weight: 1.0
 22:Path: 111 -> 110 -> 113 -> 112 | Indices: [1, 1, 1, 0] | Weight: 1.0
 23:Path: 111 -> 110 -> 112 -> 113 | Indices: [0, 2, 1, 0] | Weight: 1.0
 24:Path: 111 -> 110 -> 113 -> 112 | Indices: [0, 2, 1, 0] | Weight: 1.0
 25:Path: 111 -> 110 -> 112 -> 113 | Indices: [7, 3, 1, 0] | Weight: 1.0
 26:Path: 111 -> 110 -> 113 -> 112 | Indices: [7, 3, 1, 0] | Weight: 1.0
 27:Path: 111 -> 110 -> 112 -> 113 | Indices: [3, 4, 1, 0] | Weight: 1.0
 28:Path: 111 -> 110 -> 113 -> 112 | Indices: [3, 4, 1, 0] | Weight: 1.0
 29:Path: 111 -> 110 -> 112 -> 113 | Indices: [9, 7, 1, 0] | Weight: 1.0
 30:Path: 111 -> 110 -> 113 -> 112 | Indices: [9, 7, 1, 0] | Weight: 1.0
 31:Path: 111 -> 110 -> 112 -> 113 | Indices: [2, 8, 1, 0] | Weight: 1.0
 32:Path: 111 -> 110 -> 113 -> 112 | Indices: [2, 8, 1, 0] | Weight: 1.0
 33:Path: 111 -> 110 -> 112 -> 113 | Indices: [8, 10, 1, 0] | Weight: 1.0
 34:Path: 111 -> 110 -> 113 -> 112 | Indices: [8, 10, 1, 0] | Weight: 1.0
 35:Path: 111 -> 112 -> 113 -> 110 | Indices: [1, 0, 0, 1] | Weight: 1.0
 36:Path: 111 -> 112 -> 113 -> 110 | Indices: [1, 3, 0, 1] | Weight: 1.0
 37:Path: 111 -> 112 -> 113 -> 110 | Indices: [1, 6, 0, 1] | Weight: 1.0
 38:Path: 111 -> 112 -> 113 -> 110 | Indices: [2, 3, 0, 1] | Weight: 1.0
 39:Path: 111 -> 112 -> 113 -> 110 | Indices: [4, 6, 0, 1] | Weight: 1.0
 40:Path: 111 -> 112 -> 113 -> 110 | Indices: [5, 6, 0, 1] | Weight: 1.0
 41:Path: 111 -> 112 -> 113 -> 110 | Indices: [7, 3, 0, 1] | Weight: 1.0
 42:Path: 111 -> 112 -> 113 -> 110 | Indices: [7, 6, 0, 1] | Weight: 1.0
 43:Path: 111 -> 112 -> 113 -> 110 | Indices: [8, 0, 0, 1] | Weight: 1.0
 44:Path: 111 -> 112 -> 113 -> 110 | Indices: [8, 2, 0, 1] | Weight: 1.0
 45:Path: 111 -> 112 -> 113 -> 110 | Indices: [8, 3, 0, 1] | Weight: 1.0
 46:Path: 111 -> 112 -> 113 -> 110 | Indices: [8, 4, 0, 1] | Weight: 1.0
 47:Path: 111 -> 112 -> 113 -> 110 | Indices: [8, 6, 0, 1] | Weight: 1.0
 48:Path: 111 -> 112 -> 113 -> 110 | Indices: [9, 3, 0, 1] | Weight: 1.0
 49:Path: 111 -> 112 -> 113 -> 110 | Indices: [9, 5, 0, 1] | Weight: 1.0
 50:Path: 111 -> 112 -> 113 -> 110 | Indices: [9, 6, 0, 1] | Weight: 1.0
 */
public class GetFinalMatrix {
    public static void main(String[] args) {
        GetFinalMatrix getFinalMatrix = new GetFinalMatrix();

        List<PathResult> resulyList = getFinalMatrix.getResulyList();
        int i = 0;
        // 输出前N个结果
        System.out.println("根据权值排序的结果:");
        if (resulyList.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (PathResult result : resulyList) {
                System.out.println(i++ +":"+result);
                if (i==100){
                    break;
                }
            }
        }
    }
  public static  Map<Integer, List<RealNodeInfo>> sketchToRealMap = new CalculateGroupSim().firstFilter(); // 对应的结果下标

    public   List<PathResult> getResulyList() {
        MatrixMerger m = new MatrixMerger();
        Map<String, double[][]> matrixMerger = m.getMatrixMerger();

        List<PathResult> results = new ArrayList<PathResult>();

        // 使用 Set 获取不重复的键
        Set<String> uniqueKeys = new HashSet<>();

        // 遍历每个比较矩阵作为起始点
        for (String key : matrixMerger.keySet()) {
            String start = key.split("-")[0]; // 获取起始矩阵的行
            String end = key.split("-")[1]; // 获取起始矩阵的行
          uniqueKeys.add(start);
          uniqueKeys.add(end);
        }

        //TODO：!!!!!!!!!!!!!!!!!!
        Map<String, Boolean> visited = new HashMap<>(); //还是会导致 Path: 110 -> 111 -> 110 -> 113 -> 110 | Indices: [0, 2, 2, 1, 0] | Weight: 0.875
        // 以每个不同的为起点
        for (String start : uniqueKeys) {
            visited.clear();
            visited.put("110",false);
            visited.put("111",false);
            visited.put("112",false);
            visited.put("113",false);
           // System.out.println("Starting from: " + start);
            dfs(uniqueKeys,matrixMerger, start, 4, visited,new ArrayList<>(), new ArrayList<>(), results,1);
        }

        // 根据权值对路径结果进行排序
        results.sort((a, b) -> Double.compare(b.getWeight(), a.getWeight()));

        return results;
    }

    private static void dfs( Set<String> uniqueKeys,Map<String, double[][]> matrixMerger, String current, int remaining, Map<String, Boolean> visited ,
                            List<String> path,List<Integer> indexList, List<PathResult> results, double currentWeight) {
        //如果不包含那么就添加进去 可能会出现错误 因为多次添加只有一个值导致path也持续添加会出现 110 -> 111 -> 110 -> 113 -> 110 这种错误
            visited.put(current, true); // 标记当前节点已访问
            path.add(current); // 添加当前节点到路径中

            if (remaining == 0) { // 如果没有剩余的节点，保存路径结果  最后一次
                results.add(new PathResult(new ArrayList<>(path), new ArrayList<>(indexList), currentWeight));
            } else if (remaining == 4) {
                //第一次
                for (String nextKey : uniqueKeys) {
                    // 确保每个节点只出现一次，并同时考虑反向连接
                    String forwardKey = current + "-" + nextKey;
                    String reverseKey = nextKey + "-" + current;

                    //如果没有访问过    并且在正常的顺序中有这个key
                    if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                        // 如果是正向连接 就走(x,y)(y,j)的流程
                        if (matrixMerger.containsKey(forwardKey)) {
                            double[][] matrix = matrixMerger.get(forwardKey); // 获取当前-下一个的矩阵
                            if (matrix != null) {
                                // 处理当前矩阵到下一个矩阵的路径
                                for (int i = 0; i < matrix.length; i++) {
                                    for (int j = 0; j < matrix.length; j++) {
                                        double weightToAdd = matrix[i][j]; // 循环每一个数字
                                        if (weightToAdd != 0) {
                                            currentWeight *= weightToAdd; // 更新当前权值
                                            indexList.add(i);
                                            indexList.add(j);
                                            dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                            currentWeight /= weightToAdd; // 回溯权值
                                            indexList.remove(indexList.size() - 1); // 移除列下标
                                            indexList.remove(indexList.size() - 1); // 移除行下标
                                        }
                                    }
                                }
                            }
                        }

                        // 检查反向连接就走(x,y)(j,y)的流程
                        if (matrixMerger.containsKey(reverseKey)) {

                            double[][] reverseMatrix = matrixMerger.get(reverseKey);
                            if (reverseMatrix != null) {
                                // 处理当前矩阵到下一个矩阵的路径
                                for (int i = 0; i < reverseMatrix.length; i++) {
                                    for (int j = 0; j < reverseMatrix.length; j++) {
                                        double weightToAdd = reverseMatrix[j][i]; // 循环每一个数字
                                        if (weightToAdd != 0) {
                                            currentWeight *= weightToAdd; // 更新当前权值
                                            indexList.add(j);
                                            indexList.add(i);
                                            dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                            currentWeight /= weightToAdd; // 回溯权值
                                            indexList.remove(indexList.size() - 1); // 移除列下标
                                            indexList.remove(indexList.size() - 1); // 移除行下标
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            } else if (remaining < 4 && remaining > 1) {
                //其他次数
                for (String nextKey : uniqueKeys) {

                    // 确保每个节点只出现一次，并同时考虑反向连接
                    String forwardKey = current + "-" + nextKey;
                    String reverseKey = nextKey + "-" + current;

                    //如果没有访问过    并且在正常的顺序中有这个key
                    if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                        // 如果是正向连接 就走(x,y)(y,j)的流程
                        if (matrixMerger.containsKey(forwardKey)) {
                            double[][] matrix = matrixMerger.get(forwardKey); // 获取当前-下一个的矩阵
                            if (matrix != null) {
                                // 处理当前矩阵到下一个矩阵的路径
                                for (int i = 0; i < matrix.length; i++) {
                                    double weightToAdd = matrix[i][0]; // 循环第一行
                                    if (weightToAdd != 0) {
                                        currentWeight *= weightToAdd; // 更新当前权值
                                        indexList.add(i);
                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                        currentWeight /= weightToAdd; // 回溯权值
                                        indexList.remove(indexList.size() - 1); // 移除行下标
                                    }
                                }
                            }
                        }

                        // 检查反向连接
                        if (matrixMerger.containsKey(reverseKey)) {

                            double[][] reverseMatrix = matrixMerger.get(reverseKey);
                            if (reverseMatrix != null) {
                                for (int i = 0; i < reverseMatrix.length; i++) {
                                    double weightToAdd = reverseMatrix[0][i]; // 循环每一行的第一个
                                    if (weightToAdd != 0) {
                                        currentWeight *= weightToAdd; // 更新当前权值
                                        indexList.add(i);
                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                        currentWeight /= weightToAdd; // 回溯权值
                                        indexList.remove(indexList.size() - 1); // 移除行下标
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (remaining == 1 && visited.size() >= 3) {
                //当最后一次的时候
                String firstNode = path.get(0);
                // 确保每个节点只出现一次，并同时考虑反向连接
                String forwardKey = current + "-" + firstNode;
                String reverseKey = firstNode + "-" + current;

                //并且在正常的顺序中有这个key
                if ((matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                    // 如果是正向连接 就走(x,y)(y,j)的流程
                    if (matrixMerger.containsKey(forwardKey)) {
                        Integer index = indexList.get(0);//获取第一个元素下标
                        int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();//获取第一个元素的ID

                        double[][] matrix = matrixMerger.get(forwardKey); // 获取当前-下一个的矩阵
                        if (matrix != null) {
                            // 处理当前矩阵到下一个矩阵的路径
                            for (int i = 0; i < matrix.length; i++) {
                                double weightToAdd = matrix[i][0]; // 循环第一行
                                if (weightToAdd != 0 && realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
                                    currentWeight *= weightToAdd; // 更新当前权值
                                    indexList.add(i);
                                    dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
                                    currentWeight /= weightToAdd; // 回溯权值
                                    indexList.remove(indexList.size() - 1); // 移除行下标
                                }
                            }
                        }
                    }

                    // 检查反向连接
                    if (matrixMerger.containsKey(reverseKey)) {
                        Integer index = indexList.get(0);//获取第一个元素下标
                        int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();//获取第一个元素的ID

                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
                        if (reverseMatrix != null) {
                            for (int i = 0; i < reverseMatrix.length; i++) {
                                double weightToAdd = reverseMatrix[0][i]; // 循环每一行的第一个
                                if (weightToAdd != 0 && realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
                                    currentWeight *= weightToAdd; // 更新当前权值
                                    indexList.add(i);
                                    dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
                                    currentWeight /= weightToAdd; // 回溯权值
                                    indexList.remove(indexList.size() - 1); // 移除行下标
                                }
                            }
                        }
                    }
                }
            }

        // 回溯
        path.remove(path.size() - 1); // 移除当前节点
        visited.put(current,false); // 移除当前节点
    }


}