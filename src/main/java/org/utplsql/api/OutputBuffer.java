package org.utplsql.api;

import java.io.PrintStream;
import java.sql.*;
import java.util.List;

public abstract class OutputBuffer implements SQLData {

    private String outputId;

    public OutputBuffer(String outputId) {
        this.outputId = outputId;
    }

    public String getOutputId() {
        return outputId;
    }

    private void setOutputId(String outputId) {
        this.outputId = outputId;
    }

    public abstract void printAvailable(Connection conn, PrintStream ps) throws SQLException;

    public abstract void printAvailable(Connection conn, List<PrintStream> printStreams) throws SQLException;

    public abstract void fetchAvailable(Connection conn, Callback cb) throws SQLException;

    public abstract List<String> fetchAll(Connection conn) throws SQLException;

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setOutputId(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getOutputId());
    }

    /**
     * Callback to be called when a new line is available from the output buffer.
     */
    public interface Callback {
        void onLineFetched(String s);
    }

}
