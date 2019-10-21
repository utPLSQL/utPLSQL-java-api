package org.utplsql.api.testRunner;

import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides different implementations of TestRunnerStatement based on the version of the database framework
 *
 * @author pesse
 */
public class TestRunnerStatementProvider {

    private TestRunnerStatementProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the TestRunnerStatement-implementation compatible with the given databaseVersion.
     *
     * @param databaseVersion Version of the database framework
     * @param options         TestRunnerOptions to be used
     * @param conn            Active Connection
     * @return TestRunnerStatment compatible with the database framework
     * @throws SQLException
     */
    public static TestRunnerStatement getCompatibleTestRunnerStatement(Version databaseVersion, TestRunnerOptions options, Connection conn) throws SQLException {
        return DynamicTestRunnerStatement.forVersion(databaseVersion, conn, options, null);
    }
}
