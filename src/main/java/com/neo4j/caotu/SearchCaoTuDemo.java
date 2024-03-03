package com.neo4j.caotu;


import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;
import static org.neo4j.driver.Values.parameters;

/**
草图转换为文件格式
 */
public class SearchCaoTuDemo {
    private  ArrayList<String> searches = new ArrayList<>();//地物类型
    private  ArrayList<String> positions = new ArrayList<>();//方位关系

    public SearchCaoTuDemo(String labelName) {
        searchDemo(labelName);
    }

    public  void searchDemo(String labelName){
//        String labelName = "wangmengyi";
        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    String cypherQuery = "MATCH (n:"+labelName+") where  n.Type <> 'road' RETURN n ";
                    Result result = tx.run(cypherQuery);
                    List<Record> list = result.list();

                    reverse(list);//反转数组

                    for(int i = 0;i<list.size();i++){
                       searches.add(list.get(i).get("n").get("Type").asString());
                    }
//                    System.out.println(searches.get(1));

                  for(int i = 0;i<list.size()-1;i++) {
                      Value id1 = list.get(i).get("n").get("ID");
                      Value id2 = list.get(i + 1).get("n").get("ID");
                      String NearQuery = "MATCH p=(n:" + labelName + " {ID: $ID1 })-[r:NEAR]->(m:" + labelName + " {ID: $ID2 }) RETURN r";
                      Result secondResult = tx.run(NearQuery, parameters("ID1", id1, "ID2", id2));
                      while (secondResult.hasNext()) {
//                          System.out.println(secondResult.next().get("r").get("location"));//关系
                          positions.add(secondResult.next().get("r").get("location").asString());//添加到方位数组中
                      }
                  }

                    String location = CalculateLocation.GetDirection(list.get(0).get("n").get("bbox").asList(), list.get(list.size()-1).get("n").get("bbox").asList());
                    positions.add(location);//第一个和最后一个的方位

//                    System.out.println(list.get(list.size()-1).get("n").get("Type").asString());
//                    System.out.println(location);
//                    System.out.println(positions.size());
//                    System.out.println(searches.size());

//                  System.out.println(positions.get(1));
                    // 提交事务
                    tx.commit();
                }
                driver.close();
            }
        }
    }

    public ArrayList<String> getSearches() {
        return searches;
    }

    public void setSearches(ArrayList<String> searches) {
        this.searches = searches;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }
}
