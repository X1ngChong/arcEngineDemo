package com.demo.impl;

import com.Bean.GroupRelationship;
import com.demo.NewDemoRun.meetRelation.Demo6;

import java.util.*;

public class GetMeetMethodImplTest {
    public static void main(String[] args) {
        Demo6 d6 = new Demo6();
        List<GroupRelationship> realMeetsList = d6.getMeetList("xianLinGroup");
        List<GroupRelationship> caoTuMeetsList = d6.getMeetList("Group");

//        System.out.println("这是草图的关系");
//        caoTuMeetsList.forEach(System.out::println);
//
        System.out.println("这是真实图谱的关系");
        realMeetsList.forEach(System.out::println);

        // 创建草图关系映射
       // {112=[110, 113], 113=[112, 111], 110=[112, 111], 111=[110, 113]}
        Map<Integer, List<Integer>> sketchRelationMap = new HashMap<>();
        for (GroupRelationship relationship : caoTuMeetsList) {
            sketchRelationMap.putIfAbsent(relationship.getBlock1Id(), new ArrayList<>());
            sketchRelationMap.get(relationship.getBlock1Id()).add(relationship.getBlock2Id());
        }

        // 创建真实关系映射
       // {96=[94, 100], 97=[95, 93, 101], 98=[94, 100], 99=[95, 94, 92], 100=[98, 96], 101=[97, 91], 91=[101, 93], 92=[99, 93], 93=[92, 91, 97, 95], 94=[99, 98, 96, 95], 95=[94, 93, 99, 97]}
        Map<Integer, List<Integer>> realRelationMap = new HashMap<>();
        for (GroupRelationship relationship : realMeetsList) {
            realRelationMap.putIfAbsent(relationship.getBlock1Id(), new ArrayList<>());
            realRelationMap.get(relationship.getBlock1Id()).add(relationship.getBlock2Id());
        }

        // 查询符合草图相邻关系的真实图谱节点
        List<List<Integer>> potentialGroups = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : sketchRelationMap.entrySet()) {
            int startNode = entry.getKey();
            List<Integer> neighbors = entry.getValue();

            // 检查是否有可形成闭环的节点
            findMatchingGroups(startNode, neighbors, realRelationMap, potentialGroups);
        }

        // 输出符合条件的节点组合
        System.out.println("符合草图相邻关系的真实图谱节点组合:");
        for (List<Integer> group : potentialGroups) {
            System.out.println(group);
        }
    }

    private static void findMatchingGroups(int startNode, List<Integer> neighbors,
                                           Map<Integer, List<Integer>> realRelationMap, List<List<Integer>> potentialGroups) {
        // 形成单源邻接列表组合的所有可能性
        for (int neighbor : neighbors) {
            List<Integer> group = new ArrayList<>();
            group.add(startNode);
            group.add(neighbor);

            // 检查相邻节点是否在 realRelationMap 存在相同的邻接
            if (realRelationMap.containsKey(startNode) && realRelationMap.get(startNode).contains(neighbor)) {
                //寻找其余相邻节点
                for (int secondNeighbor : realRelationMap.get(neighbor)) {
                    if (!group.contains(secondNeighbor) && realRelationMap.containsKey(neighbor)
                            && realRelationMap.get(neighbor).contains(secondNeighbor)) {
                        group.add(secondNeighbor);
                        if (group.size() == 4) { // 确保是四个节点的组合
                            potentialGroups.add(new ArrayList<>(group));
                        }
                    }
                }
            }
        }
    }
}