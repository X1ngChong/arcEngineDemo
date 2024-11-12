package com.demo.impl.matrix;

import java.util.*;

/**
 * 找出路径 并且按照权值排序
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [0, 2, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [0, 2, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [0, 2, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [1, 1, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [1, 1, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [1, 1, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [2, 8, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [2, 8, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [2, 8, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [3, 4, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [3, 4, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [3, 4, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [7, 3, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [7, 3, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [7, 3, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [8, 10, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [8, 10, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [8, 10, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [9, 7, 1, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 112 -> 113 | Indices: [9, 7, 8, 0] | Weight: 1.0
 * Path: 110 -> 111 -> 113 -> 112 | Indices: [9, 7, 1, 0] | Weight: 1.0
 */
public class GetFinalMatrix {
    public static void main(String[] args) {
        GetFinalMatrix getFinalMatrix = new GetFinalMatrix();

        List<PathResult> resulyList = getFinalMatrix.getResulyList();
        // 输出前N个结果
        System.out.println("根据权值排序的结果:");
        if (resulyList.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (PathResult result : resulyList) {
                System.out.println(result);
            }
        }
    }
    public   List<PathResult> getResulyList() {
        MatrixMerger m = new MatrixMerger();
        Map<String, double[][]> matrixMerger = m.getMatrixMerger();

        List<PathResult> results = new ArrayList<>();

        // 使用 Set 获取不重复的键
        Set<String> uniqueKeys = new HashSet<>();

        // 遍历每个比较矩阵作为起始点
        for (String key : matrixMerger.keySet()) {
            String start = key.split("-")[0]; // 获取起始矩阵的行
            String end = key.split("-")[1]; // 获取起始矩阵的行
          uniqueKeys.add(start);
          uniqueKeys.add(end);
        }

        // 以每个不同的为起点
        for (String start : uniqueKeys) {
           // System.out.println("Starting from: " + start);
            dfs(uniqueKeys,matrixMerger, start, 3, new HashSet<>(),new ArrayList<>(), new ArrayList<>(), results,1);
        }

        // 根据权值对路径结果进行排序
        results.sort((a, b) -> Double.compare(b.getWeight(), a.getWeight()));

        return results;
    }

    private static void dfs( Set<String> uniqueKeys,Map<String, double[][]> matrixMerger, String current, int remaining, Set<String> visited,
                            List<String> path,List<Integer> indexList, List<PathResult> results, double currentWeight) {
        visited.add(current); // 标记当前节点已访问
        path.add(current); // 添加当前节点到路径中

        if (remaining == 0) { // 如果没有剩余的节点，保存路径结果  最后一次
            results.add(new PathResult(new ArrayList<>(path),new ArrayList<>(indexList),currentWeight));
        }
        else if (remaining == 3) {
            //第一次
            for (String nextKey : uniqueKeys) {
                // 确保每个节点只出现一次，并同时考虑反向连接
                String forwardKey = current + "-" + nextKey;
                String reverseKey = nextKey + "-" + current;

                //如果没有访问过    并且在正常的顺序中有这个key
                if (!visited.contains(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
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
                                        dfs(uniqueKeys,matrixMerger, nextKey, remaining - 1, visited, path,indexList, results, currentWeight);
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
                                        dfs(uniqueKeys,matrixMerger, nextKey, remaining - 1, visited, path,indexList, results, currentWeight);
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
        } else {
            //其他次数
            for (String nextKey : uniqueKeys) {

                // 确保每个节点只出现一次，并同时考虑反向连接
                String forwardKey = current + "-" + nextKey;
                String reverseKey = nextKey + "-" + current;

                //如果没有访问过    并且在正常的顺序中有这个key
                if (!visited.contains(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
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
                                    dfs(uniqueKeys,matrixMerger, nextKey, remaining - 1, visited, path,indexList, results, currentWeight);
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
                                    dfs(uniqueKeys,matrixMerger, nextKey, remaining - 1, visited, path,indexList, results, currentWeight);
                                    currentWeight /= weightToAdd; // 回溯权值
                                    indexList.remove(indexList.size() - 1); // 移除行下标
                                }
                            }
                        }
                    }
                }
            }
        }

        // 回溯
        path.remove(path.size() - 1); // 移除当前节点
        visited.remove(current); // 标记当前节点未访问
    }

    // 定义路径结果类
    static class PathResult {
        private final List<String> path;
        private final double weight;
        private final  List<Integer> indexList;

        public PathResult(List<String> path, List<Integer> indexList, double weight) {
            this.path = path;
            this.indexList = indexList;
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }

        public List<String> getPath() {
            return path;
        }

        public List<Integer> getIndexList() {
            return indexList;
        }

        @Override
        public String toString() {
            return "Path: " + String.join(" -> ", path) +
                    " | Indices: " + indexList + // 输出索引
                    " | Weight: " + weight;
        }
    }
}