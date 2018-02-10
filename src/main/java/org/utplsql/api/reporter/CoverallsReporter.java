package org.utplsql.api.reporter;

import java.sql.SQLException;

public class CoverallsReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return DefaultReporters.UT_COVERALLS_REPORTER.name();
    }
    
}
