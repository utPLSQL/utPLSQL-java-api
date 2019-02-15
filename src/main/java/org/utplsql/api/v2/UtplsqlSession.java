package org.utplsql.api.v2;


import javax.sql.DataSource;

/**
 * Created by Pavel Kaplya on 11.02.2019.
 */
public class UtplsqlSession {

    private final DataSource dataSource;

    private UtplsqlSession(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TestRunBuilder createTestRun() {
        return null;
    }


    public static UtplsqlSession create(DataSource dataSource) {
        return new UtplsqlSession(dataSource);
    }

    public ReporterFactory reporterFactory() {
        return null;
    }
}
