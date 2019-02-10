package org.utplsql.api.reporter;

import org.utplsql.api.ResourceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Consumer;

public class CoverageHTMLReporter extends DefaultReporter {

    // Could override Reporter.init and call ut_coverage_report_html_helper.get_default_html_assets_path from database,
    // but had permissions issues.
    public static final String DEFAULT_ASSETS_PATH = "https://utplsql.github.io/utPLSQL-coverage-html/assets/";

    private String projectName;
    private String assetsPath;

    public CoverageHTMLReporter() {
        super(CoreReporters.UT_COVERAGE_HTML_REPORTER.name(), null);
    }

    public CoverageHTMLReporter(String selfType, Object[] attributes) {
        super(selfType, attributes);
    }

    @Override
    protected void setAttributes(Object[] attributes) {
        super.setAttributes(attributes);

        if ( attributes != null ) {
            if ( attributes[3] != null )
                projectName = String.valueOf(attributes[3]);
            else
                projectName = null;

            if ( attributes[4] != null )
                assetsPath = String.valueOf(attributes[4]);
            else
                assetsPath = null;
        }
    }

    @Override
    protected Object[] getAttributes() {
        Object[] attributes = super.getAttributes();

        attributes[3] = projectName;
        attributes[4] = assetsPath;

        return attributes;
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

    /** Copies files from Classpath to a target directory.
     * Can omit the first x folders of the asset-path when copying to the target directory
     *
     * @param assetPath Path of the asset in the classpath
     * @param targetDirectory Target directory to copy the asset to
     * @param filterNumOfFolders Omits the first x folders of the path when copying the asset to the target directory
     * @throws IOException
     */
    private static void copyFileFromClasspath( Path assetPath, Path targetDirectory, int filterNumOfFolders ) throws IOException {

        Path assetStartPath = assetPath.subpath(filterNumOfFolders, assetPath.getNameCount());
        Path targetAssetPath = targetDirectory.resolve(Paths.get(assetStartPath.toString()));

        Files.createDirectories(targetAssetPath.getParent());

        try (InputStream is = CoverageHTMLReporter.class.getClassLoader()
                .getResourceAsStream(assetPath.toString())
        ) {
            Files.copy( is, targetAssetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Write the bundled assets necessary for the HTML Coverage report to a given targetPath
     *
     * @param targetDirectory Directory where the assets should be stored
     * @throws RuntimeException
     */
    public static void writeReportAssetsTo(Path targetDirectory) throws RuntimeException {

        try {
            Files.createDirectories(targetDirectory);

            List<Path> paths = ResourceUtil.getListOfChildren(Paths.get("CoverageHTMLReporter"), true);

            paths.forEach((ThrowingConsumer<Path>) p -> copyFileFromClasspath(p, targetDirectory, 1) );
        }
        catch ( IOException | URISyntaxException e ) {
            throw new RuntimeException(e);
        }
    }

    /** Functional Interface just to throw Exception from Consumer
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface ThrowingConsumer<T> extends Consumer<T> {

        @Override
        default void accept(final T elem) {
            try {
                acceptThrows(elem);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        void acceptThrows( T t ) throws IOException;
    }
}
