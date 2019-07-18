package org.utplsql.api.db;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicParameterListTest {

    @Test
    void firstTest() {

        final ArrayList<Object> resultArray = new ArrayList<>();

        DynamicParameterList parameterList = DynamicParameterListBuilder.create()
                .addParameter("a_object_owner", i -> resultArray.add(i + ": MyOwner"))
                .addParameter("a_num_param", i -> resultArray.add( i + ": 123"))
                .build();

        parameterList.applyFromIndex(5);
        assertEquals("a_object_owner = ?, a_num_param = ?", parameterList.getSql());

        ArrayList<Object> expectedList = new ArrayList<>();
        expectedList.add("5: MyOwner");
        expectedList.add("6: 123");
        assertEquals( expectedList, resultArray);
    }
}
