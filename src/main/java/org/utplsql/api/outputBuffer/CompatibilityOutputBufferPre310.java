package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleTypes;
import org.utplsql.api.reporter.Reporter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Compatibility Output-Buffer for 3.0.0 - 3.0.4
 *
 * @author pesse
 */
class CompatibilityOutputBufferPre310 extends AbstractOutputBuffer {

    CompatibilityOutputBufferPre310(Reporter reporter) {
        super(reporter);
    }

    @Override
    protected CallableStatement getLinesCursorStatement(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("BEGIN ? := ut_output_buffer.get_lines_cursor(?); END;");
        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.setString(2, getReporter().getId());
        return cstmt;
    }
}
