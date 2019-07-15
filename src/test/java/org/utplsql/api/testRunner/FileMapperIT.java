package org.utplsql.api.testRunner;

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

}
