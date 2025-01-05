package com.demo.overall.impl.matrix;

import com.Bean.PathResult;
import com.Bean.RealNodeInfo;
import com.Service.impl.Neo4jServiceImpl;
import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class GetFinalMatrix2 {
    public  static int TIMES = 0;

  public static  Map<Integer, List<RealNodeInfo>> sketchToRealMap ; // 对应的结果下标

    public   List<PathResult> getResulyList(String caoTuLabel,String realLabel ) {
        sketchToRealMap = new CalculateGroupSim().firstFilter(caoTuLabel,realLabel);
        MatrixMerger m = new MatrixMerger();
        Map<String, double[][]> matrixMerger = m.getMatrixMerger(caoTuLabel,realLabel);

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

        Map<String, Boolean> visited = new HashMap<>(); //还是会导致 Path: 110 -> 111 -> 110 -> 113 -> 110 | Indices: [0, 2, 2, 1, 0] | Weight: 0.875
        // 以每个不同的为起点
        // 假设我们要比较的草图节点
        Neo4jServiceImpl neo4jService = new Neo4jServiceImpl();

        Integer[] sketchNodes = neo4jService.getGroupIdsByTag(caoTuLabel);

        for (String start : uniqueKeys) {
            visited.clear();
            for (int temp:sketchNodes)
            {
                visited.put(String.valueOf(temp),false);
            }

            TIMES = visited.size();
           // System.out.println("Starting from: " + start);
            dfs(uniqueKeys,matrixMerger, start, TIMES, visited,new ArrayList<>(), new ArrayList<>(), results,1);
        }

        // 根据权值对路径结果进行排序
        results.sort((a, b) -> Double.compare(b.getWeight(), a.getWeight()));

        return results;
    }

    private static void dfs(Set<String> uniqueKeys, Map<String, double[][]> matrixMerger, String current, int remaining, Map<String, Boolean> visited,
                            List<String> path, List<Integer> indexList, List<PathResult> results, double currentWeight) {
        path.add(current); // 添加当前节点到路径中

        if (remaining == 0) { // 如果没有剩余的节点，保存路径结果
            results.add(new PathResult(new ArrayList<>(path), new ArrayList<>(indexList), currentWeight));
        } else if (remaining == TIMES) {
            // 第一次
            for (String nextKey : uniqueKeys) {
                String forwardKey = current + "-" + nextKey;
                String reverseKey = nextKey + "-" + current;

                if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                    if (matrixMerger.containsKey(forwardKey)) {
                        double[][] matrix = matrixMerger.get(forwardKey);
                        if (matrix != null) {
                            for (int i = 0; i < matrix.length; i++) {
                                for (int j = 0; j < matrix[0].length; j++) {
                                    double weightToAdd = matrix[i][j];
                                    if (weightToAdd != 0) {
                                        currentWeight *= weightToAdd;
                                    //currentWeight += weightToAdd;
                                        indexList.add(i);
                                        indexList.add(j);
                                        visited.put(current, true); // 在递归之前标记为已访问
                                        visited.put(nextKey, true); // 在递归之前标记为已访问
                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                        visited.put(nextKey, false); // 回溯后标记为未访问
                                        visited.put(current, false); // 在递归之前标记为已访问
                                        currentWeight /= weightToAdd;
                                    //currentWeight -= weightToAdd;
                                        indexList.remove(indexList.size() - 1);
                                        indexList.remove(indexList.size() - 1);
                                    }
                                }
                            }
                        }
                    }

                    if (matrixMerger.containsKey(reverseKey)) {
                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
                        if (reverseMatrix != null) {
                            for (int i = 0; i < reverseMatrix[0].length; i++) {
                                for (int j = 0; j < reverseMatrix.length; j++) {
                                    double weightToAdd = reverseMatrix[j][i];
                                    if (weightToAdd != 0) {

                                    //currentWeight += weightToAdd;
                                        currentWeight *= weightToAdd;
                                        //为什么这边调换位置可以跑了？为什么之前不能跑？TODO:????
                                        indexList.add(i);
                                        indexList.add(j);
                                        visited.put(current, true); // 在递归之前标记为已访问
                                        visited.put(nextKey, true); // 在递归之前标记为已访问
                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                        visited.put(nextKey, false); // 回溯后标记为未访问
                                        visited.put(current, false); // 在递归之前标记为已访问
                                        currentWeight /= weightToAdd;
                                    //currentWeight -= weightToAdd;
                                        indexList.remove(indexList.size() - 1);
                                        indexList.remove(indexList.size() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (remaining < TIMES && remaining > 1) {
            // 其他次数
            for (String nextKey : uniqueKeys) {
                String forwardKey = current + "-" + nextKey;
                String reverseKey = nextKey + "-" + current;

                if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                    if (matrixMerger.containsKey(forwardKey)) {
                        double[][] matrix = matrixMerger.get(forwardKey);
                        if (matrix != null) {
                            for (int i = 0; i < matrix[0].length; i++) {
                                double weightToAdd = matrix[indexList.get(indexList.size()-1)][i];
                                if (weightToAdd != 0) {
                                //currentWeight += weightToAdd;
                                   currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    visited.put(nextKey, true); // 在递归之前标记为已访问
                                    dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                    visited.put(nextKey, false); // 回溯后标记为未访问
                                 //currentWeight -= weightToAdd;
                                    currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }

                    if (matrixMerger.containsKey(reverseKey)) {
                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
                        if (reverseMatrix != null) {
                            for (int i = 0; i < reverseMatrix.length; i++) {
                                double weightToAdd = reverseMatrix[i][indexList.get(indexList.size()-1)];
                                if (weightToAdd != 0) {
                                //currentWeight += weightToAdd;
                                    currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    visited.put(nextKey, true); // 在递归之前标记为已访问
                                    dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                    visited.put(nextKey, false); // 回溯后标记为未访问
                                //currentWeight -= weightToAdd;
                                    currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }
                }
            }
        } else if (remaining == 1 && visited.size() >= (TIMES-1)) {
            // 最后一次
            String firstNode = path.get(0);
            String forwardKey = current + "-" + firstNode;
            String reverseKey = firstNode + "-" + current;

            if ((matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                if (matrixMerger.containsKey(forwardKey)) {
                    Integer index = indexList.get(0);
                    int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();

                    double[][] matrix = matrixMerger.get(forwardKey);
                    if (matrix != null) {
                        for (int i = 0; i < matrix[0].length; i++) {
                            double weightToAdd = matrix[indexList.get(indexList.size()-1)][i];
                            if (weightToAdd != 0) {
                                if (realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
                                   // currentWeight += weightToAdd;
                                   currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
                                   // currentWeight -= weightToAdd;
                                   currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }
                }

                if (matrixMerger.containsKey(reverseKey)) {
                    Integer index = indexList.get(0);
                    int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();

                    double[][] reverseMatrix = matrixMerger.get(reverseKey);
                    if (reverseMatrix != null) {
                        for (int i = 0; i < reverseMatrix.length; i++) {
                            double weightToAdd = reverseMatrix[i][indexList.get(indexList.size()-1)];
                            if (weightToAdd != 0) {
                                 if (realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
                                    // currentWeight += weightToAdd;
                                     currentWeight *= weightToAdd;
                                     indexList.add(i);
                                     dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
                                    // currentWeight -= weightToAdd;
                                     currentWeight /= weightToAdd;
                                     indexList.remove(indexList.size() - 1);
                                 }
                            }
                        }
                    }
                }
            }
        }
        // 回溯
        path.remove(path.size() - 1); // 移除当前节点
    }


}