package io.github.utplsql.api;

import io.github.utplsql.api.exception.SomeTestsFailedException;
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
    private boolean colorConsole = false;
    private List<String> coverageSchemes = new ArrayList<>();
    private List<String> sourceFiles = new ArrayList<>();
    private List<String> testFiles = new ArrayList<>();
    private List<String> includeObjects = new ArrayList<>();
    private List<String> excludeObjects = new ArrayList<>();
    private boolean failOnErrors = false;

    public TestRunner addPath(String path) {
        this.pathList.add(path);
        return this;
    }

    public TestRunner addPathList(List<String> paths) {
        if (pathList != null) this.pathList.addAll(paths);
        return this;
    }

    public TestRunner addReporter(Reporter reporter) {
        this.reporterList.add(reporter);
        return this;
    }

    public TestRunner colorConsole(boolean colorConsole) {
        this.colorConsole = colorConsole;
        return this;
    }

    public TestRunner addReporterList(List<Reporter> reporterList) {
        if (reporterList != null) this.reporterList.addAll(reporterList);
        return this;
    }

    public TestRunner addCoverageScheme(String coverageScheme) {
        this.coverageSchemes.add(coverageScheme);
        return this;
    }

    public TestRunner withSourceFiles(List<String> sourceFiles) {
        if (sourceFiles != null) this.sourceFiles.addAll(sourceFiles);
        return this;
    }

    public TestRunner withTestFiles(List<String> testFiles) {
        if (testFiles != null) this.testFiles.addAll(testFiles);
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

    public TestRunner failOnErrors(boolean failOnErrors) {
        this.failOnErrors = failOnErrors;
        return this;
    }

    public void run(Connection conn) throws SomeTestsFailedException, SQLException {
        for (Reporter r : this.reporterList)
            validateReporter(conn, r);

        if (this.pathList.isEmpty()) {
            this.pathList.add(DBHelper.getCurrentSchema(conn));
        }

        if (this.reporterList.isEmpty()) {
            this.reporterList.add(new DocumentationReporter().init(conn));
        }

        // Workaround because Oracle JDBC doesn't support passing boolean to stored procedures.
        String colorConsoleStr = Boolean.toString(this.colorConsole);
        String failOnErrors = Boolean.toString(this.failOnErrors);

        OracleConnection oraConn = conn.unwrap(OracleConnection.class);
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall(
                    "BEGIN " +
                            "ut_runner.run(" +
                            "a_paths            => ?, " +
                            "a_reporters        => ?, " +
                            "a_color_console    => " + colorConsoleStr + ", " +
                            "a_coverage_schemes => ?, " +
                            "a_source_files     => ?, " +
                            "a_test_files       => ?, " +
                            "a_include_objects  => ?, " +
                            "a_exclude_objects  => ?, " +
                            "a_fail_on_errors   => " + failOnErrors + "); " +
                            "END;");

            int paramIdx = 0;

            callableStatement.setArray(
                    ++paramIdx, oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.pathList.toArray()));

            callableStatement.setArray(
                    ++paramIdx, oraConn.createARRAY(CustomTypes.UT_REPORTERS, this.reporterList.toArray()));

            if (this.coverageSchemes.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.coverageSchemes.toArray()));
            }

            if (this.sourceFiles.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.sourceFiles.toArray()));
            }

            if (this.testFiles.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.testFiles.toArray()));
            }

            if (this.includeObjects.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.includeObjects.toArray()));
            }

            if (this.excludeObjects.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, this.excludeObjects.toArray()));
            }

            callableStatement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == SomeTestsFailedException.ERROR_CODE) {
                throw new SomeTestsFailedException(e.getMessage(), e);
            } else {
                throw e;
            }
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
