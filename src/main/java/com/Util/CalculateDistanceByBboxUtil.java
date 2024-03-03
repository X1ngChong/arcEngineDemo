package com.Util;

import java.util.List;

public class CalculateDistanceByBboxUtil {
    /**
     *
     * @param c1 原始数组
     * @param c2
     * @return
     */

    public static double calculate(List<Object>  c1,  List<Object>  c2){
        List<Object> o1 = Neo4jCalculatePointUtil.calculateCenter(c1);
        List<Object> o2 = Neo4jCalculatePointUtil.calculateCenter(c2);

        return CalculateDistanceByBboxUtil.calculateDis(o1,o2);
    }



    /**
     * 计算俩中心点的距离
     * @param c1 中心点1
     * @param c2 中心点2
     * @return
     */
    public static double calculateDis(List<Object>  c1,  List<Object>  c2){
        return haversine((Double) c1.get(0), (Double) c1.get(1),(Double)c2.get(0),(Double)c2.get(1));

    }



    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371e3 * c; // 6371e3 是地球半径（单位是米）
    }

}
