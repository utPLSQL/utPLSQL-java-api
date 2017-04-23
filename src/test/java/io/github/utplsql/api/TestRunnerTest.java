package io.github.utplsql.api;

import io.github.utplsql.api.rules.DatabaseRule;
import io.github.utplsql.api.types.BaseReporter;
import io.github.utplsql.api.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class TestRunnerTest {

    @ClassRule
    public static final DatabaseRule sInitialization = new DatabaseRule();

    @Test
    public void runWithoutParams() {
        Connection conn = null;
        try {
            conn = utPLSQL.getConnection();
            new TestRunner().run(conn);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (conn != null)
                try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    @Test
    public void runWithDocumentationReporter() {
        Connection conn = null;
        try {
            conn = utPLSQL.getConnection();
            BaseReporter reporter = new DocumentationReporter();
            new TestRunner().run(conn, "", reporter);
            Assert.assertNotNull(reporter.getReporterId());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (conn != null)
                try { conn.close(); } catch (SQLException ignored) {}
        }
    }

}
