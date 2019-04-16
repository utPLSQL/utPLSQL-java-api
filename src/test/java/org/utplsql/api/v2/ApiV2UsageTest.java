package org.utplsql.api.v2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;
import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.exception.UtplsqlConfigurationException;
import org.utplsql.api.reporter.inspect.ReporterInfo;
import org.utplsql.api.v2.reporters.ReporterFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pavel Kaplya on 28.02.2019.
 */
@Disabled
class ApiV2UsageTest extends AbstractDatabaseTest {

    @Test
    void testSessionCreation() throws UtplsqlConfigurationException, ExecutionException, InterruptedException {
        UtplsqlSession utplsqlSession = SessionFactory.create(getDataSource());

        TestRun testRun = utplsqlSession.createTestRun()
                .paths(Collections.singletonList("app"))
                .build();

        ReporterFactory reporterFactory = utplsqlSession.reporterFactory();
        testRun.addReporter(reporterFactory.documentationReporter())/*
                .addReporter(reporterFactory.coverageHtmlReporter())*/;

        TestRunResults testRunResults = testRun.execute().get();
    }

    @Test
    void testListAvailableReporters() throws SQLException, InvalidVersionException {

        UtplsqlSession utplsqlSession = SessionFactory.create(getDataSource());

        List<ReporterInfo> availableReporters = utplsqlSession.getAvailableReporters();
    }

    @Test
    void test() throws SQLException {
        UtplsqlSession utplsqlSession = SessionFactory.create(getDataSource());

        Version versionInfo = utplsqlSession.getInstalledVersionInfo();
    }
}
