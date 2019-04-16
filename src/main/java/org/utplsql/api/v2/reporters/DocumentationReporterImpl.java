package org.utplsql.api.v2.reporters;

import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.Reporter;

/**
 * Created by Pavel Kaplya on 15.03.2019.
 */
public class DocumentationReporterImpl extends AbstractReporter implements DocumentationReporter {

    private final static String REPORTER_NAME = CoreReporters.UT_DOCUMENTATION_REPORTER.name();
    private final org.utplsql.api.reporter.DocumentationReporter reporterObject;

    public DocumentationReporterImpl() {
        super(REPORTER_NAME);
        this.reporterObject = new org.utplsql.api.reporter.DocumentationReporter();
    }

    @Override
    public Reporter getReporterObject() {
        return reporterObject;
    }
}
