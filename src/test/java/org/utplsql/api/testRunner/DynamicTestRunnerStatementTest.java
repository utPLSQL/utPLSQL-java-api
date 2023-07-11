package org.utplsql.api.testRunner;

import oracle.jdbc.OracleConnection;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.verification.VerificationMode;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.FileMapping;
import org.utplsql.api.TestRunnerOptions;
import org.utplsql.api.Version;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;

public class DynamicTestRunnerStatementTest {

    private DynamicTestRunnerStatement testRunnerStatement;
    private CallableStatement callableStatement;
    private OracleConnection oracleConnection;
    private TestRunnerOptions options;
    private Object[] expectedFileMapping;

    @BeforeEach
    void initParameters() throws SQLException {
        expectedFileMapping = new Object[]{new FileMapping("someFile", "owner", "object", "PACKAGE")};

        // Mock some internals. This is not pretty, but a first step
        oracleConnection = getMockedOracleConnection(expectedFileMapping);
        callableStatement = mock(CallableStatement.class);

        // Act
        options = TestRunnerStatementProviderIT.getCompletelyFilledOptions();
    }

    @Test
    void version_3_0_2_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_0_2);

        checkBaseParameters();
        checkFailOnError(false);
        checkClientCharacterSet(false);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_0_3_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_0_3);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(false);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_0_4_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_0_4);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(false);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_0_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_0);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(false);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_1_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_1);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(false);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_2_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_2);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_3_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_3);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_4_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_4);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_5_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_5);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_6_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_6);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(false);
        checkTags(false);
        checkExpr(false);
    }

    @Test
    void version_3_1_7_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_7);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(true);
        checkTags(true);
        checkExpr(false);
    }

    @Test
    void version_3_1_8_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_8);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(true);
        checkTags(true);
        checkExpr(false);
    }

    @Test
    void version_3_1_13_parameters() throws SQLException {
        initTestRunnerStatementForVersion(Version.V3_1_13);

        checkBaseParameters();
        checkFailOnError(true);
        checkClientCharacterSet(true);
        checkRandomTestOrder(true);
        checkTags(true);
        checkExpr(true);
    }

    private OracleConnection getMockedOracleConnection(Object[] expectedFileMapping) throws SQLException {
        OracleConnection oracleConnection = mock(OracleConnection.class);
        when(oracleConnection.unwrap(OracleConnection.class))
                .thenReturn(oracleConnection);
        mockFileMapper(oracleConnection, expectedFileMapping);
        return oracleConnection;
    }

    private void mockFileMapper(OracleConnection mockedOracleConnection, Object[] expectedFileMapping) throws SQLException {
        Array fileMapperArray = mock(Array.class);
        CallableStatement fileMapperStatement = mock(CallableStatement.class);

        when(fileMapperArray.getArray())
                .thenReturn(expectedFileMapping);
        when(fileMapperStatement.getArray(1))
                .thenReturn(fileMapperArray);
        when(
                mockedOracleConnection.prepareCall(argThat(
                        a -> a.startsWith("BEGIN ? := ut_file_mapper.build_file_mappings("))
                ))
                .thenReturn(fileMapperStatement);
    }

    private Matcher<String> doesOrDoesNotContainString(String string, boolean shouldBeThere) {
        return (shouldBeThere)
                ? containsString(string)
                : not(containsString(string));
    }

    private VerificationMode doesOrDoesNotGetCalled(boolean shouldBeThere) {
        return (shouldBeThere)
                ? times(1)
                : never();
    }

    private void initTestRunnerStatementForVersion(Version version) throws SQLException {
        testRunnerStatement = DynamicTestRunnerStatement
                .forVersion(version, oracleConnection, options, callableStatement);
    }

    private void checkBaseParameters() throws SQLException {
        String sql = testRunnerStatement.getSql();

        assertThat(sql, containsString("a_paths => ?"));
        verify(callableStatement).setArray(1, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.pathList.toArray());

        assertThat(sql, containsString("a_reporters => ?"));
        verify(callableStatement).setArray(2, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_REPORTERS, options.reporterList.toArray());

        assertThat(sql, containsString("a_color_console => (case ? when 1 then true else false end)"));
        verify(callableStatement).setInt(3, 0);

        assertThat(sql, containsString("a_coverage_schemes => ?"));
        verify(callableStatement).setArray(4, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.coverageSchemes.toArray());

        assertThat(sql, containsString("a_source_file_mappings => ?"));
        verify(callableStatement).setArray(5, null);

        assertThat(sql, containsString("a_test_file_mappings => ?"));
        verify(callableStatement).setArray(6, null);
        verify(oracleConnection, times(2)).createOracleArray(CustomTypes.UT_FILE_MAPPINGS, expectedFileMapping);

        assertThat(sql, containsString("a_include_objects => ?"));
        verify(callableStatement).setArray(7, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.includeObjects.toArray());

        assertThat(sql, containsString("a_exclude_objects => ?"));
        verify(callableStatement).setArray(8, null);
        verify(oracleConnection).createOracleArray(CustomTypes.UT_VARCHAR2_LIST, options.includeObjects.toArray());
    }

    private void checkFailOnError(boolean shouldBeThere) throws SQLException {
        String sql = testRunnerStatement.getSql();

        assertThat(sql, doesOrDoesNotContainString("a_fail_on_errors => (case ? when 1 then true else false end)", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setInt(9, 1);
    }

    private void checkClientCharacterSet(boolean shouldBeThere) throws SQLException {
        String sql = testRunnerStatement.getSql();

        assertThat(sql, doesOrDoesNotContainString("a_client_character_set => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setString(10, "UTF8");
    }

    private void checkRandomTestOrder(boolean shouldBeThere) throws SQLException {
        String sql = testRunnerStatement.getSql();

        assertThat(sql, doesOrDoesNotContainString("a_random_test_order => (case ? when 1 then true else false end)", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setInt(11, 1);
        assertThat(sql, doesOrDoesNotContainString("a_random_test_order_seed => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setInt(12, 123);
    }

    private void checkTags(boolean shouldBeThere) throws SQLException {
        String sql = testRunnerStatement.getSql();

        assertThat(sql, doesOrDoesNotContainString("a_tags => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setString(13, "WIP,long_running");
    }

    private void checkExpr(boolean shouldBeThere) throws SQLException {
        String sql = testRunnerStatement.getSql();

        assertThat(sql, doesOrDoesNotContainString("a_include_schema_expr => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setString(14, "a_*");
        assertThat(sql, doesOrDoesNotContainString("a_include_object_expr => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setString(15, "a_*");
        assertThat(sql, doesOrDoesNotContainString("a_exclude_schema_expr => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setString(16, "ut3:*_package*");
        assertThat(sql, doesOrDoesNotContainString("a_exclude_object_expr => ?", shouldBeThere));
        verify(callableStatement, doesOrDoesNotGetCalled(shouldBeThere)).setString(17, "ut3:*_package*");
    }
}
