package org.utplsql.api;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.SQLException;

public class DBHelperTest {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void compatibleVersion() {
        try {
            boolean isCompatible = DBHelper.versionCompatibilityCheck(db.newConnection(), "3.0.0", "3.0.0");
            Assert.assertTrue(isCompatible);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void incompatibleVersion() {
        try {
            boolean isCompatible = DBHelper.versionCompatibilityCheck(db.newConnection(), "3.1.0", "3.0.0");
            Assert.assertFalse(isCompatible);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void getFrameworkVersion()
    {
        try {
            Version v = DBHelper.getDatabaseFrameworkVersion(db.newConnection());
            Assert.assertEquals(true, v.isValid());
        } catch (SQLException e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
