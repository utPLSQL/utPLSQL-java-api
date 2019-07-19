package org.utplsql.api.testRunner;

import org.utplsql.api.Version;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DynamicTestRunnerStatement implements TestRunnerStatement {

    private CallableStatement callableStatement;
    private final Connection connection;
    private final Version utPlSQlVersion;

    private DynamicTestRunnerStatement( Version utPlSQlVersion, Connection connection ) {
        this.utPlSQlVersion = utPlSQlVersion;
        this.connection = connection;
    }

    @Override
    public void execute() throws SQLException {
        // Implement
    }

    @Override
    public void close() throws SQLException {
        if (callableStatement != null) {
            callableStatement.close();
        }
    }

    public static DynamicTestRunnerStatement forVersion(Version version, Connection connection) {
        return new DynamicTestRunnerStatement(version, connection);
    }
}
