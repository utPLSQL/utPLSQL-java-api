package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;
import org.utplsql.api.DBHelper;
import org.utplsql.api.OutputBuffer;
import org.utplsql.api.TableOutputBuffer;

import java.sql.Connection;
import java.sql.SQLException;

public class OutputReporter extends Reporter {

    private OutputBuffer outputBuffer;

    public OutputReporter init(Connection conn) throws SQLException {
        return init(conn, new TableOutputBuffer(DBHelper.newSysGuid(conn)));
    }

    public OutputReporter init(Connection conn, OutputBuffer outputBuffer) throws SQLException {
        super.init(conn);
        setOutputBuffer(outputBuffer);
        return this;
    }

    public OutputBuffer getOutputBuffer() {
        return outputBuffer;
    }

    public void setOutputBuffer(OutputBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_OUTPUT_TABLE_BUFFER;
    }

}
