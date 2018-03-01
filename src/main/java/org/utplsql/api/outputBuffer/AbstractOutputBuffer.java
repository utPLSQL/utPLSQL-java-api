package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import org.utplsql.api.reporter.Reporter;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fetches the lines produced by a reporter.
 *
 * @author vinicius
 * @author pesse
 */
abstract class AbstractOutputBuffer implements OutputBuffer {

    private Reporter reporter;

    /**
     * Creates a new DefaultOutputBuffer.
     * @param reporter the reporter to be used
     */
    AbstractOutputBuffer(Reporter reporter) {

        assert reporter.isInit() : "Reporter is not initialized! You can only create OutputBuffers for initialized Reporters";

        this.reporter = reporter;
    }

    /**
     * Returns the reporter used by this buffer.
     * @return the reporter instance
     */
    public Reporter getReporter() {
        return reporter;
    }

    /**
     * Print the lines as soon as they are produced and write to a PrintStream.
     * @param conn DB connection
     * @param ps the PrintStream to be used, e.g: System.out
     * @throws SQLException any sql errors
     */
    public void printAvailable(Connection conn, PrintStream ps) throws SQLException {
        List<PrintStream> printStreams = new ArrayList<>(1);
        printStreams.add(ps);
        printAvailable(conn, printStreams);
    }

    /**
     * Print the lines as soon as they are produced and write to a list of PrintStreams.
     * @param conn DB connection
     * @param printStreams the PrintStream list to be used, e.g: System.out, new PrintStream(new FileOutputStream)
     * @throws SQLException any sql errors
     */
    public void printAvailable(Connection conn, List<PrintStream> printStreams) throws SQLException {
        fetchAvailable(conn, s -> {
            for (PrintStream ps : printStreams)
                ps.println(s);
        });
    }

}
