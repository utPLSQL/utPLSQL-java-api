package org.utplsql.api.reporter;

import oracle.jdbc.OracleConnection;
import org.utplsql.api.compatibility.CompatibilityProxy;

import java.sql.SQLException;

/** This is a basic Reporter implementation, using ORAData interface
 *
 * @author pesse
 */
public class DefaultReporter extends Reporter {

    public DefaultReporter(String typeName, Object[] attributes ) {
        super(typeName, attributes);
    }

    @Override
    protected void initOutputBuffer( OracleConnection oraConn, CompatibilityProxy compatibilityProxy ) throws SQLException {
        outputBuffer = compatibilityProxy.getOutputBuffer(this, oraConn);
    }
}
