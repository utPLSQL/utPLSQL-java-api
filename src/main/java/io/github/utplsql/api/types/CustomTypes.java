package io.github.utplsql.api.types;

/**
 * Database custom data types.
 */
public enum CustomTypes {
    // Object names must be upper case.
    UT_DOCUMENTATION_REPORTER("UT_DOCUMENTATION_REPORTER"),
    UT_COVERAGE_HTML_REPORTER("UT_COVERAGE_HTML_REPORTER"),
    UT_VARCHAR2_LIST("UT_VARCHAR2_LIST");

    private String typeName;

    CustomTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return this.typeName;
    }

}
