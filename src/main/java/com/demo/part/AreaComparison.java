package com.demo.part;

import com.Bean.GroupMap;
import com.Bean.PathResult;
import com.demo.overall.impl.matrix.GetFinalMatrix2;
import com.demo.overall.impl.matrix.GetFinalResultByMatrix;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用于比较两个地区是否有相同的标志性地物
 */
@Component
public class AreaComparison {

    private LandmarkCollection landmarkCollection;

    public AreaComparison() {
        this.landmarkCollection = new LandmarkCollection();
    }

    /**
     * 获取两个地区的共同标志性地物类型
     * @param area1 地区1的GroupMap列表
     * @param area2 地区2的GroupMap列表
     * @return 共同标志性地物类型的集合
     */
    public Set<String> getCommonLandmarks(List<GroupMap> area1, List<GroupMap> area2) {
        Set<String> area1Landmarks = area1.stream()
                .map(GroupMap::getType)
                .filter(landmarkCollection::isLandmark)
                .collect(Collectors.toSet());

        Set<String> area2Landmarks = area2.stream()
                .map(GroupMap::getType)
                .filter(landmarkCollection::isLandmark)
                .collect(Collectors.toSet());

        // 取交集获取共同的标志性地物类型
        area1Landmarks.retainAll(area2Landmarks);
        return area1Landmarks;
    }

    /**
     * 返回路径中每对地区的共同标志性地物类型数组
     * @param path1 第一个路径的地区ID列表
     * @param path2 第二个路径的地区ID列表
     * @param groupIdMap 地区ID到GroupMap列表的映射
     * @return 包含每对地区共同标志性地物类型的数组
     */
    public String[] getCommonLandmarksArray(List<String> path1, List<Integer> path2, HashMap<Integer, List<GroupMap>> groupIdMap) {
        String[] commonLandmarksArray = new String[path1.size()];

        for (int i = 0; i < path1.size(); i++) {
            Integer area1Id = Integer.parseInt(path1.get(i)); // 将String类型的ID转换为Integer
            Integer area2Id = path2.get(i);

            List<GroupMap> area1 = groupIdMap.get(area1Id);
            List<GroupMap> area2 = groupIdMap.get(area2Id);

            Set<String> commonLandmarks = getCommonLandmarks(area1, area2);
            if (!commonLandmarks.isEmpty()) {
                commonLandmarksArray[i] = commonLandmarks.iterator().next(); // 获取第一个共同的标志性地物
            } else {
                commonLandmarksArray[i] = "null";
            }
        }

        return commonLandmarksArray;
    }

}