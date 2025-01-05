package com.Service.impl;

import com.Bean.GroupMap;
import com.Bean.Pair;
import com.Bean.PathResult;
import com.Service.PartService;
import com.controller.DataController;
import com.demo.overall.impl.matrix.GetFinalMatrix2;
import com.demo.overall.impl.matrix.GetFinalResultByMatrix;
import com.demo.part.AreaComparison;
import com.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PartServiceImpl implements PartService {

    @Resource
    private RedisService redisService;

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
    public  List<String[]> getIconicFeatureList(String caoTuLabel,String realLabel) {
        List<String[]> result = new ArrayList<>();
        HashMap<Integer, List<GroupMap>> groupIdMap = neo4jService.getGroupIdMap();

        // 从 GetFinalMatrix2 和 GetFinalResultByMatrix 获取路径
        List<PathResult> resulyList = getFinalMatrix.getResulyList(caoTuLabel,realLabel);
        List<Integer[]> finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);

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
    public  HashMap<String, Double> getPartSim1Map(String caoTuLabel,String realLabel){
        HashMap<String, Double> locationMap = new HashMap<>();

        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
            resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }

        List<String[]> iconicFeatureList = getIconicFeatureList(caoTuLabel,realLabel); // 获取标志性地物列表

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            // 获取当前路径的前 length 个元素
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
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
                    Double partLocationSim = neo4jService.getPartLocationSimByType(Integer.parseInt(g1), g2,"null");
                    locationMap.put(g1 + "-" + g2, partLocationSim);
                }
            }
        }
        return locationMap;
    }
    @Override
    public  HashMap<String, Double> getPartSim2MapByNearOrder(String caoTuLabel,String realLabel){
        HashMap<String, Double> orderMap = new HashMap<>();

        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
            resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            // 获取当前路径的前 length 个元素
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            for (int k = 0; k < path1.size(); k++) {
                String g1 = path1.get(k);
                Integer g2 = path2.get(k);


                if (orderMap.get(g1 + "-" + g2) == null) {
                    // 获取相似性分数并存储
                    Double partLocationSim = neo4jService.getPartOrderSimByNear(Integer.parseInt(g1), g2);
                    orderMap.put(g1 + "-" + g2, partLocationSim);
                }
            }
        }
        return orderMap;
    }

    @Override
    public  HashMap<String, Double> getPartSim2MapByNextToOrder(String caoTuLabel,String realLabel){
        HashMap<String, Double> orderMap = new HashMap<>();

        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

        // 检索数据
        resultList = redisService.getPathResults("pathResults");
        finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
        if(resultList == null){
            // 从 GetFinalMatrix2  获取路径
            resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
            // 存储数据
            if (resultList != null) {
                redisService.savePathResults("pathResults", resultList);
            }
        }
        if(finalResultByMatrix == null){
            // GetFinalResultByMatrix 获取路径
            finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
            //存储数据
            if (finalResultByMatrix != null) {
                redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
            }
        }

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            // 获取当前路径的前 length 个元素
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            for (int k = 0; k < path1.size(); k++) {
                String g1 = path1.get(k);
                Integer g2 = path2.get(k);


                if (orderMap.get(g1 + "-" + g2) == null) {
                    // 获取相似性最高的分数并存储
                    Double partLocationSim = neo4jService.getPartOrderSimByNextTo(Integer.parseInt(g1), g2);
                    orderMap.put(g1 + "-" + g2, partLocationSim);
                }
            }
        }
        return orderMap;
    }

    public  HashMap<String, Double> getPartSim3Map(String caoTuLabel,String realLabel){
        HashMap<String, Double> locationMap = new HashMap<>();
        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
            resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }



    // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            // 获取当前路径的前 length 个元素
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            for (int k = 0; k < path1.size(); k++) {
                String g1 = path1.get(k);
                Integer g2 = path2.get(k);

                // 检查 locationMap 中是否已经存在相应的键 这里是获取拥有显著性地物的计算
                if (locationMap.get(g1 + "-" + g2) == null) {
                    // 获取相似性分数并存储
                    Double partLocationSim = neo4jService.getPartDistance(Integer.parseInt(g1), g2);
                    locationMap.put(g1 + "-" + g2, partLocationSim);
                }
            }
        }
        return locationMap;
    }


    @Override
    public List<Double[]> getPartSim1(String caoTuLabel,String realLabel) {
        HashMap<String, Double> partSim1Map = getPartSim1Map(caoTuLabel,realLabel);
        List<Double[]> result = new ArrayList<>();

        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
            resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            Double[] temp = getArray(path1, path2, partSim1Map,length);

            result.add(temp);
        }
        return result;
    }

    /**
     * 利用地物类型与地物类型进行存储顺序关系
     * @return
     */
    @Override
    public List<Double[]> getPartSim2(String caoTuLabel,String realLabel) {
//        HashMap<String, Double> partSim1Map = getPartSim2MapByNearOrder();
        HashMap<String, Double> partSim1Map = getPartSim2MapByNextToOrder(caoTuLabel,realLabel);

        List<Double[]> result = new ArrayList<>();

        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
            resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            Double[] temp = getArray(path1, path2, partSim1Map,length);

            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Double[]> getPartSim3(String caoTuLabel,String realLabel) {
        HashMap<String, Double> partSim1Map = getPartSim3Map(caoTuLabel,realLabel);
        List<Double[]> result = new ArrayList<>();

        List<PathResult> resultList =null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
             resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }

        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            Double[] temp = getArray(path1, path2, partSim1Map,length);

            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Double> getPartSim(String caoTuLabel,String realLabel) {
        HashMap<String, Double> partSim1Map = getPartSim1Map(caoTuLabel,realLabel);
        HashMap<String, Double> partSim2Map = getPartSim2MapByNearOrder(caoTuLabel,realLabel);
        HashMap<String, Double> partSim3Map = getPartSim3Map(caoTuLabel,realLabel);
        List<Double> finalSimResult = new ArrayList<>();

        List<PathResult> resultList = null;
        List<Integer[]> finalResultByMatrix = null;

            // 检索数据
            resultList = redisService.getPathResults("pathResults");
            finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
            if(resultList == null){
                // 从 GetFinalMatrix2  获取路径
                resultList= getFinalMatrix.getResulyList(caoTuLabel,realLabel);
                // 存储数据
                if (resultList != null) {
                    redisService.savePathResults("pathResults", resultList);
                }
            }
            if(finalResultByMatrix == null){
                // GetFinalResultByMatrix 获取路径
                finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
                //存储数据
                if (finalResultByMatrix != null) {
                    redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
                }
            }


        // 遍历并比较每对路径
        int length = finalResultByMatrix.get(0).length;
        for (int i = 0; i < finalResultByMatrix.size(); i++) {
            List<String> path1 = resultList.get(i).getPath().stream().limit(length).collect(Collectors.toList());
            List<Integer> path2 = Arrays.asList(finalResultByMatrix.get(i));

            double avg1 = getAvg(getArray(path1, path2, partSim1Map,length));
            double avg2 = getAvg(getArray(path1, path2, partSim2Map,length));
            double avg3 = getAvg(getArray(path1, path2, partSim3Map,length));
            double finalAvg = (avg1+avg2+avg3)/3;

            finalSimResult.add(finalAvg);
        }
        return finalSimResult;
    }

    @Override
    public List<Pair<Double, Integer[]>> getFinalList(String caoTuLabel,String realLabel) {
        // 使用传统的 for 循环提取 weight 属性
        List<Double> weights = new ArrayList<>();

        List<PathResult> resultList = null;
        List<Integer[]> finalResultByMatrix = null;
        List<Double> partSimList = null;

        // 检索数据
        resultList = redisService.getPathResults("pathResults");
        finalResultByMatrix = redisService.getIntegerArrays("integerArrays");
        partSimList = redisService.getDoubleArrays("partSimList");

        if (resultList == null) {
            // 从 GetFinalMatrix2 获取路径
            resultList = getFinalMatrix.getResulyList(caoTuLabel,realLabel);
            // 存储数据
            if (resultList != null) {
                redisService.savePathResults("pathResults", resultList);
            }
        }
        if (finalResultByMatrix == null) {
            // GetFinalResultByMatrix 获取路径
            finalResultByMatrix = getFinalResultByMatrix.getFinalResultByMatrix(caoTuLabel,realLabel);
            // 存储数据
            if (finalResultByMatrix != null) {
                redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
            }
        }
        if (partSimList == null) {
            partSimList = getPartSim(caoTuLabel,realLabel);
            // 存储数据
            if (partSimList != null) {
                redisService.saveDoubleArrays("partSimList", partSimList);
            }
        }

        // 填充权重数组
        if (resultList != null) {
            for (PathResult pathResult : resultList) {
                weights.add(pathResult.getWeight());
            }
        }

        // 使用 List 存储相似度和对应的整数数组
        List<Pair<Double, Integer[]>> similarityList = new ArrayList<>();
        for (int i = 0; i < partSimList.size(); i++) {
            double temp = (partSimList.get(i) + weights.get(i)) / 2;
            similarityList.add(new Pair<>(temp, finalResultByMatrix.get(i)));
        }

        // 将 List 按照相似度降序排序
        similarityList.sort((a, b) -> {
            int comparison = Double.compare(b.getKey(), a.getKey());
            if (comparison == 0) {
                // 如果相似度相等，可以根据其他标准排序，例如根据索引或其他属性
                return Integer.compare(Arrays.hashCode(a.getValue()), Arrays.hashCode(b.getValue()));
            }
            return comparison;
        });

        // 返回包含相似度和整数数组的列表
        return similarityList;
    }

    //获取组的平均值
    private double getAvg(Double[] temp1){
        // 计算平均值
        double sum = 0.0;
        int count = 0;
        for (Double value : temp1) {
            if (value != null) {
                sum += value;
                count++;
            }
        }
        return count > 0 ? sum / count : 0.0;
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
