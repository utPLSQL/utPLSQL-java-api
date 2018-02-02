package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReporterNameTest {

    @Test
    public void reporterSQLTypeName() throws SQLException {
        assertEquals(CustomTypes.UT_COVERAGE_HTML_REPORTER, new CoverageHTMLReporter().getSQLTypeName());
        assertEquals(CustomTypes.UT_COVERAGE_SONAR_REPORTER, new CoverageSonarReporter().getSQLTypeName());
        assertEquals(CustomTypes.UT_COVERALLS_REPORTER, new CoverallsReporter().getSQLTypeName());
        assertEquals(CustomTypes.UT_DOCUMENTATION_REPORTER, new DocumentationReporter().getSQLTypeName());
        assertEquals(CustomTypes.UT_SONAR_TEST_REPORTER, new SonarTestReporter().getSQLTypeName());
        assertEquals(CustomTypes.UT_TEAMCITY_REPORTER, new TeamCityReporter().getSQLTypeName());
        assertEquals(CustomTypes.UT_XUNIT_REPORTER, new XUnitReporter().getSQLTypeName());
    }

    @Test
    public void reporterFactory() {
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_COVERAGE_HTML_REPORTER)
                instanceof CoverageHTMLReporter);
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_COVERAGE_SONAR_REPORTER)
                instanceof CoverageSonarReporter);
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_COVERALLS_REPORTER)
                instanceof CoverallsReporter);
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_DOCUMENTATION_REPORTER)
                instanceof DocumentationReporter);
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_SONAR_TEST_REPORTER)
                instanceof SonarTestReporter);
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_TEAMCITY_REPORTER)
                instanceof TeamCityReporter);
        assertTrue(ReporterFactory.createReporter(CustomTypes.UT_XUNIT_REPORTER)
                instanceof XUnitReporter);
    }

}
