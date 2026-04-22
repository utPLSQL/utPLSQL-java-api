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
        String plsql = "DECLARE" +
                "  l_rep " + reporter.getTypeName() + "; " +
                "BEGIN" +
                "  l_rep := treat(:2 as " + reporter.getTypeName() + ");" +
                "  :1 := l_rep.get_lines_cursor(); " +
                "END;";
        OracleCallableStatement cstmt = (OracleCallableStatement) conn.prepareCall(plsql);
        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.setORAData(2, reporter);
        return cstmt;
    }
}
