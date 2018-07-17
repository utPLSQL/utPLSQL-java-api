package org.utplsql.api.reporter.inspect;

/** Holds information about utPLSQL Reporter-Types
 *
 * @author pesse
 */
public class ReporterInfo {

    public enum Type {
        SQL, JAVA, SQL_WITH_JAVA
    }

    private final String name;
    private final Type type;
    private final String description;

    ReporterInfo( String name, Type type, String description ) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
