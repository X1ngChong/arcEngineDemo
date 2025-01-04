package com.Common;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author JXS
 */

public class DriverCommon implements AutoCloseable {
    private Driver driver;

    // 构造函数可以保持为空
    public DriverCommon() {
        this.driver = GraphDatabase.driver(InfoCommon.url, AuthTokens.basic(InfoCommon.username, InfoCommon.password));
    }

    public Driver getGraphDatabase() {
        return this.driver;
    }

    @Override
    public void close() {
        if (driver != null) {
            driver.close();
        }
    }
}