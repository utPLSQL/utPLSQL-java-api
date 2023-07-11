package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.reporter.Reporter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class OutputBufferProvider {

    private OutputBufferProvider() {
    }

    /**
     * Returns an OutputBuffer compatible with the given databaseVersion
     * If we are at 3.1.0 or greater, returns an OutputBuffer based upon the information whether the Reporter has Output or not
     *
     * @param databaseVersion {@link Version}
     * @param reporter {@link Reporter}
     * @param conn {@link Connection}
     * @return OutputBuffer
     * @throws SQLException if there are problems with the database access
     */
    public static OutputBuffer getCompatibleOutputBuffer(Version databaseVersion, Reporter reporter, Connection conn) throws SQLException {
        OracleConnection oraConn = conn.unwrap(OracleConnection.class);

        try {
            if (databaseVersion.isGreaterOrEqualThan(Version.V3_1_0)) {
                if (hasOutput(reporter, oraConn)) {
                    return new DefaultOutputBuffer(reporter);
                } else {
                    return new NonOutputBuffer(reporter);
                }
            }
        } catch (InvalidVersionException ignored) {
        }

        // If we couldn't find an appropriate OutputBuffer, return the Pre310-Compatibility-Buffer
        return new CompatibilityOutputBufferPre310(reporter);
    }

    private static boolean hasOutput(Reporter reporter, OracleConnection oraConn) throws SQLException {

        String sql =
                "declare " +
                        "   l_result int;" +
                        "begin " +
                        "   begin " +
                        "       execute immediate '" +
                        "       begin " +
                        "           :x := case ' || dbms_assert.simple_sql_name( ? ) || '() is of (ut_output_reporter_base) when true then 1 else 0 end;" +
                        "       end;'" +
                        "       using out l_result;" +
                        "   end;" +
                        "   ? := l_result;" +
                        "end;";

        try (CallableStatement stmt = oraConn.prepareCall(sql)) {
            stmt.setQueryTimeout(3);
            stmt.setString(1, reporter.getTypeName());
            stmt.registerOutParameter(2, OracleTypes.INTEGER);

            stmt.execute();
            int result = stmt.getInt(2);
            return result == 1;
        }
    }
}
