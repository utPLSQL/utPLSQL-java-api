package io.github.utplsql.api.types;

/**
 * Database custom data types.
 */
public final class CustomTypes {

    // Object names must be upper case.
    public static final String UT_REPORTERS = "UT_REPORTERS";
    public static final String UT_DOCUMENTATION_REPORTER = "UT_DOCUMENTATION_REPORTER";
    public static final String UT_COVERAGE_HTML_REPORTER = "UT_COVERAGE_HTML_REPORTER";
    public static final String UT_VARCHAR2_LIST = "UT_VARCHAR2_LIST";

    private CustomTypes() {}

    public static BaseReporter createReporter(String reporterName) {
        switch (reporterName.toUpperCase()) {
            case UT_DOCUMENTATION_REPORTER: return new DocumentationReporter();
            case UT_COVERAGE_HTML_REPORTER: return new CoverageHTMLReporter();
        }
        throw new RuntimeException("Reporter " + reporterName + " not implemented.");
    }

}
