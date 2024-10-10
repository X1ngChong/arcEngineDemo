package com.demo.NewDemoRun;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.HashMap;
import java.util.Map;

public class Demo2 {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
             Session session = driver.session()) {

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