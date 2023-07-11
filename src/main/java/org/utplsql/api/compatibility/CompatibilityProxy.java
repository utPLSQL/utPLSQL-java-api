package org.utplsql.api.compatibility;

import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.db.DatabaseInformation;
import org.utplsql.api.db.DefaultDatabaseInformation;
import org.utplsql.api.exception.DatabaseNotCompatibleException;
import org.utplsql.api.outputBuffer.OutputBuffer;
import org.utplsql.api.outputBuffer.OutputBufferProvider;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.testRunner.TestRunnerStatement;
import org.utplsql.api.testRunner.TestRunnerStatementProvider;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class to check compatibility with database framework and also to give several specific implementations depending
 * on the version of the connected framework.
 * If one skips the compatibility check, the Proxy acts as like the framework has the same version as the API
 *
 * @author pesse
 */
public class CompatibilityProxy {

    public static final String UTPLSQL_COMPATIBILITY_VERSION = "3";
    private final DatabaseInformation databaseInformation;
    private Version utPlsqlVersion;
    private final Version realDbPlsqlVersion;
    private boolean compatible = false;

    public CompatibilityProxy(Connection conn) throws SQLException {
        this(conn, null, null);
    }

    @Deprecated
    public CompatibilityProxy(Connection conn, boolean skipCompatibilityCheck) throws SQLException {
        this(conn, skipCompatibilityCheck, null);
    }

    @Deprecated
    public CompatibilityProxy(Connection conn, boolean skipCompatibilityCheck, @Nullable DatabaseInformation databaseInformation) throws SQLException {
        this(conn, skipCompatibilityCheck ? Version.LATEST : null, databaseInformation);
    }

    public CompatibilityProxy(Connection conn, @Nullable DatabaseInformation databaseInformation) throws SQLException {
        this(conn, null, databaseInformation);
    }

    public CompatibilityProxy(Connection conn, @Nullable Version assumedUtPlsVersion) throws SQLException {
        this(conn, assumedUtPlsVersion, null);
    }

    public CompatibilityProxy(Connection conn, @Nullable Version assumedUtPlsqlVersion, @Nullable DatabaseInformation databaseInformation) throws SQLException {
        this.databaseInformation = (databaseInformation != null)
                ? databaseInformation
                : new DefaultDatabaseInformation();

        realDbPlsqlVersion = this.databaseInformation.getUtPlsqlFrameworkVersion(conn);
        if (assumedUtPlsqlVersion != null) {
            utPlsqlVersion = assumedUtPlsqlVersion;
            compatible = utPlsqlVersion.getNormalizedString().startsWith(UTPLSQL_COMPATIBILITY_VERSION);
        } else {
            doCompatibilityCheckWithDatabase(conn);
        }
    }

    /**
     * Receives the current framework version from database and checks - depending on the framework version - whether
     * the API version is compatible or not.
     *
     * @param conn {@link Connection}
     * @throws DatabaseNotCompatibleException if the database is not compatible
     */
    private void doCompatibilityCheckWithDatabase(Connection conn) throws DatabaseNotCompatibleException {
        utPlsqlVersion = realDbPlsqlVersion;
        Version clientVersion = Version.create(UTPLSQL_COMPATIBILITY_VERSION);

        if (utPlsqlVersion == null) {
            throw new DatabaseNotCompatibleException("Could not get database version", clientVersion, null, null);
        }

        if (utPlsqlVersion.getMajor() == null) {
            throw new DatabaseNotCompatibleException("Illegal database version: " + utPlsqlVersion.toString(), clientVersion, utPlsqlVersion, null);
        }

        if (OptionalFeatures.FRAMEWORK_COMPATIBILITY_CHECK.isAvailableFor(utPlsqlVersion)) {
            try {
                compatible = versionCompatibilityCheck(conn, UTPLSQL_COMPATIBILITY_VERSION, null);
            } catch (SQLException e) {
                throw new DatabaseNotCompatibleException("Compatibility-check failed with error. Aborting. Reason: " + e.getMessage(), clientVersion, Version.create("Unknown"), e);
            }
        } else {
            compatible = versionCompatibilityCheckPre303(UTPLSQL_COMPATIBILITY_VERSION);
        }
    }

    /**
     * Check the utPLSQL version compatibility.
     *
     * @param conn the connection
     * @return true if the requested utPLSQL version is compatible with the one installed on database
     * @throws SQLException any database error
     */
    private boolean versionCompatibilityCheck(Connection conn, String requested, String current)
            throws SQLException {
        try {
            return databaseInformation.frameworkCompatibilityCheck(conn, requested, current) == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 6550) {
                return false;
            } else {
                throw e;
            }
        }
    }

    /**
     * Simple fallback check for compatibility: Major and Minor version must be equal
     *
     * @param versionRequested Requested version
     * @return weather the version is available or not
     */
    private boolean versionCompatibilityCheckPre303(String versionRequested) {
        Version requestedVersion = Version.create(versionRequested);

        Objects.requireNonNull(utPlsqlVersion.getMajor(), "Illegal database Version: " + utPlsqlVersion.toString());
        return Objects.equals(utPlsqlVersion.getMajor(), requestedVersion.getMajor())
                && (requestedVersion.getMinor() == null
                || requestedVersion.getMinor().equals(utPlsqlVersion.getMinor()));
    }

    /**
     * Checks if actual API-version is compatible with utPLSQL database version and throws a DatabaseNotCompatibleException if not
     * Throws a DatabaseNotCompatibleException if version compatibility can not be checked.
     */
    public void failOnNotCompatible() throws DatabaseNotCompatibleException {
        if (!isCompatible()) {
            throw new DatabaseNotCompatibleException(utPlsqlVersion);
        }
    }

    public boolean isCompatible() {
        return compatible;
    }

    @Deprecated
    public Version getDatabaseVersion() {
        return utPlsqlVersion;
    }

    public Version getUtPlsqlVersion() {
        return utPlsqlVersion;
    }

    public Version getRealDbPlsqlVersion() {
        return realDbPlsqlVersion;
    }

    public String getVersionDescription() {
        if (utPlsqlVersion != realDbPlsqlVersion) {
            return realDbPlsqlVersion.toString() + " (Assumed: " + utPlsqlVersion.toString() + ")";
        } else {
            return utPlsqlVersion.toString();
        }
    }

    /**
     * Returns a TestRunnerStatement compatible with the current framework
     *
     * @param options {@link TestRunnerOptions}
     * @param conn    {@link Connection}
     * @return TestRunnerStatement
     * @throws SQLException if there are problems with the database access
     */
    public TestRunnerStatement getTestRunnerStatement(TestRunnerOptions options, Connection conn) throws SQLException {
        return TestRunnerStatementProvider.getCompatibleTestRunnerStatement(utPlsqlVersion, options, conn);
    }

    /**
     * Returns an OutputBuffer compatible with the current framework
     *
     * @param reporter {@link Reporter}
     * @param conn     {@link Connection}
     * @return OutputBuffer
     * @throws SQLException if there are problems with the database access
     */
    public OutputBuffer getOutputBuffer(Reporter reporter, Connection conn) throws SQLException {
        return OutputBufferProvider.getCompatibleOutputBuffer(utPlsqlVersion, reporter, conn);
    }
}
