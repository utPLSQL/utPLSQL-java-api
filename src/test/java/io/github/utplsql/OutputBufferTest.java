package io.github.utplsql;

import io.github.utplsql.rules.DatabaseRule;
import io.github.utplsql.types.BaseReporter;
import io.github.utplsql.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class OutputBufferTest {

    @ClassRule
    public static final DatabaseRule sInitialization = new DatabaseRule();

    @Test
    public void getLinesFromOutputBuffer() {
        try {
            Connection conn = UTPLSQL.getConnection();
            conn.setAutoCommit(false);

            BaseReporter reporter = new DocumentationReporter();
            reporter.setReporterId(UTPLSQL.newSysGuid());
            TestRunner.run("", reporter);

            List<String> outputLines = OutputBuffer.getAllLines(reporter.getReporterId());
            for (int i = 0; i < outputLines.size(); i++) {
                System.out.println(outputLines.get(0));
            }

            conn.commit();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
