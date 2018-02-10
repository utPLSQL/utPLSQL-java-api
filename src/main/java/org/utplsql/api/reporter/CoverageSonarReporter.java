package org.utplsql.api.reporter;

import java.sql.SQLException;

public class CoverageSonarReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return DefaultReporters.UT_COVERAGE_SONAR_REPORTER.name();
    }

}
