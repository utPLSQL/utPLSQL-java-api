package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class DocumentationReporter extends Reporter {

    private int lvl;
    private int failed;

    public DocumentationReporter() {
        this.lvl = 0;
        this.failed = 0;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_DOCUMENTATION_REPORTER;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        super.readSQL(stream, typeName);
        setLvl(stream.readInt());
        setFailed(stream.readInt());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        super.writeSQL(stream);
        stream.writeInt(getLvl());
        stream.writeInt(getFailed());
    }

}
