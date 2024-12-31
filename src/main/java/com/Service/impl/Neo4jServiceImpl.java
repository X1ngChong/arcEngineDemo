package com.Service.impl;

import com.Bean.GroupMap;
import com.Common.DriverCommon;
import com.Common.InfoCommon;
import com.Common.PathCommon;
import com.Service.Neo4jService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author JXS
 */
@Service
@Slf4j
public class Neo4jServiceImpl implements Neo4jService {

    /**
     * 通过osmId获取数据id
     * @return
     */
    public String getNameByOsmId(String[] list) {
        String osmId = list[0];
        String name = null;
        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    String cypherQuery = "MATCH (n) where n."+ PathCommon.OSMID +" ="+osmId +" RETURN n."+PathCommon.osmName+" as name";
                    Result result = tx.run(cypherQuery);
                    while (result.hasNext()) {
                        Record record = result.next();
                        //第一个节点相关数据
                        name = record.get("name").asString();
                    }
                }
            }
        }
        return name;
    }

    /**
     * 根据标签获取对应的下面的所有的id
     * @param tags
     * @return
     */
    @Override
    public List<Integer> getGroupIdByTags(String tags) {
        List<Integer> ids = new ArrayList<>();

        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    String cypherQuery = "MATCH (n:"+tags+") RETURN id(n)  as id ";
                    Result result = tx.run(cypherQuery);
                    while (result.hasNext()) {
                        Record record = result.next();
                        //第一个节点相关数据
                        ids.add(record.get("id").asInt());
                    }
                }
            }
        }

        return ids;
    }

    /**
     * 用hashmap来存储这样的所有地物
     *  * 区分出每个街区包含的地标 如 110:{{id:10,type:pitch},{id:11,type:lake}}
     * @return
     */
    @Override
    public HashMap<Integer, List<GroupMap>> getGroupIdMap() {
        HashMap<Integer,List<GroupMap>> groupMaps = new HashMap<>();

        Neo4jServiceImpl  neo4jService = new Neo4jServiceImpl();
        //获取所有的id
        List<Integer> groupIdTags = neo4jService.getGroupIdByTags(PathCommon.RealGroupTags);
        List<Integer> groupIdTags1 = neo4jService.getGroupIdByTags(PathCommon.CaoTuGroupTags);

        //合并为整个组
        // 使用 Stream API 合并
        List<Integer> combinedGroupIds = Stream.concat(groupIdTags.stream(), groupIdTags1.stream())
                .collect(Collectors.toList());

        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (Integer ids : combinedGroupIds) {
                    List<GroupMap> temp = new ArrayList<>();
                    String cypherQuery = "MATCH (n)-[r]->(m) where  id(n) = "+ids+" and m.type is NOT null " +
                            "RETURN id(m) as id ,m.type as type ";
                    Result result = tx.run(cypherQuery);
                    while (result.hasNext()) {
                        Record record = result.next();
                        temp.add(new GroupMap(record.get("id").asInt(),record.get("type").asString()));//将获取的数据临时存储
                    }
                    groupMaps.put(ids,temp);//填充集合
                }
                tx.commit(); // 提交事务
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  groupMaps;
    }

    /**
     * 计算标志性地物part的方位相似度
     * @return
     */
    @Override
    public Double getPartLocationSimByType(Integer groupId1,Integer groupId2,String type) {
        double highestSimilarity = 0.0; // 用于存储最高相似度
        Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
        try (Session session = driver.session()) {
                // 获取 groupId1 的 location
                Map<Integer, Map<String, Integer>> locationsGroup1 = getLocations(session, groupId1, type);
                // 获取 groupId2 的 location
                Map<Integer, Map<String, Integer>> locationsGroup2 = getLocations(session, groupId2, type);


            Integer bestMId1 = null; // 存储最佳 mId1
            Integer bestMId2 = null; // 存储最佳 mId2

            // 遍历 group1 中的每个 mId
            for (Map.Entry<Integer, Map<String, Integer>> entry1 : locationsGroup1.entrySet()) {
                Integer mId1 = entry1.getKey();
                Map<String, Integer> locationCountMap1 = entry1.getValue();
                int count1 = calculateTotalCount(locationCountMap1); // 获取 count1 的数量

                // 遍历 group2 中的每个 mId
                for (Map.Entry<Integer, Map<String, Integer>> entry2 : locationsGroup2.entrySet()) {
                    Integer mId2 = entry2.getKey();
                    Map<String, Integer> locationCountMap2 = entry2.getValue();
                    int count2 = calculateTotalCount(locationCountMap2); // 获取 count2 的数量

                    // 计算相同 location 的数量
                    int commonCount = 0;
                    for (String location : locationCountMap1.keySet()) {
                        commonCount += Math.min(locationCountMap1.getOrDefault(location,0), locationCountMap2.getOrDefault(location, 0));
                    }

                    // 计算相似性
                    int totalCount = Math.max(count1, count2); // 最大的方位数量
                    double similarity = totalCount > 0 ? (double) commonCount / totalCount : 0.0;

                    // 更新最高相似度
                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                        bestMId1 = mId1;
                        bestMId2 = mId2;
                    }
                }
            }

            // 输出最高相似度和对应的 mId
           // System.out.println("最高相似度: " + highestSimilarity + "，对应的 mId1: " + bestMId1 + "，mId2: " + bestMId2);
        }
            catch (Exception e){
            System.out.println(e.getMessage());
        }

        return highestSimilarity;
    }

    @Override
    public Double getPartDistance(Integer groupId1,Integer groupId2) {
        double highestSimilarity = 0.0; // 用于存储最高相似度
        Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
        try (Session session = driver.session()) {
            // 获取 groupId1 的 location
            Map<Integer, Map<String, Integer>> distancesGroup1 = getDistances(session, groupId1);
            // 获取 groupId2 的 location
            Map<Integer, Map<String, Integer>> distancesGroup2 = getDistances(session, groupId2);


            Integer bestMId1 = null; // 存储最佳 mId1
            Integer bestMId2 = null; // 存储最佳 mId2

            // 遍历 group1 中的每个 mId
            for (Map.Entry<Integer, Map<String, Integer>> entry1 : distancesGroup1.entrySet()) {
                Integer mId1 = entry1.getKey();
                Map<String, Integer> locationCountMap1 = entry1.getValue();
                int count1 = calculateTotalCount(locationCountMap1); // 获取 count1 的数量

                // 遍历 group2 中的每个 mId
                for (Map.Entry<Integer, Map<String, Integer>> entry2 : distancesGroup2.entrySet()) {
                    Integer mId2 = entry2.getKey();
                    Map<String, Integer> locationCountMap2 = entry2.getValue();
                    int count2 = calculateTotalCount(locationCountMap2); // 获取 count2 的数量

                    // 计算相同 location 的数量
                    int commonCount = 0;
                    for (String location : locationCountMap1.keySet()) {
                        commonCount += Math.min(locationCountMap1.getOrDefault(location,0), locationCountMap2.getOrDefault(location, 0));
                    }

                    // 计算相似性
                    int totalCount = Math.max(count1, count2); // 最大的方位数量
                    double similarity = totalCount > 0 ? (double) commonCount / totalCount : 0.0;

                    // 更新最高相似度
                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return highestSimilarity;
    }

    @Override
    public Double getPartOrderSim(Integer groupId1,Integer groupId2) {
        double highestSimilarity = 0.0; // 用于存储最高相似度
        Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
        try (Session session = driver.session()) {
            // 获取 groupId1 的 location
            Map<Integer, Map<String, Integer>> distancesGroup1 = getOrders(session, groupId1);
            // 获取 groupId2 的 location
            Map<Integer, Map<String, Integer>> distancesGroup2 = getOrders(session, groupId2);


            Integer bestMId1 = null; // 存储最佳 mId1
            Integer bestMId2 = null; // 存储最佳 mId2

            // 遍历 group1 中的每个 mId
            for (Map.Entry<Integer, Map<String, Integer>> entry1 : distancesGroup1.entrySet()) {
                Integer mId1 = entry1.getKey();
                Map<String, Integer> locationCountMap1 = entry1.getValue();
                int count1 = calculateTotalCount(locationCountMap1); // 获取 count1 的数量

                // 遍历 group2 中的每个 mId
                for (Map.Entry<Integer, Map<String, Integer>> entry2 : distancesGroup2.entrySet()) {
                    Integer mId2 = entry2.getKey();
                    Map<String, Integer> locationCountMap2 = entry2.getValue();
                    int count2 = calculateTotalCount(locationCountMap2); // 获取 count2 的数量

                    // 计算相同 location 的数量
                    int commonCount = 0;
                    for (String location : locationCountMap1.keySet()) {
                        commonCount += Math.min(locationCountMap1.getOrDefault(location,0), locationCountMap2.getOrDefault(location, 0));
                    }

                    // 计算相似性
                    int totalCount = Math.max(count1, count2); // 最大的方位数量
                    double similarity = totalCount > 0 ? (double) commonCount / totalCount : 0.0;

                    // 更新最高相似度
                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return highestSimilarity;
    }

    /**
     * 计算没有标志性地物 选取左上角的地物计算 (没有使用)
     * @param groupId1
     * @param groupId2
     * @return
     */
    public Double getPartLocationSimNoType(Integer groupId1,Integer groupId2) {
        double highestSimilarity = 0.0; // 用于存储最高相似度
        Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
        try (Session session = driver.session()) {
            // 获取 groupId1 的 location
            Map<Integer, Map<String, Integer>> locationsGroup1 = getLocations(session, groupId1);
            // 获取 groupId2 的 location
            Map<Integer, Map<String, Integer>> locationsGroup2 = getLocations(session, groupId2);


            Integer bestMId1 = null; // 存储最佳 mId1
            Integer bestMId2 = null; // 存储最佳 mId2

            // 遍历 group1 中的每个 mId
            for (Map.Entry<Integer, Map<String, Integer>> entry1 : locationsGroup1.entrySet()) {
                Integer mId1 = entry1.getKey();
                Map<String, Integer> locationCountMap1 = entry1.getValue();
                int count1 = calculateTotalCount(locationCountMap1); // 获取 count1 的数量

                // 遍历 group2 中的每个 mId
                for (Map.Entry<Integer, Map<String, Integer>> entry2 : locationsGroup2.entrySet()) {
                    Integer mId2 = entry2.getKey();
                    Map<String, Integer> locationCountMap2 = entry2.getValue();
                    int count2 = calculateTotalCount(locationCountMap2); // 获取 count2 的数量

                    // 计算相同 location 的数量
                    int commonCount = 0;
                    for (String location : locationCountMap1.keySet()) {
                        commonCount += Math.min(locationCountMap1.getOrDefault(location,0), locationCountMap2.getOrDefault(location, 0));
                    }

                    // 计算相似性
                    int totalCount = count1 + count2; // 总数量
                    double similarity = totalCount > 0 ? (double) (commonCount *2) / totalCount : 0.0;

                    // 更新最高相似度
                    if (similarity > highestSimilarity) {
                        highestSimilarity = similarity;
                        bestMId1 = mId1;
                        bestMId2 = mId2;
                    }
                }
            }

            // 输出最高相似度和对应的 mId
           // log.info("最高相似度:{}，对应的 mId1: {}，mId2: {}",highestSimilarity,bestMId1,bestMId2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return highestSimilarity;
    }


    private  Map<Integer, Map<String, Integer>>  getLocations(Session session,Integer groupId,String type){
        String cypherQuery = "";
        if("null".equals(type)){
            cypherQuery = "MATCH (n)-[]->(m) " +
                    "WHERE id(n) = " + groupId +
                    " MATCH (m)-[r:NEAR]->(z) " +
                    "RETURN id(m) AS mId, r.location AS location";
        }else{
            cypherQuery = "MATCH (n)-[]->(m) " +
                    "WHERE id(n) = " + groupId + " AND m.type = \"" + type + "\" " +
                    "MATCH (m)-[r:NEAR]->(z) " +
                    "RETURN id(m) AS mId, r.location AS location";
        }


        Map<Integer, Map<String, Integer>> locationCountMap = new HashMap<>();
        Result result = session.run(cypherQuery);
        while (result.hasNext()) {
            Record record = result.next();
            Integer mId = record.get("mId").asInt(); // 获取 mId
            String mType = record.get("type").asString(); // 获取类型
            String location = record.get("location").asString();

            // 获取或创建 mId 对应的 location 计数 Map
            locationCountMap.putIfAbsent(mId, new HashMap<>());
            Map<String, Integer> innerMap = locationCountMap.get(mId);
            innerMap.put(mType+"-"+location, innerMap.getOrDefault(location, 0) + 1); // 计数
        }
        return locationCountMap;
    }

    private  Map<Integer, Map<String, Integer>>  getOrders(Session session,Integer groupId){
        String cypherQuery = "";
            cypherQuery = "MATCH (n)-[]->(m) " +
                    "WHERE id(n) = " + groupId +
                    " MATCH (m)-[r:NEAR]->(z) " +
                    "RETURN id(m) AS mId,m.type as type, r.order AS order";


        Map<Integer, Map<String, Integer>> locationCountMap = new HashMap<>();
        Result result = session.run(cypherQuery);
        while (result.hasNext()) {
            Record record = result.next();
            Integer mId = record.get("mId").asInt(); // 获取 mId
            String mType = record.get("type").asString(); // 获取类型
            String order = record.get("order").asString();

            // 获取或创建 mId 对应的 location 计数 Map
            locationCountMap.putIfAbsent(mId, new HashMap<>());
            Map<String, Integer> innerMap = locationCountMap.get(mId);
            innerMap.put(mType+"-"+order, innerMap.getOrDefault(order, 0) + 1); // 用类型+顺序进行计数
        }
        return locationCountMap;
    }
    private  Map<Integer, Map<String, Integer>>  getDistances(Session session,Integer groupId){
        String cypherQuery = "";
            cypherQuery = "MATCH (n)-[]->(m) " +
                    "WHERE id(n) = " + groupId +
                    " MATCH (m)-[r:NEAR]->(z) " +
                    "RETURN id(m) AS mId, m.type as type , r.distance AS distance";


        Map<Integer, Map<String, Integer>> distanceCountMap = new HashMap<>();
        Result result = session.run(cypherQuery);
        while (result.hasNext()) {
            Record record = result.next();
            Integer mId = record.get("mId").asInt(); // 获取 mId
            String mType = record.get("type").asString(); // 获取类型
            String distance = record.get("distance").asString();

            // 获取或创建 mId 对应的 location 计数 Map
            distanceCountMap.putIfAbsent(mId, new HashMap<>());
            Map<String, Integer> innerMap = distanceCountMap.get(mId);
            innerMap.put(mType+"-"+distance, innerMap.getOrDefault(distance, 0) + 1); // 计数
        }
        return distanceCountMap;
    }

    /**
     * 当没有显著性地物的时候去计算相似度
     * @param session
     * @param groupId
     * @return
     */
    private  Map<Integer, Map<String, Integer>>  getLocations(Session session,Integer groupId){

        String cypherQuery = "MATCH (n)-[]->(m)  " +
                "WHERE id(n) = "+groupId+" AND m.OBJECTID IS NOT NULL   " +
                "WITH m,   " +
                "  m.bbox[0] AS minX,  " +
                "  m.bbox[1] AS minY  " +
                " ORDER BY minY ASC, minX ASC  " +
                " LIMIT 1  " +
                "MATCH (m)-[r:NEAR]->(z) " +
                " RETURN id(m) AS mId,m.type as type, r.location AS location ";

        Map<Integer, Map<String, Integer>> locationCountMap = new HashMap<>();
        Result result = session.run(cypherQuery);
        while (result.hasNext()) {
            Record record = result.next();
            Integer mId = record.get("mId").asInt(); // 获取 mId
            String location = record.get("location").asString();

            // 获取或创建 mId 对应的 location 计数 Map
            locationCountMap.putIfAbsent(mId, new HashMap<>());
            Map<String, Integer> innerMap = locationCountMap.get(mId);
            innerMap.put(location, innerMap.getOrDefault(location, 0) + 1); // 计数
        }
        return locationCountMap;
    }

    /**
     * 计算当前组下面的的总数
     * @param locations
     * @return
     */
    private static int calculateTotalCount(Map<String, Integer> locations) {
        int total = 0;
            for (int count : locations.values()) {
                total += count;
            }
        return total;
    }
}
