package org.utplsql.api;

import oracle.jdbc.OracleTypes;
import org.utplsql.api.exception.DatabaseNotCompatibleException;
import org.utplsql.api.exception.UtPLSQLNotInstalledException;

import java.sql.*;

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
        assert conn != null;
        try (CallableStatement callableStatement = conn.prepareCall("BEGIN ? := sys_guid(); END;")) {
            callableStatement.registerOutParameter(1, OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        }
    }

    /**
     * Return the current schema name.
     * @param conn the connection
     * @return the schema name
     * @throws SQLException any database error
     */
    public static String getCurrentSchema(Connection conn) throws SQLException {
        assert conn != null;
        try (CallableStatement callableStatement = conn.prepareCall("BEGIN ? := sys_context('userenv', 'current_schema'); END;")) {
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        }
    }

    /** Returns the Frameworks version string of the given connection
     *
     * @param conn Active db connection
     * @return Version-string of the utPLSQL framework
     * @throws SQLException any database error
     */
    public static Version getDatabaseFrameworkVersion( Connection conn ) throws SQLException {
        assert conn != null;
        Version result = new Version("");
        try (PreparedStatement stmt = conn.prepareStatement("select ut_runner.version() from dual"))
        {
            ResultSet rs = stmt.executeQuery();

            if ( rs.next() )
                result = new Version(rs.getString(1));

            rs.close();
        } catch ( SQLException e ) {
            if ( e.getErrorCode() == UtPLSQLNotInstalledException.ERROR_CODE )
                throw new UtPLSQLNotInstalledException(e);
            else
                throw e;
        }

        return result;
    }

    /** Returns the Oracle database Version from a given connection object
     *
     * @param conn Connection-Object
     * @return Returns version-string of the Oracle Database product component
     * @throws SQLException any database error
     */
    public static String getOracleDatabaseVersion( Connection conn ) throws SQLException {
        assert conn != null;
        String result = null;
        try (PreparedStatement stmt = conn.prepareStatement("select version from product_component_version where product like 'Oracle Database%'"))
        {
            ResultSet rs = stmt.executeQuery();

            if ( rs.next() )
                result = rs.getString(1);
        }

        return result;
    }

    /**
     * Enable the dbms_output buffer with unlimited size.
     * @param conn the connection
     */
    public static void enableDBMSOutput(Connection conn) {
        assert conn != null;
        try (CallableStatement call = conn.prepareCall("BEGIN dbms_output.enable(NULL); END;")) {
            call.execute();
        } catch (SQLException e) {
            System.out.println("Failed to enable dbms_output.");
        }
    }

    /**
     * Disable the dbms_output buffer.
     * @param conn the connection
     */
    public static void disableDBMSOutput(Connection conn) {
        assert conn != null;
        try (CallableStatement call = conn.prepareCall("BEGIN dbms_output.disable(); END;")) {
            call.execute();
        } catch (SQLException e) {
            System.out.println("Failed to disable dbms_output.");
        }
    }
}
