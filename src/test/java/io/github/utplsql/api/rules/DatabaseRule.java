package io.github.utplsql.api.rules;

import io.github.utplsql.api.utPLSQL;
import org.junit.rules.ExternalResource;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class DatabaseRule extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        // TODO: environment variables, config file?
        utPLSQL.init("jdbc:oracle:thin:@127.0.0.1:1521:xe", "ut3", "ut3");
    }

}
