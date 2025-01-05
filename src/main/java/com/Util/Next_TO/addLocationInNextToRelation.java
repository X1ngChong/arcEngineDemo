package com.Util.Next_TO;

import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class addLocationInNextToRelation {


    private static String groupLabel = "xianlinGroup";
    public static void main(String[] args) {
        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            Session session = driver.session();

            addLocationInNextToRelation(session);
        }

    }

    private static void addLocationInNextToRelation(Session session) {
        Result result = session.run("MATCH p=(s:"+groupLabel+")-[r:NEXT_TO]->(e:"+groupLabel+") RETURN id(r) as id,s.bbox as sb,e.bbox as eb");//获取所有的组节点ID
        while (result.hasNext()) {
            Record record = result.next();
            Integer rId = record.get("id").asInt();
            List<Object> box1 = record.get("sb").asList();
            List<Object> box2= record.get("eb").asList();
            String location = CalculateLocation.getBaFangWei(box1, box2); //计算八方位
            setLocationInNextTo(session,rId,location);
        }
    }

    private static void setLocationInNextTo(Session session, Integer rId , String location) {

        session.run("MATCH ()-[r:NEXT_TO]->() " +
                "WHERE ID(r) = $rId " +
                " set r.location = $location", parameters("rId", rId,"location",location));
        System.out.println("添加成功");

    }

}