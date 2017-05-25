package io.github.utplsql.api;

import io.github.utplsql.api.reporter.Reporter;
import oracle.jdbc.OracleTypes;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches the lines produced by a reporter.
 */
public class OutputBuffer {

    private Reporter reporter;

    /**
     * Creates a new OutputBuffer.
     * @param reporter the reporter to be used
     */
    public OutputBuffer(Reporter reporter) {
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

    /**
     * Print the lines as soon as they are produced and call the callback passing the new line.
     * @param conn DB connection
     * @param cb the callback to be called
     * @throws SQLException any sql errors
     */
    public void fetchAvailable(Connection conn, Callback cb) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM table(ut_output_buffer.get_lines(?))");
            preparedStatement.setString(1, getReporter().getReporterId());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                cb.onLineFetched(resultSet.getString(1));
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    /**
     * Get all lines from output buffer and return it as a list of strings.
     * @param conn DB connection
     * @return the lines
     * @throws SQLException any sql errors
     */
    public List<String> fetchAll(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := ut_output_buffer.get_lines_cursor(?); END;");
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, getReporter().getReporterId());
            callableStatement.execute();

            resultSet = (ResultSet) callableStatement.getObject(1);

            List<String> outputLines = new ArrayList<>();
            while (resultSet.next()) {
                outputLines.add(resultSet.getString("text"));
            }
            return outputLines;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /**
     * Callback to be called when a new line is available from the output buffer.
     */
    public interface Callback {
        void onLineFetched(String s);
    }

}
