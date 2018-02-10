package org.utplsql.api.reporter;

import java.sql.SQLException;

public class XUnitReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return DefaultReporters.UT_XUNIT_REPORTER.name();
    }

}
