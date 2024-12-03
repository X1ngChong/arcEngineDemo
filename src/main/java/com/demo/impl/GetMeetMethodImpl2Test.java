package com.demo.impl;

import com.Bean.GroupRelationship;

import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;
import com.demo.overall.NewDemoRun.meetRelation.Demo6;

import java.util.*;

/**
 * @author JXS
 */
public class GetMeetMethodImpl2Test {
    public static void main(String[] args) {
        Demo6 d6 = new Demo6();
        CalculateGroupSim d5 = new CalculateGroupSim();

        List<Integer> caoTuIds = new ArrayList<>();
        List<GroupRelationship> realMeetsList = d6.getMeetList("xianLinGroup");
        List<GroupRelationship> caoTuMeetsList = d6.getMeetList("Group");// 创建草图关系映射


    }

}