package com.demo.overall.impl.matrix;

import com.Bean.PathResult;
import com.Bean.RealNodeInfo;
import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 根据对应的下标去获取真实的groupId
 真实结果列表:
 0:[98, 94, 100, 96]
 1:[95, 93, 99, 92]
 2:[95, 93, 92, 99]
 3:[95, 99, 93, 92]
 4:[95, 99, 92, 93]
 5:[98, 96, 100, 94]
 6:[95, 92, 93, 99]
 7:[95, 92, 99, 93]
 8:[93, 95, 99, 92]
 9:[93, 95, 92, 99]
 10:[93, 99, 95, 92]
 11:[93, 99, 92, 95]
 12:[94, 100, 96, 98]
 13:[93, 92, 95, 99]
 14:[93, 92, 99, 95]
 15:[99, 92, 95, 93]
 16:[99, 92, 93, 95]
 17:[100, 96, 98, 94]
 18:[96, 98, 94, 100]
 19:[92, 95, 93, 99]
 20:[92, 95, 99, 93]
 21:[92, 99, 95, 93]
 22:[92, 99, 93, 95]
 23:[97, 101, 99, 92]
 24:[97, 101, 93, 91]
 25:[97, 92, 99, 101]
 26:[97, 91, 93, 101]
 27:[101, 99, 92, 97]
 28:[101, 93, 91, 97]
 29:[99, 92, 97, 101]
 30:[93, 91, 97, 101]
 31:[92, 99, 101, 97]
 32:[98, 94, 96, 100]
 33:[95, 93, 100, 96]
 34:[95, 97, 99, 92]
 35:[97, 93, 99, 92]
 36:[97, 101, 100, 96]
 37:[97, 101, 91, 93]
 38:[98, 100, 93, 92]
 39:[98, 100, 94, 96]
 40:[98, 100, 95, 99]
 41:[98, 100, 96, 94]
 42:[95, 99, 101, 91]
 43:[97, 93, 91, 101]
 44:[94, 99, 93, 92]
 45:[94, 96, 93, 92]
 46:[98, 92, 93, 100]
 47:[98, 96, 94, 100]
 48:[98, 99, 95, 100]
 49:[95, 92, 99, 97]
 50:[95, 96, 100, 93]
 51:[95, 91, 101, 99]
 52:[97, 92, 99, 93]
 53:[97, 96, 100, 101]
 54:[94, 92, 93, 99]
 55:[94, 92, 93, 96]
 56:[93, 95, 96, 100]
 57:[95, 99, 96, 100]
 58:[97, 100, 92, 99]
 59:[97, 100, 95, 94]
 60:[97, 100, 99, 94]
 61:[94, 93, 96, 100]
 62:[93, 94, 99, 92]
 63:[93, 94, 92, 99]
 64:[101, 96, 92, 99]
 65:[101, 96, 93, 95]
 66:[101, 96, 91, 93]
 67:[93, 99, 94, 92]
 68:[93, 99, 92, 97]
 69:[93, 100, 98, 92]
 70:[93, 100, 96, 95]
 71:[93, 96, 94, 92]
 72:[94, 100, 98, 96]
 73:[95, 100, 98, 99]
 74:[101, 99, 95, 91]
 75:[101, 100, 96, 97]
 76:[97, 99, 92, 95]
 77:[93, 92, 98, 100]
 78:[93, 92, 94, 99]
 79:[93, 92, 94, 96]
 80:[94, 96, 98, 100]
 81:[94, 96, 100, 98]
 82:[95, 99, 98, 100]
 83:[101, 91, 95, 99]
 84:[101, 91, 93, 97]
 85:[98, 98, 92, 93]
 86:[98, 98, 99, 95]
 87:[93, 93, 95, 91]
 88:[97, 94, 98, 101]
 89:[98, 96, 100, 94]
 90:[95, 96, 100, 93]
 91:[95, 96, 94, 93]
 92:[93, 96, 100, 91]
 93:[101, 96, 100, 91]
 94:[99, 92, 95, 97]
 95:[99, 92, 97, 93]
 96:[100, 96, 95, 93]
 97:[100, 96, 97, 101]
 98:[100, 96, 94, 98]
 99:[93, 91, 101, 97]
 * @author JXS
 */
@Component
public class GetFinalResultByMatrix {
    public static void main(String[] args) {
        GetFinalResultByMatrix getFinalResultByMatrix = new GetFinalResultByMatrix();
        List<Integer[]> finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix();
        GetFinalMatrix2 getFinalMatrix = new GetFinalMatrix2();
        List<PathResult> resulyList = getFinalMatrix.getResulyList();
                    // 格式化输出
            System.out.println("真实结果列表:");
       for (int i = 0; i < 100 ; i++) {
           System.out.println(i+":"+ Arrays.toString(finalResultByMatrix.get(i))+
                   ":"+ Arrays.toString(resulyList.get(i).getPath().stream().limit(4).toArray()));
       }

    }
    public   List<Integer[]> getFinalResultByMatrix() {
        CalculateGroupSim d = new CalculateGroupSim();
        Map<Integer, List<RealNodeInfo>> sketchToRealMap = d.firstFilter(); // 对应的结果下标
        GetFinalMatrix2 getFinalMatrix = new GetFinalMatrix2();
        List<Integer[]> realResultList = new ArrayList<>();

        List<PathResult> resulyList = getFinalMatrix.getResulyList();

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

                if (realResultList.size()>=100){
                    break;
                }
            }

        }
        return realResultList;
    }
}