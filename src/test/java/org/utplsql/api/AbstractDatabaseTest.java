package org.utplsql.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabaseTest {

    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASS;

    static {
        DB_URL = EnvironmentVariableUtil.getEnvValue("DB_URL", "localhost:1521:XE");
        DB_USER = EnvironmentVariableUtil.getEnvValue("DB_USER", "app");
        DB_PASS = EnvironmentVariableUtil.getEnvValue("DB_PASS", "pass");
    }

    private Connection conn;
    private final List<Connection> connectionList = new ArrayList<>();

    public static String getUser() {
        return DB_USER;
    }

    @BeforeEach
    public void setupConnection() throws SQLException {
        conn = newConnection();
    }

    protected Connection getConnection() {
        return conn;
    }

    protected synchronized Connection newConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + DB_URL, DB_USER, DB_PASS);
        connectionList.add(conn);
        return conn;
    }

    @AfterEach
    public void teardownConnection() {
        for (Connection conn : connectionList) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
