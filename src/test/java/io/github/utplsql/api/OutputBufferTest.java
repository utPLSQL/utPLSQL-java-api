package io.github.utplsql.api;

import io.github.utplsql.api.rules.DatabaseRule;
import io.github.utplsql.api.types.BaseReporter;
import io.github.utplsql.api.types.DocumentationReporter;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class OutputBufferTest {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    public BaseReporter createReporter() throws SQLException {
        Connection conn = db.newConnection();
        BaseReporter reporter = new DocumentationReporter().init(conn);
        System.out.println("Reporter ID: " + reporter.getReporterId());
        return reporter;
    }

    @Test
    public void printAvailableLines() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            final BaseReporter reporter = createReporter();

            Future<Object> task1 = executorService.submit(() -> {
                try {
                    Connection conn = db.newConnection();
                    new TestRunner().run(conn, "", reporter);

                    return Boolean.TRUE;
                } catch (SQLException e) {
                    return e;
                }
            });

            Future<Object> task2 = executorService.submit(() -> {
                FileOutputStream fileOutStream = null;
                try {
                    Connection conn = db.newConnection();
                    fileOutStream = new FileOutputStream("output.txt");

                    List<PrintStream> printStreams = new ArrayList<>();
                    printStreams.add(System.out);
                    printStreams.add(new PrintStream(fileOutStream));

                    new OutputBuffer(reporter)
                        .printAvailable(conn, printStreams);

                    return Boolean.TRUE;
                } catch (SQLException e) {
                    return e;
                } finally {
                    if (fileOutStream != null)
                        fileOutStream.close();
                }
            });

            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.MINUTES);

            Object res1 = task1.get();
            Object res2 = task2.get();

            if (res1 instanceof Exception)
                Assert.fail(((Exception) res1).getMessage());

            if (res2 instanceof Exception)
                Assert.fail(((Exception) res2).getMessage());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchAllLines() {
        try {
            final BaseReporter reporter = createReporter();
            Connection conn = db.newConnection();
            new TestRunner().run(conn, "", reporter);

            List<String> outputLines = new OutputBuffer(reporter)
                    .fetchAll(conn);

            Assert.assertTrue(outputLines.size() > 0);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
