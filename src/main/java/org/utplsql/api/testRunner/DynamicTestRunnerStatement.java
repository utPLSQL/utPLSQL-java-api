package org.utplsql.api.testRunner;

import oracle.jdbc.OracleConnection;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.FileMapping;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;
import org.utplsql.api.db.DynamicParameterList;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

        Object[] sourceMappings = (options.sourceMappingOptions!=null)
                ?FileMapper.buildFileMappingList(oracleConnection, options.sourceMappingOptions).toArray()
                :null;
        Object[] testMappings = (options.testMappingOptions!=null)
                ?FileMapper.buildFileMappingList(oracleConnection, options.testMappingOptions).toArray()
                :null;

        return DynamicParameterList.builder()
                .addIfNotEmpty("a_paths", options.pathList.toArray(), CustomTypes.UT_VARCHAR2_LIST, oracleConnection)
                .addIfNotEmpty("a_reporters", options.reporterList.toArray(), CustomTypes.UT_REPORTERS, oracleConnection)
                .addIfNotEmpty("a_color_console", options.colorConsole)
                .addIfNotEmpty("a_coverage_schemes", options.coverageSchemes.toArray(), CustomTypes.UT_VARCHAR2_LIST, oracleConnection)
                .addIfNotEmpty("a_source_file_mappings", sourceMappings, CustomTypes.UT_FILE_MAPPINGS, oracleConnection)
                .addIfNotEmpty("a_test_file_mappings", testMappings, CustomTypes.UT_FILE_MAPPINGS, oracleConnection)
                .addIfNotEmpty("a_include_objects", options.includeObjects.toArray(), CustomTypes.UT_VARCHAR2_LIST, oracleConnection)
                .addIfNotEmpty("a_exclude_objects", options.excludeObjects.toArray(), CustomTypes.UT_VARCHAR2_LIST, oracleConnection)
                .addIfNotEmpty("a_fail_on_errors", options.failOnErrors)
                .addIfNotEmpty("a_client_character_set", options.clientCharacterSet)
                .addIfNotEmpty("a_random_test_order", options.randomTestOrder)
                .addIfNotEmpty("a_random_test_order_seed", options.randomTestOrderSeed)
                .addIfNotEmpty("a_tags", options.getTagsAsString())
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
