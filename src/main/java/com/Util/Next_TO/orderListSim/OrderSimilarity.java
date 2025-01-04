package com.Util.Next_TO.orderListSim;

import java.util.*;

public class OrderSimilarity {

    public static void main(String[] args) {
        // 示例列表
        List<Object> orderList1 = Arrays.asList("A", "B", "C", "D");
        List<Object> orderList2 = Arrays.asList("A", "C", "B", "D");

        // 调用方法计算相似度
        double similarity = calculateOrderSimilarity(orderList1, orderList2);
        System.out.printf("顺序关系的相似度为: %.2f%n", similarity);
    }

    // 计算顺序关系相似度的方法
    public static double calculateOrderSimilarity(List<Object> orderList1, List<Object> orderList2) {
        // 将 List<Object> 转换为 String 数组
        String[] typeOrderList1 = orderList1.stream().toArray(String[]::new);
        String[] typeOrderList2 = orderList2.stream().toArray(String[]::new);

        // 为重复地物添加唯一标识符
        String[] distinguishedList1 = distinguishDuplicates(typeOrderList1);
        String[] distinguishedList2 = distinguishDuplicates(typeOrderList2);

        // 构建顺序关系集合
        Set<String> orderSet1 = buildOrderRelations(distinguishedList1);
        Set<String> orderSet2 = buildOrderRelations(distinguishedList2);

        // 计算交集
        Set<String> intersection = new HashSet<>(orderSet1);
        intersection.retainAll(orderSet2);

        // 最大的集合长度
        int maxSetLength = Math.max(orderSet1.size(), orderSet2.size());

        // 计算相似度
        return intersection.isEmpty() ? 0.0 : (double) intersection.size() / maxSetLength;
    }

    // 为重复地物添加唯一标识符
    private static String[] distinguishDuplicates(String[] arr) {
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
    private static Set<String> buildOrderRelations(String[] arr) {
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
}