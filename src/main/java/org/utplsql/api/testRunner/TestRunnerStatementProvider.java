package org.utplsql.api.testRunner;

import org.utplsql.api.DBHelper;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.Connection;
import java.sql.SQLException;

/** Provides different implementations of TestRunnerStatement based on the version of the database framework
 *
 * @author pesse
 */
public class TestRunnerStatementProvider {

    /** Returns the TestRunnerStatement-implementation compatible with the given databaseVersion.
     *
     * @param databaseVersion Version of the database framework
     * @param options TestRunnerOptions to be used
     * @param conn Active Connection
     * @return TestRunnerStatment compatible with the database framework
     * @throws SQLException
     */
    public static TestRunnerStatement getCompatibleTestRunnerStatement(Version databaseVersion, TestRunnerOptions options, Connection conn ) throws SQLException
    {
        AbstractTestRunnerStatement stmt = null;

        if ( databaseVersion.getMajor() == 3 && databaseVersion.getMinor() == 0 && databaseVersion.getBugfix() <= 2 )
            stmt = new Pre303TestRunnerStatement(options, conn);
        else
            stmt = new ActualTestRunnerStatement(options, conn);

        return stmt;
    }
}
