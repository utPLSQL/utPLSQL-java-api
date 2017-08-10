package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;

import java.sql.SQLException;

public class XUnitReporter extends OutputReporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_XUNIT_REPORTER;
    }

}
