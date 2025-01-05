package com.demo.overall.NewDemoRun.meetRelation;

import com.Bean.GroupRelationship;
import com.Common.DriverCommon;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

/**
 *相邻关系:
 * 1767 meets 1769
 * 1767 meets 1768
 * 1768 meets 1769
 * 1768 meets 1767
 * 1769 meets 1768
 * 1769 meets 1767
 */
public class Demo6 {
    public static void main(String[] args) {
        Demo6 d6 = new Demo6();
        List<GroupRelationship> meetsList = d6.getMeetList("S13Group");
        // 输出结果
        System.out.println("相邻关系:");
        meetsList.forEach(System.out::println);
    }
    /**
     *
     * @param label 不同label切换真实图谱还有草图
     * @return
     */
    public  List<GroupRelationship>  getMeetList(String label) {
        List<GroupRelationship> meetsList = new ArrayList<>();
        // 创建 Neo4j 驱动
        try (DriverCommon driverCommon = new DriverCommon();
             Driver driver = driverCommon.getGraphDatabase();
             Session session = driver.session()) {

            // 存储相邻关系
            session.readTransaction(tx -> {
                String query = "MATCH (b1:"+label+")-[:NEXT_TO]->(b2:"+label+") WHERE b1 <> b2 " +
                        "RETURN id(b1) AS b1ID, id(b2) AS b2ID, 'meets' AS Relationship";
                Result result = tx.run(query);
                while (result.hasNext()) {
                    Record record = result.next();
                    meetsList.add(new GroupRelationship(
                            record.get("b1ID").asInt(),
                            record.get("b2ID").asInt(),
                            record.get("Relationship").asString()
                    ));
                }
                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  meetsList;
    }
}