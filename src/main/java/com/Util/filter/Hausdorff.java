package com.Util.filter;

import org.locationtech.jts.algorithm.distance.DiscreteHausdorffDistance;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;

public class Hausdorff {
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

    public static Double calculateHausdorffDistance(ArrayList<String> geometryList, ArrayList<String> realGeometryList) {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);

        Double result = 0.0;
      //  ArrayList<Double> hausdorffDistances = new ArrayList<>();

        for (int i = 0; i < geometryList.size(); i++) {
            try {
                Geometry geom1 = reader.read(geometryList.get(i));
                Geometry geom2 = reader.read(realGeometryList.get(i));
                DiscreteHausdorffDistance hausdorffDistance = new DiscreteHausdorffDistance(geom1, geom2);
                result+=hausdorffDistance.distance();
               // hausdorffDistances.add(hausdorffDistance.distance());
            } catch (ParseException e) {
                e.printStackTrace();
                result+=0.0;
              //  hausdorffDistances.add(0.0);
            }
        }
        result= result/geometryList.size();//取平均值

        return result;
    }

}
