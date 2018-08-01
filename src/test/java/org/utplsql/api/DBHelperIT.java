package org.utplsql.api;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBHelperIT extends AbstractDatabaseTest {

    @Test
    public void getFrameworkVersion() throws SQLException {
        Version v = DBHelper.getDatabaseFrameworkVersion(getConnection());
        assertTrue(v.isValid());
        System.out.println(v.getNormalizedString() + " - " + v.toString());
    }

    @Test
    public void getOracleDatabaseVersion() throws SQLException {
        String databaseVersion = DBHelper.getOracleDatabaseVersion(getConnection());
        assertNotNull(databaseVersion);
    }

}
