package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompatibilityIT extends AbstractDatabaseTest {


    @Test
    public void compatibleVersion() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());
        proxy.failOnNotCompatible();
        assertEquals(true, proxy.isCompatible());
    }

    @Test
    public void skipCompatibilityCheck() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection(), true);
        proxy.failOnNotCompatible();
        assertEquals(true, proxy.isCompatible());
        assertEquals(CompatibilityProxy.UTPLSQL_API_VERSION, proxy.getDatabaseVersion().toString());

    }
}
