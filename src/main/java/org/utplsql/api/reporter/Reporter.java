package org.utplsql.api.reporter;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.utplsql.api.DBHelper;

import java.sql.*;
import java.util.Calendar;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class Reporter implements ORAData {

    protected String selfType;
    protected String id;
    protected Object[] attributes;

    public Reporter( String typeName, Object[] attributes ) {
        selfType = typeName;

        if ( attributes != null ) {
            this.id = String.valueOf(attributes[1]);
        }
        this.attributes = attributes;
    }

    private void setTypeName( String typeName ) {
        this.selfType = typeName.replaceAll("[^0-9a-zA-Z_]", "");
    }

    public void setParameters( Object[] parameters ) {
        // Empty method
    }

    public Reporter init( Connection con ) throws SQLException {

        OracleConnection oraConn = con.unwrap(OracleConnection.class);

        OracleCallableStatement callableStatement = (OracleCallableStatement) oraConn.prepareCall("{? = call " + selfType + "()}");
        callableStatement.registerOutParameter(1, OracleTypes.STRUCT, "UT_REPORTER_BASE");
        callableStatement.execute();

        Reporter obj = (Reporter) callableStatement.getORAData(1, ReporterFactory.getInstance());

        // TODO: Really override things
        this.attributes = obj.attributes;

        // Check whether we have output or not


        return this;
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
                StructDescriptor.createDescriptor(selfType, c);
        return new STRUCT(sd, c, attributes);
    }

}
