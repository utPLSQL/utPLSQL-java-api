package org.utplsql.api.testRunner;

import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;

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
        AbstractTestRunnerStatement stmt = null;

        try {
            if (databaseVersion.isLessThan(Version.V3_0_3)) {
                stmt = new Pre303TestRunnerStatement(options, conn);
            } else if (databaseVersion.isLessThan(Version.V3_1_2)) {
                stmt = new Pre312TestRunnerStatement(options, conn);
            }

        } catch (InvalidVersionException ignored) {
        }

        if (stmt == null) {
            stmt = new ActualTestRunnerStatement(options, conn);
        }

        return stmt;
    }
}
