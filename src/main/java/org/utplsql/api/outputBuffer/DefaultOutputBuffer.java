package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleCallableStatement;
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
        Reporter reporter = getReporter();
        String plsql = "declare" +
                "  l_rep " + reporter.getTypeName() + " := " + reporter.getTypeName() + "(); " +
                "begin" +
                "  l_rep.set_reporter_id(:reporter_id); " +
                "  :lines_cursor := l_rep.get_lines_cursor(); " +
                "end;";
        OracleCallableStatement cstmt = (OracleCallableStatement) conn.prepareCall(plsql);
        cstmt.setString("reporter_id", reporter.getId());
        cstmt.registerOutParameter("lines_cursor", OracleTypes.CURSOR);
        return cstmt;
    }
}
