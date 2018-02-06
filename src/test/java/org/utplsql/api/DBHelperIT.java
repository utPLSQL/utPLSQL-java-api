package org.utplsql.api;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBHelperIT extends AbstractDatabaseTest {

    @Test
    public void getFrameworkVersion() throws SQLException {
        Version v = DBHelper.getDatabaseFrameworkVersion(getConnection());
        assertEquals(true, v.isValid());
    }

}
