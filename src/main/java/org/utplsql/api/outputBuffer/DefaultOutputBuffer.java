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

    @Override
    protected PreparedStatement getLinesStatement(Connection conn) throws SQLException {
        OracleConnection oraConn = conn.unwrap(OracleConnection.class);
        OraclePreparedStatement pstmt = (OraclePreparedStatement)oraConn.prepareStatement("select * from table(?.get_lines())");
        pstmt.setORAData(1, getReporter());
        return pstmt;
    }

    @Override
    protected CallableStatement getLinesCursorStatement(Connection conn) throws SQLException {
        OracleConnection oraConn = conn.unwrap(OracleConnection.class);
        OracleCallableStatement cstmt = (OracleCallableStatement) oraConn.prepareCall("{? = call ?.get_lines_cursor() }");
        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.setORAData(2, getReporter());
        return cstmt;
    }
}
