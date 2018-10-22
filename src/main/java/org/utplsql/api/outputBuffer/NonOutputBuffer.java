package org.utplsql.api.outputBuffer;

import org.utplsql.api.reporter.Reporter;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** An OutputBuffer replacement which just returns nothing at all. Suitable for Reporters without any output
 *
 * @author pesse
 */
class NonOutputBuffer implements OutputBuffer {

    private final Reporter reporter;

    NonOutputBuffer( Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public Reporter getReporter() {
        return reporter;
    }

    @Override
    public OutputBuffer setFetchSize(int fetchSize) {
        return this;
    }

    @Override
    public void printAvailable(Connection conn, PrintStream ps) throws SQLException {
        List<PrintStream> printStreams = new ArrayList<>(1);
        printStreams.add(ps);
        printAvailable(conn, printStreams);
    }

    @Override
    public void printAvailable(Connection conn, List<PrintStream> printStreams) throws SQLException {
        fetchAvailable(conn, s -> {
            for (PrintStream ps : printStreams)
                ps.println(s);
        });
    }

    @Override
    public void fetchAvailable(Connection conn, Consumer<String> onLineFetched) {
        onLineFetched.accept(null);
    }

    @Override
    public List<String> fetchAll(Connection conn) {
        return new ArrayList<>();
    }
}
