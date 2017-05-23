package io.github.utplsql.api;

import io.github.utplsql.api.reporter.DocumentationReporter;
import io.github.utplsql.api.reporter.Reporter;
import oracle.jdbc.OracleConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 */
public class TestRunner {

    private List<String> pathList = new ArrayList<>();
    private List<Reporter> reporterList = new ArrayList<>();
    private List<String> coverageSchemes = new ArrayList<>();
    private List<String> sourceFiles = new ArrayList<>();
    private List<String> testFiles = new ArrayList<>();
    private List<String> includeObjects = new ArrayList<>();
    private List<String> excludeObjects = new ArrayList<>();

    public TestRunner() {
    }

    public TestRunner addPath(String path) {
        this.pathList.add(path);
        return this;
    }

    public TestRunner addPathList(List<String> paths) {
        this.pathList.addAll(paths);
        return this;
    }

    public TestRunner addReporter(Reporter reporter) {
        this.reporterList.add(reporter);
        return this;
    }

    public TestRunner addReporterList(List<Reporter> reporterList) {
        this.reporterList.addAll(reporterList);
        return this;
    }

    public TestRunner addCoverageScheme(String coverageScheme) {
        this.coverageSchemes.add(coverageScheme);
        return this;
    }

    public TestRunner withSourceFiles(List<String> sourceFiles) {
        this.sourceFiles.addAll(sourceFiles);
        return this;
    }

    public TestRunner withTestFiles(List<String> testFiles) {
        this.testFiles.addAll(testFiles);
        return this;
    }

    public TestRunner includeObject(String obj) {
        this.includeObjects.add(obj);
        return this;
    }

    public TestRunner excludeObject(String obj) {
        this.excludeObjects.add(obj);
        return this;
    }

    public void run(Connection conn) throws SQLException {
        for (Reporter r : this.reporterList)
            validateReporter(conn, r);

        if (this.pathList.isEmpty()) {
            this.pathList.add(DBHelper.getCurrentSchema(conn));
        }

        if (this.reporterList.isEmpty()) {
            this.reporterList.add(new DocumentationReporter().init(conn));
        }

        OracleConnection oraConn = conn.unwrap(OracleConnection.class);
        Array pathArray = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.pathList.toArray());
        Array reporterArray = oraConn.createARRAY(CustomTypes.UT_REPORTERS, this.reporterList.toArray());
        Array coverageSchemes = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.coverageSchemes.toArray());
        Array sourceFiles = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.sourceFiles.toArray());
        Array testFiles = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.testFiles.toArray());
        Array includeObjects = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.includeObjects.toArray());
        Array excludeObjects = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.excludeObjects.toArray());

        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall(
                    "BEGIN " +
                        "ut_runner.run(" +
                            "a_paths => ?, a_reporters => ?, a_coverage_schemes => ?," +
                            "a_source_files => ?, a_test_files => ?, " +
                            "a_include_objects => ?, a_exclude_objects => ?); " +
                    "END;");
            callableStatement.setArray(1, pathArray);
            callableStatement.setArray(2, reporterArray);
            callableStatement.setArray(3, coverageSchemes);
            callableStatement.setArray(4, sourceFiles);
            callableStatement.setArray(5, testFiles);
            callableStatement.setArray(6, includeObjects);
            callableStatement.setArray(7, excludeObjects);
            callableStatement.execute();
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /**
     * Check if the reporter was initialized, if not call reporter.init.
     * @param conn the database connection
     * @param reporter the reporter
     * @throws SQLException any sql exception
     */
    private void validateReporter(Connection conn, Reporter reporter) throws SQLException {
        if (reporter.getReporterId() == null || reporter.getReporterId().isEmpty())
            reporter.init(conn);
    }

}
