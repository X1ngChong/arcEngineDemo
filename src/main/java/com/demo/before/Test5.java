package com.demo.before;


import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;

public class Test5 {
    public static void main(String[] args) {
        String wkt1 ="MULTIPOLYGON (((13237085.097199999 3778520.556999993, 13237090.091400003 3778517.0431000027, 13237097.079599999 3778488.5896999994, 13237093.502000004 3778377.8165000025, 13237046.453 3778377.2816, 13236998.423500001 3778372.2782000024, 13236982.3832 3778367.389300006, 13236978.8411 3778516.0385999964, 13237073.3496 3778521.8364999993, 13237085.097199999 3778520.556999993)))";
        String wkt2 = "MULTIPOLYGON (((13237164.835600004 3778513.8859000006, 13237172.735800004 3778508.461000005, 13237170.4616 3778294.534999999, 13237114.099599995 3778294.7942999946, 13237116.500599997 3778512.771, 13237123.152600003 3778515.1018999964, 13237164.835600004 3778513.8859000006)))";
        double similarity = comparePolygons(wkt1, wkt2);
        System.out.println("俩个图形的相似度: " + similarity);
    }

    private static double comparePolygons(String wkt1, String wkt2) {
        WKTReader reader = new WKTReader();
        GeometryFactory geometryFactory = new GeometryFactory();
        try {
            Geometry geometry1 = reader.read(wkt1);
            Geometry geometry2 = reader.read(wkt2);

            // 确保两者都是多多边形
            if (!(geometry1 instanceof MultiPolygon) || !(geometry2 instanceof MultiPolygon)) {
                throw new IllegalArgumentException("Both inputs must be MultiPolygons.");
            }

            MultiPolygon multiPolygon1 = (MultiPolygon) geometry1;
            MultiPolygon multiPolygon2 = (MultiPolygon) geometry2;

            //计算面积差
            double areaDiff = Math.abs(multiPolygon1.getArea() - multiPolygon2.getArea());
            double maxArea = Math.max(multiPolygon1.getArea(), multiPolygon2.getArea());
            double areaSimilarity = 1 - (areaDiff / maxArea);

            //计算顶点计数差异
            int vertexCount1 = multiPolygon1.getNumPoints();
            int vertexCount2 = multiPolygon2.getNumPoints();
            double vertexDiff = Math.abs(vertexCount1 - vertexCount2);
            double maxVertexCount = Math.max(vertexCount1, vertexCount2);
            double vertexSimilarity = 1 - (vertexDiff / maxVertexCount);

            // 结合面积和顶点相似度以获得总体相似度得分
            // 根据需要调整权重
            double overallSimilarity = (areaSimilarity + vertexSimilarity) / 2;

            return overallSimilarity;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 to 代表判断不了
        }
    }
}