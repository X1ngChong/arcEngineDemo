package com.Util;

import com.Common.DriverCommon;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

/**
 * @author JXS
 */
public class AddNearRelation {
    public static void main(String[] args) {

//        String [] textNames = {"caixiangyu","chenhui","huangyi","wangmengyi","zhangpengtao"};
        String [] textNames = {"xianlin"};
        for(int i = 0;i<textNames.length;i++){
            String textName= textNames[i];
            try (DriverCommon driverCommon = new DriverCommon()){
                Driver driver = driverCommon.getGraphDatabase();
                Session session = driver.session();
                Session session2 = driver.session();
                // 遍历所有  节点
                try (Transaction tx = session.beginTransaction()){
                    Result result = tx.run("MATCH (n:"+textName+") RETURN n, n.bbox as box");

                    while (result.hasNext()) {
                        Record record = result.next();
                        Node node = record.get("n").asNode();
                        List<Object> box = record.get("box").asList();//double 类型的数组
                        System.out.println("box1 : "+box.get(0)+" "+box.get(1));


                        // 检查当前节点与其他节点的距离
                        try (Transaction tx2 = session2.beginTransaction()) {
                            Result innerResult = tx2.run("MATCH (m:"+textName+") WHERE id(m) <> $currentNodeId " +
                                    " RETURN m,m.bbox as box", parameters("currentNodeId",node.id()));
                            while (innerResult.hasNext()) {
                                Record innerRecord = innerResult.next();
                                Node node2 = innerRecord.get("m").asNode();
                                List<Object> box2 = innerRecord.get("box").asList();

                                String location = CalculateLocation.GetDirection(box, box2);

                                // 计算节点之间的距离
                            double distance = CalculateDistanceByBboxUtil.calculate(box, box2);
                            System.out.println("距离:  "+distance);

                             // 如果距离小于200，添加或删除 "NEAR" 关系
                            if (distance < 300) {

                                /*
                                  删除当前添加的关系
                                 */
                                //delNearRelation(tx2,node,node2);

                                /*
                                  添加关系
                                 */
                                addNearRelation(tx2,node,node2,location);
                                }
                            }
                            tx2.commit();
                        }
                    }
                    tx.commit();
                }
            }
        }

    }

    public static void addNearRelation(Transaction tx2,Node node,Node node2, String location){
        /**
         * 添加关系
         */
        tx2.run("MATCH (n1), (n2) " +
                " WHERE ID(n1) = $id1 AND ID(n2) = $id2" +
//                                        " AND NOT EXISTS((n1)-[:Near]-(n2)) " +
//                                        " AND NOT EXISTS((n2)-[:Near]-(n2)) " +
                " CREATE (n1)-[:NEAR {location : '" + location + "'}]->(n2) ", parameters("id1", node.id(), "id2", node2.id()));
        System.out.println("添加成功");
    }

    public static void delNearRelation(Transaction tx2,Node node,Node node2){
        /**
         * 删除当前添加的关系
         */
        tx2.run("MATCH (n1)-[r:NEAR]->(n2) " +
                "WHERE ID(n1) = $id1 AND ID(n2) = $id2 " +
                "DELETE r", parameters("id1", node.id(), "id2", node2.id()));
        System.out.println("删除成功");
    }

}
