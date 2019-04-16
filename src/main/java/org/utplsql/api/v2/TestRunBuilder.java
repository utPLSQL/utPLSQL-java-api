package org.utplsql.api.v2;

import org.utplsql.api.FileMapperOptions;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Pavel Kaplya on 11.02.2019.
 */
public interface TestRunBuilder {

    TestRunBuilder paths(List<String> paths);

    TestRunBuilder colorConsole(boolean colorConsole);

    TestRunBuilder addCoverageScheme(String coverageScheme);

    TestRunBuilder includeObjects(List<String> obj);

    TestRunBuilder excludeObjects(List<String> obj);

    TestRunBuilder sourceMappingOptions(FileMapperOptions mapperOptions);

    TestRunBuilder testMappingOptions(FileMapperOptions mapperOptions);

    TestRunBuilder failOnErrors(boolean failOnErrors);

    TestRunBuilder skipCompatibilityCheck(boolean skipCompatibilityCheck);

    TestRunBuilderImpl clientCharacterSet(Charset clientCharacterSet);

    TestRun build();
}
