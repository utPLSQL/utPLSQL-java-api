package org.utplsql.api.v2;

import org.utplsql.api.FileMapperOptions;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Holds the various possible options of TestRunner
 *
 * @author pesse
 */
class TestRunOptions  {
    private final boolean colorConsole;
    private final List<String> coverageSchemes;
    private final List<String> includeObjects;
    private final List<String> excludeObjects;
    @Nullable
    private final FileMapperOptions sourceMappingOptions;
    @Nullable
    private final FileMapperOptions testMappingOptions;
    private final boolean failOnErrors;
    private final boolean skipCompatibilityCheck;
    private final Charset clientCharacterSet;

    TestRunOptions(boolean colorConsole,
                   List<String> coverageSchemes,
                   List<String> includeObjects,
                   List<String> excludeObjects,
                   @Nullable FileMapperOptions sourceMappingOptions,
                   @Nullable FileMapperOptions testMappingOptions,
                   boolean failOnErrors,
                   boolean skipCompatibilityCheck,
                   Charset clientCharacterSet) {
        this.colorConsole = colorConsole;
        this.coverageSchemes = coverageSchemes;
        this.includeObjects = includeObjects;
        this.excludeObjects = excludeObjects;
        this.sourceMappingOptions = sourceMappingOptions;
        this.testMappingOptions = testMappingOptions;
        this.failOnErrors = failOnErrors;
        this.skipCompatibilityCheck = skipCompatibilityCheck;
        this.clientCharacterSet = clientCharacterSet;
    }

    public boolean isColorConsole() {
        return colorConsole;
    }

    public List<String> getCoverageSchemes() {
        return coverageSchemes;
    }

    public List<String> getIncludeObjects() {
        return includeObjects;
    }

    public List<String> getExcludeObjects() {
        return excludeObjects;
    }

    @Nullable
    public FileMapperOptions getSourceMappingOptions() {
        return sourceMappingOptions;
    }

    @Nullable
    public FileMapperOptions getTestMappingOptions() {
        return testMappingOptions;
    }

    public boolean isFailOnErrors() {
        return failOnErrors;
    }

    public boolean isSkipCompatibilityCheck() {
        return skipCompatibilityCheck;
    }

    public Charset getClientCharacterSet() {
        return clientCharacterSet;
    }
}
