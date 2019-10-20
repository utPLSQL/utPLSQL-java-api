package org.utplsql.api.db;

import oracle.jdbc.OracleConnection;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DynamicParameterListTest {

    @Nested
    class single_parameters {
        @Test
        void can_add_string() throws SQLException {
            CallableStatement stmt = mock(CallableStatement.class);

            DynamicParameterList paramList = DynamicParameterList.builder()
                    .add("a_param", "MyString")
                    .build();

            assertEquals("a_param => ?", paramList.getSql());

            paramList.setParamsStartWithIndex(stmt, 5);
            verify(stmt).setString(5, "MyString");
        }

        @Test
        void can_add_int() throws SQLException {
            CallableStatement stmt = mock(CallableStatement.class);

            DynamicParameterList paramList = DynamicParameterList.builder()
                    .add("a_param", 1234)
                    .build();

            assertEquals("a_param => ?", paramList.getSql());

            paramList.setParamsStartWithIndex(stmt, 10);
            verify(stmt).setInt(10, 1234);
        }

        @Test
        void can_add_array() throws SQLException {
            CallableStatement stmt = mock(CallableStatement.class);
            OracleConnection conn = mock(OracleConnection.class);

            Object[] numArr = new Object[]{1, 2};

            DynamicParameterList paramList = DynamicParameterList.builder()
                    .add("a_param", numArr, "MY_TYPE", conn)
                    .build();

            assertEquals("a_param => ?", paramList.getSql());

            paramList.setParamsStartWithIndex(stmt, 3);
            verify(conn).createOracleArray("MY_TYPE", numArr);
            verify(stmt).setArray(3, null);
        }

        @Test
        void can_add_boolean() throws SQLException {
            CallableStatement stmt = mock(CallableStatement.class);

            DynamicParameterList paramList = DynamicParameterList.builder()
                    .add("a_bool", true)
                    .build();

            assertEquals("a_bool => (case ? when 1 then true else false)", paramList.getSql());

            paramList.setParamsStartWithIndex(stmt, 3);
            verify(stmt).setInt(3, 1);
        }
    }

    @Nested
    class mutliple_parameters {

        @Test
        void several_parameters_are_issued_in_the_correct_order() throws SQLException {
            CallableStatement mockedStatement = mock(CallableStatement.class);

            DynamicParameterList parameterList = DynamicParameterList.builder()
                    .add("a_param1", "Param1")
                    .add("a_param2", "Param2")
                    .add("a_param3", "Param3")
                    .build();

            assertEquals("a_param1 => ?, a_param2 => ?, a_param3 => ?", parameterList.getSql());

            parameterList.setParamsStartWithIndex(mockedStatement, 10);

            verify(mockedStatement).setString(10, "Param1");
            verify(mockedStatement).setString(11, "Param2");
            verify(mockedStatement).setString(12, "Param3");
        }

        @Test
        void call_with_three_different_types() throws SQLException {

            CallableStatement mockedStatement = mock(CallableStatement.class);
            OracleConnection mockedConn = mock(OracleConnection.class);

            Object[] numArr = new Object[]{1, 2};

            DynamicParameterList parameterList = DynamicParameterList.builder()
                    .add("a_object_owner", "MyOwner")
                    .add("a_num_param", 123)
                    .add("a_num_array", numArr, "MY_NUM_ARR", mockedConn)
                    .build();

            assertEquals("a_object_owner => ?, a_num_param => ?, a_num_array => ?", parameterList.getSql());

            parameterList.setParamsStartWithIndex(mockedStatement, 5);

            verify(mockedStatement).setString(5, "MyOwner");
            verify(mockedStatement).setInt(6, 123);
            verify(mockedConn).createOracleArray("MY_NUM_ARR", numArr);
            verify(mockedStatement).setArray(7, null);
        }

        @Test
        void when_not_accept_empty_filter_empty_elements() throws SQLException {

            CallableStatement mockedStatement = mock(CallableStatement.class);
            OracleConnection mockedConn = mock(OracleConnection.class);

            DynamicParameterList parameterList = DynamicParameterList.builder()
                    .addIfNotEmpty("a_object_owner", (String) null)
                    .addIfNotEmpty("a_num_param", (Integer) null)
                    .addIfNotEmpty("a_num_array", new Object[]{}, "MY_NUM_ARR", mockedConn)
                    .build();

            assertEquals("", parameterList.getSql());

            parameterList.setParamsStartWithIndex(mockedStatement, 2);

            verifyNoMoreInteractions(mockedStatement);
            verifyNoMoreInteractions(mockedConn);
        }
    }
}
