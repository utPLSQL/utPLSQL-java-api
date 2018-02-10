package org.utplsql.api.reporter;

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
        return DefaultReporters.UT_DOCUMENTATION_REPORTER.name();
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
