package com.Util;

import java.util.List;

public class CalculateLocation {

    public static double[] calculateCenterPoint(List<Object> bbox) {
        if (bbox == null || bbox.size() != 4) {
            throw new IllegalArgumentException("Invalid bbox format");
        }

        // 解析 bbox 的坐标值
        double minX = ((Number) bbox.get(0)).doubleValue();
        double minY = ((Number) bbox.get(1)).doubleValue();
        double maxX = ((Number) bbox.get(2)).doubleValue();
        double maxY = ((Number) bbox.get(3)).doubleValue();

        // 计算中心点坐标
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;

        // 返回中心点坐标
        return new double[]{centerX, centerY};
    }

    public static String calculateLocation(List<Object> bbox1,List<Object> bbox2) {
        double[] center1 = calculateCenterPoint(bbox1);
        double[] center2 = calculateCenterPoint(bbox2);
            String result = "";
        if (center1[0] < center2[0]) {
           result = result+"西";
        } else if (center1[0] > center2[0]) {
            result = result+"东";
        } else {//如果在同一点
            result = result+"";
        }

        // 比较 y 轴上的相对位置
        if (center1[1] < center2[1]) {
            result = result+"南";
        } else if (center1[1] > center2[1]) {
            result = result+"北";
        } else {//如果在同一点
            result = result+"";
        }

        // 返回中心点坐标
        return result;
    }

    public static double GetJiaoDu(List<Object> bbox1,List<Object> bbox2 )
    {
        double[] center1 = calculateCenterPoint(bbox1);
        double[] center2 = calculateCenterPoint(bbox2);
        double x1 = center1[0];
        double y1 = center1[1];
        double x2 = center2[0];
        double y2 = center2[1];

        double r1 = Math.atan((y2 - y1) / (x2 - x1));
        double r2 = 0;
        double pi = Math.PI;
        // 再根据条件将象限角θ转换为方位角α：
        if ((x2 - x1) > 0 && (y2 - y1) > 0)
            r2 = 90 - r1 / pi * 180;
        if ((x2 - x1) < 0 && (y2 - y1) > 0)
            r2 = 360 + r1 / pi * 180;
        if ((x2 - x1) < 0 && (y2 - y1) < 0)
            r2 = 270 - r1 / pi * 180;
        if ((x2 - x1) > 0 && (y2 - y1) < 0)
            r2 = 90 - r1 / pi * 180;


        return r2;
    }
    public static String GetDirection(List<Object> bbox1,List<Object> bbox2)
    {
        String result = "";
        double jiaodu = GetJiaoDu(bbox1, bbox2);
        if ((jiaodu <= 10) || (jiaodu > 350)){
            result += "北";}
        else if ((jiaodu > 10) && (jiaodu <= 80)){
            result += "东北";}
        else if ((jiaodu > 80) && (jiaodu <= 100)){
            result += "东";}
        else if ((jiaodu > 100) && (jiaodu <= 170)){
            result += "东南";}
        else if ((jiaodu > 170) && (jiaodu <= 190)){
            result += "南";}
        else if ((jiaodu > 190) && (jiaodu <= 260)){
            result += "西南";}
        else if ((jiaodu > 260) && (jiaodu <= 280)){
            result +="西";}
        else if ((jiaodu > 280) && (jiaodu <= 350)){
            result += "西北";}
            return result;
    }
}
