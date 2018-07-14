package org.utplsql.api;

import org.utplsql.api.reporter.Reporter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/** Holds the various possible options of TestRunner
 *
 * @author pesse
 */
public class TestRunnerOptions {
    public final List<String> pathList = new ArrayList<>();
    public final List<Reporter> reporterList = new ArrayList<>();
    public boolean colorConsole = false;
    public final List<String> coverageSchemes = new ArrayList<>();
    public final List<String> sourceFiles = new ArrayList<>();
    public final List<String> testFiles = new ArrayList<>();
    public final List<String> includeObjects = new ArrayList<>();
    public final List<String> excludeObjects = new ArrayList<>();
    public FileMapperOptions sourceMappingOptions;
    public FileMapperOptions testMappingOptions;
    public boolean failOnErrors = false;
    public boolean skipCompatibilityCheck = false;
    public String clientCharacterSet = Charset.defaultCharset().toString();
}
