package org.utplsql.api;

import org.utplsql.api.reporter.Reporter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds the various possible options of TestRunner
 *
 * @author pesse
 */
public class TestRunnerOptions {
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
    public boolean randomTestOrder = false;
    public Integer randomTestOrderSeed;
    public final Set<String> tags = new LinkedHashSet<>();

    public String getTagsAsString() {
        return String.join(",", tags);
    }
}
