package io.github.utplsql.api.types;

/**
 * Created by Vinicius on 13/04/2017.
 * DBHelper custom data types.
 */
public enum CustomTypes {
    // Object names must be upper case.
    UT_DOCUMENTATION_REPORTER("UT_DOCUMENTATION_REPORTER"),
    UT_COVERAGE_HTML_REPORTER("UT_COVERAGE_HTML_REPORTER"),
    UT_VARCHAF2_LIST("UT_VARCHAR2_LIST");

    private String typeName;

    CustomTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return this.typeName;
    }

}
