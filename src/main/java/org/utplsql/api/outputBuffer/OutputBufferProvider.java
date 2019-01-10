package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleConnection;
import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.reporter.Reporter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OutputBufferProvider {

    /** Returns an OutputBuffer compatible with the given databaseVersion
     * If we are at 3.1.0 or greater, returns an OutputBuffer based upon the information whether the Reporter has Output or not
     *
     * @param databaseVersion
     * @param reporter
     * @param conn
     * @return
     * @throws SQLException
     */
    public static OutputBuffer getCompatibleOutputBuffer(Version databaseVersion, Reporter reporter, Connection conn ) throws SQLException {
       OracleConnection oraConn = conn.unwrap(OracleConnection.class);

       try {
           if (databaseVersion.isGreaterOrEqualThan(new Version("3.1.0"))) {
               if ( hasOutput(reporter, oraConn) ) {
                   return new DefaultOutputBuffer(reporter);
               }
               else {
                   return new NonOutputBuffer(reporter);
               }
           }
       }
       catch ( InvalidVersionException ignored ) { }

       // If we couldn't find an appropriate OutputBuffer, return the Pre310-Compatibility-Buffer
       return new CompatibilityOutputBufferPre310(reporter);
    }

    private static boolean hasOutput( Reporter reporter, OracleConnection oraConn ) throws SQLException {

        String sql = "select is_output_reporter " +
                " from table(ut_runner.get_reporters_list)" +
                " where ? = substr(reporter_object_name, length(reporter_object_name)-?+1)";
        try ( PreparedStatement stmt = oraConn.prepareStatement(sql)) {
            stmt.setQueryTimeout(3);
            stmt.setString(1, reporter.getTypeName());
            stmt.setInt(2, reporter.getTypeName().length());

            try ( ResultSet rs = stmt.executeQuery() ) {
                if ( rs.next() ) {
                    String isReporterResult = rs.getString(1);

                    if ( isReporterResult == null )
                        throw new IllegalArgumentException("The given type " + reporter.getTypeName() + " is not a valid Reporter!");
                    else
                        return isReporterResult.equalsIgnoreCase("Y");
                }
                else
                    throw new SQLException("Could not check Reporter validity");
            }
        }
    }

    private OutputBufferProvider() {
    }
}
