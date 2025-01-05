package com.Util.Next_TO;

import com.Common.InfoCommon;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

/**
 * 在NextTo关系上添加地物的投影顺序
 * @author JXS
 */
public class addTypeOrderListInNextToRelation {
    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()){
            //获取所有的NextTo关系的id 和orderList
            HashMap<Integer, List<Object>> allNextToRelation = getAllNextToRelation(session);

            for (Map.Entry<Integer, List<Object>> entry : allNextToRelation.entrySet()) {
                Integer rId = entry.getKey();//关系ID
                List<Object> orderList = entry.getValue();//道路集合
                List<String> typeByOrderList = getTypeByOrderList(session, orderList);
                setTypeOrderList(session,rId,typeByOrderList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HashMap<Integer, List<Object>> getAllNextToRelation(Session session) {
        String cypherQuery = "MATCH p=()-[r:NEXT_TO]->()  RETURN id(r) as id,r.orderList as list";

        System.out.println(cypherQuery);
        Result result = session.run(cypherQuery);
        HashMap<Integer, List<Object>> temp = new HashMap<>();
        while (result.hasNext()) {
            Record record = result.next();
            int rId = record.get("id").asInt();//关系ID
            List<Object> list = record.get("list").asList();
            temp.put(rId,list);
        }
        return temp; // 返回道路的id
    }

    private static List<String>  getTypeByOrderList(Session session, List<Object> orderList) {
        List<String> typeList = new ArrayList<>();
        String cypherQuery = "MATCH (n) where id(n) in "+ orderList +" RETURN n.fclass as type ";

        System.out.println(cypherQuery);

        Result result = session.run(cypherQuery);
        HashMap<Integer, List<Object>> temp = new HashMap<>();
        while (result.hasNext()) {
            Record record = result.next();
            String type = record.get("type").asString();//关系ID
            typeList.add(type);
        }
        return typeList; // 返回地物类型
    }
    
    public static void setTypeOrderList(Session session,Integer rId, List<String> orderList){
        /**
         * 修改当前的NEXT_TO关系,给当前的NEXT_TO关系上添加typeOrderList
         */
        session.run("MATCH p=()-[r:NEXT_TO]->() where id(r) =  $idr  set r.typeOrderList = $orderList", parameters("idr", rId,"orderList", orderList));
        System.out.println("修改成功");
    }
}
