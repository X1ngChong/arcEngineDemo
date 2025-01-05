package com.Util.Next_TO;

import org.locationtech.jts.linearref.LengthIndexedLine;
import org.neo4j.driver.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.neo4j.driver.Values.parameters;


public class addOrderListInNextToRelation{
    private static String GroupLabel  ="xianlinGroup";
    private static String BuildLabel  ="xianlinBuild";

    private static String RoadLabel  ="xianlinRoad";

    public static void main(String[] args) throws Exception {
        // 创建 Neo4j 驱动
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));

        String getRelationList = "MATCH p=(s:"+GroupLabel+")-[r:NEXT_TO]->(e:"+GroupLabel+") RETURN id(s) as s,id(e) as e,id(r) as r,r.roadId as roadId";
        List<List<Integer>> ids = new ArrayList<>();//填充需要的id列表
        try (Session session = driver.session()) {
            Result result = session.run(getRelationList);
            while (result.hasNext()) {
                List<Integer> temp = new ArrayList<>();
                Record record = result.next();
                Integer sId = record.get("s").asInt();
                Integer rId = record.get("r").asInt();
                Integer roadId = record.get("roadId").asInt();

                temp.add(sId);
                temp.add(rId);
                temp.add(roadId);

                ids.add(temp);//暂存进最终结果
            }
        }
        System.out.println("-----------数组填充完毕---------------------");
        for (List<Integer> finalList:ids
        ) {
            // 查询地物数据
            String featureQuery = "MATCH (g:"+GroupLabel+")-[r:Contain]->(f:"+BuildLabel+")  where id(g) = "+finalList.get(0) +
                    " RETURN id(g) AS groupId, id(f) AS featureId, f.geometry AS geometry, type(r) AS relationship";
            //查询道路数据
            String roadQuery = "MATCH (r:"+RoadLabel+") WHERE id(r) = "+finalList.get(2)+" RETURN r.geometry AS geometry";

            // 使用 JTS 解析 WKT
            GeometryFactory geometryFactory = new GeometryFactory();
            WKTReader wktReader = new WKTReader(geometryFactory);

            // 查询道路
            MultiLineString road = null;
            try (Session session = driver.session()) {
                road = session.readTransaction(tx -> {
                    Result result = tx.run(roadQuery);
                    String wkt = result.single().get("geometry").asString();
                    try {
                        return (MultiLineString) wktReader.read(wkt); // 修改为 MultiLineString
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse road geometry: " + wkt, e);
                    }
                });
            }

            // 查询地物
            List<FeatureProjection> featureProjections = new ArrayList<>();
            try (Session session = driver.session()) {
                MultiLineString finalRoad = road;
                session.readTransaction(tx -> {
                    Result result = tx.run(featureQuery);
                    while (result.hasNext()) {
                        Record record = result.next();
                        Integer id = record.get("featureId").asInt();
                        String wkt = record.get("geometry").asString();
                        try {
                            Geometry geometry = wktReader.read(wkt);

                            // 检查几何类型
                            Point featurePoint;
                            if (geometry instanceof Point) {
                                featurePoint = (Point) geometry;
                            } else if (geometry instanceof MultiPolygon || geometry instanceof Polygon) {
                                featurePoint = geometry.getCentroid(); // 提取中心点
                            } else {
                                throw new RuntimeException("Unsupported geometry type: " + geometry.getGeometryType());
                            }

                            // 计算投影点
                            Coordinate closestProjection = null;
                            double minDistance = Double.MAX_VALUE;
                            double projectionIndex = -1;

                            for (int i = 0; i < finalRoad.getNumGeometries(); i++) {
                                LineString lineString = (LineString) finalRoad.getGeometryN(i);

                                // 使用 LengthIndexedLine 计算投影点
                                LengthIndexedLine indexedLine = new LengthIndexedLine(lineString);
                                double index = indexedLine.project(featurePoint.getCoordinate());
                                Coordinate projectedCoordinate = indexedLine.extractPoint(index);

                                // 计算投影点到地物的距离
                                double distance = featurePoint.getCoordinate().distance(projectedCoordinate);
                                if (distance < minDistance) {
                                    minDistance = distance;
                                    closestProjection = projectedCoordinate;
                                    projectionIndex = index;
                                }
                            }

                            // 保存地物的投影信息
                            featureProjections.add(new FeatureProjection(id, closestProjection, projectionIndex, minDistance));
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse feature geometry: " + wkt, e);
                        }
                    }
                    return null;
                });
            }

            // 按投影点的索引排序
            featureProjections.sort(Comparator.comparingDouble(FeatureProjection::getProjectionIndex));

            // 输出排序结果
            List<Integer> orderList = new ArrayList<>();
            System.out.println("投影最终结果:");
            for (FeatureProjection projection : featureProjections) {
                orderList.add(projection.getId());//添加到最终结果集中
//            System.out.println("ID: " + projection.getId() +
//                    ", 距离起始点距离: " + projection.getProjectionIndex() +
//                    ", 距离道路直线距离: " + projection.getDistance());
            }

            changeOrderList(driver.session(),finalList.get(1),orderList);//将排序好的投影存储到关系上


        }
        driver.close();
    }

    public static void changeOrderList(Session session,Integer idr,List<Integer> orderList){
        /**
         * 修改当前的NEXT_TO关系,给当前的NEXT_TO关系上添加roadID
         */
        session.run("MATCH p=()-[r:NEXT_TO]->() where id(r) =  $idr  set r.orderList = $orderList", parameters("idr", idr,"orderList", orderList));
        System.out.println("修改成功");
    }

    // 定义一个类来保存地物的投影信息
    static class FeatureProjection {
        private final Integer id;
        private final Coordinate projection;
        private final double projectionIndex;
        private final double distance;

        public FeatureProjection(Integer id, Coordinate projection, double projectionIndex, double distance) {
            this.id = id;
            this.projection = projection;
            this.projectionIndex = projectionIndex;
            this.distance = distance;
        }

        public Integer getId() {
            return id;
        }

        public Coordinate getProjection() {
            return projection;
        }

        public double getProjectionIndex() {
            return projectionIndex;
        }

        public double getDistance() {
            return distance;
        }
    }
}