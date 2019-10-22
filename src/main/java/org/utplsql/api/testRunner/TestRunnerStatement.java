package org.utplsql.api.testRunner;

import java.sql.SQLException;

/**
 * Interface to hide the concrete Statement-implementations of TestRunner
 *
 * @author pesse
 */
public interface TestRunnerStatement extends AutoCloseable {

    void execute() throws SQLException;

    String getSql();

    @Override
    void close() throws SQLException;
}
