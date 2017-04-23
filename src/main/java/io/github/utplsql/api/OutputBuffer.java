package io.github.utplsql.api;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class OutputBuffer {

    private String reporterId;

    public OutputBuffer(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public OutputBufferLines fetchAvailable(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := ut_output_buffer.get_available_lines(?); END;");

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, getReporterId());
            callableStatement.execute();

            resultSet = (ResultSet) callableStatement.getObject(1);

            OutputBufferLines outputLines = new OutputBufferLines();
            while (resultSet.next()) {
                outputLines.add(resultSet.getString("text"));
                if (resultSet.getInt("is_finished") == 1)
                    outputLines.setFinished(true);
            }
            return outputLines;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    public OutputBufferLines fetchAll(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := ut_output_buffer.get_lines_cursor(?); END;");

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, getReporterId());
            callableStatement.execute();

            resultSet = (ResultSet) callableStatement.getObject(1);

            OutputBufferLines outputLines = new OutputBufferLines();
            outputLines.setFinished(true);
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
