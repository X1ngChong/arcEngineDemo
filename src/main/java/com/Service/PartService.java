package com.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 部分的计算
 * @author JXS
 */
public interface PartService {
    List<String[]> getIconicFeatureList();

    /**
     * 获取每个组相对应的方位相似度
     */
    HashMap<String, Double> getPartSim1Map();
    HashMap<String, Double> getPartSim2MapByNearOrder();
    HashMap<String, Double> getPartSim2MapByNextToOrder();

    HashMap<String, Double> getPartSim3Map();

    /**
     * 获取相似度
     */
    List<Double []> getPartSim1();
    List<Double []> getPartSim2();
    List<Double []> getPartSim3();

    List<Double> getPartSim();

    List<Integer[]> getFinalList();



}
