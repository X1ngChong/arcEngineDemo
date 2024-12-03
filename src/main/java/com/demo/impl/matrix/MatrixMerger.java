package com.demo.impl.matrix;

import java.util.HashMap;
import java.util.Map;

import static com.Util.matrix.PrintMatrix.printMatrix;

/**
 比较矩阵 111-112:
 0.625	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.625	0.625	0.0
 1.0	0.875	0.0	1.0	0.0	0.875	1.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	1.0	0.625	0.0	0.0	0.625	0.625	0.625	0.0
 0.0	0.75	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.875	0.75	0.875	0.0	0.875	1.0	0.625	0.0	0.0	0.625
 0.0	0.875	0.0	0.875	0.0	0.0	1.0	0.0	0.625	0.0	0.625
 0.625	0.0	0.625	0.0	0.625	0.0	0.75	0.625	0.625	0.625	0.625
 0.0	0.875	0.0	1.0	0.0	0.0	1.0	0.0	0.625	0.75	0.0
 1.0	0.875	1.0	1.0	1.0	0.875	1.0	0.0	0.0	0.875	0.0
 0.875	0.875	0.0	1.0	0.0	1.0	1.0	0.0	0.625	0.875	0.0
 0.875	0.875	0.875	0.875	0.0	0.875	1.0	0.0	0.0	1.0	0.875

 比较矩阵 111-113:
 0.0	0.75	0.0	0.0	0.0	0.625	0.0	0.0	0.0	0.0	0.0
 1.0	0.0	0.0	0.0	0.0	0.0	0.75	0.625	0.0	0.0	0.75
 0.0	0.875	0.0	0.0	0.0	0.75	0.75	0.0	0.875	0.0	0.0
 0.0	0.0	0.75	0.0	0.625	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.75	0.0	0.0
 0.75	0.0	0.0	0.0	0.625	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.875	0.0	0.75	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	0.75	0.75	0.0	0.0	0.625	1.0	0.0	0.0
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.75	0.0	0.0	0.875
 0.0	0.0	0.0	0.875	0.0	0.0	0.875	0.0	0.0	0.75	0.0
 0.0	0.0	0.0	0.75	0.0	0.0	0.0	0.0	0.0	0.625	0.0

 比较矩阵 110-111:
 0.875	0.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	1.0	0.0	0.0	0.0	0.75	0.0	0.0	0.0	0.875	0.0
 0.0	0.875	0.0	0.0	0.0	0.0	0.0	0.625	1.0	0.0	0.0
 0.0	0.0	0.625	0.0	1.0	0.0	0.0	0.75	0.0	0.0	0.0
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.875
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.625	0.0	0.0
 0.0	0.75	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	1.0	0.0	0.0	0.625	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	0.0	0.75	0.0	0.0	0.0	0.0	0.625	1.0
 0.0	0.0	0.0	0.875	0.0	0.875	0.0	1.0	0.0	0.0	0.0
 0.0	0.0	0.625	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0

 比较矩阵 112-113:
 1.0	0.0	0.0	0.0	0.625	0.0	0.75	0.0	0.0	0.0	0.0
 0.0	0.0	0.875	0.0	1.0	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	1.0	0.0	0.0	0.0	0.875	0.75	0.0	0.0
 0.0	1.0	0.0	0.0	0.0	0.625	0.0	0.0	0.0	0.0	0.0
 0.75	0.0	0.0	0.0	0.0	0.0	0.0	0.625	0.0	0.0	1.0
 0.0	0.875	0.0	0.0	0.0	0.0	1.0	0.0	0.875	0.0	0.0
 0.0	0.0	0.0	0.0	0.625	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.875
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.625	0.0
 0.0	0.0	0.0	0.75	0.0	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	0.875	0.0	0.0	0.625	0.0	0.0	1.0	0.0

 比较矩阵 110-112:
 0.0	0.0	0.0	0.875	0.0	0.75	0.0	0.0	0.0	0.0	0.0
 1.0	0.0	0.0	0.0	0.75	0.75	0.0	0.0	0.0	0.0	0.625
 0.0	0.0	0.875	0.0	0.875	0.0	0.0	0.75	0.0	0.0	0.0
 0.0	0.0	0.0	0.0	0.0	0.625	0.0	0.0	0.0	0.75	0.0
 0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.875	0.0	0.75
 0.0	0.0	0.0	0.0	0.75	0.0	0.0	0.625	0.0	0.0	0.0
 0.75	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0	0.0
 0.0	0.625	0.0	0.0	0.0	0.0	0.75	0.0	0.0	0.0	0.0
 0.0	0.0	0.75	0.0	0.0	0.0	0.0	0.0	0.75	1.0	0.625
 0.875	0.75	0.75	0.0	0.0	0.0	0.875	0.0	0.0	0.0	0.0
 0.0	0.0	0.0	0.75	0.0	0.625	0.0	0.0	0.0	0.0	0.0

 比较矩阵 110-113:
 0.875	1.0	0.0	0.875	0.0	0.0	0.875	0.875	0.875	0.875	0.875
 1.0	0.75	0.75	0.0	0.0	0.625	0.0	0.0	0.0	0.75	0.875
 0.875	0.75	0.75	0.0	0.75	0.625	0.0	0.0	0.875	0.0	0.875
 0.0	0.75	0.625	0.75	0.0	0.625	0.0	0.625	0.0	0.75	0.875
 0.875	0.75	0.75	0.75	0.625	0.625	0.75	0.0	0.75	0.0	0.0
 0.75	0.75	0.625	0.0	0.625	0.625	0.625	0.625	0.625	0.0	0.0
 0.0	0.75	0.625	0.0	0.625	0.625	0.0	0.625	0.0	0.625	0.75
 0.875	0.0	0.0	0.75	0.75	0.0	0.75	0.75	0.875	0.75	0.875
 0.0	0.75	0.75	0.0	0.625	0.625	0.0	0.0	0.75	0.75	0.0
 0.875	0.0	0.75	0.875	0.0	0.0	0.0	0.75	0.0	0.875	0.875
 0.75	0.0	0.0	0.75	0.0	0.0	0.75	0.75	0.75	0.75	0.75
 */
