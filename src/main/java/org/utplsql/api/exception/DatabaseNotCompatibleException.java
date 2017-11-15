package org.utplsql.api.exception;

import org.utplsql.api.DBHelper;
import org.utplsql.api.Version;
import org.utplsql.api.compatibility.CompatibilityProxy;

import java.sql.SQLException;

/** Custom exception to indicate API is not compatible with database framework
 *
 * @author pesse
 *
 */
public class DatabaseNotCompatibleException extends SQLException {

    private Version clientVersion;
    private Version databaseVersion;

    public DatabaseNotCompatibleException( String message, Version clientVersion, Version databaseVersion, Throwable cause )
    {
        super(message, cause);

        this.clientVersion = clientVersion;
        this.databaseVersion = databaseVersion;
    }

    public DatabaseNotCompatibleException( Version clientVersion, Version databaseVersion, Throwable cause )
    {
        this("utPLSQL API (" + String.valueOf(clientVersion) + ") not compatible with database (" + String.valueOf(databaseVersion) + ")", clientVersion, databaseVersion, cause);
    }

    public DatabaseNotCompatibleException( Version clientVersion, Version databaseVersion )
    {
        this(clientVersion, databaseVersion, null);
    }

    public DatabaseNotCompatibleException( Version databaseVersion, Throwable cause )
    {
        this(new Version(CompatibilityProxy.UTPLSQL_COMPATIBILITY_VERSION), databaseVersion, cause );
    }

    public DatabaseNotCompatibleException( Version databaseVersion )
    {
        this(new Version(CompatibilityProxy.UTPLSQL_COMPATIBILITY_VERSION), databaseVersion, null );
    }

    public Version getClientVersion() {
        return clientVersion;
    }

    public Version getDatabaseVersion()
    {
        return databaseVersion;
    }
}
