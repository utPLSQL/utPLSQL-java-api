package io.github.utplsql.api.reporter;

import io.github.utplsql.api.CustomTypes;

import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class CoverageHTMLReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_COVERAGE_HTML_REPORTER;
    }

}
