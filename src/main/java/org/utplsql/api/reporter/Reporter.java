package org.utplsql.api.reporter;

import java.sql.*;

/**
 * Created by Vinicius on 13/04/2017.
 */
public abstract class Reporter implements SQLData {

    private String selfType;
    private String reporterId;

    public Reporter() {}

    public Reporter init(Connection conn) throws SQLException {
        setSelfType(getSQLTypeName());
        setReporterId(null);
        return this;
    }

    public String getSelfType() {
        return this.selfType;
    }

    private void setSelfType(String selfType) {
        this.selfType = selfType;
    }

    public String getReporterId() {
        return this.reporterId;
    }

    private void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setSelfType(stream.readString());
        setReporterId(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getSelfType());
        stream.writeString(getReporterId());
    }

}
