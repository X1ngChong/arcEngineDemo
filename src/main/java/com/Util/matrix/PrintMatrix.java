package com.Util.matrix;

public class PrintMatrix {
    // 打印矩阵的方法
    public static void printMatrix(String name, double[][] matrix) {
        System.out.println(name + ":");
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + "\t"); // 使用制表符分隔
            }
            System.out.println(); // 换行
        }
        System.out.println(); // 分隔不同的矩阵
    }
    public static void printMatrix(String name, Integer[][] matrix) {
        System.out.println(name + ":");
        for (Integer[] row : matrix) {
            for (Integer value : row) {
                System.out.print(value + "\t"); // 使用制表符分隔
            }
            System.out.println(); // 换行
        }
        System.out.println(); // 分隔不同的矩阵
    }
}
