package org.utplsql.api.reporter;

import java.util.function.Supplier;

/** This enum defines default reporters, added and maintained by the utPLSQL team, and their (default) factory method
 *
 * @author pesse
 */
public enum DefaultReporters {

    UT_COVERAGE_HTML_REPORTER(CoverageHTMLReporter::new),
    UT_DOCUMENTATION_REPORTER(DocumentationReporter::new),
    UT_TEAMCITY_REPORTER(TeamCityReporter::new),
    UT_XUNIT_REPORTER(XUnitReporter::new),
    UT_COVERALLS_REPORTER(CoverallsReporter::new),
    UT_COVERAGE_SONAR_REPORTER(CoverageSonarReporter::new),
    UT_SONAR_TEST_REPORTER(SonarTestReporter::new);

    private Supplier<? extends Reporter> factoryMethod;

    DefaultReporters(Supplier<? extends Reporter> factoryMethod ) {
        this.factoryMethod = factoryMethod;
    }

    public Supplier<? extends Reporter> getFactoryMethod() {
        return factoryMethod;
    }
}
