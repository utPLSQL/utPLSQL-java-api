package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class CoverageHTMLReporter extends Reporter {

    // Could override Reporter.init and call ut_coverage_report_html_helper.get_default_html_assets_path from database,
    // but had permissions issues.
    public static final String DEFAULT_ASSETS_PATH = "https://utplsql.github.io/utPLSQL-coverage-html/assets/";

    private String projectName;
    private String assetsPath;

    public CoverageHTMLReporter() {
        this(null, DEFAULT_ASSETS_PATH);
    }

    public CoverageHTMLReporter(String projectName, String assetsPath) {
        this.projectName = projectName;
        this.assetsPath = assetsPath;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_COVERAGE_HTML_REPORTER;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(String assetsPath) {
        this.assetsPath = assetsPath;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        super.readSQL(stream, typeName);
        setProjectName(stream.readString());
        setAssetsPath(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        super.writeSQL(stream);
        stream.writeString(getProjectName());
        stream.writeString(getAssetsPath());
    }

}
