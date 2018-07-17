package org.utplsql.api.reporter.inspect;

import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;

abstract class AbstractReporterInspector implements ReporterInspector  {

    protected final ReporterFactory reporterFactory;
    protected final Connection connection;

    AbstractReporterInspector(ReporterFactory reporterFactory, Connection conn ) {
        this.reporterFactory = reporterFactory;
        this.connection = conn;
    }

}
