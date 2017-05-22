package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

/**
 * Created by vinicius.moreira on 22/05/2017.
 */
public final class ReporterFactory {

    private ReporterFactory() {}

    public static Reporter createReporter(String reporterName) {
        switch (reporterName.toUpperCase()) {
            case CustomTypes.UT_DOCUMENTATION_REPORTER: return new DocumentationReporter();
            case CustomTypes.UT_COVERAGE_HTML_REPORTER: return new CoverageHTMLReporter();
        }
        throw new RuntimeException("Reporter " + reporterName + " not implemented.");
    }

}
