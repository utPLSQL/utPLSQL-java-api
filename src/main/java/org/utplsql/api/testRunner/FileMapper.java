package org.utplsql.api.testRunner;


import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.utplsql.api.CustomTypes;
import org.utplsql.api.FileMapperOptions;
import org.utplsql.api.FileMapping;
import org.utplsql.api.KeyValuePair;
import org.utplsql.api.db.DynamicParameterList;

import java.sql.*;
import java.util.*;

final class FileMapper {

    private static final Logger logger = LoggerFactory.getLogger(FileMapper.class);

    private FileMapper() {
    }

    /**
     * Call the database api to build the custom file mappings.
     */
    private static Array buildFileMappingArray(
            Connection conn, FileMapperOptions mapperOptions) throws SQLException {
        OracleConnection oraConn = conn.unwrap(OracleConnection.class);

        Map<String, Class<?>> typeMap = conn.getTypeMap();
        typeMap.put(CustomTypes.UT_FILE_MAPPING, FileMapping.class);
        typeMap.put(CustomTypes.UT_KEY_VALUE_PAIR, KeyValuePair.class);
        conn.setTypeMap(typeMap);

        logger.debug("Building fileMappingArray");
        final Object[] filePathsArray = mapperOptions.getFilePaths().toArray();
        for ( Object elem : filePathsArray ) {
            logger.debug("Path: " + elem);
        }
        Object[] typeMapArray = null;
        if ( mapperOptions.getTypeMappings() != null ) {
            typeMapArray = mapperOptions.getTypeMappings().toArray();
        }

        DynamicParameterList parameterList =  DynamicParameterList.builder()
                .add("a_file_paths", filePathsArray, CustomTypes.UT_VARCHAR2_LIST, oraConn)
                .onlyAddIfNotEmpty()
                .add("a_object_owner", mapperOptions.getObjectOwner())
                .add("a_file_to_object_type_mapping", typeMapArray, CustomTypes.UT_KEY_VALUE_PAIRS, oraConn)
                .add("a_regex_pattern", mapperOptions.getRegexPattern())
                .add("a_object_owner_subexpression", mapperOptions.getOwnerSubExpression())
                .add("a_object_name_subexpression", mapperOptions.getNameSubExpression())
                .add("a_object_type_subexpression", mapperOptions.getTypeSubExpression())
                .build();

        CallableStatement callableStatement = conn.prepareCall(
                "BEGIN " +
                        "? := ut_file_mapper.build_file_mappings(" +
                        parameterList.getSql() +
                        "); " +
                        "END;");

        int paramIdx = 0;
        callableStatement.registerOutParameter(++paramIdx, OracleTypes.ARRAY, CustomTypes.UT_FILE_MAPPINGS);

        parameterList.setParamsStartWithIndex(callableStatement, ++paramIdx);

        callableStatement.execute();
        return callableStatement.getArray(1);
    }

    static List<FileMapping> buildFileMappingList(
            Connection conn, FileMapperOptions mapperOptions) throws SQLException {
        java.sql.Array fileMappings = buildFileMappingArray(conn, mapperOptions);

        List<FileMapping> mappingList = new ArrayList<>();
        for (Object obj : (Object[]) fileMappings.getArray()) {
            mappingList.add((FileMapping) obj);
        }

        return mappingList;
    }

}
