package org.utplsql.api.testRunner;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestRunnerStatementProviderIT {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void testGettingPre303Version() {
        try {
            TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.0.2"), new TestRunnerOptions(), db.newConnection());

            assertEquals(Pre303TestRunnerStatement.class, stmt.getClass());
        } catch (SQLException e) {
            fail(e);
        }
    }


    @Test
    public void testGettingActualVersion() {
        try {
            TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.0.3"), new TestRunnerOptions(), db.newConnection());

            assertEquals(ActualTestRunnerStatement.class, stmt.getClass());
        } catch (SQLException e) {
            fail(e);
        }
    }
}
