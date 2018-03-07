package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.CoverageHTMLReporter;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReporterFactoryIT extends AbstractDatabaseTest {


    @Test
    public void createDefaultReporterFactoryMethod() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());

        ReporterFactory reporterFactory = ReporterFactory.createDefault(proxy);

        assertTrue( reporterFactory.createReporter(CoreReporters.UT_DOCUMENTATION_REPORTER.name()) instanceof DocumentationReporter );
        assertTrue( reporterFactory.createReporter(CoreReporters.UT_COVERAGE_HTML_REPORTER.name()) instanceof CoverageHTMLReporter);
    }
}
