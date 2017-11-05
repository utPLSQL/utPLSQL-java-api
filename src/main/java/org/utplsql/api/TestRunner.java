package org.utplsql.api;

import org.utplsql.api.exception.DatabaseNotCompatibleException;
import org.utplsql.api.exception.SomeTestsFailedException;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.Reporter;
import oracle.jdbc.OracleConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
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
    private FileMapperOptions sourceMappingOptions;
    private FileMapperOptions testMappingOptions;
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

    public TestRunner includeObject(String obj) {
        this.includeObjects.add(obj);
        return this;
    }

    public TestRunner excludeObject(String obj) {
        this.excludeObjects.add(obj);
        return this;
    }

    public TestRunner sourceMappingOptions(FileMapperOptions mapperOptions) {
        this.sourceMappingOptions = mapperOptions;
        return this;
    }

    public TestRunner testMappingOptions(FileMapperOptions mapperOptions) {
        this.testMappingOptions = mapperOptions;
        return this;
    }

    public TestRunner failOnErrors(boolean failOnErrors) {
        this.failOnErrors = failOnErrors;
        return this;
    }

    public void run(Connection conn) throws SomeTestsFailedException, SQLException, DatabaseNotCompatibleException {

        // First of all check version compatibility
        DBHelper.failOnVersionCompatibilityCheckFailed(conn);

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
            DBHelper.enableDBMSOutput(conn);

            callableStatement = conn.prepareCall(
                    "BEGIN " +
                        "ut_runner.run(" +
                            "a_paths                => ?, " +
                            "a_reporters            => ?, " +
                            "a_color_console        => " + colorConsoleStr + ", " +
                            "a_coverage_schemes     => ?, " +
                            "a_source_file_mappings => ?, " +
                            "a_test_file_mappings   => ?, " +
                            "a_include_objects      => ?, " +
                            "a_exclude_objects      => ?, " +
                            "a_fail_on_errors       => " + failOnErrors + "); " +
                    "END;");

            int paramIdx = 0;

            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, this.pathList.toArray()));

            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_REPORTERS, this.reporterList.toArray()));

            if (this.coverageSchemes.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, this.coverageSchemes.toArray()));
            }

            if (this.sourceMappingOptions == null) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_FILE_MAPPINGS);
            } else {
                List<FileMapping> sourceMappings = FileMapper.buildFileMappingList(conn, this.sourceMappingOptions);

                callableStatement.setArray(
                        ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_FILE_MAPPINGS, sourceMappings.toArray()));
            }

            if (this.testMappingOptions == null) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_FILE_MAPPINGS);
            } else {
                List<FileMapping> sourceMappings = FileMapper.buildFileMappingList(conn, this.testMappingOptions);

                callableStatement.setArray(
                        ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_FILE_MAPPINGS, sourceMappings.toArray()));
            }

            if (this.includeObjects.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, this.includeObjects.toArray()));
            }

            if (this.excludeObjects.isEmpty()) {
                callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
            } else {
                callableStatement.setArray(
                        ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, this.excludeObjects.toArray()));
            }

            callableStatement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == SomeTestsFailedException.ERROR_CODE) {
                throw new SomeTestsFailedException(e.getMessage(), e);
            } else {
                throw e;
            }
        } finally {
            if (callableStatement != null) {
                callableStatement.close();
            }

            DBHelper.disableDBMSOutput(conn);
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
