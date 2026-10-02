package org.utplsql.api.outputBuffer;

import oracle.jdbc.OracleTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.utplsql.api.reporter.Reporter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Connection pools (e.g. HikariCP) return their own proxy statements, which are not
 * {@link oracle.jdbc.OracleCallableStatement} instances. The output buffer must work with plain JDBC statements.
 */
class DefaultOutputBufferTest {

    private Reporter reporter;
    private Connection connection;
    private CallableStatement callableStatement;

    @BeforeEach
    void setUp() throws SQLException {
        reporter = mock(Reporter.class);
        when(reporter.isInit()).thenReturn(true);
        when(reporter.getTypeName()).thenReturn("UT_DOCUMENTATION_REPORTER");
        when(reporter.getId()).thenReturn("ABC123");

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("text")).thenReturn("line 1", "line 2");

        // A plain CallableStatement, as returned by a connection pool proxy
        callableStatement = mock(CallableStatement.class);
        when(callableStatement.getObject("lines_cursor")).thenReturn(resultSet);

        connection = mock(Connection.class);
        when(connection.prepareCall(anyString())).thenReturn(callableStatement);
    }

    @Test
    void fetchAllWorksWithNonOracleCallableStatement() throws SQLException {
        List<String> lines = new DefaultOutputBuffer(reporter).fetchAll(connection);

        assertThat(lines, contains("line 1", "line 2"));
        verify(callableStatement).setString("reporter_id", "ABC123");
        verify(callableStatement).registerOutParameter("lines_cursor", OracleTypes.CURSOR);
        verify(callableStatement).close();
    }

    @Test
    void printAvailableWorksWithNonOracleCallableStatement() throws SQLException {
        new DefaultOutputBuffer(reporter).setFetchSize(1).printAvailable(connection, mock(java.io.PrintStream.class));

        verify(callableStatement).setFetchSize(1);
        verify(callableStatement).execute();
        verify(callableStatement).close();
    }

    @Test
    void statementIsClosedWhenBindingFails() throws SQLException {
        SQLException bindError = new SQLException("bind failed");
        doThrow(bindError).when(callableStatement).setString(anyString(), anyString());

        SQLException thrown = assertThrows(SQLException.class, () -> new DefaultOutputBuffer(reporter).fetchAll(connection));

        assertThat(thrown, sameInstance(bindError));
        verify(callableStatement).close();
        verify(callableStatement, never()).execute();
    }
}
