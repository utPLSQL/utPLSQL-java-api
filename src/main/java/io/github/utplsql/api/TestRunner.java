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

    public void run(Connection conn, String path, BaseReporter reporter) throws SQLException {
        validateReporter(conn, reporter);
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ut_runner.run(a_path => :path, a_reporter => :reporter); END;");
            callableStatement.setString(":path", path);
            callableStatement.setObject(":reporter", reporter);
            callableStatement.execute();
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /**
     * Check if the reporter was initialized, if not call reporter.init.
     * @param conn the database connection
     * @param reporter the reporter
     * @throws SQLException any sql exception
     */
    private void validateReporter(Connection conn, BaseReporter reporter) throws SQLException {
        if (reporter.getReporterId() == null || reporter.getReporterId().isEmpty())
            reporter.init(conn);
    }

}
