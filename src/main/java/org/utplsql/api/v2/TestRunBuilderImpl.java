package org.utplsql.api.v2;

import org.utplsql.api.FileMapperOptions;
import org.utplsql.api.reporter.Reporter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Kaplya on 11.02.2019.
 */
public class TestRunBuilderImpl /*implements TestRunBuilder*/ {
    private List<String> pathList = new ArrayList<>();
    private List<Reporter> reporterList = new ArrayList<>();
    private boolean colorConsole = false;
    private List<String> coverageSchemes;
    private List<String> sourceFiles;
    private List<String> testFiles = new ArrayList<>();
    private List<String> includeObjects = new ArrayList<>();
    private List<String> excludeObjects = new ArrayList<>();
    private FileMapperOptions sourceMappingOptions;
    private FileMapperOptions testMappingOptions;
    private boolean failOnErrors = false;
    private boolean skipCompatibilityCheck = false;
    private String clientCharacterSet = Charset.defaultCharset().toString();


    public TestRunBuilderImpl addPath(String path) {
        this.pathList.add(path);
        return this;
    }

    public TestRunBuilderImpl paths(List<String> paths) {
        this.pathList.addAll(paths);
        return this;
    }

    public TestRunBuilderImpl addReporter(Reporter reporter) {
        this.reporterList.add(reporter);
        return this;
    }

    public TestRunBuilderImpl colorConsole(boolean colorConsole) {
        this.colorConsole = colorConsole;
        return this;
    }

    public TestRunBuilderImpl addReporterList(List<Reporter> reporterList) {
        this.reporterList.addAll(reporterList);
        return this;
    }

    public TestRunBuilderImpl addCoverageScheme(String coverageScheme) {
        this.coverageSchemes.add(coverageScheme);
        return this;
    }

    public TestRunBuilderImpl includeObject(String obj) {
        this.includeObjects.add(obj);
        return this;
    }

    public TestRunBuilderImpl excludeObject(String obj) {
        this.excludeObjects.add(obj);
        return this;
    }

    public TestRunBuilderImpl includeObjects(List<String> obj) {
        this.includeObjects.addAll(obj);
        return this;
    }

    public TestRunBuilderImpl excludeObjects(List<String> obj) {
        this.excludeObjects.addAll(obj);
        return this;
    }

    public TestRunBuilderImpl sourceMappingOptions(FileMapperOptions mapperOptions) {
        this.sourceMappingOptions = mapperOptions;
        return this;
    }

    public TestRunBuilderImpl testMappingOptions(FileMapperOptions mapperOptions) {
        this.testMappingOptions = mapperOptions;
        return this;
    }

    public TestRunBuilderImpl failOnErrors(boolean failOnErrors) {
        this.failOnErrors = failOnErrors;
        return this;
    }

    public TestRunBuilderImpl skipCompatibilityCheck( boolean skipCompatibilityCheck )
    {
        this.skipCompatibilityCheck = skipCompatibilityCheck;
        return this;
    }
    
}
