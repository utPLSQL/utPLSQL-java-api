package org.utplsql.api;

import org.utplsql.api.reporter.Reporter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the various possible options of TestRunner
 *
 * @author pesse
 */
public class TestRunnerOptionsImpl implements TestRunnerOptions {
    public final List<String> pathList = new ArrayList<>();
    public final List<Reporter> reporterList = new ArrayList<>();
    public final List<String> coverageSchemes = new ArrayList<>();
    public final List<String> sourceFiles = new ArrayList<>();
    public final List<String> testFiles = new ArrayList<>();
    public final List<String> includeObjects = new ArrayList<>();
    public final List<String> excludeObjects = new ArrayList<>();
    public boolean colorConsole = false;
    public FileMapperOptions sourceMappingOptions;
    public FileMapperOptions testMappingOptions;
    public boolean failOnErrors = false;
    public boolean skipCompatibilityCheck = false;
    public String clientCharacterSet = Charset.defaultCharset().toString();

    @Override
    public List<String> getPathList() {
        return pathList;
    }

    @Override
    public List<Reporter> getReporterList() {
        return reporterList;
    }

    @Override
    public List<String> getCoverageSchemes() {
        return coverageSchemes;
    }

    @Override
    public List<String> getIncludeObjects() {
        return includeObjects;
    }

    @Override
    public List<String> getExcludeObjects() {
        return excludeObjects;
    }

    @Override
    public boolean isColorConsole() {
        return colorConsole;
    }

    @Override
    public FileMapperOptions getSourceMappingOptions() {
        return sourceMappingOptions;
    }

    @Override
    public FileMapperOptions getTestMappingOptions() {
        return testMappingOptions;
    }

    @Override
    public boolean isFailOnErrors() {
        return failOnErrors;
    }

    @Override
    public boolean isSkipCompatibilityCheck() {
        return skipCompatibilityCheck;
    }

    @Override
    public Charset getClientCharacterSet() {
        return Charset.forName(clientCharacterSet);
    }
}
