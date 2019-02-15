package org.utplsql.api.v2;

import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;

import java.util.Collections;

/**
 * Created by Pavel Kaplya on 28.02.2019.
 */
class ApiV2UsageTest extends AbstractDatabaseTest {

    @Test
    void testSessionCreation() {
        UtplsqlSession utplsqlSession = UtplsqlSession.create(getDataSource());

        TestRun testRun = utplsqlSession.createTestRun()
                .paths(Collections.singletonList("app"))
                .build();

        ReporterFactory reporterFactory = utplsqlSession.reporterFactory();
        testRun.addReporter(reporterFactory.documentationReporter());

        //testRun.execute();

    }
}