public class MatrixMerger {
    public static void main(String[] args) {
        MatrixMerger m = new MatrixMerger();

        Map<String, double[][]> matrixMerger = m.getMatrixMerger();
        // 打印所有比较矩阵
        for (Map.Entry<String, double[][]> entry : matrixMerger.entrySet()) {
            printMatrix("比较矩阵 " + entry.getKey(), entry.getValue());
        }
    }

    public  Map<String, double[][]>  getMatrixMerger () {
        Map<String, double[][]> matrixMergerMap = new HashMap<>(); // 合并后的矩阵

        GetLocationRelationshipsMatrix gl = new GetLocationRelationshipsMatrix();
        Map<String, double[][]> locationRelationshipsMatrix = gl.getLocationRelationshipsMatrix();

        GetTopologicalRelationshipsMatrix gt = new GetTopologicalRelationshipsMatrix();
        Map<String, Integer[][]> topologicalRelationshipsMatrix = gt.getTopologicalRelationshipsMatrix();


        MatrixMerger merger = new MatrixMerger();
        // 利用其中一个矩阵的key去合并矩阵
        for (Map.Entry<String, double[][]> entry : locationRelationshipsMatrix.entrySet()) {
            double[][] doubles = merger.mergeMatrices(locationRelationshipsMatrix,topologicalRelationshipsMatrix,entry.getKey(), 0.5, 0.5);
            matrixMergerMap.put(entry.getKey(),doubles);
        }

        return matrixMergerMap;
    }

    public double[][] mergeMatrices(  Map<String, double[][]> locationMatrixMap,Map<String, Integer[][]> topologicalMatrixMap,String key, double omega3, double omega4) {
        double[][] locationMatrix = locationMatrixMap.get(key);
        Integer[][] topologicalMatrix = topologicalMatrixMap.get(key);

        if (locationMatrix == null || topologicalMatrix == null) {
            throw new IllegalArgumentException("Matrices for the given key do not exist.");
        }

        int rows = locationMatrix.length;
        int cols = locationMatrix[0].length;
        double[][] resultMatrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = (omega3 * topologicalMatrix[i][j]) + (omega4 * locationMatrix[i][j]);
                resultMatrix[i][j] = (value <= 0.5) ? 0 : value;
            }
        }

        return resultMatrix;
    }



}