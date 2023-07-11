package org.utplsql.api.reporter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("binary")
class CoverageHTMLReporterAssetTest {

    private static final String TEST_FOLDER = "__testAssets";

    @TempDir
    Path tempDir;

    private void testFileExists(Path filePath) {
        File f = new File(tempDir.resolve(TEST_FOLDER).resolve(filePath).toUri());

        assertTrue(f.exists(), () -> "File " + f + " does not exist");
    }

    @Disabled("No idea why this ever worked")
    @Test
    void writeReporterAssetsTo() throws RuntimeException {

        Path targetPath = tempDir.resolve(TEST_FOLDER);

        // Act
        CoverageHTMLReporter.writeReportAssetsTo(targetPath);

        testFileExists(Paths.get("colorbox", "border.png"));
        testFileExists(Paths.get("colorbox", "controls.png"));
        testFileExists(Paths.get("colorbox", "loading.gif"));
        testFileExists(Paths.get("colorbox", "loading_background.png"));

        testFileExists(Paths.get("images", "ui-bg_flat_0_aaaaaa_40x100.png"));
        testFileExists(Paths.get("images", "ui-bg_flat_75_ffffff_40x100.png"));
        testFileExists(Paths.get("images", "ui-bg_glass_55_fbf9ee_1x400.png"));
        testFileExists(Paths.get("images", "ui-bg_glass_65_ffffff_1x400.png"));
        testFileExists(Paths.get("images", "ui-bg_glass_75_dadada_1x400.png"));
        testFileExists(Paths.get("images", "ui-bg_glass_75_e6e6e6_1x400.png"));
        testFileExists(Paths.get("images", "ui-bg_glass_95_fef1ec_1x400.png"));
        testFileExists(Paths.get("images", "ui-bg_highlight-soft_75_cccccc_1x100.png"));
        testFileExists(Paths.get("images", "ui-icons_2e83ff_256x240.png"));
        testFileExists(Paths.get("images", "ui-icons_222222_256x240.png"));
        testFileExists(Paths.get("images", "ui-icons_454545_256x240.png"));
        testFileExists(Paths.get("images", "ui-icons_888888_256x240.png"));
        testFileExists(Paths.get("images", "ui-icons_cd0a0a_256x240.png"));

        testFileExists(Paths.get("application.css"));
        testFileExists(Paths.get("application.js"));
        testFileExists(Paths.get("favicon_green.png"));
        testFileExists(Paths.get("favicon_red.png"));
        testFileExists(Paths.get("favicon_yellow.png"));
        testFileExists(Paths.get("loading.gif"));
        testFileExists(Paths.get("magnify.png"));

    }
}
