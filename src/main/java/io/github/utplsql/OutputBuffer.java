package io.github.utplsql;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 13/04/2017.
 */
public final class OutputBuffer {

    private OutputBuffer() {}

    public static List<String> getAllLines(String reporterId) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            callableStatement = UTPLSQL.getConnection()
                    .prepareCall("BEGIN :lines := ut_output_buffer.get_lines_cursor(:reporter_id); END;");

            callableStatement.registerOutParameter(":lines", OracleTypes.CURSOR);
            callableStatement.setString(":reporter_id", reporterId);
            callableStatement.execute();

            resultSet = (ResultSet) callableStatement.getObject(":lines");

            List<String> outputLines = new ArrayList<>();
            while (resultSet.next()) {
                outputLines.add(resultSet.getString(0));
            }
            return outputLines;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (callableStatement != null)
                callableStatement.close();
        }
    }

}
