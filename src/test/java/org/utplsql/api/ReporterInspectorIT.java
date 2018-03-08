package org.utplsql.api;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleType;
import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.reporter.ReporterFactory;
import org.utplsql.api.reporter.inspect.ReporterInfo;
import org.utplsql.api.reporter.inspect.ReporterInspector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReporterInspectorIT extends AbstractDatabaseTest {

    private ReporterFactory getReporterFactory() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());

        return ReporterFactory.createDefault(proxy);
    }

    @Test
    public void testGetReporterInfo() throws SQLException, InvalidVersionException {

        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());

        ReporterInspector inspector = ReporterInspector.create(getReporterFactory(), getConnection());

        Map<String, ReporterInfo> infos = inspector.getReporterInfoMap();

        assertEquals( infos.get(CoreReporters.UT_COVERAGE_HTML_REPORTER.name()).getType(), ReporterInfo.Type.SQL_WITH_JAVA );
        assertEquals( infos.get(CoreReporters.UT_COVERAGE_SONAR_REPORTER.name()).getType(), ReporterInfo.Type.SQL );
        assertEquals( infos.get(CoreReporters.UT_COVERALLS_REPORTER.name()).getType(), ReporterInfo.Type.SQL );
        assertEquals( infos.get(CoreReporters.UT_DOCUMENTATION_REPORTER.name()).getType(), ReporterInfo.Type.SQL_WITH_JAVA );
        assertEquals( infos.get(CoreReporters.UT_SONAR_TEST_REPORTER.name()).getType(), ReporterInfo.Type.SQL);
        assertEquals( infos.get(CoreReporters.UT_TEAMCITY_REPORTER.name()).getType(), ReporterInfo.Type.SQL);
        assertEquals( infos.get(CoreReporters.UT_XUNIT_REPORTER.name()).getType(), ReporterInfo.Type.SQL);

        if ( CoreReporters.UT_COVERAGE_COBERTURA_REPORTER.isAvailableFor(proxy.getDatabaseVersion())) {
            assertEquals(infos.get(CoreReporters.UT_COVERAGE_COBERTURA_REPORTER.name()).getType(), ReporterInfo.Type.SQL);
        }
    }

    @Test
    public void printReporterInfos() throws SQLException, InvalidVersionException {

        ReporterInspector inspector = ReporterInspector.create(getReporterFactory(), getConnection());

        inspector.getReporterInfos().stream()
                .sorted(Comparator.comparing(ReporterInfo::getName))
                .forEach(r -> {
                    System.out.println(r.getName() + " (" + r.getType().name() + "): " + r.getDescription());
                    System.out.println();
                });

    }
}
