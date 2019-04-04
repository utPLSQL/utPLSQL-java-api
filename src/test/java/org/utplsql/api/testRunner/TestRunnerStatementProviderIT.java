package org.utplsql.api.testRunner;

import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRunnerStatementProviderIT extends AbstractDatabaseTest {

    @Test
    void testGettingPre303Version() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(Version.V3_0_2, new TestRunnerOptions(), getConnection());
        assertEquals(Pre303TestRunnerStatement.class, stmt.getClass());
    }


    @Test
    void testGettingPre312Version_from_303() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(Version.V3_0_3, new TestRunnerOptions(), getConnection());
        assertEquals(Pre312TestRunnerStatement.class, stmt.getClass());
    }

    @Test
    void testGettingPre312Version_from_311() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(Version.V3_1_1, new TestRunnerOptions(), getConnection());
        assertEquals(Pre312TestRunnerStatement.class, stmt.getClass());
    }

    @Test
    void testGettingPre317Version_from_312() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(Version.V3_1_2, new TestRunnerOptions(), getConnection());
        assertEquals(Pre317TestRunnerStatement.class, stmt.getClass());
    }

    @Test
    void testGettingPre317Version_from_316() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(Version.V3_1_6, new TestRunnerOptions(), getConnection());
        assertEquals(Pre317TestRunnerStatement.class, stmt.getClass());
    }

    @Test
    void testGettingActualVersion_from_latest() throws SQLException {
        TestRunnerStatement stmt = TestRunnerStatementProvider.getCompatibleTestRunnerStatement(Version.LATEST, new TestRunnerOptions(), getConnection());
        assertEquals(ActualTestRunnerStatement.class, stmt.getClass());
    }
}
