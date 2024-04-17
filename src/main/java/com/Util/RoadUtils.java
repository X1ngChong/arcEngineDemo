package com.Util;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoadUtils {

    public static List<Double> extractNumbers(String line) {
        List<Double> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            numbers.add(Double.parseDouble(matcher.group()));
        }
        return numbers;
    }

    public static Line2D.Double[] convertToLineSegments(List<Double> coordinates) {
        List<Line2D.Double> segments = new ArrayList<>();
        for (int i = 0; i < coordinates.size() - 3; i += 2) {
            double x1 = coordinates.get(i);
            double y1 = coordinates.get(i + 1);
            double x2 = coordinates.get(i + 2);
            double y2 = coordinates.get(i + 3);
            segments.add(new Line2D.Double(x1, y1, x2, y2));
        }
        return segments.toArray(new Line2D.Double[0]);
    }

    // 判断两条线段是否相交
    public static boolean checkIntersection(Line2D.Double line1, Line2D.Double line2) {
        return line1.intersectsLine(line2);
    }

}
