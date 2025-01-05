package com.demo.overall.impl.matrix;

import com.Bean.Pair;
import com.Bean.PathResult;
import com.Bean.RealNodeInfo;
import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GetFinalResultByMatrix {
    public   List<Integer[]> getFinalResultByMatrix(String caoTuLabel,String realLabel) {
        CalculateGroupSim d = new CalculateGroupSim();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = d.firstFilter(caoTuLabel,realLabel); // 对应的结果下标
        GetFinalMatrix2 getFinalMatrix = new GetFinalMatrix2();
        List<Integer[]> realResultList = new ArrayList<>();

        List<PathResult> resulyList = getFinalMatrix.getResulyList(caoTuLabel,realLabel);

        // 输出前N个结果
       // System.out.println("根据权值排序的结果:");
        if (resulyList.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (PathResult result : resulyList) {
                List<String> path = result.getPath();
                List<Integer> indexList = result.getIndexList();
                Integer[] temp = new Integer[path.size()-1];
                for (int i = 0; i < path.size()-1; i++) {
                    List<RealNodeInfo> realNodeInfos = sketchToRealMap.get(Integer.parseInt(path.get(i)));
                    int realNodeId = realNodeInfos.get(indexList.get(i)).getRealNodeId();
                    temp[i] = realNodeId;
                }
                realResultList.add(temp);

                if (realResultList.size()>=200){
                    break;
                }
            }

        }
        return realResultList;
    }

    public List<Pair<Double, Integer[]>> getFinalResult(String caoTuLabel,String realLabel) {
        CalculateGroupSim d = new CalculateGroupSim();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = d.firstFilter(caoTuLabel,realLabel); // 对应的结果下标
        GetFinalMatrix2 getFinalMatrix = new GetFinalMatrix2();
        List<Pair<Double, Integer[]>> realResultList = new ArrayList<>();

        List<PathResult> resultList = getFinalMatrix.getResulyList(caoTuLabel,realLabel);

        // 输出前N个结果
        if (resultList.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (PathResult result : resultList) {
                List<String> path = result.getPath();
                List<Integer> indexList = result.getIndexList();
                Integer[] temp = new Integer[path.size() - 1];
                for (int i = 0; i < path.size() - 1; i++) {
                    List<RealNodeInfo> realNodeInfos = sketchToRealMap.get(Integer.parseInt(path.get(i)));
                    int realNodeId = realNodeInfos.get(indexList.get(i)).getRealNodeId();
                    temp[i] = realNodeId;
                }

                // 获取权值
                double weight = result.getWeight();

                // 将权值和对应的整数数组加入结果列表
                realResultList.add(new Pair<>(weight, temp));

                if (realResultList.size() >= 200) {
                    break;
                }
            }
        }

        // 按照权值降序排序
        realResultList.sort((a, b) -> {
            int comparison = Double.compare(b.getKey(), a.getKey());
            if (comparison == 0) {
                // 如果权值相等，可以根据其他标准排序，例如根据数组的哈希值
                return Integer.compare(Arrays.hashCode(a.getValue()), Arrays.hashCode(b.getValue()));
            }
            return comparison;
        });

        return realResultList;
    }
}