package org.utplsql.api.compatibility;

import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;

import java.sql.Connection;
import java.sql.SQLException;

public enum OptionalFeatures {

    FAIL_ON_ERROR("3.0.3.1266", null),
    FRAMEWORK_COMPATIBILITY_CHECK("3.0.3.1266", null),
    CUSTOM_REPORTERS("3.1.0.1849", null),
    CLIENT_CHARACTER_SET("3.1.2.2130", null),
    RANDOM_EXECUTION_ORDER("3.1.7.2795", null);

    private final Version minVersion;
    private final Version maxVersion;

    OptionalFeatures(String minVersion, String maxVersion) {
        this.minVersion = minVersion != null ? Version.create(minVersion) : null;
        this.maxVersion = maxVersion != null ? Version.create(maxVersion) : null;
    }

    public boolean isAvailableFor(Version version) {

        try {
            return (minVersion == null || version.isGreaterOrEqualThan(minVersion)) &&
                    (maxVersion == null || maxVersion.isGreaterOrEqualThan(version));
        } catch (InvalidVersionException e) {
            return false; // We have no optional features for invalid versions
        }
    }

    public boolean isAvailableFor(Connection conn) throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(conn);
        return isAvailableFor(proxy.getUtPlsqlVersion());
    }

    public Version getMinVersion() { return minVersion; }

    public Version getMaxVersion() { return maxVersion; }
}
