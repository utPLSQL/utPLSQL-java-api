package org.utplsql.api.v2;

import org.utplsql.api.FileMapperOptions;
import org.utplsql.api.reporter.Reporter;

import java.util.List;

/**
 * Created by Pavel Kaplya on 11.02.2019.
 */
public interface TestRunBuilder {
    TestRunBuilder addPath(String path);

    TestRunBuilder paths(List<String> paths);

    TestRunBuilder addReporter(Reporter reporter);

    TestRunBuilder colorConsole(boolean colorConsole);

    TestRunBuilder addReporterList(List<Reporter> reporterList);

    TestRunBuilder addCoverageScheme(String coverageScheme);

    TestRunBuilder includeObject(String obj);

    TestRunBuilder excludeObject(String obj);

    TestRunBuilder includeObjects(List<String> obj);

    TestRunBuilder excludeObjects(List<String> obj);

    TestRunBuilder sourceMappingOptions(FileMapperOptions mapperOptions);

    TestRunBuilder testMappingOptions(FileMapperOptions mapperOptions);

    TestRunBuilder failOnErrors(boolean failOnErrors);

    TestRunBuilder skipCompatibilityCheck(boolean skipCompatibilityCheck);

    //TestRunBuilder paths(List<String> app);

    TestRun build();
}
