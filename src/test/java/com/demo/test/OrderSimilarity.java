package com.demo.test;

import java.util.*;

public class OrderSimilarity {

    public static void main(String[] args) {
        // 示例数组
        String[] typeOrderList1 = {"A", "B", "C","D"};
        String[] typeOrderList2 = {"A", "C", "B","D"};

        // 为重复地物添加唯一标识符
        String[] distinguishedList1 = distinguishDuplicates(typeOrderList1);
        String[] distinguishedList2 = distinguishDuplicates(typeOrderList2);

        // 构建顺序关系集合
        Set<String> orderSet1 = buildOrderRelations(distinguishedList1);
        Set<String> orderSet2 = buildOrderRelations(distinguishedList2);

        // 计算相似度
        double similarity = calculateSimilarity(orderSet1, orderSet2);
        System.out.printf("顺序关系的相似度为: %.2f%n", similarity);
    }

    // 为重复地物添加唯一标识符
    public static String[] distinguishDuplicates(String[] arr) {
        Map<String, Integer> countMap = new HashMap<>();
        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            String element = arr[i];
            countMap.put(element, countMap.getOrDefault(element, 0) + 1);
            result[i] = element + countMap.get(element); // 添加唯一标识符
        }

        return result;
    }

    // 构建顺序关系集合
    public static Set<String> buildOrderRelations(String[] arr) {
        Set<String> relations = new HashSet<>();

        // 遍历数组中的每对地物，生成顺序关系
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                relations.add(arr[i] + "," + arr[j] + ",before"); // arr[i] 在 arr[j] 之前
                relations.add(arr[j] + "," + arr[i] + ",after");  // arr[j] 在 arr[i] 之后
            }
        }

        return relations;
    }

    // 计算顺序关系的相似度
    public static double calculateSimilarity(Set<String> orderSet1, Set<String> orderSet2) {
        // 计算交集
        Set<String> intersection = new HashSet<>(orderSet1);
        intersection.retainAll(orderSet2);

       //最大的集合长度
        int maxSetLength = Math.max(orderSet1.size(), orderSet2.size());

        // 计算相似度
        return intersection.isEmpty() ? 0.0 : (double) intersection.size() / maxSetLength;
    }
}