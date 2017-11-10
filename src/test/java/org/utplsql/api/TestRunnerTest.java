package org.utplsql.api;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.utplsql.api.exception.SomeTestsFailedException;
import org.utplsql.api.reporter.*;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class TestRunnerTest {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void runWithDefaultParameters() {
        try {
            Connection conn = db.newConnection();
            new TestRunner().run(conn);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void runWithoutCompatibilityCheck() {
        try {
            Connection conn = db.newConnection();
            new TestRunner()
                    .skipCompatibilityCheck(true)
                    .run(conn);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void runWithManyReporters() {
        try {
            Connection conn = db.newConnection();
            new TestRunner()
                    .addPath(db.getUser())
                    .addReporter(new DocumentationReporter().init(conn))
                    .addReporter(new CoverageHTMLReporter().init(conn))
                    .addReporter(new CoverageSonarReporter().init(conn))
                    .addReporter(new CoverallsReporter().init(conn))
                    .addReporter(new SonarTestReporter().init(conn))
                    .addReporter(new TeamCityReporter().init(conn))
                    .addReporter(new XUnitReporter().init(conn))
                    .run(conn);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void failOnErrors() {
        try {
            Connection conn = db.newConnection();
            new TestRunner()
                    .failOnErrors(true)
                    .run(conn);
            Assert.fail();
        } catch (SomeTestsFailedException ignored) {
            System.out.println("Expected exception object thrown.");
        } catch (SQLException e) {
            Assert.fail("Wrong exception object thrown.");
        }
    }

}
