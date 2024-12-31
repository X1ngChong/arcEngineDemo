package com.controller;

import com.Bean.PathResult;
import com.Service.Neo4jGetGroupNodesService;
import com.Service.PartService;
import com.demo.overall.impl.matrix.GetFinalMatrix2;
import com.demo.overall.impl.matrix.GetFinalResultByMatrix;

import com.redis.RedisService;
import com.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author JXS
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class DataController {

    @Resource
    private PartService partService;

    @Resource
    private RedisService redisService;

    @Resource
    private GetFinalResultByMatrix getFinalResultByMatrix;

    @Resource
    private GetFinalMatrix2 getFinalMatrix;

    @Resource
    private Neo4jGetGroupNodesService neo4jGetGroupNodesService;
    @GetMapping("/S10AndXianLin2")
    public ResponseData<ArrayList<Integer[]>> getS10AndXianLin2() {
        List<Integer[]> finalResultByMatrix = null;
        finalResultByMatrix = redisService.getIntegerArrays("integerArrays");

        if(finalResultByMatrix == null){
            // GetFinalResultByMatrix 获取路径
            finalResultByMatrix  = getFinalResultByMatrix.getFinalResultByMatrix();
            //存储数据
            if (finalResultByMatrix != null) {
                redisService.saveIntegerArrays("integerArrays", finalResultByMatrix);
            }
        }

        //获取OBJECTID作为主键去查找
        ArrayList<Integer[]> list = neo4jGetGroupNodesService.getObjectIdByIds(finalResultByMatrix);
        for (Integer [] temp:list
             ) {
            log.info("结果列表:{}",Arrays.toString(temp));
        }
        return ResponseData.succeed(list);
    }

    @GetMapping("/partMethod")
    public ResponseData partMethod() {
        List<String[]> iconicFeatureList = partService.getIconicFeatureList();
        for (String [] temp:iconicFeatureList) {
            log.info("标志性地物列表 : " + Arrays.toString(temp));
        }
        return ResponseData.succeed();
    }

    @GetMapping("/getPartSim1Map")
    public ResponseData getPartSim1Map() {
        HashMap<String, Double> partSim1Map = partService.getPartSim1Map();
        // 遍历 HashMap 的键值对
        for (Map.Entry<String, Double> entry : partSim1Map.entrySet()) {
            String key = entry.getKey(); // 获取键
            Double value = entry.getValue(); // 获取值
            log.info("节点ID: " + key + ", 相似度: " + value);
        }
        return ResponseData.succeed();
    }

    @GetMapping("/getPartSim1")
    public ResponseData getPartSim1() {
        List<Double[]> partSim1List = partService.getPartSim1();
        for (Double[] array : partSim1List) {
            // 计算平均值
            double sum = 0.0;
            int count = 0;
            for (Double value : array) {
                if (value != null) {
                    sum += value;
                    count++;
                }
            }
            double average = count > 0 ? sum / count : 0.0;

            // 记录整个 Double[] 数组和平均值
            log.info("Values: " + Arrays.toString(array) + ", Average: " + average);
        }
        return ResponseData.succeed();
    }

    @GetMapping("/getPartSim2")
    public ResponseData getPartSim2() {
        List<Double[]> partSim1List = partService.getPartSim2();
        for (Double[] array : partSim1List) {
            // 计算平均值
            double sum = 0.0;
            int count = 0;
            for (Double value : array) {
                if (value != null) {
                    sum += value;
                    count++;
                }
            }
            double average = count > 0 ? sum / count : 0.0;

            // 记录整个 Double[] 数组和平均值
            log.info("Values: " + Arrays.toString(array) + ", Average: " + average);
        }
        return ResponseData.succeed();
    }

    @GetMapping("/getPartSim3")
    public ResponseData getPartSim3() {
        List<Double[]> partSim1List = partService.getPartSim3();
        for (Double[] array : partSim1List) {
            // 计算平均值
            double sum = 0.0;
            int count = 0;
            for (Double value : array) {
                if (value != null) {
                    sum += value;
                    count++;
                }
            }
            double average = count > 0 ? sum / count : 0.0;

            // 记录整个 Double[] 数组和平均值
            log.info("Values: " + Arrays.toString(array) + ", Average: " + average);
        }
        return ResponseData.succeed();
    }
    @GetMapping("/getPartSim")
    public ResponseData getPartSim() {
        List<Integer[]> finalList = partService.getFinalList();
        //获取OBJECTID作为主键去查找
        ArrayList<Integer[]> list = neo4jGetGroupNodesService.getObjectIdByIds(finalList);
        for (Integer [] temp:list
        ) {
            log.info("结果列表:{}",Arrays.toString(temp));
        }

        return ResponseData.succeed(list);
    }

}
