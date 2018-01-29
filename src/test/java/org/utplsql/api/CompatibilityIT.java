package org.utplsql.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.Connection;
import java.sql.SQLException;

public class CompatibilityIT {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void compatibleVersion() {
        try {
            Connection conn = db.newConnection();
            CompatibilityProxy proxy = new CompatibilityProxy(conn);
            proxy.failOnNotCompatible();
            Assert.assertEquals(true, proxy.isCompatible());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void skipCompatibilityCheck()
    {
        try {
            Connection conn = db.newConnection();
            CompatibilityProxy proxy = new CompatibilityProxy(conn, true);
            proxy.failOnNotCompatible();
            Assert.assertEquals(true, proxy.isCompatible());
            Assert.assertEquals(CompatibilityProxy.UTPLSQL_API_VERSION, proxy.getDatabaseVersion().toString());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
