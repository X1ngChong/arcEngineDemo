package com.Util.Group;

import com.Common.InfoCommon;
import org.neo4j.driver.*;

import java.util.*;

/**
 * 获取所有父节点的ID 然后用区域内的左上角的点还有右下角的点作为整个地物的包围盒 完成--
 */
public class addGroupBox {
    public final static String Relationship = "Contain";//真实图谱的关系
    public static void main(String[] args) {
        try ( Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
              Session session = driver.session()) {


            // 查找所有的组节点的ID
            List<Integer> fatherIds  = fetchFatherIds(session);

            //根据组节点ID去设置这个组的范围大小
            setFatherBbox(session,fatherIds);

            System.out.println(fatherIds);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static List<Integer> fetchFatherIds(Session session) {
        String fetchFatherIdQuery = "MATCH p=(start)-[r:"+Relationship+"]->(end)  " +
                "WITH start, COLLECT(p) AS paths  " +
                "RETURN id(start) AS startId, HEAD(paths) AS firstPath ";
        List<Integer> fatherIds = new ArrayList<>();
        Result result = session.run(fetchFatherIdQuery);
        while (result.hasNext()) {
            Record record = result.next();
            fatherIds.add(record.get("startId").asInt());
        }
        return fatherIds;
    }

    private static void setFatherBbox(Session session, List<Integer> fatherIds) {
        for (Integer fatherId : fatherIds) {
            String fetchBboxQuery = "MATCH p=(start)-[r:"+Relationship+"]->(end) WHERE id(start) = " + fatherId + " RETURN end.bbox AS bbox";
            List<List<Double>> bboxLists = new ArrayList<>();
            Result result = session.run(fetchBboxQuery);

            while (result.hasNext()) {
                Record record = result.next();
                bboxLists.add(record.get("bbox").asList(Value::asDouble));
            }

            // 计算最左上角和最右下角的点
            if (!bboxLists.isEmpty()) {
                double minX = Double.MAX_VALUE;
                double maxY = Double.NEGATIVE_INFINITY;
                double maxX = Double.NEGATIVE_INFINITY;
                double minY = Double.MAX_VALUE;

                for (List<Double> bbox : bboxLists) {
                    minX = Math.min(minX, bbox.get(0));
                    maxY = Math.max(maxY, bbox.get(1));
                    maxX = Math.max(maxX, bbox.get(2));
                    minY = Math.min(minY, bbox.get(3));
                }

                // 创建新的 bbox 属性
                List<Double> newBbox = Arrays.asList(minX, maxY, maxX, minY);

                // 更新 start 节点的 bbox 属性
                String updateBboxQuery = "MATCH (start) WHERE id(start) = " + fatherId + " SET start.bbox = $bbox";
                session.run(updateBboxQuery, Values.parameters("bbox", newBbox));
            }
        }
    }

}
