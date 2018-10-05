package org.utplsql.api.db;

import org.utplsql.api.Version;

import java.sql.Connection;
import java.sql.SQLException;

/** Abstraction-interface to encapsulate Database-Calls (and potentially mock them)
 *
 * @author pesse
 */
public interface DatabaseInformation {

    Version getUtPlsqlFrameworkVersion(Connection conn ) throws SQLException;

    String getOracleVersion( Connection conn ) throws SQLException;

    String getCurrentSchema( Connection conn ) throws SQLException;

    int frameworkCompatibilityCheck(Connection conn, String requested, String current) throws SQLException;
}
