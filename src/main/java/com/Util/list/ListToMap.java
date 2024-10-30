package com.Util.list;

import java.util.*;

public class ListToMap {

    public static Map<String[], Double> populateMap(ArrayList<String[]> list,Map<String[], Double> map) {
        for (String[] array : list) {
            // 将 String[] 转换为 List<String[]> 作为键
            // 将键和对应的值 (1.0) 放入 Map 中  
            map.put(array, 1.0);
        }
        return map;
    }

    public static  void printMap(Map<String[], Double> map) {
        for (Map.Entry<String[], Double> entry : map.entrySet()) {
            System.out.println("Key: " + Arrays.toString(entry.getKey()) + ", Value: " + entry.getValue());
        }
    }

}