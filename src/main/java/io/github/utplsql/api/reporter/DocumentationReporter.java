package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

import java.sql.SQLException;

public class DocumentationReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_DOCUMENTATION_REPORTER;
    }

}
