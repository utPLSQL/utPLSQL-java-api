package org.utplsql.api.reporter;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReporterInitTest {

    @Test
    void initClosesReporterStatement() throws SQLException {
        OracleCallableStatement callableStatement = mock(OracleCallableStatement.class);
        Reporter dbReporter = new DefaultReporter("UT_DOCUMENTATION_REPORTER",
                new Object[]{"UT_DOCUMENTATION_REPORTER", new byte[]{0x0A, 0x0B}});
        when(callableStatement.getORAData(eq(1), any())).thenReturn(dbReporter);

        OracleConnection oracleConnection = mock(OracleConnection.class);
        when(oracleConnection.prepareCall(anyString())).thenReturn(callableStatement);

        // Connection as handed out by a connection pool
        Connection connection = mock(Connection.class);
        when(connection.unwrap(OracleConnection.class)).thenReturn(oracleConnection);

        Reporter reporter = new DefaultReporter("UT_DOCUMENTATION_REPORTER", null)
                .init(connection, mock(CompatibilityProxy.class), new ReporterFactory());

        assertThat(reporter.isInit(), equalTo(true));
        assertThat(reporter.getId(), equalTo("0A0B"));
        verify(callableStatement).close();
    }
}
