package org.utplsql.api;

import oracle.jdbc.OracleTypes;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TableOutputBuffer extends OutputBuffer {

    private java.sql.Date startDate;

    public TableOutputBuffer(String outputId) {
        super(outputId);
        setStartDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
    }

    public Date getStartDate() {
        return startDate;
    }

    private void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_OUTPUT_TABLE_BUFFER;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        super.readSQL(stream, typeName);
        setStartDate(stream.readDate());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        super.writeSQL(stream);
        stream.writeDate(getStartDate());
    }

    /**
     * Print the lines as soon as they are produced and write to a PrintStream.
     * @param conn DB connection
     * @param ps the PrintStream to be used, e.g: System.out
     * @throws SQLException any sql errors
     */
    public void printAvailable(Connection conn, PrintStream ps) throws SQLException {
        List<PrintStream> printStreams = new ArrayList<>(1);
        printStreams.add(ps);
        printAvailable(conn, printStreams);
    }

    /**
     * Print the lines as soon as they are produced and write to a list of PrintStreams.
     * @param conn DB connection
     * @param printStreams the PrintStream list to be used, e.g: System.out, new PrintStream(new FileOutputStream)
     * @throws SQLException any sql errors
     */
    public void printAvailable(Connection conn, List<PrintStream> printStreams) throws SQLException {
        fetchAvailable(conn, s -> {
            for (PrintStream ps : printStreams)
                ps.println(s);
        });
    }

    /**
     * Print the lines as soon as they are produced and call the callback passing the new line.
     * @param conn DB connection
     * @param cb the callback to be called
     * @throws SQLException any sql errors
     */
    public void fetchAvailable(Connection conn, OutputBuffer.Callback cb) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM table(ut_output_table_buffer(?).get_lines())");
            preparedStatement.setString(1, getOutputId());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                cb.onLineFetched(resultSet.getString(1));
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    /**
     * Get all lines from output buffer and return it as a list of strings.
     * @param conn DB connection
     * @return the lines
     * @throws SQLException any sql errors
     */
    public List<String> fetchAll(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := ut_output_table_buffer(?).get_lines_cursor(); END;");
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, getOutputId());
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
