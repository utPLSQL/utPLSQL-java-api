package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.utplsql.api.reporter.Reporter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Fetches the lines produced by a reporter.
 *
 * @author vinicius
 * @author pesse
 */
class DefaultOutputBuffer extends AbstractOutputBuffer {

    /**
     * Creates a new DefaultOutputBuffer.
     *
     * @param reporter the reporter to be used
     */
    DefaultOutputBuffer(Reporter reporter) {
        super(reporter);
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
