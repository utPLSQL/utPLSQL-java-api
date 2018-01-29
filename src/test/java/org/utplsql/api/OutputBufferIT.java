package org.utplsql.api;

import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.rules.DatabaseRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
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
public class OutputBufferIT {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    public Reporter createReporter() throws SQLException {
        Connection conn = db.newConnection();
        Reporter reporter = new DocumentationReporter().init(conn);
        System.out.println("Reporter ID: " + reporter.getReporterId());
        return reporter;
    }

    @Test
    public void printAvailableLines() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            final Reporter reporter = createReporter();

            Future<Object> task1 = executorService.submit(() -> {
                try {
                    Connection conn = db.newConnection();
                    new TestRunner()
                            .addPath(db.getUser())
                            .addReporter(reporter)
                            .run(conn);

                    return Boolean.TRUE;
                } catch (SQLException e) {
                    return e;
                }
            });

            Future<Object> task2 = executorService.submit(() -> {
                FileOutputStream fileOutStream = null;
                File outFile = new File("output.txt");
                try {
                    Connection conn = db.newConnection();
                    fileOutStream = new FileOutputStream(outFile);

                    List<PrintStream> printStreams = new ArrayList<>();
                    printStreams.add(System.out);
                    printStreams.add(new PrintStream(fileOutStream));

                    new OutputBuffer(reporter)
                        .printAvailable(conn, printStreams);

                    return Boolean.TRUE;
                } catch (SQLException e) {
                    return e;
                } finally {
                    if (fileOutStream != null) {
                        fileOutStream.close();
                        outFile.delete();
                    }
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
            final Reporter reporter = createReporter();
            Connection conn = db.newConnection();
            new TestRunner()
                    .addPath(db.getUser())
                    .addReporter(reporter)
                    .run(conn);

            List<String> outputLines = new OutputBuffer(reporter)
                    .fetchAll(conn);

            Assert.assertTrue(outputLines.size() > 0);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
