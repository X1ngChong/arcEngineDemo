package com.demo.part;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于存储标志性地物的集合
 */
@Component
public class LandmarkCollection {

    private Set<String> landmarks;

    public LandmarkCollection() {
        this.landmarks = new HashSet<>();
        initializeLandmarks();
    }

    /**
     * 初始化标志性地物集合
     */
    private void initializeLandmarks() {
        landmarks.add("school");
        landmarks.add("park");
        landmarks.add("mall");
        landmarks.add("lake");
        // 可以根据需要添加更多的地物类型
    }

    /**
     * 检查地物是否为标志性地物
     * @param type 地物类型
     * @return 如果是标志性地物则返回 true，否则返回 false
     */
    public boolean isLandmark(String type) {
        return landmarks.contains(type);
    }

    /**
     * 获取所有标志性地物
     * @return 标志性地物集合
     */
    public Set<String> getLandmarks() {
        return landmarks;
    }
} 