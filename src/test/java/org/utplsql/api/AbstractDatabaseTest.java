package org.utplsql.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabaseTest {

    private static final String sUrl = "localhost:1521:XE";
    private static final String sUser = "APP";
    private static final String sPass = "pass";

    private Connection conn;
    private final List<Connection> connectionList = new ArrayList<>();

    public static String getUser() {
        return sUser;
    }

    @BeforeEach
    public void setupConnection() throws SQLException {
        conn = newConnection();
    }

    protected Connection getConnection() {
        return conn;
    }

    protected synchronized Connection newConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + sUrl, sUser, sPass);
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
