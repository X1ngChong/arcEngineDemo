package com.Service.impl;

import com.Common.DriverCommon;
import com.Common.PathCommon;
import com.Service.Neo4jGetGroupNodes;
import com.demo.impl.GetMeetMethodImpl;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Neo4jGetGroupNodesImpl implements Neo4jGetGroupNodes {
    public static void main(String[] args) {
        GetMeetMethodImpl g = new GetMeetMethodImpl();
        List<Integer[]> realResultList = g.getRealResultList();
        Neo4jGetGroupNodesImpl test = new Neo4jGetGroupNodesImpl();
        ArrayList<String[]> nodeListByIds = test.getNodeListByIds(realResultList);
        for (String[] s:nodeListByIds){
            System.out.println(Arrays.toString(s));
        }

    }

    @Override
    public ArrayList<String[]> getNodeListByIds(List<Integer[]> resultIdList) {
        ArrayList<String[]> resultList = new ArrayList<>();

        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    for (Integer[] list : resultIdList) {
                        List<String> tempList = new ArrayList<>(); // 使用 ArrayList 来存储 osmID
                        for (Integer i : list) {
                            String cypherQuery = "MATCH p=(n)-[r:Have]->(m) WHERE id(n) = " + i + " RETURN m." + PathCommon.OSMID + " as osmID";
                            Result result = tx.run(cypherQuery);
                            while (result.hasNext()) {
                                Record record = result.next();
                                tempList.add("'"+record.get("osmID").asString()+"'"); // 正确地将 osmID 添加到 tempList
                                //[155438410, 155438043, 8006, 8005, 871590011, 871600929, 871600928, 871600927, 871600926, 871600925, 871600924, 8007, 8004, 871600937, 871600933, 871600930, 525942922, 871600936, 871600935, 871600934, 871600932, 871600931, 871600922, 583447630]
                                //["'460504071'", "'8001'", "'8005'"]
                                //["'"155438410"'", "'"155438043"'", "'"8006"'", "'"8005"'", "'"871590011"'", "'"871600929"'", "'"871600928"'", "'"871600927"'", "'"871600926"'", "'"871600925"'", "'"871600924"'", "'"8007"'", "'"8004"'", "'"871600937"'", "'"871600933"'", "'"871600930"'", "'"525942922"'", "'"871600936"'", "'"871600935"'", "'"871600934"'", "'"871600932"'", "'"871600931"'", "'"871600922"'", "'"583447630"'"]
                            }
                        }
                        // 将 tempList 转换为 String[] 并加入 resultList
                        resultList.add(tempList.toArray(new String[0]));
                    }
                    tx.commit(); // 提交事务
                }
            }
        }
        return resultList;
    }
}