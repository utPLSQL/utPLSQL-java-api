package io.github.utplsql;

import io.github.utplsql.rules.DatabaseRule;
import io.github.utplsql.types.BaseReporter;
import io.github.utplsql.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class OutputBufferTest {

    @ClassRule
    public static final DatabaseRule sInitialization = new DatabaseRule();

    private BaseReporter currentReporter = null;

    public BaseReporter createReporter() throws SQLException {
        BaseReporter reporter = new DocumentationReporter();
        reporter.setReporterId(UTPLSQL.newSysGuid());
        System.out.println("Reporter ID: " + reporter.getReporterId());
        return reporter;
    }

    @Test
    public void getLinesFromOutputBuffer() {
        try {
            final BaseReporter reporter = createReporter();
//            new TestRunner().run("", reporter);
            new Thread(() -> {
                try {
                    new TestRunner().run("", reporter);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();

            List<String> outputLines = new OutputBuffer(reporter.getReporterId())
                    .getLines();

            for (int i = 0; i < outputLines.size(); i++) {
                System.out.println(outputLines.get(i));
            }
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getAllLinesFromOutputBuffer() {
        try {
            final BaseReporter reporter = createReporter();
            new TestRunner().run("", reporter);

            List<String> outputLines = new OutputBuffer(reporter.getReporterId())
                    .getAllLines();

            for (int i = 0; i < outputLines.size(); i++) {
                System.out.println(outputLines.get(i));
            }
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
