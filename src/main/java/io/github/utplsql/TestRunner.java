package io.github.utplsql;

import io.github.utplsql.types.BaseReporter;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 */
public final class TestRunner {

    private TestRunner() {}

    public static void run() throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = UTPLSQL.getConnection()
                    .prepareCall("BEGIN ut_runner.run(); END;");
            callableStatement.execute();
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    public static void run(String path, BaseReporter reporter) throws SQLException {
        if (reporter.getReporterId() == null || reporter.getReporterId().isEmpty()) {
            reporter.setReporterId(UTPLSQL.newSysGuid());
        }

        CallableStatement callableStatement = null;
        try {
            callableStatement = UTPLSQL.getConnection()
                    .prepareCall("BEGIN ut_runner.run(:path, :reporter); END;");
            callableStatement.setString(":path", path);
            callableStatement.setObject(":reporter", reporter);
            callableStatement.execute();
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

}
