package org.utplsql.api.testRunner;

import oracle.jdbc.OracleConnection;
import org.junit.jupiter.api.Test;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.FileMapping;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class DynamicTestRunnerStatementTest {

    @Test
    void explore() throws SQLException {
        // Expectation objects
        Object[] expectedSourceFileMapping = new Object[]{new FileMapping("source", "owner", "source", "PACKAGE")};
        Object[] expectedTestFileMapping = new Object[]{new FileMapping("test", "owner", "test", "PACKAGE")};

        // Mock some internals. This is not pretty, but a first step
        OracleConnection oracleConnection = mock(OracleConnection.class);
        when(oracleConnection.unwrap(OracleConnection.class))
                .thenReturn(oracleConnection);
        CallableStatement callableStatement = mock(CallableStatement.class);

        // FileMapper mocks
        CallableStatement fileMapperStatement = mock(CallableStatement.class);
        when(
                oracleConnection.prepareCall(argThat(
                        a -> a.startsWith("BEGIN ? := ut_file_mapper.build_file_mappings("))
                ))
                .thenReturn(fileMapperStatement);
        Array fileMapperArray = mock(Array.class);
        when(fileMapperStatement.getArray(1))
                .thenReturn(fileMapperArray);
        when(fileMapperArray.getArray())
                .thenReturn(expectedSourceFileMapping);

        // Act
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
        verify(callableStatement).setInt(3, 0);

        assertThat(testRunnerStatement.getSql(), containsString("a_coverage_schemes => ?"));
        verify(callableStatement).setArray(4, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.coverageSchemes.toArray());

        assertThat(testRunnerStatement.getSql(), containsString("a_source_file_mappings => ?"));
        verify(callableStatement).setArray(5, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_FILE_MAPPINGS, expectedSourceFileMapping);


    }
}
