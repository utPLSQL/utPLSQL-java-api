package org.utplsql.api.testRunner;

import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestRunnerStatementProviderIT extends AbstractDatabaseTest {

    @Test
    public void testGettingPre303Version() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.0.2"), new TestRunnerOptions(), getConnection());
        assertEquals(Pre303TestRunnerStatement.class, stmt.getClass());
    }


    @Test
    public void testGettingPre312Version_from_303() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.0.3"), new TestRunnerOptions(), getConnection());
        assertEquals(Pre312TestRunnerStatement.class, stmt.getClass());
    }

    @Test
    public void testGettingPre312Version_from_311() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.1.1"), new TestRunnerOptions(), getConnection());
        assertEquals(Pre312TestRunnerStatement.class, stmt.getClass());
    }

    @Test
    public void testGettingActualVersion() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(new Version("3.1.2"), new TestRunnerOptions(), getConnection());
        assertEquals(ActualTestRunnerStatement.class, stmt.getClass());
    }
}
