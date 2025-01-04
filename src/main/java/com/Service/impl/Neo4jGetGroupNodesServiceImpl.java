package com.Service.impl;

import com.Common.DriverCommon;
import com.Common.InfoCommon;
import com.Common.PathCommon;
import com.Service.Neo4jGetGroupNodesService;
import com.demo.overall.impl.GetMeetMethodImpl;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class Neo4jGetGroupNodesServiceImpl implements Neo4jGetGroupNodesService {
    public static void main(String[] args) {
        GetMeetMethodImpl g = new GetMeetMethodImpl();
        List<Integer[]> realResultList = g.getRealResultList();
        Neo4jGetGroupNodesServiceImpl test = new Neo4jGetGroupNodesServiceImpl();
        ArrayList<String[]> nodeListByIds = test.getNodeListByIds(realResultList);
        for (String[] s : nodeListByIds) {
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
                                tempList.add("'" + record.get("osmID").asString() + "'"); // 正确地将 osmID 添加到 tempList
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
        } catch (Exception e) {
            log.info("报错信息:{}", e.getMessage());
        }
        return resultList;
    }

    @Override
    public ArrayList<Integer[]> getObjectIdByIds(List<Integer[]> resultIdList) {
        ArrayList<Integer[]> resultList = new ArrayList<>();

        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (Integer[] list : resultIdList) {
                    List<Integer> tempList = new ArrayList<>(); // 使用 ArrayList 来存储 osmID
                    for (Integer i : list) {
                        String cypherQuery = "MATCH p=(n)-[r:Have]->(m) WHERE id(n) = " + i + " RETURN m.OBJECTID as osmID";
                        Result result = tx.run(cypherQuery);
                        while (result.hasNext()) {
                            Record record = result.next();
                            tempList.add(record.get("osmID").asInt()); // 正确地将 osmID 添加到 tempList
                            //[155438410, 155438043, 8006, 8005, 871590011, 871600929, 871600928, 871600927, 871600926, 871600925, 871600924, 8007, 8004, 871600937, 871600933, 871600930, 525942922, 871600936, 871600935, 871600934, 871600932, 871600931, 871600922, 583447630]
                            //["'460504071'", "'8001'", "'8005'"]
                            //["'"155438410"'", "'"155438043"'", "'"8006"'", "'"8005"'", "'"871590011"'", "'"871600929"'", "'"871600928"'", "'"871600927"'", "'"871600926"'", "'"871600925"'", "'"871600924"'", "'"8007"'", "'"8004"'", "'"871600937"'", "'"871600933"'", "'"871600930"'", "'"525942922"'", "'"871600936"'", "'"871600935"'", "'"871600934"'", "'"871600932"'", "'"871600931"'", "'"871600922"'", "'"583447630"'"]
                        }
                    }
                    // 将 tempList 转换为 String[] 并加入 resultList
                    resultList.add(tempList.toArray(new Integer[0]));
                }
                tx.commit(); // 提交事务
            }
        } catch (Exception e) {
            log.info("报错信息:{}", e.getMessage());
        }
        return resultList;
    }
}