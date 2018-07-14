package org.utplsql.api.reporter.inspect;

import org.utplsql.api.reporter.ReporterFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AbstractReporterInspector implements ReporterInspector  {

    protected final ReporterFactory reporterFactory;
    protected final Connection connection;
    protected final Set<ReporterInfo> infos;

    AbstractReporterInspector(ReporterFactory reporterFactory, Connection conn ) throws SQLException {
        this.reporterFactory = reporterFactory;
        this.connection = conn;
        this.infos = loadReporterInfos();
    }

    protected abstract Set<ReporterInfo> loadReporterInfos() throws SQLException;

    @Override
    public Map<String, ReporterInfo> getReporterInfoMap() {
        return infos.stream().collect(Collectors.toMap(ReporterInfo::getName, Function.identity()));
    }

    @Override
    public List<ReporterInfo> getReporterInfos() {
        return new ArrayList<>(infos);
    }

}
