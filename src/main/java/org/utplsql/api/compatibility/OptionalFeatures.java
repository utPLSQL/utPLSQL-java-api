package org.utplsql.api.compatibility;

import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;

import java.sql.Connection;
import java.sql.SQLException;

public enum OptionalFeatures {

    FAIL_ON_ERROR("3.0.3", null),
    FRAMEWORK_COMPATIBILITY_CHECK("3.0.3", null),
    CUSTOM_REPORTERS("3.1.0", null);

    private Version minVersion;
    private Version maxVersion;

    OptionalFeatures( String minVersion, String maxVersion )
    {
        if ( minVersion != null )
            this.minVersion = new Version(minVersion);
        if ( maxVersion != null)
            this.maxVersion = new Version(maxVersion);
    }

    public boolean isAvailableFor(Version version ) {

        try {
            if ((minVersion == null || version.isGreaterOrEqualThan(minVersion)) &&
                    (maxVersion == null || maxVersion.isGreaterOrEqualThan(version))
                    )
                return true;
            else
                return false;
        } catch ( InvalidVersionException e ) {
            return false; // We have no optional features for invalid versions
        }
    }

    public boolean isAvailableFor(Connection conn) throws SQLException {
        CompatibilityProxy proxy = new CompatibilityProxy(conn);
        return isAvailableFor(proxy.getDatabaseVersion());
    }
}
