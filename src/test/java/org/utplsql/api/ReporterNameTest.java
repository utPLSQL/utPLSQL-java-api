package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.*;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReporterNameTest {

    @Test
    public void reporterSQLTypeName() throws SQLException {
        assertEquals(DefaultReporters.UT_COVERAGE_HTML_REPORTER.name(), new CoverageHTMLReporter().getSQLTypeName());
        assertEquals(DefaultReporters.UT_COVERAGE_SONAR_REPORTER.name(), new CoverageSonarReporter().getSQLTypeName());
        assertEquals(DefaultReporters.UT_COVERALLS_REPORTER.name(), new CoverallsReporter().getSQLTypeName());
        assertEquals(DefaultReporters.UT_DOCUMENTATION_REPORTER.name(), new DocumentationReporter().getSQLTypeName());
        assertEquals(DefaultReporters.UT_SONAR_TEST_REPORTER.name(), new SonarTestReporter().getSQLTypeName());
        assertEquals(DefaultReporters.UT_TEAMCITY_REPORTER.name(), new TeamCityReporter().getSQLTypeName());
        assertEquals(DefaultReporters.UT_XUNIT_REPORTER.name(), new XUnitReporter().getSQLTypeName());
    }

    @Test
    public void reporterFactory() {
        ReporterFactory reporterFactory = ReporterFactory.getInstance();
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_COVERAGE_HTML_REPORTER.name())
                instanceof CoverageHTMLReporter);
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_COVERAGE_SONAR_REPORTER.name())
                instanceof CoverageSonarReporter);
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_COVERALLS_REPORTER.name())
                instanceof CoverallsReporter);
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_DOCUMENTATION_REPORTER.name())
                instanceof DocumentationReporter);
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_SONAR_TEST_REPORTER.name())
                instanceof SonarTestReporter);
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_TEAMCITY_REPORTER.name())
                instanceof TeamCityReporter);
        assertTrue(reporterFactory.createReporter(DefaultReporters.UT_XUNIT_REPORTER.name())
                instanceof XUnitReporter);
    }

    @Test
    public void defaultReporterFactoryNamesList() {
        Map<String, String> reporterDescriptions = ReporterFactory.getInstance().getRegisteredReporterInfo();

        for ( DefaultReporters r : DefaultReporters.values() ) {
            assertTrue(reporterDescriptions.containsKey(r.name()));
        }
    }

}
