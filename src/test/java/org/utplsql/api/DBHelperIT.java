package org.utplsql.api;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.SQLException;

public class DBHelperIT {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

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
