package org.utplsql.api.outputBuffer;

import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;
import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;
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
    void testGettingActualVersion() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection(), Version.LATEST);
        ReporterFactory reporterFactory = ReporterFactory.createDefault(proxy);

        Reporter r = reporterFactory.createReporter(CoreReporters.UT_DOCUMENTATION_REPORTER.name());
        r.init(getConnection(), proxy, reporterFactory);

        OutputBuffer buffer = proxy.getOutputBuffer(r, getConnection());

        assertThat(buffer, instanceOf(DefaultOutputBuffer.class));
    }
}
