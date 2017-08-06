package io.github.utplsql.api.reporter;

import io.github.utplsql.api.DBHelper;

import java.sql.*;
import java.util.Calendar;

/**
 * Created by Vinicius on 13/04/2017.
 */
public abstract class Reporter implements SQLData {

    private String selfType;
    private String reporterId;
    private java.sql.Date startDate;

    public Reporter() {}

    public Reporter init(Connection conn) throws SQLException {
        setSelfType(getSQLTypeName());
        setStartDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        setReporterId(DBHelper.newSysGuid(conn));
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

    public java.sql.Date getStartDate() {
        return this.startDate;
    }

    private void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setSelfType(stream.readString());
        setReporterId(stream.readString());
        setStartDate(stream.readDate());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getSelfType());
        stream.writeString(getReporterId());
        stream.writeDate(getStartDate());
    }

}
