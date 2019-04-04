package org.utplsql.api.outputBuffer;

import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;
import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.exception.InvalidVersionException;
import org.utplsql.api.reporter.CoreReporters;
import org.utplsql.api.reporter.Reporter;
import org.utplsql.api.reporter.ReporterFactory;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class OutputBufferProviderIT extends AbstractDatabaseTest {

    @Test
    void testGettingPre310Version() throws SQLException {

        CompatibilityProxy proxy = new CompatibilityProxy(getConnection(), Version.V3_0_4);
        ReporterFactory reporterFactory = ReporterFactory.createDefault(proxy);

        Reporter r = reporterFactory.createReporter(CoreReporters.UT_DOCUMENTATION_REPORTER.name());
        r.init(getConnection(), proxy, reporterFactory);

        OutputBuffer buffer = proxy.getOutputBuffer(r, getConnection());

        assertThat(buffer, instanceOf(CompatibilityOutputBufferPre310.class));
    }

    @Test
    void testGettingActualVersion() throws SQLException, InvalidVersionException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection(), Version.LATEST);

        // We can only test new behaviour with DB-Version >= 3.1.0
        if ( proxy.getRealDbPlsqlVersion().isGreaterOrEqualThan(Version.V3_1_0)) {
            ReporterFactory reporterFactory = ReporterFactory.createDefault(proxy);

            Reporter r = reporterFactory.createReporter(CoreReporters.UT_DOCUMENTATION_REPORTER.name());
            r.init(getConnection(), proxy, reporterFactory);

            OutputBuffer buffer = proxy.getOutputBuffer(r, getConnection());

            assertThat(buffer, instanceOf(DefaultOutputBuffer.class));
        }
    }
}
