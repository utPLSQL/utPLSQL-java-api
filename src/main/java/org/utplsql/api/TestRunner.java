package org.utplsql.api;

import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.exception.DatabaseNotCompatibleException;
import org.utplsql.api.exception.SomeTestsFailedException;
import org.utplsql.api.exception.UtPLSQLNotInstalledException;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.reporter.ReporterFactory;
import org.utplsql.api.testRunner.TestRunnerStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 *
 * @author Vinicius Avellar
 * @author pesse
 */
public class TestRunner {

    private final TestRunnerOptions options = new TestRunnerOptions();
    private CompatibilityProxy compatibilityProxy;
    private ReporterFactory reporterFactory;
    private final List<String> reporterNames = new ArrayList<>();

    public TestRunner addPath(String path) {
        options.pathList.add(path);
        return this;
    }

    public TestRunner addPathList(List<String> paths) {
        options.pathList.addAll(paths);
        return this;
    }

    public TestRunner addReporter(Reporter reporter) {
        options.reporterList.add(reporter);
        return this;
    }

    public TestRunner addReporter( String reporterName ) {
        if ( reporterFactory != null )
            options.reporterList.add(reporterFactory.createReporter(reporterName));
        else
            reporterNames.add(reporterName);
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

    public TestRunner includeObjects(List<String> obj) {
        options.includeObjects.addAll(obj);
        return this;
    }

    public TestRunner excludeObjects(List<String> obj) {
        options.excludeObjects.addAll(obj);
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

    public TestRunner skipCompatibilityCheck( boolean skipCompatibilityCheck )
    {
        options.skipCompatibilityCheck = skipCompatibilityCheck;
        return this;
    }

    public TestRunner setReporterFactory( ReporterFactory reporterFactory ) {
        this.reporterFactory = reporterFactory;
        return this;
    }

    private void delayedAddReporters() {
        if ( reporterFactory != null )
            reporterNames.forEach( this::addReporter );
        else
            throw new IllegalStateException("ReporterFactory must be set to add delayed Reporters!");
    }

    public void run(Connection conn) throws SomeTestsFailedException, SQLException, DatabaseNotCompatibleException, UtPLSQLNotInstalledException {

        compatibilityProxy = new CompatibilityProxy(conn, options.skipCompatibilityCheck);
        if ( reporterFactory ==  null )
            reporterFactory = ReporterFactory.createDefault(compatibilityProxy);

        delayedAddReporters();

        // First of all check version compatibility
        compatibilityProxy.failOnNotCompatible();

        for (Reporter r : options.reporterList)
            validateReporter(conn, r);

        if (options.pathList.isEmpty()) {
            options.pathList.add(DBHelper.getCurrentSchema(conn));
        }

        if (options.reporterList.isEmpty()) {
            options.reporterList.add(new DocumentationReporter().init(conn));
        }

        DBHelper.enableDBMSOutput(conn);
        try(TestRunnerStatement testRunnerStatement = compatibilityProxy.getTestRunnerStatement(options, conn)) {
            testRunnerStatement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == SomeTestsFailedException.ERROR_CODE) {
                throw new SomeTestsFailedException(e.getMessage(), e);
            }
            else if (e.getErrorCode() == UtPLSQLNotInstalledException.ERROR_CODE) {
                throw new UtPLSQLNotInstalledException(e);
            }
            else {
                throw e;
            }
        } finally {
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
        if (!reporter.isInit() || reporter.getId() == null || reporter.getId().isEmpty())
            reporter.init(conn, compatibilityProxy, reporterFactory);
    }

    /** Returns the databaseVersion the TestRunner was run against
     *
     * @return Version of the database the TestRunner was run against
     */
    public Version getUsedDatabaseVersion() {
        if ( compatibilityProxy != null )
            return compatibilityProxy.getDatabaseVersion();
        else
            return null;
    }

}
