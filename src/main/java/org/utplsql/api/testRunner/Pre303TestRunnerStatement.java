package org.utplsql.api.testRunner;

import org.utplsql.api.TestRunnerOptions;

import java.sql.Connection;
import java.sql.SQLException;

public class Pre303TestRunnerStatement extends AbstractTestRunnerStatement {

    public Pre303TestRunnerStatement(TestRunnerOptions options, Connection conn) throws SQLException {
        super(options, conn);
    }

    @Override
    protected String getSql() {
        // Workaround because Oracle JDBC doesn't support passing boolean to stored procedures.
        String colorConsoleStr = Boolean.toString(options.colorConsole);

        return "BEGIN " +
                "ut_runner.run(" +
                "a_paths            => ?, " +
                "a_reporters        => ?, " +
                "a_color_console    => " + colorConsoleStr + ", " +
                "a_coverage_schemes => ?, " +
                "a_source_files     => ?, " +
                "a_test_files       => ?, " +
                "a_include_objects  => ?, " +
                "a_exclude_objects  => ?); " +
                "END;";
    }
}
