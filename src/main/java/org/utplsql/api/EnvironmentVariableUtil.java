package org.utplsql.api;

/**
 * This class provides an easy way to get environmental variables.
 * This is mainly to improve testability but also to standardize the way how utPLSQL API and CLI read from
 * environment.
 * <p>
 * Variables are obtained from the following scopes in that order (chain breaks as soon as a value is obtained):
 * <ul>
 * <li>Properties (System.getProperty())</li>
 * <li>Environment (System.getEnv())</li>
 * <li>Default value</li>
 * </ul>
 * <p>
 * An empty string is treated the same as null.
 *
 * @author pesse
 */
public class EnvironmentVariableUtil {

    private EnvironmentVariableUtil() {}

    /**
     * Returns the value for a given key from environment (see class description)
     *
     * @param key Key of environment or property value
     * @return Environment value or null
     */
    public static String getEnvValue(String key) {
        return getEnvValue(key, null);
    }

    /**
     * Returns the value for a given key from environment or a default value (see class description)
     *
     * @param key          Key of environment or property value
     * @param defaultValue Default value if nothing found
     * @return Environment value or defaultValue
     */
    public static String getEnvValue(String key, String defaultValue) {

        String val = System.getProperty(key);
        if (val == null || val.isEmpty()) val = System.getenv(key);
        if (val == null || val.isEmpty()) val = defaultValue;

        return val;
    }


}
