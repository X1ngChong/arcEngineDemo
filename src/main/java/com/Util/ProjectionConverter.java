package com.Util;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JXS 坐标系的转换
 */
public class ProjectionConverter {

    public static String converter(String wkt) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem sourceCRS = factory.createFromName("EPSG:3857");
        CoordinateReferenceSystem targetCRS = factory.createFromName("EPSG:4326");

        String[] coordinates = wkt.replaceAll("[^0-9.\\s,]", "").split("\\s+|,\\s*");

        List<String> convertedCoordinates = new ArrayList<>();

        for (int i = 0; i < coordinates.length; i++) {
            if (!coordinates[i].isEmpty()) {
                double x = Double.parseDouble(coordinates[i]);
                i++;
                if (i < coordinates.length && !coordinates[i].isEmpty()) {
                    double y = Double.parseDouble(coordinates[i]);

                    ProjCoordinate sourceCoord = new ProjCoordinate(x, y);
                    ProjCoordinate targetCoord = new ProjCoordinate();

                    sourceCRS.getProjection().inverseProject(sourceCoord, targetCoord);
                    targetCRS.getProjection().project(targetCoord, sourceCoord);

                    convertedCoordinates.add(targetCoord.x + " " + targetCoord.y);
                }
            }
        }

        String convertedWKT = "MULTIPOLYGON (((" + String.join(", ", convertedCoordinates) + ")))";

        return convertedWKT;
    }

    public static ArrayList<String> converter(ArrayList<String> geometryList) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem sourceCRS = factory.createFromName("EPSG:3857");
        CoordinateReferenceSystem targetCRS = factory.createFromName("EPSG:4326");

        ArrayList<String> convertedGeometryList = new ArrayList<>();

        for (String wkt : geometryList) {
            String[] coordinates = wkt.replaceAll("[^0-9.\\s,]", "").split("\\s+|,\\s*");

            List<String> convertedCoordinates = new ArrayList<>();

            for (int i = 0; i < coordinates.length; i++) {
                if (!coordinates[i].isEmpty()) {
                    double x = Double.parseDouble(coordinates[i]);
                    i++;
                    if (i < coordinates.length && !coordinates[i].isEmpty()) {
                        double y = Double.parseDouble(coordinates[i]);

                        ProjCoordinate sourceCoord = new ProjCoordinate(x, y);
                        ProjCoordinate targetCoord = new ProjCoordinate();

                        sourceCRS.getProjection().inverseProject(sourceCoord, targetCoord);
                        targetCRS.getProjection().project(targetCoord, sourceCoord);

                        convertedCoordinates.add(targetCoord.x + " " + targetCoord.y);
                    }
                }
            }

            String convertedWKT = "MULTIPOLYGON (((" + String.join(", ", convertedCoordinates) + ")))";

            convertedGeometryList.add(convertedWKT.toString());
        }

        return convertedGeometryList;
    }
}