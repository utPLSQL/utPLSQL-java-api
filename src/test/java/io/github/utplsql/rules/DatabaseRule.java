package io.github.utplsql.rules;

import io.github.utplsql.UTPLSQL;
import org.junit.rules.ExternalResource;

/**
 * Created by Vinicius on 13/04/2017.
 */
public class DatabaseRule extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        // TODO: Options file.
        UTPLSQL.init("jdbc:oracle:thin:@127.0.0.1:1521:xe", "ut3", "ut3");
    }

    @Override
    protected void after() {
        UTPLSQL.close();
    }

}
