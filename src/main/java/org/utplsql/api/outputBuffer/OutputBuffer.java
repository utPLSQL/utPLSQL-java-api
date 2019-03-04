package org.utplsql.api.outputBuffer;

import org.utplsql.api.reporter.Reporter;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public interface OutputBuffer {

    Reporter getReporter();

    /**
     * Override the fetchSize of the OutputBuffer
     *
     * @param fetchSize the ResultSet fetch-size.
     * @return this Output-Buffer
     */
    OutputBuffer setFetchSize(int fetchSize);

    /**
     * Print the lines as soon as they are produced and write to a PrintStream.
     *
     * @param conn DB connection
     * @param ps   the PrintStream to be used, e.g: System.out
     * @throws SQLException any sql errors
     */
    void printAvailable(Connection conn, PrintStream ps) throws SQLException;

    /**
     * Print the lines as soon as they are produced and write to a list of PrintStreams.
     *
     * @param conn         DB connection
     * @param printStreams the PrintStream list to be used, e.g: System.out, new PrintStream(new FileOutputStream)
     * @throws SQLException any sql errors
     */
    void printAvailable(Connection conn, List<PrintStream> printStreams) throws SQLException;

    /**
     * Print the lines as soon as they are produced and call the callback passing the new line.
     *
     * @param conn          DB connection
     * @param onLineFetched the callback to be called
     * @throws SQLException any sql errors
     */
    void fetchAvailable(Connection conn, Consumer<String> onLineFetched) throws SQLException;

    /**
     * Get all lines from output buffer and return it as a list of strings.
     *
     * @param conn DB connection
     * @return the lines
     * @throws SQLException any sql errors
     */
    List<String> fetchAll(Connection conn) throws SQLException;

}
