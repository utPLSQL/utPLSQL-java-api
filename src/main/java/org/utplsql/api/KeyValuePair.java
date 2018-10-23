package org.utplsql.api;

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

    public String getValue() {
        return value;
    }

    @Override
    public String getSQLTypeName() {
        return CustomTypes.UT_KEY_VALUE_PAIR;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        key = stream.readString();
        value = stream.readString();
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(key);
        stream.writeString(value);
    }

    @Override
    public String toString() {
        return String.format("%s => %s", key, value);
    }

}
