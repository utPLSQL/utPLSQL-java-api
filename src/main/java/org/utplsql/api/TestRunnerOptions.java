package org.utplsql.api;

import org.utplsql.api.reporter.Reporter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Holds the various possible options of TestRunner
 *
 * @author pesse
 */
public class TestRunnerOptions {
    public List<String> pathList = new ArrayList<>();
    public List<Reporter> reporterList = new ArrayList<>();
    public boolean colorConsole = false;
    public List<String> coverageSchemes = new ArrayList<>();
    public List<String> sourceFiles = new ArrayList<>();
    public List<String> testFiles = new ArrayList<>();
    public List<String> includeObjects = new ArrayList<>();
    public List<String> excludeObjects = new ArrayList<>();
    public FileMapperOptions sourceMappingOptions;
    public FileMapperOptions testMappingOptions;
    public boolean failOnErrors = false;
    public boolean skipCompatibilityCheck = false;
    public String clientCharacterSet = Charset.defaultCharset().toString();
}
