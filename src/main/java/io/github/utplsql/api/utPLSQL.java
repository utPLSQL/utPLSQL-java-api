package io.github.utplsql.api;

import io.github.utplsql.api.types.CustomTypes;
import io.github.utplsql.api.types.DocumentationReporter;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 */
public final class utPLSQL {

    private static String sUrl;
    private static String sUser;
    private static String sPassword;

    private utPLSQL() {}

    public static void init(String url, String user, String password) throws SQLException {
        sUrl = url;
        sUser = user;
        sPassword = password;
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(sUrl, sUser, sPassword);
        createTypeMap(conn);
        return conn;
    }

    public static String newSysGuid(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN :id := sys_guid(); END;");
            callableStatement.registerOutParameter(":id", OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(":id");
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    private static void createTypeMap(Connection conn) throws SQLException {
        Map typeMap = conn.getTypeMap();
        typeMap.put(CustomTypes.UT_DOCUMENTATION_REPORTER.getName(), DocumentationReporter.class);
        conn.setTypeMap(typeMap);
    }

}
