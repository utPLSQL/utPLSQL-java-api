package io.github.utplsql.api;

import io.github.utplsql.api.rules.DatabaseRule;
import io.github.utplsql.api.types.BaseReporter;
import io.github.utplsql.api.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class TestRunnerTest {

    @Rule
    public static final DatabaseRule db = new DatabaseRule();

    @Test
    public void runWithDocumentationReporter() {
        try {
            Connection conn = db.newConnection();
            BaseReporter reporter = new DocumentationReporter();
            new TestRunner().run(conn, "", reporter);
            Assert.assertNotNull(reporter.getReporterId());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
