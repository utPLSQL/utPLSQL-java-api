package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class CoverageHTMLReporter extends Reporter {

    private String projectName;
    private String assetsPath;

    public CoverageHTMLReporter() {

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
