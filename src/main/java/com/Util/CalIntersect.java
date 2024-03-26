package com.Util;


import com.Bean.Line;
import com.Bean.Point;

/**
 * @author JXS 判断两个地物之间是否有道路
 */
public class CalIntersect {

    // 判断点是否在线段上
    private static boolean isPointOnSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    // 判断两条直线是否相交
    public static boolean doLinesIntersect(Line line1, Line line2) {


        // 计算直线的斜率
        double slope1 = (line1.end.y - line1.start.y) / (line1.end.x - line1.start.x);
        double slope2 = (line2.end.y - line2.start.y) / (line2.end.x - line2.start.x);

        // 如果两条直线斜率相同，则它们平行
        if (slope1 == slope2) {
            return false;
        }

        // 计算截距
        double intercept1 = line1.start.y - slope1 * line1.start.x;
        double intercept2 = line2.start.y - slope2 * line2.start.x;

        // 计算交点
        double x = (intercept2 - intercept1) / (slope1 - slope2);
        double y = slope1 * x + intercept1;

        // 检查交点是否在两条直线的有效范围内
        return isPointOnSegment(line1.start, new Point(x, y), line1.end) &&
                isPointOnSegment(line2.start, new Point(x, y), line2.end);
    }

    public static void main(String[] args) {
        // 定义四个点，形成两条直线
        Point p1 = new Point(118.86775669923097, 32.06550953814714);
        Point p2 = new Point(118.94868140000003, 32.117768600000005);
        Point p3 = new Point(1, 0);
        Point p4 = new Point(2, 1);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p3, p4);

        // 检查两条直线是否相交
        boolean isIntersecting = doLinesIntersect(line1, line2);
        System.out.println("是否相交 : " + isIntersecting);
    }
}