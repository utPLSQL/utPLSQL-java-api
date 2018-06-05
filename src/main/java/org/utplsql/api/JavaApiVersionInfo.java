package org.utplsql.api;

/** This class is getting updated automatically by the build process.
 * Please do not update its constants manually cause they will be overwritten.
 *
 * @author pesse
 */
public class JavaApiVersionInfo {

    private static final String BUILD_NO = "123";
    private static final String MAVEN_PROJECT_NAME = "utPLSQL-java-api";
    private static final String MAVEN_PROJECT_VERSION = "3.1.1-SNAPSHOT";

    public static String getVersion() {
        return MAVEN_PROJECT_VERSION + "." + BUILD_NO;
    }
    public static String getInfo() { return MAVEN_PROJECT_NAME + " " + getVersion(); }

}
