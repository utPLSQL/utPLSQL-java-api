package org.utplsql.api.reporter.inspect;

import org.utplsql.api.reporter.ReporterFactory;

import java.sql.*;
import java.util.*;

/**
 * ReporterInspector for v3.1.0 upwards
 *
 * @author pesse
 */
class ReporterInspector310 extends AbstractReporterInspector {

    private final Map<String, String> registeredReporterFactoryMethods;
    private final Set<ReporterInfo> infos;

    ReporterInspector310(ReporterFactory reporterFactory, Connection conn) throws SQLException {
        super(reporterFactory, conn);
        registeredReporterFactoryMethods = reporterFactory.getRegisteredReporterInfo();

        Set<ReporterInfo> reporterInfos = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement("select * from table(ut_runner.get_reporters_list) order by 1")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reporterInfos.add(getReporterInfo(rs.getString(1)));
                }
            }
        }
        this.infos = reporterInfos;

    }

    @Override
    public List<ReporterInfo> getReporterInfos() {
        return new ArrayList<>(infos);
    }

    private ReporterInfo getReporterInfo(String reporterNameWithOwner) throws SQLException {
        String reporterName = reporterNameWithOwner.substring(reporterNameWithOwner.indexOf(".") + 1).toUpperCase();

        ReporterInfo.Type type = ReporterInfo.Type.SQL;
        String description = getDescription(reporterNameWithOwner);

        if (registeredReporterFactoryMethods.containsKey(reporterName)) {
            type = ReporterInfo.Type.SQL_WITH_JAVA;
            description += "\n" + registeredReporterFactoryMethods.get(reporterName);
        }

        return new ReporterInfo(reporterName, type, description);
    }

    private String getDescription(String reporterNameWithOwner) throws SQLException {
        String plsql = "DECLARE l_obj " + reporterNameWithOwner + " := " + reporterNameWithOwner + "(); "
                + "BEGIN :1 := l_obj.get_description(); END;";
        try (CallableStatement stmt = connection.prepareCall(plsql)) {
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.execute();
            return stmt.getString(1);
        }
    }

}
