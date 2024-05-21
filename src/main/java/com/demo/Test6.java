package com.demo;

import com.Util.ProjectionConverter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.algorithm.distance.DiscreteHausdorffDistance;

/**
 * // TODO 把这个加入最终结果的筛选 需要更换相同坐标系
 * 豪斯多夫距离  需要草图在同一地理坐标系
 *
 * 较小的Hausdorff距离 可以认为两个多边形的相似度较高。
 *
 * 较大的Hausdorff距离 可以认为两个多边形的相似度较低。
 *
 */
public class Test6 {

    public static void main(String[] args) {
       // String wkt1 = "MULTIPOLYGON (((13237085.097199999 3778520.556999993, 13237090.091400003 3778517.0431000027, 13237097.079599999 3778488.5896999994, 13237093.502000004 3778377.8165000025, 13237046.453 3778377.2816, 13236998.423500001 3778372.2782000024, 13236982.3832 3778367.389300006, 13236978.8411 3778516.0385999964, 13237073.3496 3778521.8364999993, 13237085.097199999 3778520.556999993)))";
        String wkt1 = "MULTIPOLYGON (((118.94868140000003 32.11839429999999, 118.94942550000005 32.11865599999997, 118.94974149999999 32.118021699999986, 118.94897960000002 32.117768600000005, 118.94868140000003 32.11839429999999)))";
        //String wkt2 = "MULTIPOLYGON (((13237164.835600004 3778513.8859000006, 13237172.735800004 3778508.461000005, 13237170.4616 3778294.534999999, 13237114.099599995 3778294.7942999946, 13237116.500599997 3778512.771, 13237123.152600003 3778515.1018999964, 13237164.835600004 3778513.8859000006)))";
       // String wkt2 = "MULTIPOLYGON (((13237085.097199999 3778520.556999993, 13237090.091400003 3778517.0431000027, 13237097.079599999 3778488.5896999994, 13237093.502000004 3778377.8165000025, 13237046.453 3778377.2816, 13236998.423500001 3778372.2782000024, 13236982.3832 3778367.389300006, 13236978.8411 3778516.0385999964, 13237073.3496 3778521.8364999993, 13237085.097199999 3778520.556999993)))";
        String temp = "MULTIPOLYGON (((13237085.097199999 3778520.556999993, 13237090.091400003 3778517.0431000027, 13237097.079599999 3778488.5896999994, 13237093.502000004 3778377.8165000025, 13237046.453 3778377.2816, 13236998.423500001 3778372.2782000024, 13236982.3832 3778367.389300006, 13236978.8411 3778516.0385999964, 13237073.3496 3778521.8364999993, 13237085.097199999 3778520.556999993)))";
        String wkt2 = ProjectionConverter.converter(temp);//转换后
        System.out.println(wkt2);
        double hausdorffDistance = calculateHausdorffDistance(wkt1, wkt2);
        System.out.println("Hausdorff 距离: " + hausdorffDistance);
    }

    public static double calculateHausdorffDistance(String wkt1, String wkt2) {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        try {
            Geometry geom1 = reader.read(wkt1);
            Geometry geom2 = reader.read(wkt2);
            DiscreteHausdorffDistance hausdorffDistance = new DiscreteHausdorffDistance(geom1, geom2);
            return hausdorffDistance.distance();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}