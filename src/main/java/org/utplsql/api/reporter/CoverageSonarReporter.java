package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;

import java.sql.SQLException;

public class CoverageSonarReporter extends OutputReporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_COVERAGE_SONAR_REPORTER;
    }

}
