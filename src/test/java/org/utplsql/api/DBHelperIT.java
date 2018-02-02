package org.utplsql.api;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.utplsql.api.rules.DatabaseRule;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DBHelperIT {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void getFrameworkVersion()
    {
        try {
            Version v = DBHelper.getDatabaseFrameworkVersion(db.newConnection());
            assertEquals(true, v.isValid());
        } catch (SQLException e)
        {
            fail(e);
        }
    }

}
