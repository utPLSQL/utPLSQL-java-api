package org.utplsql.api;

import org.utplsql.api.compatibility.CompatibilityProvider;
import org.utplsql.api.exception.DatabaseNotCompatibleException;
import org.utplsql.api.exception.SomeTestsFailedException;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.testRunner.AbstractTestRunnerStatement;
import org.utplsql.api.testRunner.TestRunnerStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 *
 * @author Vinicius Avellar
 * @author pesse
 */
public class TestRunner {

    private TestRunnerOptions options = new TestRunnerOptions();

    public TestRunner addPath(String path) {
        options.pathList.add(path);
        return this;
    }

    public TestRunner addPathList(List<String> paths) {
        if (options.pathList != null) options.pathList.addAll(paths);
        return this;
    }

    public TestRunner addReporter(Reporter reporter) {
        options.reporterList.add(reporter);
        return this;
    }

    public TestRunner colorConsole(boolean colorConsole) {
        options.colorConsole = colorConsole;
        return this;
    }

    public TestRunner addReporterList(List<Reporter> reporterList) {
        if (options.reporterList != null) options.reporterList.addAll(reporterList);
        return this;
    }

    public TestRunner addCoverageScheme(String coverageScheme) {
        options.coverageSchemes.add(coverageScheme);
        return this;
    }

    public TestRunner includeObject(String obj) {
        options.includeObjects.add(obj);
        return this;
    }

    public TestRunner excludeObject(String obj) {
        options.excludeObjects.add(obj);
        return this;
    }

    public TestRunner sourceMappingOptions(FileMapperOptions mapperOptions) {
        options.sourceMappingOptions = mapperOptions;
        return this;
    }

    public TestRunner testMappingOptions(FileMapperOptions mapperOptions) {
        options.testMappingOptions = mapperOptions;
        return this;
    }

    public TestRunner failOnErrors(boolean failOnErrors) {
        options.failOnErrors = failOnErrors;
        return this;
    }

    public void run(Connection conn) throws SomeTestsFailedException, SQLException, DatabaseNotCompatibleException {

        // First of all check version compatibility
        DBHelper.failOnVersionCompatibilityCheckFailed(conn);

        for (Reporter r : options.reporterList)
            validateReporter(conn, r);

        if (options.pathList.isEmpty()) {
            options.pathList.add(DBHelper.getCurrentSchema(conn));
        }

        if (options.reporterList.isEmpty()) {
            options.reporterList.add(new DocumentationReporter().init(conn));
        }

        TestRunnerStatement testRunnerStatement = null;

        try {
            DBHelper.enableDBMSOutput(conn);

            testRunnerStatement = CompatibilityProvider.getTestRunnerStatement(options, conn);

            testRunnerStatement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == SomeTestsFailedException.ERROR_CODE) {
                throw new SomeTestsFailedException(e.getMessage(), e);
            } else {
                throw e;
            }
        } finally {
            if (testRunnerStatement != null) {
                testRunnerStatement.close();
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
