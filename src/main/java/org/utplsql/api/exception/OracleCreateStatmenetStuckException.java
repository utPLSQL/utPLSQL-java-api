package org.utplsql.api.exception;

import java.sql.SQLException;

public class OracleCreateStatmenetStuckException extends SQLException {
    public OracleCreateStatmenetStuckException(Throwable cause) {
        super("Oracle driver stuck during creating the TestRunner statement. Retry.", cause);
    }
}
