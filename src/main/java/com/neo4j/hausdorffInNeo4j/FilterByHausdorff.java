package com.neo4j.hausdorffInNeo4j;

import com.Common.DriverCommon;
import com.Common.PathCommon;
import com.Util.ProjectionConverter;
import com.Util.filter.Hausdorff;
import org.neo4j.driver.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用豪斯多夫去进行图形相似度计算
 */

public class FilterByHausdorff {
    public static String[] doFilter(ArrayList<String> geometryList, List<String[]> realOsmIdList) {
        ArrayList<String> converterGeometryList = ProjectionConverter.converter(geometryList);

      //  System.out.println(converterGeometryList.size());

        ArrayList<String> realGeometryList = new ArrayList<>();

        ArrayList<Double> avgList = new ArrayList<>();

        String[] correspondingValue;


        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    for (int i = 0; i < realOsmIdList.size(); i++) {
                        String[] realOsmId = realOsmIdList.get(i);
                            // 构建 Cypher 查询
                            String cypherQuery = String.format("MATCH (n:xianlin) WHERE n.osm_id IN   %s  RETURN n.geometry as geometry", Arrays.toString(realOsmId));

                      //  System.out.println(cypherQuery); //MATCH (n:xianlin) WHERE n.osm_id IN   ['189248135', '475184563', '156126701']  RETURN n.geometry

                            // 执行查询并获取结果
                            Result result = tx.run(cypherQuery);
                            while (result.hasNext()) {
                                Record record = result.next();
                                String geometry = record.get("geometry").asString();
                                realGeometryList.add(geometry);
                            }
                     //   System.out.println(realGeometryList); //构建完成realGeometryList
                        Double avgValue = Hausdorff.calculateHausdorffDistance(converterGeometryList, realGeometryList);//一个组的平均值
                        avgList.add(avgValue);
                        realGeometryList.clear();
                    }
                     correspondingValue = getCorrespondingValue(avgList, realOsmIdList);//进行筛选最终值

                // 提交事务
                    tx.commit();
                }
            }
        }
        return correspondingValue;
    }

    public static String[] getCorrespondingValue(List<Double> avgList, List<String[]> realOsmIdList) {
        int minIndex = 0;
        double minValue = avgList.get(0);

        for (int i = 1; i < avgList.size(); i++) {
            if (avgList.get(i) < minValue) {
                minValue = avgList.get(i);
                minIndex = i;
            }
        }

        if (minIndex < realOsmIdList.size()) {
            return realOsmIdList.get(minIndex);
        }

        return new String[0];
    }

}
