package com.Common;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class DriverCommon implements AutoCloseable {
    private Driver driver;

    static String url = InfoCommon.url;
    static String username =InfoCommon.username;
    static String password =InfoCommon.password;

    public DriverCommon() {
        this.driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
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
