package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.compatibility.OptionalFeatures;
import org.utplsql.api.db.DatabaseInformation;
import org.utplsql.api.db.DefaultDatabaseInformation;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.exception.SomeTestsFailedException;
import org.utplsql.api.reporter.CoreReporters;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration-test for TestRunner
 *
 * @author viniciusam
 * @author pesse
 */
class TestRunnerIT extends AbstractDatabaseTest {

    @Test
    void runWithDefaultParameters() throws SQLException {
        new TestRunner().run(getConnection());
    }


    /**
     * This can only be run against versions >= 3.0.3
     */
    @Test
    void runWithoutCompatibilityCheck() throws SQLException, InvalidVersionException {

        DatabaseInformation databaseInformation = new DefaultDatabaseInformation();

        // We can only test this for the versions of the latest TestRunnerStatement-Change
        if ( OptionalFeatures.CLIENT_CHARACTER_SET.isAvailableFor(databaseInformation.getUtPlsqlFrameworkVersion(getConnection())) ) {
            new TestRunner()
                    .skipCompatibilityCheck(true)
                    .run(getConnection());
        }
    }

    @Test
    void runWithManyReporters() throws SQLException {
        Connection conn = getConnection();
        new TestRunner()
                .addPath(getUser())
                .addReporter(CoreReporters.UT_DOCUMENTATION_REPORTER.name())
                .addReporter(CoreReporters.UT_COVERAGE_HTML_REPORTER.name())
                .addReporter(CoreReporters.UT_COVERAGE_SONAR_REPORTER.name())
                .addReporter(CoreReporters.UT_COVERALLS_REPORTER.name())
                .addReporter(CoreReporters.UT_SONAR_TEST_REPORTER.name())
                .addReporter(CoreReporters.UT_TEAMCITY_REPORTER.name())
                .addReporter(CoreReporters.UT_XUNIT_REPORTER.name())
                .run(conn);
    }


    /**
     * This can only be tested on frameworks >= 3.0.3
     */
    @Test
    void failOnErrors() throws SQLException, InvalidVersionException {
        Connection conn = getConnection();

        CompatibilityProxy proxy = new CompatibilityProxy(conn);

        if (proxy.getUtPlsqlVersion().isGreaterOrEqualThan(Version.V3_0_3)) {
            Executable throwingTestRunner = () -> new TestRunner()
                    .failOnErrors(true)
                    .run(conn);
            assertThrows(SomeTestsFailedException.class, throwingTestRunner);
        }
    }

    @Test
    void runWithRandomExecutionOrder() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());

        new TestRunner()
                .randomTestOrder(true)
                .randomTestOrderSeed(123)
                .run(getConnection());
    }

}
