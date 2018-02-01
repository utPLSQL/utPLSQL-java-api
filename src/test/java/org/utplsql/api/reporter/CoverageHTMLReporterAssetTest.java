package org.utplsql.api.reporter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CoverageHTMLReporterAssetTest {

    private static final String TEST_FOLDER = "__testAssets";

    private void testFileExists( Path filePath )
    {
        File f = new File( filePath.toUri() );

        assertTrue(f.exists(), () -> "File " + f.toString() + " does not exist");
    }

    @Test
    public void writeReporterAssetsTo()
    {
        try {
            Path targetPath = Paths.get(TEST_FOLDER);

            // Act
            CoverageHTMLReporter.writeReportAssetsTo(targetPath);

            testFileExists(targetPath.resolve(Paths.get("colorbox", "border.png")));
            testFileExists(targetPath.resolve(Paths.get("colorbox", "controls.png")));
            testFileExists(targetPath.resolve(Paths.get("colorbox", "loading.gif")));
            testFileExists(targetPath.resolve(Paths.get("colorbox", "loading_background.png")));

            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_flat_0_aaaaaa_40x100.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_flat_75_ffffff_40x100.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_glass_55_fbf9ee_1x400.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_glass_65_ffffff_1x400.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_glass_75_dadada_1x400.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_glass_75_e6e6e6_1x400.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_glass_95_fef1ec_1x400.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-bg_highlight-soft_75_cccccc_1x100.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-icons_2e83ff_256x240.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-icons_222222_256x240.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-icons_454545_256x240.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-icons_888888_256x240.png")));
            testFileExists(targetPath.resolve(Paths.get("images", "ui-icons_cd0a0a_256x240.png")));

            testFileExists(targetPath.resolve(Paths.get("application.css")));
            testFileExists(targetPath.resolve(Paths.get("application.js")));
            testFileExists(targetPath.resolve(Paths.get("favicon_green.png")));
            testFileExists(targetPath.resolve(Paths.get("favicon_red.png")));
            testFileExists(targetPath.resolve(Paths.get("favicon_yellow.png")));
            testFileExists(targetPath.resolve(Paths.get("loading.gif")));
            testFileExists(targetPath.resolve(Paths.get("magnify.png")));
        }
        catch ( IOException e )
        {
            fail(e.getMessage());
        }

    }

    @AfterAll
    public static void clearTestAssetsFolder() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
