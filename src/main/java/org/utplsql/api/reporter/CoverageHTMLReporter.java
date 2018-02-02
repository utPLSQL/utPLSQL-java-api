package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class CoverageHTMLReporter extends Reporter {

    // Could override Reporter.init and call ut_coverage_report_html_helper.get_default_html_assets_path from database,
    // but had permissions issues.
    public static final String DEFAULT_ASSETS_PATH = "https://utplsql.github.io/utPLSQL-coverage-html/assets/";

    private String projectName;
    private String assetsPath;

    public CoverageHTMLReporter() {
        this(null, DEFAULT_ASSETS_PATH);
    }

    public CoverageHTMLReporter(String projectName, String assetsPath) {
        this.projectName = projectName;
        this.assetsPath = assetsPath;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return CustomTypes.UT_COVERAGE_HTML_REPORTER;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(String assetsPath) {
        this.assetsPath = assetsPath;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        super.readSQL(stream, typeName);
        setProjectName(stream.readString());
        setAssetsPath(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        super.writeSQL(stream);
        stream.writeString(getProjectName());
        stream.writeString(getAssetsPath());
    }

    private static void copyFileFromClasspath( Path assetPath, Path targetDirectory ) throws IOException {
        Files.createDirectories(targetDirectory.resolve(assetPath).getParent());

        try (InputStream is = CoverageHTMLReporter.class.getClassLoader()
                .getResourceAsStream(
                        Paths.get("CoverageHTMLReporter").resolve(assetPath).toString()
                )
        ) {
            Files.copy( is, targetDirectory.resolve(assetPath) );
        }
    }

    /** Write the bundled assets necessary for the HTML Coverage report to a given targetPath
     *
     * @param targetDirectory Directory where the assets should be stored
     * @throws IOException
     */
    public static void writeReportAssetsTo(Path targetDirectory) throws IOException {

        Files.createDirectories(targetDirectory);

        // Simplest approach to start with
        copyFileFromClasspath(Paths.get("application.css"), targetDirectory);
        copyFileFromClasspath(Paths.get("application.js"), targetDirectory);
        copyFileFromClasspath(Paths.get("favicon_green.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("favicon_red.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("favicon_yellow.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("loading.gif"), targetDirectory);
        copyFileFromClasspath(Paths.get("magnify.png"), targetDirectory);

        copyFileFromClasspath(Paths.get("colorbox", "border.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("colorbox", "controls.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("colorbox", "loading.gif"), targetDirectory);
        copyFileFromClasspath(Paths.get("colorbox", "loading_background.png"), targetDirectory);

        copyFileFromClasspath(Paths.get("images", "ui-bg_flat_0_aaaaaa_40x100.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_flat_75_ffffff_40x100.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_glass_55_fbf9ee_1x400.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_glass_65_ffffff_1x400.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_glass_75_dadada_1x400.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_glass_75_e6e6e6_1x400.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_glass_95_fef1ec_1x400.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-bg_highlight-soft_75_cccccc_1x100.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-icons_2e83ff_256x240.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-icons_222222_256x240.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-icons_454545_256x240.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-icons_888888_256x240.png"), targetDirectory);
        copyFileFromClasspath(Paths.get("images", "ui-icons_cd0a0a_256x240.png"), targetDirectory);


    }
}
