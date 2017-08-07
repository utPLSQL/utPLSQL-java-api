package org.utplsql.api;

import org.utplsql.api.rules.DatabaseRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileMapperTest {

    @Rule
    public final DatabaseRule db = new DatabaseRule();

    @Test
    public void testFileMapper() throws SQLException {
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

        List<FileMapping> fileMappings = FileMapper.buildFileMappingList(db.newConnection(), mapperOptions);

        if (fileMappings.size() != 2)
            Assert.fail("Wrong mapping list size.");

        assertMapping(fileMappings.get(0), "APP", "AWARD_BONUS", "PROCEDURE");
        assertMapping(fileMappings.get(1), "APP", "BETWNSTR", "FUNCTION");
    }

    private void assertMapping(FileMapping fileMapping, String owner, String name, String type) {
        Assert.assertEquals(owner, fileMapping.getObjectOwner());
        Assert.assertEquals(name, fileMapping.getObjectName());
        Assert.assertEquals(type, fileMapping.getObjectType());
    }

}
