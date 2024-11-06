package com.demo.NewDemoRun;

import com.Bean.GroupRelationship;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选草图的相邻关系
 * 相邻关系:
 * 110 meets 112
 * 110 meets 111
 * 111 meets 110
 * 111 meets 113
 * 112 meets 110
 * 112 meets 113
 * 113 meets 112
 * 113 meets 111
 * 不相邻关系:
 * 110 not meets 113
 * 111 not meets 112
 * 112 not meets 111
 * 113 not meets 110
 */
public class Demo6 {
    public static void main(String[] args) {
        Demo6 d6 = new Demo6();
        List<GroupRelationship> meetsList = d6.getMeetList("Group");
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
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
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

//            // 存储不相邻关系
//            List<Relationship> notMeetsList = new ArrayList<>();
//            session.readTransaction(tx -> {
//                String query = "MATCH (b1:"+label+"), (b2:"+label+") " +
//                        "WHERE b1 <> b2 AND NOT (b1)-[:NEXT_TO]->(b2) " +
//                        "RETURN id(b1) AS b1ID, id(b2) AS b2ID, 'not meets' AS Relationship";
//                Result result = tx.run(query);
//                while (result.hasNext()) {
//                    Record record = result.next();
//                    notMeetsList.add(new Relationship(
//                            record.get("b1ID").asInt(),
//                            record.get("b2ID").asInt(),
//                            record.get("Relationship").asString()
//                    ));
//                }
//                return null;
//            });

//            System.out.println("不相邻关系:");
//            notMeetsList.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  meetsList;
    }
}