package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.Reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integration-test for OutputBuffers
 *
 * @author viniciusam
 * @author pesse
 */
public class OutputBufferIT extends AbstractDatabaseTest {

    public Reporter createReporter() throws SQLException {
        Reporter reporter = new DocumentationReporter().init(newConnection());
        System.out.println("Reporter ID: " + reporter.getId());
        return reporter;
    }

    @Test
    public void printAvailableLines() throws SQLException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            final Reporter reporter = createReporter();

            Future<Object> task1 = executorService.submit(() -> {
                try {
                    new TestRunner()
                            .addPath(getUser())
                            .addReporter(reporter)
                            .run(getConnection());

                    return Boolean.TRUE;
                } catch (SQLException e) {
                    return e;
                }
            });

            Future<Object> task2 = executorService.submit(() -> {
                FileOutputStream fileOutStream = null;
                File outFile = new File("output.txt");
                try {
                    fileOutStream = new FileOutputStream(outFile);

                    List<PrintStream> printStreams = new ArrayList<>();
                    printStreams.add(System.out);
                    printStreams.add(new PrintStream(fileOutStream));

                    reporter.getOutputBuffer().printAvailable(newConnection(), printStreams);

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
                fail((Exception) res1);

            if (res2 instanceof Exception)
                fail((Exception) res2);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchAllLines() throws SQLException {
        final Reporter reporter = createReporter();
        new TestRunner()
                .addPath(getUser())
                .addReporter(reporter)
                .run(getConnection());

        List<String> outputLines = reporter.getOutputBuffer().fetchAll(getConnection());

        assertTrue(outputLines.size() > 0);
    }

}
