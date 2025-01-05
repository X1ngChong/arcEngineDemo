package com.Util.addNearRelation;

import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.neo4j.driver.Values.parameters;

/**
 * 在计算Near关系的时候 为一个组下面的所有的节点添加Near关系 问题这么做可能会使得组的判断要重新考虑
 * Near:{distance:距离的远近,location:方位,order:顺序关系}
 * @author JXS
 */
public class newAddNearRelation {

    public static void main(String[] args) {

        String[] textNames = {"xianlinGroup"};
       // String[] textNames = {"xianLinGroup"};


        for (int i = 0; i < textNames.length; i++) {
            String textName = textNames[i];
            try (DriverCommon driverCommon = new DriverCommon()) {
                Driver driver = driverCommon.getGraphDatabase();
                Session session = driver.session();
                Session session2 = driver.session();
                // 遍历所有  节点
                try (Transaction tx = session.beginTransaction()) {
                    List<Integer> groupIds = new ArrayList<>();
                    Result result = tx.run("MATCH (n:" + textName + ") RETURN id(n) as id;");//获取所有的组节点ID
                    while (result.hasNext()) {
                        Record record = result.next();
                        Integer groupId = record.get("id").asInt();
                        groupIds.add(groupId);
                    }//填充完所有的组

                    for (Integer groupId : groupIds) {
                        System.out.println("计算组节点ID:"+groupId);
                        List<Integer> childIds = new ArrayList<>();
                        Result result2 = tx.run(" Match (n)-[]->(m)  where m.OBJECTID IS NOT NULL and id(n)=" + groupId + " RETURN id(m) as id ;");//获取组节点下面的所有的ID
                        while (result2.hasNext()) {
                            Record record2 = result2.next();
                            Integer childId = record2.get("id").asInt();
                            childIds.add(childId);
                        }//填充所有的当前组下面的所有节点

                        // 计算所有最短距离的最大值
                        double maxShortestDistance = calculateMaxShortestDistance(tx, childIds);

                        //对当前的组下面的所有节点进行Near关系计算 俩俩去计算Near关系
                        for (Integer childId1 : childIds) {
                            for (Integer childId2 : childIds) {
                                if (!Objects.equals(childId1, childId2)) {
                                    Result result3 = tx.run(" MATCH (n), (m)  " +
                                            "WHERE id(n) = " + childId1 + " AND id(m) = " + childId2 +
                                            " WITH n, m,  " +
                                            "     n.bbox[0] AS n_minX, n.bbox[1] AS n_minY, n.bbox[2] AS n_maxX, n.bbox[3] AS n_maxY,  " +
                                            "     m.bbox[0] AS m_minX, m.bbox[1] AS m_minY, m.bbox[2] AS m_maxX, m.bbox[3] AS m_maxY  " +
                                            " RETURN   n.bbox as box1, " +
                                            "        m.bbox as box2,  " +
                                            "    CASE   " +
                                            "        WHEN n_maxX < m_minX THEN m_minX - n_maxX  " +
                                            "        WHEN n_minX > m_maxX THEN n_minX - m_maxX  " +
                                            "        ELSE 0  " +
                                            "    END AS x_distance, " +
                                            "    CASE   " +
                                            "        WHEN n_maxY < m_minY THEN m_minY - n_maxY   " +
                                            "        WHEN n_minY > m_maxY THEN n_minY - m_maxY  " +
                                            "        ELSE 0  " +
                                            "    END AS y_distance,  " +
                                            "    CASE   " +
                                            "        WHEN n_maxX < m_minX OR n_minX > m_maxX OR n_maxY < m_minY OR n_minY > m_maxY THEN  " +
                                            "            sqrt(  " +
                                            "                (CASE   " +
                                            "                    WHEN n_maxX < m_minX THEN m_minX - n_maxX  " +
                                            "                    WHEN n_minX > m_maxX THEN n_minX - m_maxX  " +
                                            "                    ELSE 0  " +
                                            "                END * CASE   " +
                                            "                    WHEN n_maxX < m_minX THEN m_minX - n_maxX  " +
                                            "                    WHEN n_minX > m_maxX THEN n_minX - m_maxX  " +
                                            "                    ELSE 0  " +
                                            "                END) +   " +
                                            "                (CASE   " +
                                            "                    WHEN n_maxY < m_minY THEN m_minY - n_maxY  " +
                                            "                    WHEN n_minY > m_maxY THEN n_minY - m_maxY  " +
                                            "                    ELSE 0  " +
                                            "                END * CASE   " +
                                            "                    WHEN n_maxY < m_minY THEN m_minY - n_maxY  " +
                                            "                    WHEN n_minY > m_maxY THEN n_minY - m_maxY  " +
                                            "                    ELSE 0  " +
                                            "                END)  " +
                                            "            )  " +
                                            "        ELSE 0  " +
                                            "    END AS shortest_distance");//获取两个节点的最短距离
                                    while (result3.hasNext()) {
                                        Record record3 = result3.next();
                                        Double shortestDistance = record3.get("shortest_distance").asDouble();

                                        List<Object> box1= record3.get("box1").asList();
                                        List<Object> box2 = record3.get("box2").asList();

                                        // 归一化处理
                                        double normalizedDistance = shortestDistance / maxShortestDistance;


                                        String distance = CalculateLocation.getDistanceRelation(normalizedDistance);//计算LD MD SD
                                        String location = CalculateLocation.getBaFangWei(box1, box2); //计算八方位
                                        String order =  CalculateLocation.getOrder(box1,box2);

                                        // 添加 NEAR 关系
                                        addNearRelation(session2, childId1, childId2, location, distance,order);

                                        //添加Orde关系
                                        //changeOrderRelation(session2, childId1, childId2,order);
                                    }
                                }
                            }
                        }
                    }

                    tx.commit();
                }
            }
        }
    }

