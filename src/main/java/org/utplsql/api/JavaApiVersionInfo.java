package org.utplsql.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is getting updated automatically by the build process.
 * Please do not update its constants manually because they will be overwritten.
 *
 * @author pesse
 */
public class JavaApiVersionInfo {

    private static final String MAVEN_PROJECT_NAME = "utPLSQL-java-api";
    private static String MAVEN_PROJECT_VERSION = "unknown";

    static {
        try {
            try (InputStream in = JavaApiVersionInfo.class.getClassLoader().getResourceAsStream("utplsql-api.version")) {
                assert in != null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    MAVEN_PROJECT_VERSION = reader.readLine();
                }
            }
        } catch (IOException e) {
            System.out.println("WARNING: Could not get Version information!");
        }
    }

    private JavaApiVersionInfo() {
    }

    public static String getVersion() {
        return MAVEN_PROJECT_VERSION;
    }

    public static String getInfo() {
        return MAVEN_PROJECT_NAME + " " + getVersion();
    }

}
