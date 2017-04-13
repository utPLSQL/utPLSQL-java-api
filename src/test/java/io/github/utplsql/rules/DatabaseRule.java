package io.github.utplsql.rules;

import io.github.utplsql.UTPLSQL;
import org.junit.rules.ExternalResource;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class DatabaseRule extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        UTPLSQL.init("jdbc:oracle:thin:@docker:1521:xe", "app", "app");
    }

    @Override
    protected void after() {
        UTPLSQL.close();
    }

}
