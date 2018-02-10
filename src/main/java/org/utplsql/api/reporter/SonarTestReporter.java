package org.utplsql.api.reporter;

import java.sql.SQLException;

public class SonarTestReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return DefaultReporters.UT_SONAR_TEST_REPORTER.name();
    }

}
