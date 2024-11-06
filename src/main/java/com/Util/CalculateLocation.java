package com.Util;


import com.Common.PathCommon;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author JXS
 */
@Slf4j
public class CalculateLocation {

    /**
     * 计算中心点
     * @param bbox
     * @return
     */
    public static double[] calculateCenterPoint(List<Object> bbox) {
        if (bbox == null || bbox.size() != 4) {
            throw new IllegalArgumentException("bbox 内容未初始化");
        }

        // 解析 bbox 的坐标值
        double minX = ((Number) bbox.get(0)).doubleValue();
        double minY = ((Number) bbox.get(1)).doubleValue();
        double maxX = ((Number) bbox.get(2)).doubleValue();
        double maxY = ((Number) bbox.get(3)).doubleValue();

        // 计算中心点坐标
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;

        // 返回中心点坐标
        return new double[]{centerX, centerY};
    }

    /**
     * 获取角度
     * @param bbox1
     * @param bbox2
     * @return
     */
    public static double getAngle(List<Object> bbox1, List<Object> bbox2) {
        double[] center1 = calculateCenterPoint(bbox1);
        double[] center2 = calculateCenterPoint(bbox2);
        double x1 = center1[0];
        double y1 = center1[1];
        double x2 = center2[0];
        double y2 = center2[1];

        // 计算两点之间的角度（弧度）
        double angleInRadians = Math.atan2(y2 - y1, x2 - x1);

        // 将弧度转换为角度（0到360度）
        double angleInDegrees = Math.toDegrees(angleInRadians);

        // 处理负角度，将其转换为0到360度的范围
        if (angleInDegrees < 0) {
            angleInDegrees += 360;
        }

        return angleInDegrees;
    }

    /**
     * 精确计算方位 用于处理原始地图方位
     * @param bbox1
     * @param bbox2
     * @return
     */

    public static String GetDirection(List<Object> bbox1, List<Object> bbox2) {
        double angle = getAngle(bbox1, bbox2);
        String result = "";

        /**
         * 修改下面 用精确的角度去计算模拟
         */

        if (angle >= 0 && angle < 22.5) {
            result = "东";
        } else if (angle >= 22.5 && angle < 67.5) {
            result = "东北";
        } else if (angle >= 67.5 && angle < 112.5) {
            result = "北";
        } else if (angle >= 112.5 && angle < 157.5) {
            result = "西北";
        } else if (angle >= 157.5 && angle < 202.5) {
            result = "西";
        } else if (angle >= 202.5 && angle < 247.5) {
            result = "西南";
        } else if (angle >= 247.5 && angle < 292.5) {
            result = "南";
        } else if (angle >= 292.5 && angle < 337.5) {
            result = "东南";
        } else if (angle >= 337.5 && angle <= 360) {
            result = "东";
        }

        return result;
    }

    public static Double GetDirectionNew(List<Object> bbox1, List<Object> bbox2) {
        /*
         * 修改下面 用精确的角度去计算模拟  相较于前面一种返回的是纯数字
         */
        return getAngle(bbox1, bbox2);
    }

    public static boolean compareAndCalculate(Double beginValue, String endNode) {
        // 将字符串转换为数字
        double endValue = Double.parseDouble(endNode);

        // 计算边界值
        double lowerBound = endValue - PathCommon.ANGLE_RANGE;
        double upperBound = endValue + PathCommon.ANGLE_RANGE;

        // 判断条件
        return beginValue > lowerBound && beginValue < upperBound;
    }

    /**
     * 明显方位             ******  需要采用相离相交包含等关系去计算明显位置
     * @param bbox1
     * @param bbox2
     * @return
     */
    public static String getDirection(List<Object> bbox1, List<Object> bbox2)
    {
        double[] center1 = calculateCenterPoint(bbox1);
        double[] center2 = calculateCenterPoint(bbox2);

        String result = "";

        // 计算两点之间的斜率
        double slope = (center1[0] - center2[0]) / (center1[1] - center2[1]);

        // 如果斜率的绝对值大于1.4，则认为明显在东西方位
        if (Math.abs(slope) > 1.3) {
            if(center1[0] - center2[0] < 0 ){
                result += "东";
            }
           else if(center1[0] - center2[0] > 0 ){
                result += "西";
            }
        }
        // 如果斜率的绝对值小于0.7，则认为明显在南北方位
        else if(Math.abs(slope) < 0.7){
            if(center1[1] - center2[1] < 0 ){
                result += "北";
            }
            else if(center1[1] - center2[1] > 0 ){
                result += "南";
            }
        }
        else{
            result  = GetDirection(bbox1, bbox2);
        }
        return result;
    }


    /**
     * 计算明显方位
     * @param bbox1 第一个边界框
     * @param bbox2 第二个边界框
     * @return 明显方位结果，例如 "东"、"西"、"南"、"北" 等
     */
    public static String getDirection2(List<Object> bbox1, List<Object> bbox2) {
        String result = "";
        // 使用 GetDirection 方法
        result = String.valueOf(GetDirectionNew(bbox1,bbox2));

        log.info("方位角:{}",result);

        return result;
    }


    /**
     * 将 Object 类型的坐标列表转换为 Double[] 数组
     * @param coords 坐标列表
     * @return Double[] 数组
     */
    private static Double[] convertToDoubleArray(List<Object> coords) {
        Double[] result = new Double[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            result[i] = (Double) coords.get(i);
        }
        return result;
    }



}
