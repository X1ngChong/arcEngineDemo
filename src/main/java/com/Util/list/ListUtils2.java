package com.Util.list;

import com.neo4j.hausdorffInNeo4j.FilterByHausdorff;

import java.util.*;

/**
 * 使用权值集合进行排序
 * @author JXS
 */
public class ListUtils2 {

    public  ArrayList<String[]> sortAndFilterList(ArrayList<String[]> list, ArrayList<String> geometryList) {
        if (list == null) {
            return new ArrayList<String[]>();
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

        // 第三步：创建一个新的ArrayList<String[]>来存储排序和过滤后的结果
        ArrayList<String[]> sortedAndFilteredList = new ArrayList<>();

        // 第四步：将Map按每个列表的大小降序排序
        List<Map.Entry<String, List<String[]>>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()));

        // 第五步：遍历排序后的entrySet
        for (Map.Entry<String, List<String[]>> entry : sortedEntries) {
            // 取第一个匹配的String[]列表，并添加到结果中
            List<String[]> items = entry.getValue();
            if (!items.isEmpty()) {
                sortedAndFilteredList.add( FilterByHausdorff.doFilter(geometryList,items));//添加值最低的那个
            }
        }
        return sortedAndFilteredList;
    }

}