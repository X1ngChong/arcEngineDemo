package com.Method;

import com.esri.arcgis.geometry.Point;

public class PointDirection {

    //后续通过地物之间的重心来比较


    //根据点之间的角度算方向
    public static String getDirection(Point p1, Point p2) {
        double jiaodu = getJiaoDu(p1, p2);

        if ((jiaodu > 0 && jiaodu <= 22.5) || (jiaodu < 360 && jiaodu >= 337.5))
            return "North";
        else if (jiaodu > 22.5 && jiaodu <= 67.5)
            return "NorthEast";
        else if (jiaodu <= 112.5 && jiaodu > 67.5)
            return "East";
        else if (jiaodu > 112.5 && jiaodu <= 157.5)
            return "SouthEast";
        else if (jiaodu > 157.5 && jiaodu <= 202.5)
            return "South";
        else if (jiaodu > 202.5 && jiaodu <= 247.5)
            return "SouthWest";
        else if (jiaodu > 247.5 && jiaodu <= 292.5)
            return "West";
        else if (jiaodu > 292.5 && jiaodu <= 337.5)
            return "NorthWest";
        else
            return "错误角度";
    }

    //算点之间的角度
    public static double getJiaoDu(Point p1, Point p2) {

        double r2 = 0;
        try {
            double x1 = p1.getX();
            double y1 = p1.getY();
            double x2 = p2.getX();
            double y2 = p2.getY();

            double r1 = Math.atan2(y2 - y1, x2 - x1);
            double pi = Math.PI;

            if ((x2 - x1) > 0 && (y2 - y1) > 0)
                r2 = 90 - r1 * 180 / pi;
            if ((x2 - x1) < 0 && (y2 - y1) > 0)
                r2 = 270 - r1 * 180 / pi;
            if ((x2 - x1) < 0 && (y2 - y1) < 0)
                r2 = 270 - r1 * 180 / pi;
            if ((x2 - x1) > 0 && (y2 - y1) < 0)
                r2 = 90 - r1 * 180 / pi;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return r2;
    }

}
