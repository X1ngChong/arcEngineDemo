package com.demo.impl.matrix;

import com.Bean.RealNodeInfo;
import com.demo.NewDemoRun.meetRelation.CalculateGroupSim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 根据对应的下标去获取真实的groupId
 * @author JXS
 */
public class GetFinalResultByMatrix {
    public static void main(String[] args) {
        GetFinalResultByMatrix getFinalResultByMatrix = new GetFinalResultByMatrix();
        List<Integer[]> finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix();
                    // 格式化输出
            System.out.println("真实结果列表:");
            for (Integer[] realResult : finalResultByMatrix) {
                System.out.println(Arrays.toString(realResult));
            }
    }
    public   List<Integer[]> getFinalResultByMatrix() {
        CalculateGroupSim d = new CalculateGroupSim();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = d.firstFilter(); // 对应的结果下标
        GetFinalMatrix getFinalMatrix = new GetFinalMatrix();
        List<Integer[]> realResultList = new ArrayList<>();

        List<GetFinalMatrix.PathResult> resulyList = getFinalMatrix.getResulyList();

        // 输出前N个结果
       // System.out.println("根据权值排序的结果:");
        if (resulyList.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (GetFinalMatrix.PathResult result : resulyList) {
                List<String> path = result.getPath();
                List<Integer> indexList = result.getIndexList();
                Integer[] temp = new Integer[path.size()];
                for (int i = 0; i < path.size(); i++) {
                    List<RealNodeInfo> realNodeInfos = sketchToRealMap.get(Integer.parseInt(path.get(i)));
                    int realNodeId = realNodeInfos.get(indexList.get(i)).getRealNodeId();
                    temp[i] = realNodeId;
                realResultList.add(temp);
                }
            }

        }
        return realResultList;
    }
}