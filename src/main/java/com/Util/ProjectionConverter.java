package com.Util;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;

/**
 * @author JXS 坐标系的转换
 */
public class ProjectionConverter {

    public static String converter(String wkt ) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem sourceCRS = factory.createFromName("EPSG:3857");
        CoordinateReferenceSystem targetCRS = factory.createFromName("EPSG:4326");

        String[] coordinates = wkt.replaceAll("[^0-9.\\s,]", "").split("\\s|,");

        StringBuilder convertedWKT = new StringBuilder("MULTIPOLYGON (");

        for (int i = 0; i < coordinates.length; i += 2) {
            if (i + 1 < coordinates.length && !coordinates[i].isEmpty() && !coordinates[i + 1].isEmpty()) {
                double x = Double.parseDouble(coordinates[i]);
                double y = Double.parseDouble(coordinates[i + 1]);

                ProjCoordinate sourceCoord = new ProjCoordinate(x, y);
                ProjCoordinate targetCoord = new ProjCoordinate();

                sourceCRS.getProjection().inverseProject(sourceCoord, targetCoord);
                targetCRS.getProjection().project(targetCoord, sourceCoord);

                convertedWKT.append(targetCoord.x).append(" ").append(targetCoord.y);

                if (i < coordinates.length - 2) {
                    convertedWKT.append(", ");
                }
            }
        }

        convertedWKT.append(")");

      //  System.out.println(convertedWKT.toString());

        return convertedWKT.toString();
    }
}