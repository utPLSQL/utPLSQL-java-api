package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.db.DatabaseInformation;
import org.utplsql.api.db.DefaultDatabaseInformation;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseInformationIT extends AbstractDatabaseTest {

    @Test
    void getFrameworkVersion() throws SQLException {
        DatabaseInformation databaseInformation = new DefaultDatabaseInformation();

        Version v = databaseInformation.getUtPlsqlFrameworkVersion(getConnection());
        assertTrue(v.isValid());
        System.out.println(v.getNormalizedString() + " - " + v);
    }

    @Test
    void getOracleDatabaseVersion() throws SQLException {
        DatabaseInformation databaseInformation = new DefaultDatabaseInformation();

        String databaseVersion = databaseInformation.getOracleVersion(getConnection());
        assertNotNull(databaseVersion);
    }
}
