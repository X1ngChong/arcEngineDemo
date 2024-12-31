package com.Util.Next_TO;

import com.Common.InfoCommon;
import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

/**
 * 在NextTo关系上添加经过了哪条道路的ID
 * @author JXS
 */
public class addRoadIdInNextToRelation {
    // public static final String LAYER_NAME = "xianLinRoad"; //设置道路节点所在图层的名称
    public static final String LAYER_NAME = "xianLinTest"; //设置道路节点所在图层的名称

    public static final String GroupName = "Group"; //设置道路节点所在图层的名称


    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
             Session session = driver.session()){
            addRoadId(session);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer getGroupRoadId(Session session, Integer groupID1, Integer groupID2) {
        String cypherQuery =
                "MATCH (b1:"+GroupName+"), (b2:"+GroupName+") " +
                        "WHERE id(b1) = $groupID1 AND id(b2) = $groupID2 " +
                        "WITH b1, b2, " +
                        "((b1.bbox[0] + b1.bbox[2]) / 2) AS centerX1, " +
                        "((b1.bbox[1] + b1.bbox[3]) / 2) AS centerY1, " +
                        "((b2.bbox[0] + b2.bbox[2]) / 2) AS centerX2, " +
                        "((b2.bbox[1] + b2.bbox[3]) / 2) AS centerY2 " +
                        "WITH 'LINESTRING(' + " +
                        "toString(centerX1) + ' ' + toString(centerY1) + ', ' + " +
                        "toString(centerX2) + ' ' + toString(centerY2) + ')' AS line " +
                        "CALL spatial.intersects('"+LAYER_NAME+"', line) YIELD node " +
                        "WHERE 'road' IN labels(node)"+//这边是因为草图的图层道路和地物在一个图层
                        "RETURN id(node) as id";

        System.out.println(cypherQuery);
        Result result = session.run(cypherQuery, parameters("groupID1", groupID1, "groupID2", groupID2));
        int id = 0;
        while (result.hasNext()) {
            Record record = result.next();
            id = record.get("id").asInt();//道路ID
        }
        return id; // 返回道路的id
    }

    private static void addRoadId(Session session) {
        String cypherQuery = "MATCH p=(s:"+GroupName+")-[r:NEXT_TO]->(e:"+GroupName+") RETURN id(s) as s,id(e) as e,id(r) as r";

        System.out.println(cypherQuery);
        Result result = session.run(cypherQuery);
        while (result.hasNext()) {
            Record record = result.next();
            Integer groupRoadId = getGroupRoadId(session, record.get("s").asInt(), record.get("e").asInt());//获取道路的id
            changeLocation(session,record.get("r").asInt(),groupRoadId);//将道路的id添加到关系上
        }
    }

    public static void changeLocation(Session session,Integer idr,Integer roadId){
        /**
         * 修改当前的NEXT_TO关系,给当前的NEXT_TO关系上添加roadID
         */
        session.run("MATCH p=()-[r:NEXT_TO]->() where id(r) =  $idr  set r.roadId = $roadId", parameters("idr", idr,"roadId", roadId));
        System.out.println("修改成功");
    }
}
