package com.population.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class DB {
    private static final Properties props = new Properties();

    static {
        try (var in = DB.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Choose URL based on environment setting (default: local)
            String env = System.getenv("RUN_ENV");
            if (env == null) {
                env = System.getProperty("run.env", "local");
            }

            if (env.equalsIgnoreCase("docker")) {
                props.setProperty("db.url", props.getProperty("db.url.docker"));
            } else {
                props.setProperty("db.url", props.getProperty("db.url.local"));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DB() {}

    public static Connection connect() throws java.sql.SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.pass"));
    }
}
