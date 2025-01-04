package com.Util.list;

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

        // 第二步：将Map按value降序排序 sortedEntries存储带权值的map
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
                //TODO 在这进行相同草图间数据的筛选,把最相似的放在最上面
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
}
