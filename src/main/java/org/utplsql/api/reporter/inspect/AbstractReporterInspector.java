package org.utplsql.api.reporter.inspect;

import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractReporterInspector implements ReporterInspector  {

    protected ReporterFactory reporterFactory;
    protected Connection connection;
    protected Set<ReporterInfo> infos;

    AbstractReporterInspector(ReporterFactory reporterFactory, Connection conn ) throws SQLException {
        this.reporterFactory = reporterFactory;
        this.connection = conn;

        load();
    }

    protected abstract void load() throws SQLException;

    @Override
    public Map<String, ReporterInfo> getReporterInfoMap() {
        return infos.stream().collect(Collectors.toMap(ReporterInfo::getName, i -> i));
    }

    @Override
    public List<ReporterInfo> getReporterInfos() {
        return new ArrayList<>(infos);
    }

}
