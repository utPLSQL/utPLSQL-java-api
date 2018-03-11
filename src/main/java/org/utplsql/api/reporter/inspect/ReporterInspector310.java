package org.utplsql.api.reporter.inspect;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleType;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/** ReporterInspector for v3.1.0 upwards
 *
 * @author pesse
 */
class ReporterInspector310 extends AbstractReporterInspector {


    private Map<String, String> registeredReporterFactoryMethods;
    private CompatibilityProxy compatibilityProxy;

    ReporterInspector310(ReporterFactory reporterFactory, Connection conn ) throws SQLException {
        super(reporterFactory, conn);
    }

    @Override
    protected void load() throws SQLException {

        registeredReporterFactoryMethods = reporterFactory.getRegisteredReporterInfo();
        compatibilityProxy = new CompatibilityProxy(connection);

        infos = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement("select * from table(ut_runner.get_reporters_list) order by 1")) {
            try (ResultSet rs = stmt.executeQuery() ) {
                while (rs.next())
                    infos.add(getReporterInfo(rs.getString(1)));
            }
        }
    }

    private ReporterInfo getReporterInfo( String reporterNameWithOwner ) throws SQLException {
        String reporterName = reporterNameWithOwner.substring(reporterNameWithOwner.indexOf(".")+1).toUpperCase();

        ReporterInfo.Type type = ReporterInfo.Type.SQL;
        String description = getDescription(reporterName);

        if ( registeredReporterFactoryMethods.containsKey(reporterName) ) {
            type = ReporterInfo.Type.SQL_WITH_JAVA;
            description += "\n" + registeredReporterFactoryMethods.get(reporterName);
        }

        return new ReporterInfo(reporterName, type, description);
    }

    private String getDescription( String reporterName ) throws SQLException {
        Reporter reporter = reporterFactory.createReporter(reporterName).init(connection, compatibilityProxy, reporterFactory);
        OracleConnection oraCon = connection.unwrap(OracleConnection.class);

        try (OracleCallableStatement stmt = (OracleCallableStatement) oraCon.prepareCall("{ ? = call ?.get_description() }")) {
            stmt.registerOutParameter(1, OracleType.VARCHAR2);
            stmt.setORAData(2, reporter);
            stmt.execute();

            return stmt.getString(1);
        }
    }

}
