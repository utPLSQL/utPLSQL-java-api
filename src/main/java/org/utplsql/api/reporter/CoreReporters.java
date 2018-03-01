package org.utplsql.api.reporter;

import java.util.function.BiFunction;

/** This enum defines default reporters, added and maintained by the utPLSQL team, and their (default) factory method
 *
 * @author pesse
 */
public enum CoreReporters {

    UT_COVERAGE_HTML_REPORTER(CoverageHTMLReporter::new, "Generates a HTML coverage report with summary and line by line information on code coverage.\n" +
            "Based on open-source simplecov-html coverage reporter for Ruby.\n" +
            "Includes source code in the report."),
    UT_DOCUMENTATION_REPORTER(DocumentationReporter::new, "A textual pretty-print of unit test results (usually use for console output)"),
    UT_TEAMCITY_REPORTER(DefaultReporter::new, "For reporting live progress of test execution with Teamcity CI."),
    UT_XUNIT_REPORTER(DefaultReporter::new, "Used for reporting test results with CI servers like Jenkins/Hudson/Teamcity."),
    UT_COVERALLS_REPORTER(DefaultReporter::new, "Generates a JSON coverage report providing information on code coverage with line numbers.\n" +
            "Designed for [Coveralls](https://coveralls.io/)."),
    UT_COVERAGE_SONAR_REPORTER(DefaultReporter::new, "Generates a JSON coverage report providing information on code coverage with line numbers.\n" +
            "Designed for [SonarQube](https://about.sonarqube.com/) to report coverage."),
    UT_SONAR_TEST_REPORTER(DefaultReporter::new, "Generates a JSON report providing detailed information on test execution.\n" +
            "Designed for [SonarQube](https://about.sonarqube.com/) to report test execution.");

    private BiFunction<String, Object[], ? extends Reporter> factoryMethod;
    private String description;

    CoreReporters(BiFunction<String, Object[], ? extends Reporter> factoryMethod, String description ) {
        this.factoryMethod = factoryMethod;
        this.description = description;
    }

    public BiFunction<String, Object[], ? extends Reporter> getFactoryMethod() {
        return factoryMethod;
    }

    public String getDescription() {
        return description;
    }
}
