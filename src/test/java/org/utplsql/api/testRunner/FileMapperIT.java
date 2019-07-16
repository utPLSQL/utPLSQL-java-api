package org.utplsql.api.testRunner;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.utplsql.api.AbstractDatabaseTest;
import org.utplsql.api.FileMapperOptions;
import org.utplsql.api.FileMapping;
import org.utplsql.api.KeyValuePair;
import org.utplsql.api.testRunner.FileMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FileMapperIT extends AbstractDatabaseTest {

    @Test
    void testFileMapper() throws SQLException {
        List<KeyValuePair> typeMappings = new ArrayList<>();
        typeMappings.add(new KeyValuePair("procedures", "PROCEDURE"));
        typeMappings.add(new KeyValuePair("functions", "FUNCTION"));

        List<String> filePaths = java.util.Arrays.asList(
                "sources/app/procedures/award_bonus.sql",
                "sources/app/functions/betwnstr.sql");

        FileMapperOptions mapperOptions = new FileMapperOptions(filePaths);
        mapperOptions.setObjectOwner("APP");
        mapperOptions.setTypeMappings(typeMappings);
        mapperOptions.setRegexPattern("\\w+[\\\\\\/](\\w+)[\\\\\\/](\\w+)[\\\\\\/](\\w+)[.](\\w{3})");
        mapperOptions.setOwnerSubExpression(1);
        mapperOptions.setTypeSubExpression(2);
        mapperOptions.setNameSubExpression(3);

        List<FileMapping> fileMappings = FileMapper.buildFileMappingList(getConnection(), mapperOptions);

        if (fileMappings.size() != 2) {
            fail("Wrong mapping list size.");
        }

        assertMapping(fileMappings.get(0), "APP", "AWARD_BONUS", "PROCEDURE");
        assertMapping(fileMappings.get(1), "APP", "BETWNSTR", "FUNCTION");
    }

    private void assertMapping(FileMapping fileMapping, String owner, String name, String type) {
        assertEquals(owner, fileMapping.getObjectOwner());
        assertEquals(name, fileMapping.getObjectName());
        assertEquals(type, fileMapping.getObjectType());
    }

    @Nested
    class Default_type_mapping {

        void checkTypeMapping( List<KeyValuePair> typeMappings ) throws SQLException {
            List<String> filePaths = java.util.Arrays.asList(
                    "/award_bonus.prc",
                    "/betwnstr.fnc",
                    "/package_body.pkb",
                    "/type_body.tpb",
                    "/trigger.trg");
            FileMapperOptions mapperOptions = new FileMapperOptions(filePaths);
            mapperOptions.setTypeMappings(typeMappings);

            List<FileMapping> fileMappings = FileMapper.buildFileMappingList(getConnection(), mapperOptions);

            assertEquals("PROCEDURE", fileMappings.get(0).getObjectType());
            assertEquals("FUNCTION", fileMappings.get(1).getObjectType());
            assertEquals("PACKAGE BODY", fileMappings.get(2).getObjectType());
            assertEquals("TYPE BODY", fileMappings.get(3).getObjectType());
            assertEquals("TRIGGER", fileMappings.get(4).getObjectType());
        }

        @Test
        void is_used_on_null_parameter() throws SQLException {
            checkTypeMapping(null);
        }

        @Test
        void is_used_on_empty_parameter() throws SQLException {
            checkTypeMapping(new ArrayList<>());
        }
    }

}
