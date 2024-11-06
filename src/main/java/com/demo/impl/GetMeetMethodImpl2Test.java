package com.demo.impl;

import com.Bean.GroupRelationship;
import com.demo.NewDemoRun.Demo5;
import com.demo.NewDemoRun.Demo6;

import java.util.*;

/**
 * @author JXS
 */
public class GetMeetMethodImpl2Test {
    public static void main(String[] args) {
        Demo6 d6 = new Demo6();
        Demo5 d5 = new Demo5();

        List<Integer> caoTuIds = new ArrayList<>();
        List<GroupRelationship> realMeetsList = d6.getMeetList("xianLinGroup");
        List<GroupRelationship> caoTuMeetsList = d6.getMeetList("Group");// 创建草图关系映射


    }

}