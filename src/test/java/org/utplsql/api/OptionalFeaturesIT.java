package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.compatibility.OptionalFeatures;
import org.utplsql.api.exception.InvalidVersionException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionalFeaturesIT extends AbstractDatabaseTest {


    private Version getDatabaseVersion() throws SQLException {
        return new CompatibilityProxy(getConnection()).getDatabaseVersion();
    }

    @Test
    public void failOnError() throws SQLException, InvalidVersionException {

        boolean available = OptionalFeatures.FAIL_ON_ERROR.isAvailableFor(getConnection());

        if (getDatabaseVersion().isGreaterOrEqualThan(new Version("3.0.3")))
            assertEquals(true, available);
        else
            assertEquals(false, available);
    }

    @Test
    public void frameworkCompatibilityCheck() throws SQLException, InvalidVersionException {

        boolean available = OptionalFeatures.FRAMEWORK_COMPATIBILITY_CHECK.isAvailableFor(getConnection());

        if (getDatabaseVersion().isGreaterOrEqualThan(new Version("3.0.3")))
            assertEquals(true, available);
        else
            assertEquals(false, available);
    }

    @Test
    public void customReporters() throws SQLException, InvalidVersionException {

        boolean available = OptionalFeatures.CUSTOM_REPORTERS.isAvailableFor(getConnection());

        if (getDatabaseVersion().isGreaterOrEqualThan(new Version("3.1.0")))
            assertEquals(true, available);
        else
            assertEquals(false, available);
    }
}
