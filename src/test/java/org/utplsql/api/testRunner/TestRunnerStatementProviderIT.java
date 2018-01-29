package org.utplsql.api.testRunner;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.SQLException;

public class TestRunnerStatementProviderIT {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void testGettingPre303Version() {
        try {
            TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.0.2"), new TestRunnerOptions(), db.newConnection());

            Assert.assertEquals(Pre303TestRunnerStatement.class, stmt.getClass());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


    @Test
    public void testGettingActualVersion() {
        try {
            TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.0.3"), new TestRunnerOptions(), db.newConnection());

            Assert.assertEquals(ActualTestRunnerStatement.class, stmt.getClass());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
