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
           if (new Version("3.0.4.1610").isGreaterOrEqualThan(databaseVersion)) {
               if ( hasOutput(reporter, oraConn) ) {
                   return new DefaultOutputBuffer(reporter);
               }
               else {
                   return new NonOutputBuffer(reporter);
               }
           }
       }
       catch ( InvalidVersionException e ) { }

       // If we couldn't find an appropriate OutputBuffer, return the Pre310-Compatibility-Buffer
       return new CompatibilityOutputBufferPre310(reporter);
    }

    private static boolean hasOutput( Reporter reporter, OracleConnection oraConn ) throws SQLException {

        try ( PreparedStatement stmt = oraConn.prepareStatement("select ut_runner.is_output_reporter(?) from dual")) {
            stmt.setString(1, reporter.getTypeName());

            try ( ResultSet rs = stmt.executeQuery() ) {
                if ( rs.next() ) {
                    String isReporterResult = rs.getString(1);

                    if ( isReporterResult == null )
                        throw new IllegalArgumentException("The given type " + reporter.getTypeName() + " is not a valid Reporter!");
                    else if (isReporterResult.equalsIgnoreCase("Y") )
                        return true;
                    else
                        return false;
                }
                else
                    throw new SQLException("Could not check Reporter validity");
            }
        }
    }
}
