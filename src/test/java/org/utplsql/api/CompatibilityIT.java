package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CompatibilityIT extends AbstractDatabaseTest {


    @Test
    void compatibleVersion() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection());
        proxy.failOnNotCompatible();
        assertTrue(proxy.isCompatible());
    }

    @Test
    void skipCompatibilityCheck() throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(getConnection(), true);
        proxy.failOnNotCompatible();
        assertTrue(proxy.isCompatible());

    }
}
