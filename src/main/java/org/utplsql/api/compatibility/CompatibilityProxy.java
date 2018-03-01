package org.utplsql.api.compatibility;

import org.utplsql.api.DBHelper;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.exception.DatabaseNotCompatibleException;
import org.utplsql.api.outputBuffer.OutputBuffer;
import org.utplsql.api.outputBuffer.OutputBufferProvider;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.testRunner.TestRunnerStatement;
import org.utplsql.api.testRunner.TestRunnerStatementProvider;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/** Class to check compatibility with database framework and also to give several specific implementations depending
 * on the version of the connected framework.
 * If one skips the compatibility check, the Proxy acts as like the framework has the same version as the API
 *
 * @author pesse
 */
public class CompatibilityProxy {

    public static final String UTPLSQL_API_VERSION = "3.1.0";
    public static final String UTPLSQL_COMPATIBILITY_VERSION = "3.0";

    private Version databaseVersion;
    private boolean compatible = false;

    public CompatibilityProxy( Connection conn ) throws SQLException
    {
        this(conn, false);
    }

    public CompatibilityProxy( Connection conn, boolean skipCompatibilityCheck ) throws SQLException
    {
        if ( skipCompatibilityCheck )
            doExpectCompatibility();
        else
            doCompatibilityCheckWithDatabase(conn);
    }

    /** Receives the current framework version from database and checks - depending on the framework version - whether
     * the API version is compatible or not.
     *
     * @param conn
     * @throws SQLException
     */
    private void doCompatibilityCheckWithDatabase( Connection conn ) throws SQLException
    {
        databaseVersion = DBHelper.getDatabaseFrameworkVersion(conn);

        if (OptionalFeatures.FRAMEWORK_COMPATIBILITY_CHECK.isAvailableFor(databaseVersion)) {
            try {
                compatible = versionCompatibilityCheck(conn, UTPLSQL_COMPATIBILITY_VERSION, null);
            } catch (SQLException e) {
                throw new DatabaseNotCompatibleException("Compatibility-check failed with error. Aborting. Reason: " + e.getMessage(), new Version(UTPLSQL_COMPATIBILITY_VERSION), new Version("Unknown"), e);
            }
        } else
            compatible = versionCompatibilityCheckPre303(UTPLSQL_COMPATIBILITY_VERSION);
    }

    /** Just prepare the proxy to expect compatibility, expecting the database framework to be the same version as the API
     *
     */
    private void doExpectCompatibility()
    {
        databaseVersion = new Version(UTPLSQL_API_VERSION);
        compatible = true;
    }

    /**
     * Check the utPLSQL version compatibility.
     * @param conn the connection
     * @return true if the requested utPLSQL version is compatible with the one installed on database
     * @throws SQLException any database error
     */
    private boolean versionCompatibilityCheck(Connection conn, String requested, String current)
            throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := ut_runner.version_compatibility_check(?, ?); END;");
            callableStatement.registerOutParameter(1, Types.SMALLINT);
            callableStatement.setString(2, requested);

            if (current == null)
                callableStatement.setNull(3, Types.VARCHAR);
            else
                callableStatement.setString(3, current);

            callableStatement.executeUpdate();
            return callableStatement.getInt(1) == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 6550)
                return false;
            else
                throw e;
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /** Simple fallback check for compatiblity: Major and Minor version must be equal
     *
     * @param requested
     * @return
     */
    private boolean versionCompatibilityCheckPre303( String requested )
    {
        Version requesteVersion = new Version(requested);

        if ( databaseVersion.getMajor() == requesteVersion.getMajor() && (requesteVersion.getMinor() == null || databaseVersion.getMinor() == requesteVersion.getMinor()) )
            return true;
        else
            return false;
    }

    /** Checks if actual API-version is compatible with utPLSQL database version and throws a DatabaseNotCompatibleException if not
     * Throws a DatabaseNotCompatibleException if version compatibility can not be checked.
     *
     */
    public void failOnNotCompatible() throws DatabaseNotCompatibleException
    {
        if ( !isCompatible() )
            throw new DatabaseNotCompatibleException( databaseVersion );
    }

    public boolean isCompatible()
    {
        return compatible;
    }

    public Version getDatabaseVersion()
    {
        return databaseVersion;
    }

    /** Returns a TestRunnerStatement compatible with the current framework
     *
     * @param options
     * @param conn
     * @return
     * @throws SQLException
     */
    public TestRunnerStatement getTestRunnerStatement(TestRunnerOptions options, Connection conn) throws SQLException
    {
        return TestRunnerStatementProvider.getCompatibleTestRunnerStatement(databaseVersion, options, conn);
    }

    /** Returns an OutputBuffer compatible with the current framework
     *
     * @param reporter
     * @param conn
     * @return
     * @throws SQLException
     */
    public OutputBuffer getOutputBuffer(Reporter reporter, Connection conn) throws SQLException {
        return OutputBufferProvider.getCompatibleOutputBuffer(databaseVersion, reporter, conn);
    }
}
