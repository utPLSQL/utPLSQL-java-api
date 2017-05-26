package io.github.utplsql.api;

import io.github.utplsql.api.reporter.*;
import io.github.utplsql.api.rules.DatabaseRule;
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
    public void runWithManyReporters() {
        try {
            Connection conn = db.newConnection();
            new TestRunner()
                    .addPath("ut3")
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

}
