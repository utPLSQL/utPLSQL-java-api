package org.utplsql.api.v2;


import oracle.jdbc.OracleConnection;
import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.db.DefaultDatabaseInformation;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.exception.UtplsqlException;
import org.utplsql.api.reporter.inspect.ReporterInfo;
import org.utplsql.api.reporter.inspect.ReporterInspector;
import org.utplsql.api.v2.reporters.ReporterFactory;
import org.utplsql.api.v2.reporters.ReporterFactoryImpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Pavel Kaplya on 11.02.2019.
 */
public class UtplsqlSessionImpl implements UtplsqlSession {

    private final DataSource dataSource;

    UtplsqlSessionImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TestRunBuilder createTestRun() {
        return new TestRunBuilderImpl(this);
    }

    @Override
    public ReporterFactory reporterFactory() {
        return new ReporterFactoryImpl(dataSource);
    }

    @Override
    public List<ReporterInfo> getAvailableReporters() throws SQLException, InvalidVersionException {
        try (OracleConnection connection = getConnection()) {
            CompatibilityProxy compatibilityProxy = new CompatibilityProxy(connection);
            ReporterInspector reporterInspector = ReporterInspector.create(org.utplsql.api.reporter.ReporterFactory.createDefault(compatibilityProxy), connection);
            return reporterInspector.getReporterInfos();
        }
    }

    @Override
    public Version getInstalledVersionInfo() throws SQLException {
        try (OracleConnection connection = getConnection()) {
            return new DefaultDatabaseInformation().getUtPlsqlFrameworkVersion(connection);
        }
    }

    @Override
    public OracleConnection getConnection() throws SQLException {
        return dataSource.getConnection().unwrap(OracleConnection.class);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    void doCompatibilityCheckWithDatabase() {
        try (OracleConnection conn = getConnection()) {
            new CompatibilityProxy(conn, false);
        } catch (SQLException e) {
            throw new UtplsqlException(e);
        }
    }
}
