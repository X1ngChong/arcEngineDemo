package com.Util.list.LineUtils;

import com.Bean.Point;
import com.Common.InfoCommon;
import com.Common.PathCommon;
import com.Util.Neo4jCalculatePointUtil;
import org.neo4j.driver.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JXS
 */
public class AddNearLineRelationTest1 {
//    public final static String  LAYER_NAME = "xianlinRoads";
public final static String  LAYER_NAME = "xianLinRoad";

    public static void main(String[] args) {

        int i = 0;
        try {
            Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
            Session session = driver.session();
            try (Transaction tx = session.beginTransaction()) {

                // 执行 Cypher 查询    分批次执行
                //String cypherQuery = "MATCH p=(n:xianlin)-[r:NEAR]->(m:xianlin) RETURN p, id(r) as id skip 0 limit 500 ";
                //String cypherQuery = "MATCH p=(n:xianlin)-[r:NEAR]->(m:xianlin) RETURN p, id(r) as id ";//将所有的xianlin数据去匹配是否有道路经过
                String cypherQuery = "MATCH p=(n:xianLinBuidling)-[r:NEAR]->(m:xianLinBuidling) RETURN p, id(r) as id ";//将所有的xianlin数据去匹配是否有道路经过


                Result result = tx.run(cypherQuery);
                // 处理查询结果
                while (result.hasNext()) {
                    Record record = result.next();

                    // 获取节点数据
                    List start = record.get("p").asPath().start().get("bbox").asList();//获取头节点
                    List end = record.get("p").asPath().end().get("bbox").asList();//获取尾节点


                    int relationshipId = record.get("id").asInt();//获取关系ID


                    Point sPpint = Neo4jCalculatePointUtil.calculateCenterAsPoint(start);
                    Point ePpint = Neo4jCalculatePointUtil.calculateCenterAsPoint(end);


                    // 通过这个查询在俩个地物之间是否经过道路
                    String cypherQuery2 = "WITH 'LINESTRING(' + " +
                            "toString(" + sPpint.getX() + ") + ' ' + toString(" + sPpint.getY() + ")"+ "+ ',' +" + " "+
                            "toString(" + ePpint.getX() + ") + ' ' + toString(" + ePpint.getY() + ")+ ')' AS line " +
                            "CALL spatial.intersects('" + LAYER_NAME + "', line) YIELD node " +
                            "RETURN node";

                   // System.out.println(cypherQuery2);
                    Result result2 = tx.run(cypherQuery2);

                    //如果有道路经过
                    if (result2.hasNext()) {
                        String cypherQuery3 = "MATCH (a {"+ PathCommon.OSMID+": $ad})-[r:NEAR]->(b {"+PathCommon.OSMID+": $bd}) WHERE  id(r) = $relationshipId and r.line is null  SET r.line = $line return a,b ";

                        Map<String, Object> parameters = new HashMap<>();

                        // 获取节点数据osm的id
//                        String aosmId = record.get("p").asPath().start().get("osm_id").asString();
//                        String bosmId = record.get("p").asPath().end().get("osm_id").asString();
                        String aosmId = record.get("p").asPath().start().get(PathCommon.OSMID).asString();
                       String bosmId = record.get("p").asPath().end().get(PathCommon.OSMID).asString();

                        parameters.put("ad", aosmId);
                        parameters.put("bd", bosmId);
                        parameters.put("relationshipId", relationshipId);//整数类型
                        parameters.put("line", "isIntersects"); // 把line的属性设置为是相交的

                        Result result3 = tx.run(cypherQuery3, parameters);
                        if (result3.hasNext()) {
                            Record record3 = result3.next();
                            System.out.println("添加道路关系成功" + i++);
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