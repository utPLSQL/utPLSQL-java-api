package org.utplsql.api;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBHelperIT extends AbstractDatabaseTest {

    @Test
    void getFrameworkVersion() throws SQLException {
        Version v = DBHelper.getDatabaseFrameworkVersion(getConnection());
        assertTrue(v.isValid());
        System.out.println(v.getNormalizedString() + " - " + v.toString());
    }

    @Test
    void getOracleDatabaseVersion() throws SQLException {
        String databaseVersion = DBHelper.getOracleDatabaseVersion(getConnection());
        assertNotNull(databaseVersion);
    }

}
