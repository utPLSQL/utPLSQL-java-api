package org.utplsql.api.v2.reporters;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Pavel Kaplya on 16.03.2019.
 */
public class ReporterFactoryImpl implements ReporterFactory {
    private final DataSource dataSource;
    public ReporterFactoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DocumentationReporter documentationReporter() {
        return new DocumentationReporterImpl();
    }

    @Override
    public DocumentationReporter documentationReporter(OutputStream outputStream) {
        return null;
    }

    @Override
    public CoverageHTMLReporter coverageHtmlReporter() {
        return null;
    }

    @Override
    public CoverageSonarReporter coverageSonarReporter() {
        return null;
    }

    @Override
    public CoverageCoberturaReporter coverageCuberturaReporter() {
        return null;
    }

    @Override
    public CoverallsReporter coverallsReporter() {
        return null;
    }

    @Override
    public SonarTestReporter sonarTestReporter() {
        return null;
    }

    @Override
    public TeamcityReporter teamcityReporter() {
        return null;
    }

    @Override
    public XUnitReporter xUnitReporter() {
        return null;
    }

    @Override
    public CustomReporter customReporter(String reporterTypeName) {
        return null;
    }

    @Override
    public List<Reporter> getAvailableReporters() {
        return null;
    }
}
