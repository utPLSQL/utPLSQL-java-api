package org.utplsql.api.testRunner;

import oracle.jdbc.OracleConnection;
import org.junit.jupiter.api.Test;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DynamicTestRunnerStatementTest {

    @Test
    void explore() throws SQLException {

        OracleConnection oracleConnection = mock(OracleConnection.class);
        CallableStatement callableStatement = mock(CallableStatement.class);

        TestRunnerOptions options = TestRunnerStatementProviderIT.getCompletelyFilledOptions();

        DynamicTestRunnerStatement testRunnerStatement = DynamicTestRunnerStatement
                .forVersion(Version.V3_1_7, oracleConnection, options, callableStatement);

        /*
        "ut_runner.run(" +
                        "a_paths                  => ?, " +
                        "a_reporters              => ?, " +
                        "a_color_console          => " + colorConsoleStr + ", " +
                        "a_coverage_schemes       => ?, " +
                        "a_source_file_mappings   => ?, " +
                        "a_test_file_mappings     => ?, " +
                        "a_include_objects        => ?, " +
                        "a_exclude_objects        => ?, " +
                        "a_fail_on_errors         => " + failOnErrors + ", " +
                        "a_client_character_set   => ?, " +
                        "a_random_test_order      => " + randomExecutionOrder + ", " +
                        "a_random_test_order_seed => ?, "+
                        "a_tags => ?"+
                        "); " +
                        "END;";
         */
        assertThat(testRunnerStatement.getSql(), containsString("a_paths => ?"));
        verify(callableStatement).setArray(1, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.pathList.toArray());

        assertThat(testRunnerStatement.getSql(), containsString("a_reporters => ?"));
        verify(callableStatement).setArray(2, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_REPORTERS, options.reporterList.toArray());

        assertThat(testRunnerStatement.getSql(), containsString("a_color_console => (case ? when 1 then true else false)"));
        verify(callableStatement).setArray(1, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.pathList.toArray());
    }
}
