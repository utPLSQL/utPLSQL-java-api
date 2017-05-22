package io.github.utplsql.api;

import io.github.utplsql.api.rules.DatabaseRule;
import io.github.utplsql.api.reporter.Reporter;
import io.github.utplsql.api.reporter.CoverageHTMLReporter;
import io.github.utplsql.api.reporter.DocumentationReporter;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class TestRunnerTest {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void runWithDocumentationReporter() {
        try {
            Connection conn = db.newConnection();
            Reporter reporter = new DocumentationReporter();
            new TestRunner().run(conn, db.getUser(), reporter);
            Assert.assertNotNull(reporter.getReporterId());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void runWithTwoReporters() {
        try {
            Connection conn = db.newConnection();

            List<String> pathList = new ArrayList<>();
            pathList.add(db.getUser());

            List<Reporter> reporterList = new ArrayList<>();
            reporterList.add(new DocumentationReporter().init(conn));
            reporterList.add(new CoverageHTMLReporter().init(conn));

            new TestRunner().run(conn, pathList, reporterList);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
