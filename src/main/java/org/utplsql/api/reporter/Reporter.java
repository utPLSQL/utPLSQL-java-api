package org.utplsql.api.reporter;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.utplsql.api.compatibility.CompatibilityProxy;
import org.utplsql.api.outputBuffer.OutputBuffer;

import javax.xml.bind.DatatypeConverter;
import java.sql.Connection;
import java.sql.SQLException;

/** This is a basic Reporter implementation, using ORAData interface
 *
 * @author pesse
 */
public abstract class Reporter implements ORAData {

    private String selfType;
    private String id;
    private Object[] attributes;
    private boolean init = false;
    protected OutputBuffer outputBuffer;

    public Reporter( String typeName, Object[] attributes ) {
        setTypeName(typeName);
        setAttributes( attributes );
    }

    protected void setTypeName( String typeName ) {
        this.selfType = typeName.replaceAll("[^0-9a-zA-Z_\\.]", "");
    }


    public Reporter init(Connection con, CompatibilityProxy compatibilityProxy, ReporterFactory reporterFactory ) throws SQLException {

        if ( compatibilityProxy == null )
            compatibilityProxy = new CompatibilityProxy(con);
        if ( reporterFactory == null )
            reporterFactory = new ReporterFactory();

        OracleConnection oraConn = con.unwrap(OracleConnection.class);

        initDbReporter( oraConn, reporterFactory );

        init = true;

        initOutputBuffer( oraConn, compatibilityProxy);

        return this;
    }

    public Reporter init(Connection con) throws SQLException {
        return init(con, null, null);
    }

    protected abstract void initOutputBuffer( OracleConnection oraConn, CompatibilityProxy compatibilityProxy ) throws SQLException;

    /** Initializes the Reporter from database
     * This is necessary because we set up DefaultOutputBuffer (and maybe other stuff) we don't want to know and care about
     * in the java API. Let's just do the instantiation of the Reporter in the database and map it into this object.
     *
     * @param oraConn
     * @throws SQLException
     */
    private void initDbReporter( OracleConnection oraConn, ReporterFactory reporterFactory ) throws SQLException {
        OracleCallableStatement callableStatement = (OracleCallableStatement) oraConn.prepareCall("{? = call " + selfType + "()}");
        callableStatement.registerOutParameter(1, OracleTypes.STRUCT, "UT_REPORTER_BASE");
        callableStatement.execute();

        Reporter obj = (Reporter) callableStatement.getORAData(1, reporterFactory);

        setAttributes(obj.getAttributes());
    }

    protected void setAttributes(Object[] attributes ) {
        if (attributes != null) {
            this.id = DatatypeConverter.printHexBinary((byte[])attributes[1]);
        }
        this.attributes = attributes;
    }

    protected Object[] getAttributes() {
        return attributes;
    }

    public boolean isInit() {
        return init;
    }

    public String getTypeName() {
        return this.selfType;
    }

    public String getId() {
        return this.id;
    }


    public Datum toDatum(Connection c) throws SQLException
    {
        StructDescriptor sd =
                StructDescriptor.createDescriptor(getTypeName(), c);
        return new STRUCT(sd, c, getAttributes());
    }

    public OutputBuffer getOutputBuffer() {
        return outputBuffer;
    }
}
