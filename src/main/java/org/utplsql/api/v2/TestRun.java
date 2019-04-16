package org.utplsql.api.v2;

import org.utplsql.api.exception.UtplsqlConfigurationException;
import org.utplsql.api.v2.reporters.Reporter;

import java.util.concurrent.Future;

/**
 * Created by Pavel Kaplya on 02.03.2019.
 */
public interface TestRun {
    TestRun addReporter(Reporter reporterFactory);

    Future<TestRunResults> execute() throws UtplsqlConfigurationException;
}
