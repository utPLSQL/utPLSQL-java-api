package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

import java.sql.SQLException;

public class CoverallsReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_COVERALLS_REPORTER;
    }
    
}
