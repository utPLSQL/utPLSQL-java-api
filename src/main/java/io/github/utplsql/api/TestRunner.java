package io.github.utplsql.api;

import io.github.utplsql.api.types.BaseReporter;
import io.github.utplsql.api.types.CustomTypes;
import oracle.jdbc.OracleConnection;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    public void run(Connection conn, List<String> pathList, List<BaseReporter> reporterList) throws SQLException {
        for (BaseReporter r : reporterList)
            validateReporter(conn, r);

        OracleConnection oraConn = conn.unwrap(OracleConnection.class);
        Array pathArray = oraConn.createARRAY(CustomTypes.UT_VARCHAR2_LIST, pathList.toArray());
        Array reporterArray = oraConn.createARRAY(CustomTypes.UT_REPORTERS, reporterList.toArray());

        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ut_runner.run(a_paths => ?, a_reporters => ?); END;");
            callableStatement.setArray(1, pathArray);
            callableStatement.setArray(2, reporterArray);
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
