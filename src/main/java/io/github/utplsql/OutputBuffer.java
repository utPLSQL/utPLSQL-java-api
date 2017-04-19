package io.github.utplsql;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getLines() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = UTPLSQL.getConnection()
                    .prepareStatement("SELECT * FROM TABLE(ut_output_buffer.get_lines(?, 1))");

            preparedStatement.setString(1, getReporterId());
            resultSet = preparedStatement.executeQuery();

            List<String> outputLines = new ArrayList<>();
            while (resultSet.next()) {
                outputLines.add(resultSet.getString(1));
            }
            return outputLines;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    public List<String> getAllLines() throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            callableStatement = UTPLSQL.getConnection()
                    .prepareCall("BEGIN ? := ut_output_buffer.get_lines_cursor(?); END;");

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, getReporterId());
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
