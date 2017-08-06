package io.github.utplsql.api.exception;

import java.sql.SQLException;

/**
 * Custom exception class to indicate if some tests failed.
 */
public class SomeTestsFailedException extends SQLException {

    public static final int ERROR_CODE = 20213;

    public SomeTestsFailedException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