    private static double calculateMaxShortestDistance(Transaction tx, List<Integer> childIds) {
        double maxDistance = 0.0;
        for (Integer childId1 : childIds) {
            for (Integer childId2 : childIds) {
                if (!Objects.equals(childId1, childId2)) {
                    Result result = tx.run(" MATCH (n), (m)  " +
                            "WHERE id(n) = " + childId1 + " AND id(m) = " + childId2 +
                            " WITH n, m,  " +
                            "     n.bbox[0] AS n_minX, n.bbox[1] AS n_minY, n.bbox[2] AS n_maxX, n.bbox[3] AS n_maxY,  " +
                            "     m.bbox[0] AS m_minX, m.bbox[1] AS m_minY, m.bbox[2] AS m_maxX, m.bbox[3] AS m_maxY  " +
                            " RETURN   " +
                            "    CASE   " +
                            "        WHEN n_maxX < m_minX THEN m_minX - n_maxX  " +
                            "        WHEN n_minX > m_maxX THEN n_minX - m_maxX  " +
                            "        ELSE 0  " +
                            "    END AS x_distance, " +
                            "    CASE   " +
                            "        WHEN n_maxY < m_minY THEN m_minY - n_maxY   " +
                            "        WHEN n_minY > m_maxY THEN n_minY - m_maxY  " +
                            "        ELSE 0  " +
                            "    END AS y_distance,  " +
                            "    CASE   " +
                            "        WHEN n_maxX < m_minX OR n_minX > m_maxX OR n_maxY < m_minY OR n_minY > m_maxY THEN  " +
                            "            sqrt(  " +
                            "                (CASE   " +
                            "                    WHEN n_maxX < m_minX THEN m_minX - n_maxX  " +
                            "                    WHEN n_minX > m_maxX THEN n_minX - m_maxX  " +
                            "                    ELSE 0  " +
                            "                END * CASE   " +
                            "                    WHEN n_maxX < m_minX THEN m_minX - n_maxX  " +
                            "                    WHEN n_minX > m_maxX THEN n_minX - m_maxX  " +
                            "                    ELSE 0  " +
                            "                END) +   " +
                            "                (CASE   " +
                            "                    WHEN n_maxY < m_minY THEN m_minY - n_maxY  " +
                            "                    WHEN n_minY > m_maxY THEN n_minY - m_maxY  " +
                            "                    ELSE 0  " +
                            "                END * CASE   " +
                            "                    WHEN n_maxY < m_minY THEN m_minY - n_maxY  " +
                            "                    WHEN n_minY > m_maxY THEN n_minY - m_maxY  " +
                            "                    ELSE 0  " +
                            "                END)  " +
                            "            )  " +
                            "        ELSE 0  " +
                            "    END AS shortest_distance");//获取两个节点的最短距离
                    while (result.hasNext()) {
                        Record record = result.next();
                        Double shortest_distance = record.get("shortest_distance").asDouble();
                        if (shortest_distance > maxDistance) {
                            maxDistance = shortest_distance;
                        }
                    }
                }
            }
        }
        return maxDistance;
    }

    public static void addNearRelation(Session session, Integer nodeId1, Integer nodeId2, String location, String distance,String order) {
        // 检查是否已经存在 NEAR 关系
        Result checkResult = session.run("MATCH (n1)-[r:NEAR]->(n2) " +
                "WHERE ID(n1) = $id1 AND ID(n2) = $id2 " +
                "RETURN r", parameters("id1", nodeId1, "id2", nodeId2));
    
        if (!checkResult.hasNext()) {
            // 如果不存在，则添加关系
            session.run("MATCH (n1), (n2) " +
                    "WHERE ID(n1) = $id1 AND ID(n2) = $id2 " +
                    "CREATE (n1)-[:NEAR {location : $location, distance: $distance, order: $order}]->(n2) ",
                    parameters("id1", nodeId1, "id2", nodeId2, "location", location, "distance",distance,"order",order));
            System.out.println("添加成功");
        } else {
            System.out.println("关系已存在，未添加");
        }
    }

    public static void changeOrderRelation(Session session,Integer nodeId1, Integer nodeId2, String order){
        /**
         * 修改当前的near关系
         */
        session.run("MATCH (n1)-[r:NEAR]->(n2) " +
                "WHERE ID(n1) = $id1 AND ID(n2) = $id2 " +
                "set r.order = $order", parameters("id1", nodeId1, "id2", nodeId2,"order",order));
        System.out.println("修改成功");
    }
}
