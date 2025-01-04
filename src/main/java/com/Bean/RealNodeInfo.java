package com.Bean;

/**
 * 这个类用于存储id还有相似度
 */
public class RealNodeInfo {


     public int realNodeId;
    public double similarity1;

    public RealNodeInfo(int realNodeId, double similarity) {
        this.realNodeId = realNodeId;
        this.similarity1 = similarity;
    }

    public int getRealNodeId() {
        return realNodeId;
    }

    public void setRealNodeId(int realNodeId) {
        this.realNodeId = realNodeId;
    }

    public double getSimilarity1() {
        return similarity1;
    }

    public void setSimilarity(double similarity) {
        this.similarity1 = similarity;
    }
    @Override
    public String toString() {
        return "RealNodeInfo{" +
                "realNodeId=" + realNodeId +
                ", similarity=" + similarity1 +
                '}';
    }
}
