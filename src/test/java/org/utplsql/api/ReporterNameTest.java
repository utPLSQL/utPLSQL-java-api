package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReporterNameTest {

    @Test
    public void defaultReporterFactoryNamesList() {
        Map<String, String> reporterDescriptions = ReporterFactory.getInstance().getRegisteredReporterInfo();

        for ( CoreReporters r : CoreReporters.values() ) {
            assertTrue(reporterDescriptions.containsKey(r.name()));
        }
    }

}
