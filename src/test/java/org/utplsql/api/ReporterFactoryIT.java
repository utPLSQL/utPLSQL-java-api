package org.utplsql.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.CoverageHTMLReporter;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;

class ReporterFactoryIT extends AbstractDatabaseTest {


    @Test
    void createDefaultReporterFactoryMethod() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());

        ReporterFactory reporterFactory = ReporterFactory.createDefault(proxy);

        assertThat(reporterFactory.createReporter(CoreReporters.UT_DOCUMENTATION_REPORTER.name()), Matchers.isA(DocumentationReporter.class));
        assertThat(reporterFactory.createReporter(CoreReporters.UT_COVERAGE_HTML_REPORTER.name()), Matchers.isA(CoverageHTMLReporter.class));
    }
}
