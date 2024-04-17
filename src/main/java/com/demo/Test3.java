package com.demo;

import org.neo4j.driver.*;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test3 {

    public static void main() {
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
             Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // 执行 Cypher 查询
                String cypherQuery = "MATCH (n:xianlinRoads) where n.osm_id <> 'null' RETURN n.geometry as linePoint LIMIT 25";

                Result result = tx.run(cypherQuery);
                // 处理查询结果
                List<Line2D.Double[]> roadSegmentsList = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    // 获取节点数据
                    String linePoint = record.get("linePoint").asString();
                    List<Double> coordinates = extractNumbers(linePoint);
                    Line2D.Double[] roadSegments = convertToLineSegments(coordinates);
                    roadSegmentsList.add(roadSegments);
                }
                // 提交事务
                tx.commit();

                // 假设我们有一些地物线段
                Line2D.Double[] features = {
                        new Line2D.Double(5, 5, 20, 20),
                };

                // 对每个道路线段和每个地物线段进行相交判断
                for (Line2D.Double[] roadSegments : roadSegmentsList) {
                    for (Line2D.Double feature : features) {
                        for (Line2D.Double roadSegment : roadSegments) {
                            boolean isIntersecting = checkIntersection(roadSegment, feature);
                            if (isIntersecting) {
                                System.out.println("道路线段与地物线段相交！");
                            } else {
                                System.out.println("道路线段与地物线段不相交。");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 判断两条线段是否相交
    private static boolean checkIntersection(Line2D.Double line1, Line2D.Double line2) {
        return line1.intersectsLine(line2);
    }

    private static List<Double> extractNumbers(String line) {
        List<Double> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            numbers.add(Double.parseDouble(matcher.group()));
        }
        return numbers;
    }

    private static Line2D.Double[] convertToLineSegments(List<Double> coordinates) {
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
}
