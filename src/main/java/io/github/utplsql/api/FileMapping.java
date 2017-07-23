package io.github.utplsql.api;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Created by Vinicius on 17/07/2017.
 */
public class FileMapping implements SQLData {

    private String fileName;
    private String objectOwner;
    private String objectName;
    private String objectType;

    public FileMapping() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getObjectOwner() {
        return objectOwner;
    }

    public void setObjectOwner(String objectOwner) {
        this.objectOwner = objectOwner;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_FILE_MAPPING;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setFileName(stream.readString());
        setObjectOwner(stream.readString());
        setObjectName(stream.readString());
        setObjectType(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getFileName());
        stream.writeString(getObjectOwner());
        stream.writeString(getObjectName());
        stream.writeString(getObjectType());
    }

    @Override
    public String toString() {
        return String.format("%s/%s.%s", getObjectType(), getObjectOwner(), getObjectName());
    }

}
