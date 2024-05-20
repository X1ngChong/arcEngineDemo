package com.neo4j.hausdorffInNeo4j;

import com.Common.DriverCommon;
import org.neo4j.driver.*;


import java.util.ArrayList;
import java.util.List;

/**
 * 使用豪斯多夫去进行图形相似度计算
 */

public class FilterByHausdorff {
    public static ArrayList<String[]> searchDemo(ArrayList<String> geometryList, List<String[]> realOsmIdList){
        //1.先获取出对应草图的 geometryList
        //2.使用实际结果的geometryList进行图形相似度匹配
        ArrayList<String[]> list = new ArrayList<>();
        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    String cypherQuery = "";
                    Result result = tx.run(cypherQuery);
                    while (result.hasNext()) {

                    }
                    // 提交事务
                    tx.commit();
                }
                session.close();
                driver.close();
            }
            return list;
        }
    }

}
