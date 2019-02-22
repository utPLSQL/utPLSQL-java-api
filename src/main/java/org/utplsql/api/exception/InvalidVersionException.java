package org.utplsql.api.exception;

import org.utplsql.api.Version;

/**
 * Exception thrown when trying to do stuff which requires a valid version object (like comparing)
 *
 * @author pesse
 */
public class InvalidVersionException extends Exception {
    private final Version version;

    public InvalidVersionException(Version version) {
        this(version, null);
    }

    public InvalidVersionException(Version version, Throwable cause) {
        super("Version '" + version.toString() + "' is invalid", cause);

        this.version = version;
    }

    public Version getVersion() {
        return version;
    }
}
