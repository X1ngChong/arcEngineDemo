package com.demo.NewDemoRun.meetRelation;

import java.util.HashMap;
import java.util.Map;

/**
 * 完善后的匹配结果
 * 未找到匹配: 草图节点 ID = 28
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 99, 类型 = school, 草图数量 = 2, 真实数量 = 2
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 99, 类型 = resident, 草图数量 = 4, 真实数量 = 4
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 100, 类型 = school, 草图数量 = 2, 真实数量 = 2
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 100, 类型 = resident, 草图数量 = 4, 真实数量 = 4
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 104, 类型 = school, 草图数量 = 2, 真实数量 = 2
 * 匹配: 草图节点 ID = 29, 真实节点 ID = 104, 类型 = resident, 草图数量 = 4, 真实数量 = 4
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 96, 类型 = resident, 草图数量 = 3, 真实数量 = 3
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 97, 类型 = resident, 草图数量 = 3, 真实数量 = 3
 * 匹配: 草图节点 ID = 30, 真实节点 ID = 103, 类型 = resident, 草图数量 = 3, 真实数量 = 3
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 类型 = square, 草图数量 = 1, 真实数量 = 1
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 类型 = school, 草图数量 = 2, 真实数量 = 2
 * 匹配: 草图节点 ID = 31, 真实节点 ID = 98, 类型 = resident, 草图数量 = 2, 真实数量 = 2
 */
public class NodeData {
    public Map<String, Integer> typesCount = new HashMap<>();

    public void addType(String type, int count) {
        typesCount.put(type, count);
    }

    // 获取所有类型数量的总和
    public int getTotalCount() {
        return typesCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "typesCount=" + typesCount +
                '}';
    }
}
