package org.utplsql.api.testRunner;

import java.sql.SQLException;

/** Interface to hide the concrete Statement-implementations of TestRunner
 *
 * @author pesse
 */
public interface TestRunnerStatement {

    void execute() throws SQLException;

    void close() throws SQLException;
}
