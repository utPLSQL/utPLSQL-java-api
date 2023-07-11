package org.utplsql.api.reporter;

import org.utplsql.api.ResourceUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    /**
     * Write the bundled assets necessary for the HTML Coverage report to a given targetPath
     *
     * @param targetDirectory Directory where the assets should be stored
     */
    protected static void writeReportAssetsTo(Path targetDirectory) {
        ResourceUtil.copyResources(Paths.get("CoverageHTMLReporter"), targetDirectory);
    }

    @Override
    protected Object[] getAttributes() {
        Object[] attributes = super.getAttributes();

        attributes[3] = projectName;
        attributes[4] = assetsPath;

        return attributes;
    }

    @Override
    protected void setAttributes(Object[] attributes) {
        super.setAttributes(attributes);

        if (attributes != null) {
            if (attributes[3] != null) {
                projectName = String.valueOf(attributes[3]);
            } else {
                projectName = null;
            }

            if (attributes[4] != null) {
                assetsPath = String.valueOf(attributes[4]);
            } else {
                assetsPath = null;
            }
        }
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

}
