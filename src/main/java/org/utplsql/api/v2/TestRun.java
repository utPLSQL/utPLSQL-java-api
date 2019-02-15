package org.utplsql.api.v2;

/**
 * Created by Pavel Kaplya on 02.03.2019.
 */
public interface TestRun {
    TestRun addReporter(Reporter reporterFactory);
}
