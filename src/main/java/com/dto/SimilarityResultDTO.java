package com.dto;

import java.util.Arrays;

public class SimilarityResultDTO {
    private double similarity;
    private Integer[] resultArray;

    @Override
    public String toString() {
        return "SimilarityResultDTO{" +
                "similarity=" + similarity +
                ", resultArray=" + Arrays.toString(resultArray) +
                '}';
    }

    // 构造方法
    public SimilarityResultDTO(double similarity, Integer[] resultArray) {
        this.similarity = similarity;
        this.resultArray = resultArray;
    }

    // Getter 和 Setter
    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public Integer[] getResultArray() {
        return resultArray;
    }

    public void setResultArray(Integer[] resultArray) {
        this.resultArray = resultArray;
    }
}