package org.utplsql.api.reporter.inspect;

import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Gives information about available reporters
 *
 * @author pesse
 */
public interface ReporterInspector {

    List<ReporterInfo> getReporterInfos();

    default Map<String, ReporterInfo> getReporterInfoMap() {
        return getReporterInfos().stream().collect(Collectors.toMap(ReporterInfo::getName, Function.identity()));
    }

    /**
     * Returns a new instance of a ReporterInspector, based on the utPLSQL version used in the connection
     *
     * @param reporterFactory
     * @param conn
     * @return
     * @throws SQLException
     */
    static ReporterInspector create(ReporterFactory reporterFactory, Connection conn) throws SQLException, InvalidVersionException {

        CompatibilityProxy proxy = new CompatibilityProxy(conn);

        if (proxy.getDatabaseVersion().isGreaterOrEqualThan(new Version("3.1.0")))
            return new ReporterInspector310(reporterFactory, conn);
        else
            return new ReporterInspectorPre310(reporterFactory, conn);
    }

}
