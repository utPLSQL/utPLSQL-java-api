package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

public final class ReporterFactory {

    private ReporterFactory() {}

    public static Reporter createReporter(String reporterName) {
        switch (reporterName.toUpperCase()) {
            case CustomTypes.UT_DOCUMENTATION_REPORTER: return new DocumentationReporter();
            case CustomTypes.UT_COVERAGE_HTML_REPORTER: return new CoverageHTMLReporter();
            case CustomTypes.UT_TEAMCITY_REPORTER: return new TeamCityReporter();
            case CustomTypes.UT_XUNIT_REPORTER: return new XUnitReporter();
            case CustomTypes.UT_COVERALLS_REPORTER: return new CoverallsReporter();
            case CustomTypes.UT_COVERAGE_SONAR_REPORTER: return new CoverageSonarReporter();
            case CustomTypes.UT_SONAR_TEST_REPORTER: return new SonarTestReporter();
        }
        throw new RuntimeException("Reporter " + reporterName + " not implemented.");
    }

}
