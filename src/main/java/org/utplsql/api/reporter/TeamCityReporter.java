package org.utplsql.api.reporter;

import java.sql.SQLException;

public class TeamCityReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return DefaultReporters.UT_TEAMCITY_REPORTER.name();
    }

}
