package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.compatibility.OptionalFeatures;
import org.utplsql.api.exception.InvalidVersionException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalFeaturesIT extends AbstractDatabaseTest {


    private Version getDatabaseVersion() throws SQLException {
        return new CompatibilityProxy(getConnection()).getUtPlsqlVersion();
    }

    @Test
    void failOnError() throws SQLException, InvalidVersionException {

        boolean available = OptionalFeatures.FAIL_ON_ERROR.isAvailableFor(getConnection());

        if (getDatabaseVersion().isGreaterOrEqualThan(Version.V3_0_3)) {
            assertTrue(available);
        } else {
            assertFalse(available);
        }
    }

    @Test
    void frameworkCompatibilityCheck() throws SQLException, InvalidVersionException {

        boolean available = OptionalFeatures.FRAMEWORK_COMPATIBILITY_CHECK.isAvailableFor(getConnection());

        if (getDatabaseVersion().isGreaterOrEqualThan(Version.V3_0_3)) {
            assertTrue(available);
        } else {
            assertFalse(available);
        }
    }

    @Test
    void customReporters() throws SQLException, InvalidVersionException {

        boolean available = OptionalFeatures.CUSTOM_REPORTERS.isAvailableFor(getConnection());

        if (getDatabaseVersion().isGreaterOrEqualThan(Version.V3_1_0)) {
            assertTrue(available);
        } else {
            assertFalse(available);
        }
    }
}
