package org.utplsql.api.exception;

import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * Custom exception class to indicate if some tests failed.
 */
public class SomeTestsFailedException extends SQLException {

    public static final int ERROR_CODE = 20213;

    public SomeTestsFailedException(String reason, @Nullable Throwable cause) {
        super(reason, cause);
    }

}
