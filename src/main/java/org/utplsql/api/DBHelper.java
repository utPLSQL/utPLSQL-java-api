package org.utplsql.api;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Database utility functions.
 */
public final class DBHelper {

    private DBHelper() {}

    /**
     * Return a new sys_guid from database.
     * @param conn the connection
     * @return the new id string
     * @throws SQLException any database error
     */
    public static String newSysGuid(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := sys_guid(); END;");
            callableStatement.registerOutParameter(1, OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /**
     * Return the current schema name.
     * @param conn the connection
     * @return the schema name
     * @throws SQLException any database error
     */
    public static String getCurrentSchema(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := sys_context('userenv', 'current_schema'); END;");
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

}
