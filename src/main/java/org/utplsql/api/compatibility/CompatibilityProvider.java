package org.utplsql.api.compatibility;

import org.utplsql.api.DBHelper;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.testRunner.AbstractTestRunnerStatement;
import org.utplsql.api.testRunner.Pre303TestRunnerStatement;
import org.utplsql.api.testRunner.ActualTestRunnerStatement;
import org.utplsql.api.testRunner.TestRunnerStatement;

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
        Version version = DBHelper.getDatabaseFrameworkVersion(conn);

        AbstractTestRunnerStatement stmt = null;

        if ( version.getMajor() == 3 && version.getMinor() == 0 && version.getBugfix() <= 2 )
            stmt = new Pre303TestRunnerStatement(options, conn);
        else
            stmt = new ActualTestRunnerStatement(options, conn);

        return stmt;
    }
}
