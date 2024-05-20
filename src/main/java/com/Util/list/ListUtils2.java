package com.Util.list;

import java.util.*;

/**
 * 使用权值集合进行排序
 * @author JXS
 */
public class ListUtils2 {

    public HashMap<String, List<String[]>> sortAndFilterList(ArrayList<String[]> list) {
        if (list == null) {
            return new HashMap<>();
        }

        // 第一步：使用HashMap来存储第一个元素及其对应的所有String[]列表
        HashMap<String, List<String[]>> map = new HashMap<>();

        // 第二步：填充HashMap
        for (String[] item : list) {
            if (item != null && item.length > 0) {
                String firstElement = item[0];
                map.computeIfAbsent(firstElement, k -> new ArrayList<>()).add(item);
            }
        }

        // 第三步：创建一个新的HashMap来存储排序和过滤后的结果
        HashMap<String, List<String[]>> sortedAndFilteredList = new HashMap<>();

        // 第四步：将Map按每个列表的大小降序排序
        List<Map.Entry<String, List<String[]>>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()));

        // 第五步：遍历排序后的entrySet
        for (Map.Entry<String, List<String[]>> entry : sortedEntries) {
            String firstElement = entry.getKey();
            // 取第一个匹配的String[]列表，并添加到结果中
            List<String[]> items = entry.getValue();
            if (!items.isEmpty()) {
                sortedAndFilteredList.put(firstElement, items);
            }
        }
        return sortedAndFilteredList;
    }

    // 将HashMap转换为ArrayList
    public ArrayList<String[]> convertMapToList(HashMap<String, List<String[]>> map) {
        ArrayList<String[]> resultList = new ArrayList<>();

        // 遍历map中的每个List，并将第一个元素添加到resultList中
        for (List<String[]> list : map.values()) {
            resultList.add(list.get(0));
        }

        return resultList;
    }
}