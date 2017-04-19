package io.github.utplsql;

import io.github.utplsql.types.DocumentationReporter;
import io.github.utplsql.types.CustomTypes;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.Map;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 */
public final class UTPLSQL {

    private static Connection sConnection;

    private UTPLSQL() {}

    public static void init(String url, String user, String password) throws SQLException {
        UTPLSQL.init(DriverManager.getConnection(url, user, password));
    }

    public static void init(Connection conn) throws SQLException {
        sConnection = conn;
        createTypeMap();
    }

    public static void close() {
        if (sConnection != null)
            try { sConnection.close(); } catch (SQLException ignored) {}
    }

    protected static Connection getConnection() {
        if (sConnection == null)
            throw new RuntimeException("Connection not initialized.");

        return sConnection;
    }

    public static String newSysGuid() throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = sConnection.prepareCall("BEGIN :id := sys_guid(); END;");
            callableStatement.registerOutParameter(":id", OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(":id");
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    private static void createTypeMap() throws SQLException {
        Map typeMap = sConnection.getTypeMap();
        typeMap.put(CustomTypes.UT_DOCUMENTATION_REPORTER.getName(), DocumentationReporter.class);
        sConnection.setTypeMap(typeMap);
    }

}
