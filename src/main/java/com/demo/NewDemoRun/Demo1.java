package com.demo.NewDemoRun;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.HashMap;
import java.util.Map;

/**有点问题
 *
 * 95: resident (1), university (1), college (1)
 * 96: resident (3), mall (1), park (1)
 * 97: resident (3), mall (1), park (1)
 * 98: square (1), school (2), resident (2)
 * 99: school (2), resident (5)
 * 100: resident (4), school (2)
 * 101: resident (5), school (1), square (1)
 * 102: hospital (1), school (1), park (1), resident (1)
 * 103: resident (3)
 * 104: school (2), resident (4)
 * 105: resident (1)
 *
 * 28: resident (1), mall (1), park (1)
 * 29: resident (5), school (2)
 * 30: resident (3)
 * 31: square (1), school (2), resident (2)
 */
class NodeData {
    private Map<String, Integer> typesCount;

    public NodeData() {
        this.typesCount = new HashMap<>();
    }

    public void addType(String type) {
        typesCount.put(type, typesCount.getOrDefault(type, 0) + 1);
    }

    public Map<String, Integer> getTypesCount() {
        return typesCount;
    }
}

public class Demo1 {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));
             Session session = driver.session()) {

            // 收集真实数据
            Map<Integer, NodeData> realData = collectDataFromDb(session, "MATCH p=()-[r:Have]->() RETURN p");
            System.out.println("真实数据: " + realData);

            // 收集草图数据
            Map<Integer, NodeData> sketchData = collectDataFromDb(session, "MATCH p=()-[r:CONTAINS]->() RETURN p");
            System.out.println("草图数据: " + sketchData);

            // 比较数据
            compareData(realData, sketchData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, NodeData> collectDataFromDb(Session session, String query) {
        Map<Integer, NodeData> nodeData = new HashMap<>();
        session.run(query).forEachRemaining(record -> {
            Path path = record.get("p").asPath();
            Node parentNode = path.start();
            int parentId = (int) parentNode.id();

            if (path.end().containsKey("type")) {
                String type = path.end().get("type").asString();
                nodeData.putIfAbsent(parentId, new NodeData());
                nodeData.get(parentId).addType(type);
            }
        });
        return nodeData;
    }

    private static void compareData(Map<Integer, NodeData> realData, Map<Integer, NodeData> sketchData) {
        for (Map.Entry<Integer, NodeData> entry : realData.entrySet()) {
            int nodeId = entry.getKey();
            NodeData realNodeData = entry.getValue();
            NodeData sketchNodeData = sketchData.getOrDefault(nodeId, new NodeData());

            for (Map.Entry<String, Integer> typeEntry : realNodeData.getTypesCount().entrySet()) {
                String type = typeEntry.getKey();
                int realCount = typeEntry.getValue();
                int sketchCount = sketchNodeData.getTypesCount().getOrDefault(type, 0);

                if (realCount == sketchCount) {
                    System.out.println("匹配: 真实节点 ID = " + nodeId + ", 草图节点 ID = " + nodeId +
                            ", 类型 = " + type + ", 数量 = " + realCount);
                } else {
                    System.out.println("不匹配: 真实节点 ID = " + nodeId + ", 草图节点 ID = " + nodeId +
                            ", 类型 = " + type + ", 真实数量 = " + realCount + ", 草图数量 = " + sketchCount);
                }
            }
        }
    }
}