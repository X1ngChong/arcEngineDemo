package com.Util;

import java.util.ArrayList;
import java.util.List;

public class Neo4jCalculatePointUtil {
    /**
     * 根据list box算出中心point
     * 返回一个List数组
     * @param bbox
     * @return
     */
    public static  List<Object>  calculateCenter(List<Object> bbox) {
         double  longitude1 = (double) bbox.get(0);
        double latitude1 =  (double) bbox.get(1);
        double longitude2 =  (double) bbox.get(2);
        double latitude2 =  (double) bbox.get(3);

        double x = (longitude1 + longitude2) / 2;
        double y = (latitude1 + latitude2) / 2;
        List<Object> temp = new  ArrayList<Object>();
        temp.add(x);
        temp.add(y);

        return  temp ;
    }
}