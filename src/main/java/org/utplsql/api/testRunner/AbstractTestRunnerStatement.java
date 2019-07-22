package org.utplsql.api.testRunner;

import oracle.jdbc.OracleConnection;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.FileMapping;
import org.utplsql.api.TestRunnerOptions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Abstract class which creates a callable statement for running tests
 * The SQL to be used has to be implemented for there are differences between the Framework-versions
 *
 * @author pesse
 */
abstract class AbstractTestRunnerStatement implements TestRunnerStatement {

    protected final TestRunnerOptions options;
    protected final CallableStatement callableStatement;
    private final Connection conn;

    public AbstractTestRunnerStatement(TestRunnerOptions options, Connection conn) throws SQLException {
        this.options = options;
        this.conn = conn;

        callableStatement = conn.prepareCall(getSql());

        createStatement();
    }

    protected abstract String getSql();

    protected int createStatement() throws SQLException {

        OracleConnection oraConn = conn.unwrap(OracleConnection.class);

        int paramIdx = 0;

        callableStatement.setArray(
                ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.pathList.toArray()));

        callableStatement.setArray(
                ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_REPORTERS, options.reporterList.toArray()));

        if (options.coverageSchemes.isEmpty()) {
            callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
        } else {
            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.coverageSchemes.toArray()));
        }

        if (options.sourceMappingOptions == null) {
            callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_FILE_MAPPINGS);
        } else {
            List<FileMapping> sourceMappings = FileMapper.buildFileMappingList(conn, options.sourceMappingOptions);

            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_FILE_MAPPINGS, sourceMappings.toArray()));
        }

        if (options.testMappingOptions == null) {
            callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_FILE_MAPPINGS);
        } else {
            List<FileMapping> sourceMappings = FileMapper.buildFileMappingList(conn, options.testMappingOptions);

            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_FILE_MAPPINGS, sourceMappings.toArray()));
        }

        if (options.includeObjects.isEmpty()) {
            callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
        } else {
            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.includeObjects.toArray()));
        }

        if (options.excludeObjects.isEmpty()) {
            callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_VARCHAR2_LIST);
        } else {
            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.excludeObjects.toArray()));
        }

        return paramIdx;
    }

    public void execute() throws SQLException {
        callableStatement.execute();
    }

    @Override
    public void close() throws SQLException {
        if (callableStatement != null) {
            callableStatement.close();
        }
    }
}
