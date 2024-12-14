package com.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 部分的计算
 * @author JXS
 */
public interface PartService {
    List<String[]> getIconicFeatureList();

    HashMap<String, Double> getPartSim1Map();

    List<Double []> getPartSim1();
}
