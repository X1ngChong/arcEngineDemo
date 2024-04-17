package com.Util;

import java.util.*;

/**
 * @author JXS
 */
public class ListUtils {
    private ArrayList<String[]> list = null;

    public ArrayList<String[]> sortAndFilterList(ArrayList<String[]> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        // 第一步：统计每个第一个元素出现的次数
        Map<String, Integer> countMap = countFirstElements(list);

        // 第二步：将Map按value降序排序
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(countMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 第三步：使用HashSet来确保不重复添加第一个元素
        HashSet<String> addedFirstElements = new HashSet<>();

        // 第四步：创建新的ArrayList来存储结果
        ArrayList<String[]> sortedAndFilteredList = new ArrayList<>();

        // 第五步：遍历排序后的entrySet
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String firstElement = entry.getKey();
            // 检查是否已添加过该第一个元素
            if (addedFirstElements.add(firstElement)) {
                // 查找原始list中第一个元素匹配的第一个String[]并添加到结果中
                for (String[] item : list) {
                    if (item != null && item.length > 0 && item[0].equals(firstElement)) {
                        sortedAndFilteredList.add(item);
                        break; // 找到后跳出循环
                    }
                }
            }
        }

        return sortedAndFilteredList;
    }

    // 这个方法用于统计每个第一个元素出现的次数，与之前的countFirstElements方法相同
    private Map<String, Integer> countFirstElements(ArrayList<String[]> list) {
        Map<String, Integer> countMap = new HashMap<>();

        for (String[] entry : list) {
            if (entry != null && entry.length > 0) {
                String firstElement = entry[0];
                countMap.put(firstElement, countMap.getOrDefault(firstElement, 0) + 1);
            }
        }

        return countMap;
    }

    public static void main(String[] args) {
        ListUtils sorter = new ListUtils();

        ArrayList<String[]> dataList = new ArrayList<>();
        dataList.add(new String[]{"156128805", "265949063", "156126719"});
        dataList.add(new String[]{"156128805", "265949063", "155915368"});
        dataList.add(new String[]{"156128805", "265949063", "155915372"});
        dataList.add(new String[]{"156128805", "265949063", "475183085"});
        dataList.add(new String[]{"156128805", "265949063", "156134103"});
        dataList.add(new String[]{"156128806", "265949063", "123456789"}); // 另一个不同的第一个元素

        // 调用方法来获取排序和过滤后的列表
        ArrayList<String[]> sortedAndFilteredList = sorter.sortAndFilterList(dataList);

        // 打印结果
        for (String[] item : sortedAndFilteredList) {
            System.out.println(Arrays.toString(item));
        }
    }
}
