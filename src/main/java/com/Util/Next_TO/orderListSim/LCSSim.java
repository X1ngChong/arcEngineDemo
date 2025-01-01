package com.Util.Next_TO.orderListSim;

import java.util.List;

/**
 * 最长公共子序列（LCS）***
 */
public class LCSSim {

    /**
     * 计算两个列表的 LCS 相似度
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 相似度（0.0 ~ 1.0）
     */
    public static double calculateLCSSimilarity(List<Object> list1, List<Object> list2) {
        int lcsLength = findLCSLength(list1, list2);
        int maxLength = Math.max(list1.size(), list2.size());
        return (double) lcsLength / maxLength;
    }

    /**
     * 计算两个列表的 LCS 长度
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return LCS 的长度
     */
    public  static int findLCSLength(List<Object> list1, List<Object> list2) {
        int m = list1.size();
        int n = list2.size();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (list1.get(i - 1).equals(list2.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }
}
