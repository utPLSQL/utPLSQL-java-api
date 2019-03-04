package org.utplsql.api.exception;

import java.sql.SQLException;

/**
 * Exception to track when utPLSQL framework is not installed or accessible on the used database
 *
 * @author pesse
 */
public class UtPLSQLNotInstalledException extends SQLException {

    public static final int ERROR_CODE = 904;

    public UtPLSQLNotInstalledException(SQLException cause) {
        super("utPLSQL framework is not installed on your database or not accessable to the user you are connected with", cause);
    }
}
