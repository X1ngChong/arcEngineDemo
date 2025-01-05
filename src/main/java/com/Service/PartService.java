package com.Service;

import com.Bean.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 部分的计算
 * @author JXS
 */
public interface PartService {
    List<String[]> getIconicFeatureList(String caoTuLabel,String realLabel);

    /**
     * 获取每个组相对应的方位相似度
     */
    HashMap<String, Double> getPartSim1Map(String caoTuLabel,String realLabel);
    HashMap<String, Double> getPartSim2MapByNearOrder(String caoTuLabel,String realLabel);
    HashMap<String, Double> getPartSim2MapByNextToOrder(String caoTuLabel,String realLabel);

    HashMap<String, Double> getPartSim3Map(String caoTuLabel,String realLabel);

    /**
     * 获取相似度
     */
    List<Double []> getPartSim1(String caoTuLabel,String realLabel);
    List<Double []> getPartSim2(String caoTuLabel,String realLabel);
    List<Double []> getPartSim3(String caoTuLabel,String realLabel);

    List<Double> getPartSim(String caoTuLabel,String realLabel);

    List<Pair<Double, Integer[]>> getFinalList(String caoTuLabel,String realLabel);



}
