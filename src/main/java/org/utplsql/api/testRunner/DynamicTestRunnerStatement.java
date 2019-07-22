package org.utplsql.api.testRunner;

import oracle.jdbc.OracleConnection;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.db.DynamicParameterList;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DynamicTestRunnerStatement implements TestRunnerStatement {

    private CallableStatement stmt;
    private final OracleConnection oracleConnection;
    private final Version utPlSQlVersion;
    private final TestRunnerOptions options;
    private final DynamicParameterList dynamicParameterList;

    private DynamicTestRunnerStatement( Version utPlSQlVersion, OracleConnection connection, TestRunnerOptions options, CallableStatement statement ) throws SQLException {
        this.utPlSQlVersion = utPlSQlVersion;
        this.oracleConnection = connection;
        this.options = options;
        this.stmt = statement;

        this.dynamicParameterList = initParameterList();

        prepareStatement();
    }

    private DynamicParameterList initParameterList() throws SQLException {
        /*
        "BEGIN " +
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
        return DynamicParameterList.builder()
                .addIfNotEmpty("a_paths", options.pathList.toArray(), CustomTypes.UT_VARCHAR2_LIST, oracleConnection)
                .addIfNotEmpty("a_reporters", options.reporterList.toArray(), CustomTypes.UT_REPORTERS, oracleConnection)
                .build();
    }

    private void prepareStatement() throws SQLException {
        if ( stmt == null )
            oracleConnection.prepareCall(dynamicParameterList.getSql());

        dynamicParameterList.setParamsStartWithIndex(stmt, 1);
    }

    @Override
    public void execute() throws SQLException {

        // Implement
    }

    @Override
    public String getSql() {
        return dynamicParameterList.getSql();
    }

    @Override
    public void close() throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    public static DynamicTestRunnerStatement forVersion(Version version, OracleConnection connection, TestRunnerOptions options, CallableStatement statement ) throws SQLException {
        return new DynamicTestRunnerStatement(version, connection, options, statement);
    }
}
