package io.github.utplsql.api;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vinicius Avellar on 12/04/2017.
 */
public final class DBHelper {

    public static String newSysGuid(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN :id := sys_guid(); END;");
            callableStatement.registerOutParameter(":id", OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(":id");
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

}
