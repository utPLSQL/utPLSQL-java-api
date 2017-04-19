package io.github.utplsql.types;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.Calendar;

/**
 * Created by Vinicius on 13/04/2017.
 */
public abstract class BaseReporter implements SQLData {

    private String selfType;
    private String reporterId;
    private java.sql.Date startDate;

    public BaseReporter() {
        startDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    }

    public String getSelfType() {
        return this.selfType;
    }

    public void setSelfType(String selfType) {
        this.selfType = selfType;
    }

    public String getReporterId() {
        return this.reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public java.sql.Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
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
