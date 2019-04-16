package org.utplsql.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.db.DatabaseInformation;
import org.utplsql.api.db.DefaultDatabaseInformation;
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

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    private final TestRunnerOptionsImpl options = new TestRunnerOptionsImpl();
    private final List<String> reporterNames = new ArrayList<>();
    private CompatibilityProxy compatibilityProxy;
    private ReporterFactory reporterFactory;

    public TestRunner addPath(String path) {
        options.getPathList().add(path);
        return this;
    }

    public TestRunner addPathList(List<String> paths) {
        options.getPathList().addAll(paths);
        return this;
    }

    public TestRunner addReporter(Reporter reporter) {
        options.getReporterList().add(reporter);
        return this;
    }

    public TestRunner addReporter(String reporterName) {
        if (reporterFactory != null) {
            options.getReporterList().add(reporterFactory.createReporter(reporterName));
        } else {
            reporterNames.add(reporterName);
        }
        return this;
    }

    public TestRunner colorConsole(boolean colorConsole) {
        options.colorConsole = colorConsole;
        return this;
    }

    public TestRunner addReporterList(List<Reporter> reporterList) {
        options.getReporterList().addAll(reporterList);
        return this;
    }

    public TestRunner addCoverageScheme(String coverageScheme) {
        options.getCoverageSchemes().add(coverageScheme);
        return this;
    }

    public TestRunner includeObject(String obj) {
        options.getIncludeObjects().add(obj);
        return this;
    }

    public TestRunner excludeObject(String obj) {
        options.getExcludeObjects().add(obj);
        return this;
    }

    public TestRunner includeObjects(List<String> obj) {
        options.getIncludeObjects().addAll(obj);
        return this;
    }

    public TestRunner excludeObjects(List<String> obj) {
        options.getExcludeObjects().addAll(obj);
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

    public TestRunner skipCompatibilityCheck(boolean skipCompatibilityCheck) {
        options.skipCompatibilityCheck = skipCompatibilityCheck;
        return this;
    }

    public TestRunner setReporterFactory(ReporterFactory reporterFactory) {
        this.reporterFactory = reporterFactory;
        return this;
    }

    private void delayedAddReporters() {
        if (reporterFactory != null) {
            reporterNames.forEach(this::addReporter);
        } else {
            throw new IllegalStateException("ReporterFactory must be set to add delayed Reporters!");
        }
    }

    public void run(Connection conn) throws SQLException {

        logger.info("TestRunner initialized");

        DatabaseInformation databaseInformation = new DefaultDatabaseInformation();

        compatibilityProxy = new CompatibilityProxy(conn, options.isSkipCompatibilityCheck(), databaseInformation);
        logger.info("Running on utPLSQL {}", compatibilityProxy.getDatabaseVersion());

        if (reporterFactory == null) {
            reporterFactory = ReporterFactory.createDefault(compatibilityProxy);
        }

        delayedAddReporters();

        // First of all check version compatibility
        compatibilityProxy.failOnNotCompatible();

        logger.info("Initializing reporters");
        for (Reporter r : options.getReporterList()) {
            validateReporter(conn, r);
        }

        if (options.getPathList().isEmpty()) {
            options.getPathList().add(databaseInformation.getCurrentSchema(conn));
        }

        if (options.getReporterList().isEmpty()) {
            logger.info("No reporter given so choosing ut_documentation_reporter");
            options.getReporterList().add(new DocumentationReporter().init(conn));
        }

        try (TestRunnerStatement testRunnerStatement = compatibilityProxy.getTestRunnerStatement(options, conn)) {
            logger.info("Running tests");
            testRunnerStatement.execute();
            logger.info("Running tests finished.");
        } catch (SQLException e) {
            if (e.getErrorCode() == SomeTestsFailedException.ERROR_CODE) {
                throw new SomeTestsFailedException(e.getMessage(), e);
            } else if (e.getErrorCode() == UtPLSQLNotInstalledException.ERROR_CODE) {
                throw new UtPLSQLNotInstalledException(e);
            } else {
                throw e;
            }
        }
    }

    /**
     * Check if the reporter was initialized, if not call reporter.init.
     *
     * @param conn     the database connection
     * @param reporter the reporter
     * @throws SQLException any sql exception
     */
    private void validateReporter(Connection conn, Reporter reporter) throws SQLException {
        if (!reporter.isInit() || reporter.getId() == null || reporter.getId().isEmpty()) {
            reporter.init(conn, compatibilityProxy, reporterFactory);
        }
    }

    /**
     * Returns the databaseVersion the TestRunner was run against
     *
     * @return Version of the database the TestRunner was run against
     */
    public Version getUsedDatabaseVersion() {
        if (compatibilityProxy != null) {
            return compatibilityProxy.getDatabaseVersion();
        } else {
            return null;
        }
    }

}
