package org.utplsql.api;

import oracle.jdbc.OracleTypes;
import org.utplsql.api.db.DatabaseInformation;
import org.utplsql.api.db.DefaultDatabaseInformation;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Database utility functions.
 */
public final class DBHelper {

    private DBHelper() {
    }

    /**
     * Return a new sys_guid from database.
     *
     * @param conn the connection
     * @return the new id string
     * @throws SQLException any database error
     */
    public static String newSysGuid(Connection conn) throws SQLException {
        try (CallableStatement callableStatement = conn.prepareCall("BEGIN ? := sys_guid(); END;")) {
            callableStatement.registerOutParameter(1, OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        }
    }

    /**
     * Return the current schema name.
     * Deprecated. Use DatabaseInformation-Interface instead.
     *
     * @param conn the connection
     * @return the schema name
     * @throws SQLException any database error
     */
    @Deprecated
    public static String getCurrentSchema(Connection conn) throws SQLException {
        try (CallableStatement callableStatement = conn.prepareCall("BEGIN ? := sys_context('userenv', 'current_schema'); END;")) {
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        }
    }

    /**
     * Returns the Frameworks version string of the given connection
     * Deprecated. Use DatabaseInformation-Interface instead.
     *
     * @param conn Active db connection
     * @return Version-string of the utPLSQL framework
     * @throws SQLException any database error
     */
    @Deprecated
    public static Version getDatabaseFrameworkVersion(Connection conn) throws SQLException {
        DatabaseInformation databaseInformation = new DefaultDatabaseInformation();
        return databaseInformation.getUtPlsqlFrameworkVersion(conn);

    }

    /**
     * Returns the Oracle database Version from a given connection object
     * Deprecated. Use DatabaseInformation-Interface instead.
     *
     * @param conn Connection-Object
     * @return Returns version-string of the Oracle Database product component
     * @throws SQLException any database error
     */
    @Deprecated
    public static String getOracleDatabaseVersion(Connection conn) throws SQLException {
        DatabaseInformation databaseInformation = new DefaultDatabaseInformation();
        return databaseInformation.getOracleVersion(conn);
    }

    /**
     * Enable the dbms_output buffer with unlimited size.
     *
     * @param conn the connection
     */
    public static void enableDBMSOutput(Connection conn) {
        try (CallableStatement call = conn.prepareCall("BEGIN dbms_output.enable(NULL); END;")) {
            call.execute();
        } catch (SQLException e) {
            System.out.println("Failed to enable dbms_output.");
        }
    }

    /**
     * Disable the dbms_output buffer.
     *
     * @param conn the connection
     */
    public static void disableDBMSOutput(Connection conn) {
        try (CallableStatement call = conn.prepareCall("BEGIN dbms_output.disable(); END;")) {
            call.execute();
        } catch (SQLException e) {
            System.out.println("Failed to disable dbms_output.");
        }
    }
}
