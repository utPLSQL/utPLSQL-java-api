package io.github.utplsql.api;

import io.github.utplsql.api.rules.DatabaseRule;
import io.github.utplsql.api.types.BaseReporter;
import io.github.utplsql.api.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class OutputBufferTest {

    @ClassRule
    public static final DatabaseRule sInitialization = new DatabaseRule();

    public BaseReporter createReporter() throws SQLException {
        Connection conn = null;
        try {
            conn = utPLSQL.getConnection();
            BaseReporter reporter = new DocumentationReporter();
            reporter.setReporterId(utPLSQL.newSysGuid(conn));
            System.out.println("Reporter ID: " + reporter.getReporterId());
            return reporter;
        } finally {
            if (conn != null)
                conn.close();
        }
    }

    @Test
    public void getLinesFromOutputBuffer() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            final BaseReporter reporter = createReporter();

            executorService.submit(() -> {
                Connection conn = null;
                try {
                    conn = utPLSQL.getConnection();
                    new TestRunner().run(conn, "", reporter);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Assert.fail(e.getMessage());
                } finally {
                    if (conn != null)
                        try { conn.close(); } catch (SQLException ignored) {}
                }
            });

            executorService.submit(() -> {
                Connection conn = null;
                try {
                    conn = utPLSQL.getConnection();
                    OutputBufferLines outputLines;
                    do {
                        outputLines = new OutputBuffer(reporter.getReporterId())
                                .fetchAvailable(conn);

                        Thread.sleep(1000);

                        if (outputLines.getLines().size() > 0)
                            System.out.println(outputLines.toString());
                    } while (!outputLines.isFinished());
                } catch (SQLException e) {
                    e.printStackTrace();
                    Assert.fail(e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null)
                        try { conn.close(); } catch (SQLException ignored) {}
                }
            });

            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.MINUTES);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllLinesFromOutputBuffer() {
        Connection conn = null;
        try {
            final BaseReporter reporter = createReporter();
            conn = utPLSQL.getConnection();
            new TestRunner().run(conn, "", reporter);

            OutputBufferLines outputLines = new OutputBuffer(reporter.getReporterId())
                    .fetchAll(conn);

            System.out.println(outputLines.toString());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (conn != null)
                try { conn.close(); } catch (SQLException ignored) {}
        }
    }

}
