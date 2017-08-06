package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;

import java.sql.SQLException;

public class TeamCityReporter extends Reporter {

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_TEAMCITY_REPORTER;
    }

}
