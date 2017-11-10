package org.utplsql.api.compatibility;

import org.utplsql.api.DBHelper;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.testRunner.*;

import java.sql.Connection;
import java.sql.SQLException;

/** Very simple and basic class to provide different implementations of classes based on Database Framework version
 * If compatibility-issues get more intense we might have to introduce some more lose coupling, but for the moment
 * this very basic approach should do what we want.
 * Putting the compatibility checks to the concrete classes might be worth a refactoring, too
 *
 * @author pesse
 */
public class CompatibilityProvider {

    public static TestRunnerStatement getTestRunnerStatement(TestRunnerOptions options, Connection conn) throws SQLException
    {
        return TestRunnerStatementProvider.getCompatibleTestRunnerStatement(DBHelper.getDatabaseFrameworkVersion(conn), options, conn);
    }
}
