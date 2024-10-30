package com.Util;

import java.util.*;

public class UtilTest {
    public static void main(String[] args) {
        // 创建一个 Map  
        Map<List<String[]>, Double> map = new HashMap<>();

        // 创建一个 List<String[]> 作为键  
        List<String[]> key = new ArrayList<>();
        key.add(new String[]{"value1", "value2"});
        key.add(new String[]{"value3", "value4"});

        // 将键值对放入 Map 中  
        map.put(key, 10.5);

        // 取值  
        Double value = map.get(key);
        if (value != null) {
            System.out.println("取到的值: " + value);
        } else {
            System.out.println("未找到对应的值");
        }
    }
}