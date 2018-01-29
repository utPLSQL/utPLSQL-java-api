package org.utplsql.api.rules;

import org.junit.rules.ExternalResource;
import org.utplsql.api.EnvironmentVariableUtil;

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
        sUrl  = EnvironmentVariableUtil.getEnvValue("DB_URL", "192.168.99.100:1521:XE");
        sUser = EnvironmentVariableUtil.getEnvValue("DB_USER", "app");
        sPass = EnvironmentVariableUtil.getEnvValue("DB_PASS", "app");
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
