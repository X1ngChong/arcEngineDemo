package com.demo.overall.NewDemoRun;

import com.Common.InfoCommon;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

/**
 * **有点问题
 *  *
 *  * 95: resident (1), university (1), college (1)
 *  * 96: resident (3), mall (1), park (1)
 *  * 97: resident (3), mall (1), park (1)
 *  * 98: square (1), school (2), resident (2)
 *  * 99: school (2), resident (5)
 *  * 100: resident (4), school (2)
 *  * 101: resident (5), school (1), square (1)
 *  * 102: hospital (1), school (1), park (1), resident (1)
 *  * 103: resident (3)
 *  * 104: school (2), resident (4)
 *  * 105: resident (1)
 *  *
 *  * 28: resident (1), mall (1), park (1)
 *  * 29: resident (5), school (2) ----
 *  * 30: resident (3)
 *  * 31: square (1), school (2), resident (2)
 */
import java.util.HashMap;
import java.util.Map;

public class Demo2 {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()){

            // Collect real map data types and counts
            Map<String, Integer> realMapTypeCount = collectDataFromDb(session, "MATCH p=()-[r:Have]->() RETURN p");
            System.out.println("Real Map Data: " + realMapTypeCount);

            // Collect sketch data types and counts
            Map<String, Integer> sketchTypeCount = collectDataFromDb(session, "MATCH p=()-[r:CONTAINS]->() RETURN p");
            System.out.println("Sketch Data: " + sketchTypeCount);

            // Compare the real map and sketch data
            compareData(realMapTypeCount, sketchTypeCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> collectDataFromDb(Session session, String query) {
        Map<String, Integer> typeCount = new HashMap<>();
        session.run(query).forEachRemaining(record -> {
            Path path = record.get("p").asPath();
            Node parent = path.start();
            Node node = path.end();

            if (node.containsKey("type")) {
                String type = node.get("type").asString();
                String key = parent.id() + "_" + type;
                typeCount.put(key, typeCount.getOrDefault(key, 0) + 1);
            }
        });
        return typeCount;
    }

    private static void compareData(Map<String, Integer> realMapTypeCount, Map<String, Integer> sketchTypeCount) {
        realMapTypeCount.forEach((key, realCount) -> {
            String[] parts = key.split("_");
            Integer areaId = Integer.valueOf(parts[0]);
            String type = parts[1];

            Integer sketchCount = sketchTypeCount.get(key);
            if (sketchCount != null && sketchCount.equals(realCount)) {
                System.out.println("Match: ");
                System.out.println("Area ID = " + areaId + ", Type = " + type + ", Count = " + realCount);
            } else {
                sketchCount = sketchCount == null ? 0 : sketchCount;
                System.out.println("不匹配: Area ID = " + areaId + ", Type = " + type + ", Real Count = " + realCount + ", Sketch Count = " + sketchCount);
            }
        });
    }
}