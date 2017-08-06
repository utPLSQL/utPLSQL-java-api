package org.utplsql.api;

import org.junit.Assert;
import org.junit.Test;
import org.utplsql.api.reporter.*;

import java.sql.SQLException;

public class ReporterNameTest {

    @Test
    public void reporterSQLTypeName() throws SQLException {
        Assert.assertEquals(CustomTypes.UT_COVERAGE_HTML_REPORTER, new CoverageHTMLReporter().getSQLTypeName());
        Assert.assertEquals(CustomTypes.UT_COVERAGE_SONAR_REPORTER, new CoverageSonarReporter().getSQLTypeName());
        Assert.assertEquals(CustomTypes.UT_COVERALLS_REPORTER, new CoverallsReporter().getSQLTypeName());
        Assert.assertEquals(CustomTypes.UT_DOCUMENTATION_REPORTER, new DocumentationReporter().getSQLTypeName());
        Assert.assertEquals(CustomTypes.UT_SONAR_TEST_REPORTER, new SonarTestReporter().getSQLTypeName());
        Assert.assertEquals(CustomTypes.UT_TEAMCITY_REPORTER, new TeamCityReporter().getSQLTypeName());
        Assert.assertEquals(CustomTypes.UT_XUNIT_REPORTER, new XUnitReporter().getSQLTypeName());
    }

    @Test
    public void reporterFactory() {
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_COVERAGE_HTML_REPORTER)
                instanceof CoverageHTMLReporter);
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_COVERAGE_SONAR_REPORTER)
                instanceof CoverageSonarReporter);
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_COVERALLS_REPORTER)
                instanceof CoverallsReporter);
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_DOCUMENTATION_REPORTER)
                instanceof DocumentationReporter);
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_SONAR_TEST_REPORTER)
                instanceof SonarTestReporter);
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_TEAMCITY_REPORTER)
                instanceof TeamCityReporter);
        Assert.assertTrue(ReporterFactory.createReporter(CustomTypes.UT_XUNIT_REPORTER)
                instanceof XUnitReporter);
    }

}
