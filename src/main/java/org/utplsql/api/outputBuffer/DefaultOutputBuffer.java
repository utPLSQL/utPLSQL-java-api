package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import org.utplsql.api.reporter.Reporter;
import oracle.jdbc.OracleTypes;

import javax.xml.transform.Result;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fetches the lines produced by a reporter.
 *
 * @author vinicius
 * @author pesse
 */
class DefaultOutputBuffer extends AbstractOutputBuffer {

    /**
     * Creates a new DefaultOutputBuffer.
     * @param reporter the reporter to be used
     */
    DefaultOutputBuffer(Reporter reporter) {
        super(reporter);
    }

    /**
     * Print the lines as soon as they are produced and call the callback passing the new line.
     * @param conn DB connection
     * @param onLineFetched the callback to be called
     * @throws SQLException any sql errors
     */
    public void fetchAvailable(Connection conn, Consumer<String> onLineFetched) throws SQLException {

        OracleConnection oraConn = conn.unwrap(OracleConnection.class);

        try (OraclePreparedStatement pstmt = (OraclePreparedStatement)oraConn.prepareStatement("select * from table(?.get_lines())")) {

            pstmt.setORAData(1, getReporter());
            try (ResultSet resultSet = pstmt.executeQuery() ) {
                while (resultSet.next())
                    onLineFetched.accept(resultSet.getString(1));
            }
        }
    }

    /**
     * Get all lines from output buffer and return it as a list of strings.
     * @param conn DB connection
     * @return the lines
     * @throws SQLException any sql errors
     */
    public List<String> fetchAll(Connection conn) throws SQLException {

        OracleConnection oraConn = conn.unwrap(OracleConnection.class);

        try (OracleCallableStatement cstmt = (OracleCallableStatement)oraConn.prepareCall("{? = call ?.get_lines_cursor() }")) {

            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setORAData(2, getReporter());

            cstmt.execute();

            try ( ResultSet resultSet = (ResultSet) cstmt.getObject(1)) {

                List<String> outputLines = new ArrayList<>();
                while (resultSet.next()) {
                    outputLines.add(resultSet.getString("text"));
                }
                return outputLines;
            }
        }
    }

}
