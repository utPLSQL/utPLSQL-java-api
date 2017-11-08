package org.utplsql.api.testRunner;

import org.utplsql.api.TestRunnerOptions;

import java.sql.Connection;
import java.sql.SQLException;

public class TestRunnerStatement extends AbstractTestRunnerStatement {

    public TestRunnerStatement(TestRunnerOptions options, Connection connection ) throws SQLException {
        super( options, connection);
    }

    @Override
    protected String getSql() {
        // Workaround because Oracle JDBC doesn't support passing boolean to stored procedures.
        String colorConsoleStr = Boolean.toString(options.colorConsole);
        String failOnErrors = Boolean.toString(options.failOnErrors);

        return
                "BEGIN " +
                        "ut_runner.run(" +
                        "a_paths                => ?, " +
                        "a_reporters            => ?, " +
                        "a_color_console        => " + colorConsoleStr + ", " +
                        "a_coverage_schemes     => ?, " +
                        "a_source_file_mappings => ?, " +
                        "a_test_file_mappings   => ?, " +
                        "a_include_objects      => ?, " +
                        "a_exclude_objects      => ?, " +
                        "a_fail_on_errors       => " + failOnErrors + "); " +
                        "END;";
    }
}
