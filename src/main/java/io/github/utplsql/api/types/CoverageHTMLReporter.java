package io.github.utplsql.api.types;

import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class CoverageHTMLReporter extends BaseReporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_COVERAGE_HTML_REPORTER.getName();
    }

}
