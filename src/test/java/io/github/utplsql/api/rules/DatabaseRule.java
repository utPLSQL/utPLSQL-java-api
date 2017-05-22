package io.github.utplsql.api.rules;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class DatabaseRule extends ExternalResource {

    private static String sUrl;
    private static String sUser;
    private static String sPass;

    static {
        sUrl  = System.getenv("API_DB_URL")  != null ? System.getenv("API_DB_URL")  : "127.0.0.1:1521:XE";
        sUser = System.getenv("API_DB_USER") != null ? System.getenv("API_DB_USER") : "app";
        sPass = System.getenv("API_DB_PASS") != null ? System.getenv("API_DB_PASS") : "app";
    }

    private List<Connection> connectionList;

    public DatabaseRule() {
        connectionList = new ArrayList<>();
    }

    public String getUser() {
        return sUser;
    }

    public synchronized Connection newConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + sUrl, sUser, sPass);
        connectionList.add(conn);
        return conn;
    }

    @Override
    protected void after() {
        for (Connection conn : connectionList)
            try { conn.close(); } catch (SQLException ignored) {}
    }

}
