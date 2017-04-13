package io.github.utplsql;

import io.github.utplsql.rules.DatabaseRule;
import io.github.utplsql.types.BaseReporter;
import io.github.utplsql.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class TestRunnerTest {

    @ClassRule
    public static final DatabaseRule sInitialization = new DatabaseRule();

    @Test
    public void runWithoutParams() {
        try {
            TestRunner.run();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void runWithDocumentationReporter() {
        try {
            BaseReporter reporter = new DocumentationReporter();
            TestRunner.run("", reporter);
            Assert.assertNotNull(reporter.getReporterId());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
