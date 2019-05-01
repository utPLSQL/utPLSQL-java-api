package org.utplsql.api;

import org.utplsql.api.reporter.Reporter;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the various possible options of TestRunner
 *
 * @author pesse
 */
public class TestRunnerOptionsImpl implements TestRunnerOptions {
    private final List<String> pathList = new ArrayList<>();
    private final List<Reporter> reporterList = new ArrayList<>();
    private final List<String> coverageSchemes = new ArrayList<>();
    private final List<String> includeObjects = new ArrayList<>();
    private final List<String> excludeObjects = new ArrayList<>();
    boolean colorConsole = false;
    FileMapperOptions sourceMappingOptions;
    FileMapperOptions testMappingOptions;
    public boolean failOnErrors = false;
    boolean skipCompatibilityCheck = false;
    private String clientCharacterSet = Charset.defaultCharset().toString();
    boolean randomTestOrder = false;
    @Nullable
    Integer randomTestOrderSeed;

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

    @Override
    public boolean isRandomTestOrder() {
        return randomTestOrder;
    }

    @Override
    @Nullable
    public Integer getRandomTestOrderSeed() {
        return randomTestOrderSeed;
    }
}
