package org.utplsql.api;

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

    public FileMapping() {
    }

    public FileMapping(String fileName, String objectOwner, String objectName, String objectType) {
        this.fileName = fileName;
        this.objectOwner = objectOwner;
        this.objectName = objectName;
        this.objectType = objectType;
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getObjectOwner() {
        return objectOwner;
    }

    private void setObjectOwner(String objectOwner) {
        this.objectOwner = objectOwner;
    }

    public String getObjectName() {
        return objectName;
    }

    private void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectType() {
        return objectType;
    }

    private void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String getSQLTypeName() {
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
