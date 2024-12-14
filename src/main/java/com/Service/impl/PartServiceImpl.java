package com.Service.impl;

import com.Bean.GroupMap;
import com.Bean.PathResult;
import com.Service.PartService;
import com.demo.overall.impl.matrix.GetFinalMatrix2;
import com.demo.overall.impl.matrix.GetFinalResultByMatrix;
import com.demo.part.AreaComparison;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {

    @Resource
    private AreaComparison areaComparison;

    @Resource
    private Neo4jServiceImpl neo4jService;

    @Resource
    private GetFinalMatrix2 getFinalMatrix; // 从 GetFinalMatrix2 和 GetFinalResultByMatrix 获取路径

    @Resource
    private GetFinalResultByMatrix getFinalResultByMatrix;


    /**
     * 打印标志性地物列表 比较路径对 0: [mall, school, null, school]
     */
    @Override
    public  List<String[]> getIconicFeatureList() {
        List<String[]> result = new ArrayList<>();
        HashMap<Integer, List<GroupMap>> groupIdMap = neo4jService.getGroupIdMap();

        // 从 GetFinalMatrix2 和 GetFinalResultByMatrix 获取路径
        List<PathResult> resulyList = getFinalMatrix.getResulyList();
        List<Integer[]> finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix();

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            List<String> path1 = resulyList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            String[] commonLandmarksArray = areaComparison.getCommonLandmarksArray(path1, path2, groupIdMap);
            result.add(commonLandmarksArray);
        }
        return result;
    }

    /**
     * 获取每个组相对应的方位相似度
     */
    @Override
    public  HashMap<String, Double> getPartSim1Map(){
        HashMap<String, Double> locationMap = new HashMap<>();

        // 从 GetFinalMatrix2 和 GetFinalResultByMatrix 获取路径
        List<PathResult> resulyList = getFinalMatrix.getResulyList();
        List<Integer[]> finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix();
        List<String[]> iconicFeatureList = getIconicFeatureList(); // 获取标志性地物列表

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            // 获取当前路径的前 length 个元素
            List<String> path1 = resulyList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));
            String[] feature = iconicFeatureList.get(i); // 当前路径的标志性地物列表

            for (int k = 0; k < path1.size(); k++) {
                String g1 = path1.get(k);
                Integer g2 = path2.get(k);

                // 检查 locationMap 中是否已经存在相应的键 这里是获取拥有显著性地物的计算
                if (locationMap.get(g1 + "-" + g2) == null && feature[k] != null && !"null".equals(feature[k])) {
                    // 获取相似性分数并存储
                    Double partLocationSim = neo4jService.getPartLocationSimByType(Integer.parseInt(g1), g2, feature[k]);
                    locationMap.put(g1 + "-" + g2, partLocationSim);
                }
                // 检查 locationMap 中是否已经存在相应的键 这里是对没有显著性地物的计算
                if (locationMap.get(g1 + "-" + g2) == null && feature[k] != null && "null".equals(feature[k])) {
                    // 获取相似性分数并存储
                    Double partLocationSim = neo4jService.getPartLocationSimNoType(Integer.parseInt(g1), g2);
                    locationMap.put(g1 + "-" + g2, partLocationSim);
                }
            }
        }
        return locationMap;
    }

    @Override
    public List<Double[]> getPartSim1() {
        HashMap<String, Double> partSim1Map = getPartSim1Map();
        List<Double[]> result = new ArrayList<>();
        HashMap<Integer, List<GroupMap>> groupIdMap = neo4jService.getGroupIdMap();

        // 从 GetFinalMatrix2 和 GetFinalResultByMatrix 获取路径
        List<PathResult> resulyList = getFinalMatrix.getResulyList();
        List<Integer[]> finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix();

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            List<String> path1 = resulyList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            Double[] temp = getArray(path1, path2, partSim1Map,length);

            result.add(temp);
        }
        return result;
    }

    private  Double [] getArray(List<String> path1, List<Integer> path2 ,HashMap<String, Double> partSim1Map,Integer length){

        Double [] doubleArray = new Double[length];
        for (int k = 0;k<length;k++){
            String key = path1.get(k)+"-"+path2.get(k);
            if(partSim1Map.get(key)!=null){
                doubleArray[k]= partSim1Map.get(key);
            }else {
                doubleArray[k] = 0.0;
            }
        }

        return doubleArray;
    }
}
