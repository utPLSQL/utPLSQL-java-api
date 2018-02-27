package org.utplsql.api.reporter;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class DocumentationReporter extends Reporter {

    private int lvl;
    private int failed;

    public DocumentationReporter() {
        super( DefaultReporters.UT_DOCUMENTATION_REPORTER.name(), null );
    }

    public DocumentationReporter(String selfType, Object[] attributes ) {
        super(selfType, attributes);
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

}
