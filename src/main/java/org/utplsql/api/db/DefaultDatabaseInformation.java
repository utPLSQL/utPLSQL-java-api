package org.utplsql.api.db;

import org.utplsql.api.Version;
import org.utplsql.api.exception.UtPLSQLNotInstalledException;

import javax.annotation.Nullable;
import java.sql.*;

public class DefaultDatabaseInformation implements DatabaseInformation {

    @Override
    public Version getUtPlsqlFrameworkVersion(Connection conn) throws SQLException {
        Version result = Version.create("");
        try (PreparedStatement stmt = conn.prepareStatement("select ut_runner.version() from dual"))
        {
            ResultSet rs = stmt.executeQuery();

            if ( rs.next() )
                result = Version.create(rs.getString(1));

            rs.close();
        } catch ( SQLException e ) {
            if ( e.getErrorCode() == UtPLSQLNotInstalledException.ERROR_CODE )
                throw new UtPLSQLNotInstalledException(e);
            else
                throw e;
        }

        return result;
    }

    @Override
    public String getOracleVersion(Connection conn) throws SQLException {
        String result = null;
        try (PreparedStatement stmt = conn.prepareStatement("select version from product_component_version where product like 'Oracle Database%'"))
        {
            ResultSet rs = stmt.executeQuery();

            if ( rs.next() )
                result = rs.getString(1);
        }

        return result;
    }

    @Override
    public String getCurrentSchema(Connection conn) throws SQLException {
        try (CallableStatement callableStatement = conn.prepareCall("BEGIN ? := sys_context('userenv', 'current_schema'); END;")) {
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        }
    }

    @Override
    public int frameworkCompatibilityCheck(Connection conn, String requested, @Nullable String current) throws SQLException {
        try(CallableStatement callableStatement = conn.prepareCall("BEGIN ? := ut_runner.version_compatibility_check(?, ?); END;")) {
            callableStatement.registerOutParameter(1, Types.SMALLINT);
            callableStatement.setString(2, requested);

            if (current == null)
                callableStatement.setNull(3, Types.VARCHAR);
            else
                callableStatement.setString(3, current);

            callableStatement.executeUpdate();
            return callableStatement.getInt(1);
        }
    }
}
