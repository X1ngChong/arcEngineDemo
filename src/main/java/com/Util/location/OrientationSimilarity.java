package com.Util.location;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义方位差矩阵
 */
public class OrientationSimilarity {

    // 方位差异矩阵
    private static final int[][] ORIENTATION_DISTANCE_MATRIX = {
            {0, 1, 2, 3, 4, 3, 2, 1}, // N
            {1, 0, 1, 2, 3, 4, 3, 2}, // NE
            {2, 1, 0, 1, 2, 3, 4, 3}, // E
            {3, 2, 1, 0, 1, 2, 3, 4}, // SE
            {4, 3, 2, 1, 0, 1, 2, 3}, // S
            {3, 4, 3, 2, 1, 0, 1, 2}, // SW
            {2, 3, 4, 3, 2, 1, 0, 1}, // W
            {1, 2, 3, 4, 3, 2, 1, 0}  // NW
    };

    // 方位名称与其对应索引的映射  
    private static final Map<String, Integer> ORIENTATION_MAP = new HashMap<>();

    static {
        ORIENTATION_MAP.put("North", 0);
        ORIENTATION_MAP.put("NorthEast", 1);
        ORIENTATION_MAP.put("East", 2);
        ORIENTATION_MAP.put("SouthEast", 3);
        ORIENTATION_MAP.put("South", 4);
        ORIENTATION_MAP.put("SouthWest", 5);
        ORIENTATION_MAP.put("West", 6);
        ORIENTATION_MAP.put("NorthWest", 7);
    }

    // 计算相似度的方法  
    public static double calculateSimilarity(String orientation1, String orientation2) {
        Integer index1 = ORIENTATION_MAP.get(orientation1);
        Integer index2 = ORIENTATION_MAP.get(orientation2);

        if (index1 == null || index2 == null) {
           return 0;
        }

        int distance = ORIENTATION_DISTANCE_MATRIX[index1][index2];
        double maxDif = 4.0; // 根据您的矩阵定义的最大差异值  
        return 1.0 - (distance / maxDif);
    }

    public static void main(String[] args) {
        String orientation1 = "North"; // 输入的第一个方位
        String orientation2 = "SouthEast"; // 输入的第二个方位

        double similarity = calculateSimilarity(orientation1, orientation2);
        System.out.println("相似度: " + similarity);
    }
}