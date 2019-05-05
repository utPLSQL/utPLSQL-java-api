package org.utplsql.api.v2.reporters;

import javax.sql.DataSource;

/**
 * Created by Pavel Kaplya on 16.03.2019.
 */
public abstract class AbstractReporter implements Reporter {

    private final String name;
    protected final DataSource dataSource;

    protected AbstractReporter(String name, DataSource dataSource) {
        this.name = name;
        this.dataSource = dataSource;
    }

    @Override
    public String getName() {
        return name;
    }
}
