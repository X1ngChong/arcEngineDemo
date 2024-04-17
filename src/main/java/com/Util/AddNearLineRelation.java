package com.Util;


import com.Bean.Line;
import com.Bean.Point;
import com.esri.arcgis.geoprocessing.tools.analysistools.Intersect;
import org.neo4j.driver.*;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JXS
 */
public class AddNearLineRelation {
    public static void main(String[] args) {
        int i = 0;
        try  {
            Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
            Session session = driver.session();
            try (Transaction tx = session.beginTransaction()) {
                // 执行 Cypher 查询    分批次执行
                String cypherQuery = "MATCH p=()-[r:NEAR]->() RETURN p, id(r) as id skip 0 limit 500 ";

                Result result = tx.run(cypherQuery);
                // 处理查询结果
                while (result.hasNext()) {
                    Record record = result.next();

                    // 获取节点数据
                    List start = record.get("p").asPath().start().get("bbox").asList();
                    List end = record.get("p").asPath().end().get("bbox").asList();


                    int relationshipId = record.get("id").asInt();


                    Point sPpint = Neo4jCalculatePointUtil.calculateCenterAsPoint(start);
                    Point ePpint = Neo4jCalculatePointUtil.calculateCenterAsPoint(end);


                    // 俩个道路之间的线段
                    Line2D.Double[] line1 = {
                            new Line2D.Double(sPpint.getX(), sPpint.getY(), ePpint.getX(), ePpint.getY()),
                    };

                    String cypherQuery2 = "MATCH (n:xianlinRoads) where n.osm_id <> 'null' RETURN n.geometry as linePoint , n.osm_id as roadOsm ";

                    Result result2 = tx.run(cypherQuery2);
                    List<Line2D.Double[]> roadSegmentsList = new ArrayList<>();
                    while (result2.hasNext()) {
                        Record record2 = result2.next();

                        String roadOsm = record2.get("roadOsm").asString();


                        String linePoint = record2.get("linePoint").asString();
                        List<Double> coordinates = RoadUtils.extractNumbers(linePoint);
                        Line2D.Double[] roadSegments = RoadUtils.convertToLineSegments(coordinates);
                        roadSegmentsList.add(roadSegments);


                        // 对每个道路线段和每个地物线段进行相交判断

                        // 创建一个新的关系
                        outermost:  for (Line2D.Double[] roadSegments2 : roadSegmentsList) {
                            for (Line2D.Double feature : line1) {
                                for (Line2D.Double roadSegment : roadSegments2) {
                                    boolean isIntersecting = RoadUtils.checkIntersection(roadSegment, feature);
                                    if (isIntersecting) {
                                       // System.out.println("是否相交: " + isIntersecting);

                                        // 获取节点数据osm的id
                                        String aosmId = record.get("p").asPath().start().get("osm_id").asString();
                                        String bosmId = record.get("p").asPath().end().get("osm_id").asString();

                                        // 创建一个新的关系
                                        String cypherQuery3 = "MATCH (a {osm_id: $ad})-[r:NEAR]->(b {osm_id: $bd}) WHERE  id(r) = $relationshipId and r.line is null  SET r.line = $line return a,b ";
                                        Map<String, Object> parameters = new HashMap<>();
                                        parameters.put("ad", aosmId);
                                        parameters.put("bd", bosmId);
                                        parameters.put("relationshipId", relationshipId);//整数类型
                                        parameters.put("line", roadOsm); // 这里设置您想要的新值

                                        Result result3 = tx.run(cypherQuery3, parameters);
                                        if (result3.hasNext()) {
                                            Record record3 = result3.next();
                                            System.out.println("添加成功" + i++);
                                            break outermost; // 使用标记退出所有循环
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
                // 提交事务
                System.out.println("正在提交事务");
                tx.commit();
            }
            driver.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
