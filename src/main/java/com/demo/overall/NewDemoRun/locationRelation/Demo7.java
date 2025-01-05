package com.demo.overall.NewDemoRun.locationRelation;

import com.Bean.GroupLocationRelationship;
import com.Common.InfoCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

/**
 方位关系:
 GroupLocationRelationship{block1Id=1767, block2Id=1769, locationRelationship='South'}
 GroupLocationRelationship{block1Id=1767, block2Id=1768, locationRelationship='SouthWest'}
 GroupLocationRelationship{block1Id=1768, block2Id=1769, locationRelationship='East'}
 GroupLocationRelationship{block1Id=1768, block2Id=1767, locationRelationship='NorthEast'}
 GroupLocationRelationship{block1Id=1769, block2Id=1768, locationRelationship='West'}
 GroupLocationRelationship{block1Id=1769, block2Id=1767, locationRelationship='North'}
 * @author JXS
 */
public class Demo7 {//这边没问题
    /**
     *
     * @param label 不同label切换真实图谱还有草图
     * @return
     */
    public  List<GroupLocationRelationship>  getLocationList(String label) {
        List<GroupLocationRelationship> locationList = new ArrayList<>();
        // 创建 Neo4j 驱动
        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()) {

            // 存储方位关系
            try (Transaction tx = session.beginTransaction()) {
                    String query = "MATCH (b1:" + label + ")-[r:NEXT_TO]->(b2:" + label + ") WHERE b1 <> b2 " +
                            "RETURN id(b1) AS b1ID, id(b2) AS b2ID, r.location AS location";
                    Result result = tx.run(query);
                    while (result.hasNext()) {
                        Record record = result.next();
                        locationList.add(new GroupLocationRelationship(
                                record.get("b1ID").asInt(),
                                record.get("b2ID").asInt(),
                                record.get("location").asString()
                        ));
                    }
                    /**
                     * 这里因为不能让没有NEXT_TO关系的没有方位导致矩阵为0 所以要填充其他不相邻的数组
                     */
                    String query2 = "MATCH (b1:" + label + "), (b2:" + label + ")  " +
                            "WHERE b1 <> b2 AND NOT (b1)-[:NEXT_TO]->(b2) And b1.bbox is not null and b2.bbox is not null  " +
                            "RETURN id(b1) AS b1ID, id(b2) AS b2ID,b1.bbox AS box1,b2.bbox AS box2";
                    Result result2 = tx.run(query2);
                    while (result2.hasNext()) {
                        Record record2 = result2.next();
                        locationList.add(new GroupLocationRelationship(
                                record2.get("b1ID").asInt(),
                                record2.get("b2ID").asInt(),
                                CalculateLocation.getBaFangWei(record2.get("box1").asList(), record2.get("box2").asList())
                        ));
                    }
                tx.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  locationList;
    }
}