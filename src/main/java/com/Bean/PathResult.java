package com.Bean;

import java.util.List;

/**
 * @author JXS
 */
public class PathResult {
    private final List<String> path;
    private final double weight;
    private final  List<Integer> indexList;

    public PathResult(List<String> path, List<Integer> indexList, double weight) {
        this.path = path;
        this.indexList = indexList;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public List<String> getPath() {
        return path;
    }

    public List<Integer> getIndexList() {
        return indexList;
    }

    @Override
    public String toString() {
        return "Path: " + String.join(" -> ", path) +
                " | Indices: " + indexList + // 输出索引
                " | Weight: " + weight;
    }
}