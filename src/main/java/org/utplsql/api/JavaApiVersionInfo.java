package org.utplsql.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/** This class is getting updated automatically by the build process.
 * Please do not update its constants manually cause they will be overwritten.
 *
 * @author pesse
 */
public class JavaApiVersionInfo {

    private JavaApiVersionInfo() { }


    private static final String MAVEN_PROJECT_NAME = "utPLSQL-java-api";
    private static String MAVEN_PROJECT_VERSION = "unknown";

    static {
        try {
            MAVEN_PROJECT_VERSION = Files.readAllLines(
                    Paths.get(JavaApiVersionInfo.class.getClassLoader().getResource("utplsql-api.version").toURI())
                    , Charset.defaultCharset())
                    .get(0);
        }
        catch ( IOException | URISyntaxException e ) {
            System.out.println("WARNING: Could not get Version information!");
        }
    }

    public static String getVersion() { return MAVEN_PROJECT_VERSION; }
    public static String getInfo() { return MAVEN_PROJECT_NAME + " " + getVersion(); }

}
