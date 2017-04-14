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
                    .prepareCall("BEGIN ? := ut_output_buffer.get_lines_cursor(?); END;");

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, reporterId);
            callableStatement.execute();

            resultSet = (ResultSet) callableStatement.getObject(1);

            List<String> outputLines = new ArrayList<>();
            while (resultSet.next()) {
                outputLines.add(resultSet.getString("text"));
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
