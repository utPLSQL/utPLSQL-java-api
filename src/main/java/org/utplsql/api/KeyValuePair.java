package io.github.utplsql.api;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Created by Vinicius on 22/07/2017.
 */
public class KeyValuePair implements SQLData {

    private String key;
    private String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_KEY_VALUE_PAIR;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setKey(stream.readString());
        setValue(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getKey());
        stream.writeString(getValue());
    }

    @Override
    public String toString() {
        return String.format("%s => %s", getKey(), getValue());
    }

}
