package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.DefaultReporter;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.reporter.ReporterFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterCustomReporterTest {

    @Test
    public void addCustomReporter() {

        ReporterFactory reporterFactory = new ReporterFactory();
        reporterFactory.registerReporterFactoryMethod("ut_custom_reporter",
                (type, attr) -> new DefaultReporter("UT_EXISTING_REPORTER", attr),
                "My custom Reporter");

        Reporter r = reporterFactory.createReporter("ut_custom_reporter");

        assertEquals(r.getTypeName(), "UT_EXISTING_REPORTER");
    }

    @Test
    public void createCustomDefaultReporter() {
        ReporterFactory reporterFactory = new ReporterFactory();
        Reporter r = reporterFactory.createReporter("ut_custom_reporter");

        assertEquals(r.getTypeName(), "UT_CUSTOM_REPORTER");
    }
}
