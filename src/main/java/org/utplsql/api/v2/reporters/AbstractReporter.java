package org.utplsql.api.v2.reporters;

/**
 * Created by Pavel Kaplya on 16.03.2019.
 */
public abstract class AbstractReporter implements Reporter {

    private final String name;

    protected AbstractReporter(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
