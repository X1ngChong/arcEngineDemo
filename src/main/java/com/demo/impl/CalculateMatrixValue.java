package com.demo.impl;

/**
 * 计算4*4 矩阵的值
 * @author JXS
 */
public class CalculateMatrixValue {

    public static void main(String[] args) {
//        double[][] matrix = {
//                {0, 1, 1, 0},
//                {1, 0, 0, 1},
//                {1, 0, 0, 1},
//                {0, 1, 1, 0}
//        };
        double[][] matrix = {
                {1, 0.75, 1, 75},
                {0.75, 1, 0.5, 1},
                {0.5, 0.5, 1, 1},
                {0.25, 1, 1, 1}
        };

        double determinant = calculateDeterminant(matrix);
        System.out.println("The value of the matrix is: " + determinant);
    }

    public static double calculateDeterminant(double[][] matrix) {
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new IllegalArgumentException("Matrix 必须是 4x4.");
        }

        double det = 0;

        for (int i = 0; i < 4; i++) {
            det += (i % 2 == 0 ? 1 : -1) * matrix[0][i] * calculateMinor(matrix, 0, i);
        }

        return det;
    }

    private static double calculateMinor(double[][] matrix, int row, int col) {
        double[][] minor = new double[3][3];
        int minorRow = 0, minorCol;

        for (int i = 0; i < 4; i++) {
            if (i == row) continue;
            minorCol = 0;
            for (int j = 0; j < 4; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }

        return calculate3x3Determinant(minor);
    }

    private static double calculate3x3Determinant(double[][] matrix) {
        return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
    }
}