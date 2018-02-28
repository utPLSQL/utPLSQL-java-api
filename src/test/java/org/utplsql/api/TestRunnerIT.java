package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.exception.SomeTestsFailedException;
import org.utplsql.api.reporter.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration-test for TestRunner
 *
 * @author viniciusam
 * @author pesse
 */
public class TestRunnerIT extends AbstractDatabaseTest {

    @Test
    public void runWithDefaultParameters() throws SQLException {
        new TestRunner().run(getConnection());
    }

    @Test
    /** This can only be run against versions >= 3.0.3
     */
    public void runWithoutCompatibilityCheck() throws SQLException, InvalidVersionException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());

        if (proxy.getDatabaseVersion().isGreaterOrEqualThan(new Version("3.0.3"))) {
            new TestRunner()
                    .skipCompatibilityCheck(true)
                    .run(getConnection());
        }
    }

    @Test
    public void runWithManyReporters() throws SQLException {
        Connection conn = getConnection();
        new TestRunner()
                .addPath(getUser())
                .addReporter(ReporterFactory.create(DefaultReporters.UT_DOCUMENTATION_REPORTER))
                .addReporter(ReporterFactory.create(DefaultReporters.UT_COVERAGE_HTML_REPORTER))
                .addReporter(ReporterFactory.create(DefaultReporters.UT_COVERAGE_SONAR_REPORTER))
                .addReporter(ReporterFactory.create(DefaultReporters.UT_COVERALLS_REPORTER))
                .addReporter(ReporterFactory.create(DefaultReporters.UT_SONAR_TEST_REPORTER))
                .addReporter(ReporterFactory.create(DefaultReporters.UT_TEAMCITY_REPORTER))
                .addReporter(ReporterFactory.create(DefaultReporters.UT_XUNIT_REPORTER))
                .run(conn);
    }

    @Test
    /** This can only be tested on frameworks >= 3.0.3
     */
    public void failOnErrors() throws SQLException, InvalidVersionException {
        Connection conn = getConnection();

        CompatibilityProxy proxy = new CompatibilityProxy(conn);

        if (proxy.getDatabaseVersion().isGreaterOrEqualThan(new Version("3.0.3"))) {
            Executable throwingTestRunner = () -> new TestRunner()
                    .failOnErrors(true)
                    .run(conn);
            assertThrows(SomeTestsFailedException.class, throwingTestRunner);
        }
    }

}
