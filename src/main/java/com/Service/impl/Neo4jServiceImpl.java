package com.Service.impl;

import com.Common.DriverCommon;
import com.Service.Neo4jService;
import org.neo4j.driver.*;

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
                    String cypherQuery = "MATCH (n) where n.osm_id ="+osmId +" RETURN n.name as name";
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
}
