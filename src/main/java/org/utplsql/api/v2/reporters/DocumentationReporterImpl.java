package org.utplsql.api.v2.reporters;

import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.Reporter;

import javax.sql.DataSource;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Pavel Kaplya on 15.03.2019.
 */
public class DocumentationReporterImpl extends AbstractReporter implements DocumentationReporter {

    private final static String REPORTER_NAME = CoreReporters.UT_DOCUMENTATION_REPORTER.name();
    private final org.utplsql.api.reporter.DocumentationReporter reporterObject;

    DocumentationReporterImpl(DataSource dataSource) {
        super(REPORTER_NAME, dataSource);
        this.reporterObject = new org.utplsql.api.reporter.DocumentationReporter();
    }

    @Override
    public Reporter getReporterObject() {
        return reporterObject;
    }

    @Override
    public void printAvailable(PrintStream ps) {
        return reporterObject.getOutputBuffer().printAvailable();
    }

    @Override
    public void printAvailable(Connection conn, List<PrintStream> printStreams) throws SQLException {

    }

    @Override
    public void fetchAvailable(Connection conn, Consumer<String> onLineFetched) throws SQLException {

    }

    @Override
    public List<String> fetchAll(Connection conn) throws SQLException {
        return null;
    }
}
