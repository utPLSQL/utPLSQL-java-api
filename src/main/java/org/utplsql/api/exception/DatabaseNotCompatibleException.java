package org.utplsql.api.exception;

import org.utplsql.api.DBHelper;

import java.sql.SQLException;

/** Custom exception to indicate API is not compatible with database framework
 *
 * @author pesse
 *
 */
public class DatabaseNotCompatibleException extends SQLException {

    private String clientVersion;
    private String databaseVersion;

    public DatabaseNotCompatibleException( String message, String clientVersion, String databaseVersion, Throwable cause )
    {
        super(message, cause);

        this.clientVersion = clientVersion;
        this.databaseVersion = databaseVersion;
    }

    public DatabaseNotCompatibleException( String clientVersion, String databaseVersion, Throwable cause )
    {
        this("utPLSQL API (" + String.valueOf(clientVersion) + ") not compatible with database (" + String.valueOf(databaseVersion) + ")", clientVersion, databaseVersion, cause);
    }

    public DatabaseNotCompatibleException( String clientVersion, String databaseVersion )
    {
        this(clientVersion, databaseVersion, null);
    }

    public DatabaseNotCompatibleException( String databaseVersion, Throwable cause )
    {
        this(DBHelper.UTPLSQL_COMPATIBILITY_VERSION, databaseVersion, cause );
    }

    public DatabaseNotCompatibleException( String databaseVersion )
    {
        this(DBHelper.UTPLSQL_COMPATIBILITY_VERSION, databaseVersion, null );
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getDatabaseVersion()
    {
        return databaseVersion;
    }
}
