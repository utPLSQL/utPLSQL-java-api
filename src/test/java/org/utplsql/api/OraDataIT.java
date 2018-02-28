package org.utplsql.api;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ORAData;
import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.DefaultReporters;
import org.utplsql.api.reporter.DocumentationReporter;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.*;

public class OraDataIT extends AbstractDatabaseTest {

    @Test
    public void myTest2() throws SQLException {

        OracleConnection oraConn = getConnection().unwrap(OracleConnection.class);

        Reporter obj = new DocumentationReporter().init(oraConn);

        String runnerSql = "begin ut_runner.run( a_paths => ut_varchar2_list('app'), a_reporters => ?); end;";

        CallableStatement stmt = oraConn.prepareCall(runnerSql);

        ORAData[] reporters = new ORAData[]{ obj };

        stmt.setArray(
                1, oraConn.createOracleArray(CustomTypes.UT_REPORTERS, reporters));

        stmt.execute();


        OraclePreparedStatement stmt2 = (OraclePreparedStatement)oraConn.prepareStatement("select COLUMN_VALUE from table(?.get_lines())");
        stmt2.setORAData(1, obj);

        ResultSet resultSet = stmt2.executeQuery();

        while (resultSet.next())
            System.out.println(resultSet.getString(1));


    }

    /*
        begin
  declare
    x '||reporter_name||' := '||reporter_name||();'
  begin
    if x is of (ut_output_reporter_base)  then .... --- it an output
    elsif x is of (ut_output_reporter) then ---it's a reporter without output
    else
    --not a valid reproter
   end if;
exception
  when .... then
   --ut reporter object type is not a valid type or is not accessible
end;
         */

}
