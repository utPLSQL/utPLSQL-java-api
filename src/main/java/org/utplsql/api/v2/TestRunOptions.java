package org.utplsql.api.v2;

import org.utplsql.api.FileMapperOptions;
import org.utplsql.api.reporter.Reporter;

import java.util.List;

/** Holds the various possible options of TestRunner
 *
 * @author pesse
 */
public class TestRunOptions {
    public /*final*/ List<String> pathList;
    public /*final*/ List<Reporter> reporterList;
    public /*final*/ boolean colorConsole;
    public /*final*/ List<String> coverageSchemes;
    public /*final*/ List<String> sourceFiles;
    public /*final*/ List<String> testFiles;
    public /*final*/ List<String> includeObjects;
    public /*final*/ List<String> excludeObjects;
    public /*final*/ FileMapperOptions sourceMappingOptions;
    public /*final*/ FileMapperOptions testMappingOptions;
    public /*final*/ boolean failOnErrors;
    public /*final*/ boolean skipCompatibilityCheck;
    public /*final*/ String clientCharacterSet;
}
