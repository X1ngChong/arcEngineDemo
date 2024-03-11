package com.demo;


import com.Bean.Line;
import com.Bean.Point;
import com.Util.CalIntersect;
import com.Util.Neo4jCalculatePointUtil;
import org.neo4j.driver.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JXS
 */
public class Test2 {
    public static void main(String[] args) {
        int i = 0;
        try  {
            Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
            Session session = driver.session();
            try (Transaction tx = session.beginTransaction()) {
                // 执行 Cypher 查询
                String cypherQuery = "MATCH p=()-[r:NEAR]->() RETURN p, id(r) as id  limit 100";

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


                    Line line1 = new Line(sPpint, ePpint);

                    String cypherQuery2 = "MATCH (n:xianlinRoads) where n.osm_id <> 'null' RETURN n , n.osm_id as roadOsm ";

                    Result result2 = tx.run(cypherQuery2);
                    while (result2.hasNext()) {
                        Record record2 = result2.next();
                        List bbox = record2.get("n").get("bbox").asList();
                        String roadOsm = record2.get("roadOsm").asString();

                        Point p1 = new Point((Double) bbox.get(0), (Double) bbox.get(1));
                        Point p2 = new Point((Double) bbox.get(2), (Double) bbox.get(3));

                        Line line2 = new Line(p1, p2);

                        boolean isIntersecting = CalIntersect.doLinesIntersect(line1, line2);

                        if (isIntersecting) {
                            System.out.println("是否相交: " + isIntersecting);

                            // 获取节点数据osm的id
                            String aosmId =  record.get("p").asPath().start().get("osm_id").asString();
                            String bosmId = record.get("p").asPath().end().get("osm_id").asString();

                            // 创建一个新的关系
                            String cypherQuery3 = "MATCH (a {osm_id: $ad})-[r:NEAR]->(b {osm_id: $bd}) WHERE  id(r) = $relationshipId SET r.line = $line return a,b ";
                            Map<String, Object> parameters = new HashMap<>();
                            parameters.put("ad", aosmId);
                            parameters.put("bd", bosmId);
                            parameters.put("relationshipId", relationshipId);//整数类型
                            parameters.put("line", roadOsm); // 这里设置您想要的新值

                            Result result3 = tx.run(cypherQuery3,parameters);
                            while (result3.hasNext()) {
                                Record record3 = result3.next();
                                System.out.println("添加成功" + i++);
                            }
                        }

                    }

                }
                // 提交事务
                tx.commit();
            }
            driver.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
