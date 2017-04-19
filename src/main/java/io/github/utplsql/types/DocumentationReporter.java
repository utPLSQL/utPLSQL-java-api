package io.github.utplsql.types;

import java.sql.SQLException;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class DocumentationReporter extends BaseReporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_DOCUMENTATION_REPORTER.getName();
    }

}
