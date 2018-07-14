package org.utplsql.api.reporter.inspect;

import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

class ReporterInspectorPre310 extends AbstractReporterInspector {

    private final Map<String, String> registeredReporterFactoryMethods;
    private final Map<CoreReporters, String> descriptions = new HashMap<>();
    private final Set<ReporterInfo> infos;

    ReporterInspectorPre310(ReporterFactory reporterFactory, Connection conn) throws SQLException {
        super(reporterFactory, conn);
        registeredReporterFactoryMethods = reporterFactory.getRegisteredReporterInfo();
        initDefaultDescriptions();

        Version databaseVersion = new CompatibilityProxy(connection).getDatabaseVersion();
        this.infos = Arrays.stream(CoreReporters.values())
                .filter(r -> r.isAvailableFor(databaseVersion))
                .map(this::getReporterInfo)
                .collect(Collectors.toSet());
    }

    private void initDefaultDescriptions() {
        descriptions.put(CoreReporters.UT_COVERAGE_HTML_REPORTER, "");
        descriptions.put(CoreReporters.UT_COVERAGE_SONAR_REPORTER, "");
        descriptions.put(CoreReporters.UT_COVERALLS_REPORTER, "");
        descriptions.put(CoreReporters.UT_DOCUMENTATION_REPORTER, "");
        descriptions.put(CoreReporters.UT_SONAR_TEST_REPORTER, "");
        descriptions.put(CoreReporters.UT_TEAMCITY_REPORTER, "");
        descriptions.put(CoreReporters.UT_XUNIT_REPORTER, "");
    }

    @Override
    public List<ReporterInfo> getReporterInfos() {
        return new ArrayList<>(this.infos);
    }

    private ReporterInfo getReporterInfo(CoreReporters reporter) {

        ReporterInfo.Type type = ReporterInfo.Type.SQL;
        String description = descriptions.get(reporter);

        if (registeredReporterFactoryMethods.containsKey(reporter.name())) {
            type = ReporterInfo.Type.SQL_WITH_JAVA;
            description += "\n" + registeredReporterFactoryMethods.get(reporter.name());
        }

        return new ReporterInfo(reporter.name(), type, description);
    }
}
