package com.Util.filter;

import com.Common.PathCommon;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadFilter {
    public static ArrayList<String[]> roadFilter(Driver driver, Boolean[] roadRelation, ArrayList<String[]> list, Map<String[], Double> map){
        String labelName = PathCommon.caoTuLabel;//  需要去查询的图层
        System.out.println(list.toString());
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                int listSize = list.size();
                for (int i = 0 ;i<listSize;i++){
                    String[] strings = list.get(i);
                    for(int k = 0;k<strings.length;k++) {
                        String cypherQuery2 = "MATCH " +
                                "(n1:"+labelName+"{"+PathCommon.OSMID+": $osmid1 })-[r:NEAR]->(n2:"+labelName+" {"+PathCommon.OSMID+": $osmid2 }) " +
                                " RETURN r,r.line AS line limit 1";

                        //语句有错误修改完成 使用parameters传入的参数是 "'123321'" 带俩个双引号的数据
                        Map<String, Object> parameters = new HashMap<>();
                        // 创建一个参数映射

                        if (k >= strings.length - 1) {
                            break;
                        }
                        parameters.put("osmid1", strings[k].split("'")[1]);// '99661839'
                        parameters.put("osmid2", strings[k + 1].split("'")[1]); //'118862383'


                        //加入roadRelation
                        Result result = tx.run(cypherQuery2,parameters);
                        while(result.hasNext()){
                            Record record = result.next();
                            boolean boolen = record.get("line").asString().contains("isIntersects");//如果结果里面有这个判断
                            //System.out.println(boolen);
                            if (boolen == roadRelation[k]){
                               // list.add(list.get(i));//当俩个有一个相等的时候就 再次添加
                                Double value = map.get(strings);//现在使用权值去判断

                                if (value != null) {
                                    value += 1.0; // 增加 1
                                    map.put(strings, value); // 更新 Map 中的值
                                }

                                break;
                            }
                        }
                    }
                }

                // 提交事务
                tx.commit();

            }
        }


        return  list;
    }
}
