package org.utplsql.api.reporter;

import org.utplsql.api.compatibility.CompatibilityProxy;

/**
 * Helper-class which registers default ReporterFactoryMethods based on the given databaseVersion
 *
 * @author pesse
 */
class DefaultReporterFactoryMethodRegistrator {

    public static void registerDefaultReporters(ReporterFactory reporterFactory, CompatibilityProxy compatibilityProxy) {

        // At the moment we don't have version-specific reporters which need a special MethodFactory
        reporterFactory.registerReporterFactoryMethod(CoreReporters.UT_DOCUMENTATION_REPORTER.name(), DocumentationReporter::new, "Provides additional properties lvl and failed");
        reporterFactory.registerReporterFactoryMethod(CoreReporters.UT_COVERAGE_HTML_REPORTER.name(), CoverageHTMLReporter::new, "Provides additional properties projectName and assetPath");
    }


}
