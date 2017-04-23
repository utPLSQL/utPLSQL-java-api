package io.github.utplsql.api;

import io.github.utplsql.api.types.BaseReporter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 */
public class TestRunner {

    public TestRunner() {}

    public void run(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ut_runner.run(); END;");
            callableStatement.execute();
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    public void run(Connection conn, String path, BaseReporter reporter) throws SQLException {
        if (reporter.getReporterId() == null || reporter.getReporterId().isEmpty()) {
            reporter.setReporterId(utPLSQL.newSysGuid(conn));
        }

        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ut_runner.run(:path, :reporter); END;");
            callableStatement.setString(":path", path);
            callableStatement.setObject(":reporter", reporter);
            callableStatement.execute();
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

}
