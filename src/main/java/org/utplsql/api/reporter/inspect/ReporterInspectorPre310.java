package org.utplsql.api.reporter.inspect;

import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ReporterInspectorPre310 extends AbstractReporterInspector {

    private Map<String, String> registeredReporterFactoryMethods;
    private Map<CoreReporters, String> descriptions;

    ReporterInspectorPre310(ReporterFactory reporterFactory, Connection conn ) throws SQLException {
        super(reporterFactory, conn);
    }

    private void initDefaultDescriptions() {
        descriptions = new HashMap<>();
        descriptions.put(CoreReporters.UT_COVERAGE_HTML_REPORTER, "");
        descriptions.put(CoreReporters.UT_COVERAGE_SONAR_REPORTER, "");
        descriptions.put(CoreReporters.UT_COVERALLS_REPORTER, "");
        descriptions.put(CoreReporters.UT_DOCUMENTATION_REPORTER, "");
        descriptions.put(CoreReporters.UT_SONAR_TEST_REPORTER, "");
        descriptions.put(CoreReporters.UT_TEAMCITY_REPORTER, "");
        descriptions.put(CoreReporters.UT_XUNIT_REPORTER, "");
    }

    @Override
    protected void load() throws SQLException {
        initDefaultDescriptions();
        Version databaseVersion = new CompatibilityProxy(connection).getDatabaseVersion();
        registeredReporterFactoryMethods = reporterFactory.getRegisteredReporterInfo();
        infos = Arrays.stream(CoreReporters.values())
                .filter(r -> r.isAvailableFor(databaseVersion))
                .map(this::getReporterInfo)
                .collect(Collectors.toSet());
    }

    private ReporterInfo getReporterInfo( CoreReporters reporter ) {

        ReporterInfo.Type type = ReporterInfo.Type.SQL;
        String description = descriptions.get(reporter);

        if ( registeredReporterFactoryMethods.containsKey(reporter.name()) ) {
            type = ReporterInfo.Type.SQL_WITH_JAVA;
            description += "\n" + registeredReporterFactoryMethods.get(reporter.name());
        }

        return new ReporterInfo( reporter.name(), type, description);
    }
}
