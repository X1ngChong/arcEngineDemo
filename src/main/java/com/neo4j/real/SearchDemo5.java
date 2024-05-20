package com.neo4j.real;

import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

//简便部分查询
public class SearchDemo5 {
    public static void main(String[] args) {
        searchDemo();
    }
    public static ArrayList<String[]> searchDemo(){
        ArrayList<String[]> list = new ArrayList<>();
        String search1 = "pitch";
        String search2 = "pitch";
        String search3 = "pitch";
        String search4 = "building";
        String position1 = "北";
        String position2 = "东";
        String position3 = "北";
        String lastposition = "北";

        try (DriverCommon driverCommon = new DriverCommon()) {
            int all = 0;
            Driver driver = driverCommon.getGraphDatabase();

            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    // 第一次查询
                    Result firstResult = runQuery(tx, search1, search2, position1,null);

                    while (firstResult.hasNext()) {
                        Record firstRecord = firstResult.next();
                        Node firstNode = firstRecord.get("beginNode").asNode();
                        Node secondNode = firstRecord.get("endNode").asNode();

                        List<Object> firstBbox = firstNode.get("bbox").asList();

                        // 第二次查询
                        Result secondResult = runQuery(tx, null, search3, position2, secondNode.id());
                        while (secondResult.hasNext()) {
                            Record secondRecord = secondResult.next();
                            Node thirdNode = secondRecord.get("endNode").asNode();

                            // 第三次查询
                            Result thirdResult = runQuery(tx, null, search4, position3, thirdNode.id());
                            while (thirdResult.hasNext()) {
                                Record thirdRecord = thirdResult.next();
                                Node lastNode = thirdRecord.get("endNode").asNode();
                                List<Object> lastBbox = lastNode.get("bbox").asList();

                                if (CalculateLocation.GetDirection(firstBbox, lastBbox).contains(lastposition)) {
                                    System.out.println(firstNode.get("name")+" "+secondNode.get("name")+" "+thirdNode.get("name")+" "+lastNode.get("name"));
                                    all++;
                                }

                            }
                        }
                    }
                    System.out.println(all);
                    // 提交事务
                    tx.commit();
                }
                driver.close();
            }
        }

        return list;
    }

    private static Result runQuery(Transaction tx, String search, String search2, String position, Long beginNodeId) {
        StringBuilder cypherQuery = new StringBuilder();
        if(search == null){
            cypherQuery.append("MATCH (beginNode)");
        }else{
            cypherQuery.append("MATCH (beginNode {fclass: '" + search + "'})");
        }


        if (search2 != null) {
            cypherQuery.append("-[r:NEAR]->(endNode {fclass: '" + search2 + "'})");
        }


        if(beginNodeId != null){
            cypherQuery.append(" WHERE ID(beginNode) = $beginNodeId AND r.location CONTAINS '"+position+"' ");//判断方位
        }else{
            cypherQuery.append(" WHERE  r.location CONTAINS '"+position+"' ");//判断方位
        }

        cypherQuery.append(" RETURN ");

        cypherQuery.append(search != null ? "beginNode,endNode" : "endNode");

        Result result;

        result = tx.run(cypherQuery.toString(), parameters("beginNodeId",beginNodeId));
        System.out.println(cypherQuery);
        return result;
    }

}
