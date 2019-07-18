package org.utplsql.api.db;

import oracle.jdbc.OracleConnection;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DynamicParameterListTest {

    @Test
    void callWithThreeDifferentTypes() throws SQLException {

        CallableStatement mockedStatement = mock(CallableStatement.class);
        OracleConnection mockedConn = mock(OracleConnection.class);

        Object[] numArr = new Object[]{1, 2};

        DynamicParameterList parameterList = DynamicParameterListBuilder.create()
                .add("a_object_owner", "MyOwner")
                .add("a_num_param", 123)
                .add("a_num_array", numArr, "MY_NUM_ARR", mockedConn)
                .build();

        assertEquals("a_object_owner = ?, a_num_param = ?, a_num_array = ?", parameterList.getSql());

        parameterList.setParamsStartWithIndex(mockedStatement, 5);
        verify(mockedStatement).setString(5, "MyOwner");
        verify(mockedStatement).setInt(6, 123);
        verify(mockedConn).createOracleArray("MY_NUM_ARR", numArr);
        verify(mockedStatement).setArray(7, null);
    }
}
