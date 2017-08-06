package org.utplsql.api;


import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FileMapper {

    private FileMapper() {}

    /**
     * Call the database api to build the custom file mappings.
     */
    public static Array buildFileMappingArray(
            Connection conn, FileMapperOptions mapperOptions) throws SQLException {
        OracleConnection oraConn = conn.unwrap(OracleConnection.class);

        Map typeMap = conn.getTypeMap();
        typeMap.put(CustomTypes.UT_FILE_MAPPING, FileMapping.class);
        typeMap.put(CustomTypes.UT_KEY_VALUE_PAIR, KeyValuePair.class);
        conn.setTypeMap(typeMap);

        CallableStatement callableStatement = conn.prepareCall(
                "BEGIN " +
                    "? := ut_file_mapper.build_file_mappings(" +
                        "a_object_owner                => ?, " +
                        "a_file_paths                  => ?, " +
                        "a_file_to_object_type_mapping => ?, " +
                        "a_regex_pattern               => ?, " +
                        "a_object_owner_subexpression  => ?, " +
                        "a_object_name_subexpression   => ?, " +
                        "a_object_type_subexpression   => ?); " +
                "END;");

        int paramIdx = 0;
        callableStatement.registerOutParameter(++paramIdx, OracleTypes.ARRAY, CustomTypes.UT_FILE_MAPPINGS);

        if (mapperOptions.getRegexPattern() == null) {
            callableStatement.setNull(++paramIdx, Types.VARCHAR);
        } else {
            callableStatement.setString(++paramIdx, mapperOptions.getObjectOwner());
        }

        callableStatement.setArray(
                ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_VARCHAR2_LIST, mapperOptions.getFilePaths().toArray()));

        if (mapperOptions.getTypeMappings() == null) {
            callableStatement.setNull(++paramIdx, Types.ARRAY, CustomTypes.UT_KEY_VALUE_PAIR);
        } else {
            callableStatement.setArray(
                    ++paramIdx, oraConn.createOracleArray(CustomTypes.UT_KEY_VALUE_PAIRS, mapperOptions.getTypeMappings().toArray()));
        }

        if (mapperOptions.getRegexPattern() == null) {
            callableStatement.setNull(++paramIdx, Types.VARCHAR);
        } else {
            callableStatement.setString(++paramIdx, mapperOptions.getRegexPattern());
        }

        if (mapperOptions.getOwnerSubExpression() == null) {
            callableStatement.setNull(++paramIdx, Types.INTEGER);
        } else {
            callableStatement.setInt(++paramIdx, mapperOptions.getOwnerSubExpression());
        }

        if (mapperOptions.getNameSubExpression() == null) {
            callableStatement.setNull(++paramIdx, Types.INTEGER);
        } else {
            callableStatement.setInt(++paramIdx, mapperOptions.getNameSubExpression());
        }

        if (mapperOptions.getTypeSubExpression() == null) {
            callableStatement.setNull(++paramIdx, Types.INTEGER);
        } else {
            callableStatement.setInt(++paramIdx, mapperOptions.getTypeSubExpression());
        }

        callableStatement.execute();
        return callableStatement.getArray(1);
    }

    public static List<FileMapping> buildFileMappingList(
            Connection conn, FileMapperOptions mapperOptions) throws SQLException {
        java.sql.Array fileMappings = buildFileMappingArray(conn, mapperOptions);

        List<FileMapping> mappingList = new ArrayList<>();
        for (Object obj : (Object[]) fileMappings.getArray()) {
            mappingList.add((FileMapping) obj);
        }

        return mappingList;
    }

}
