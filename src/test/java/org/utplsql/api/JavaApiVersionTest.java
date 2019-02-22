package org.utplsql.api;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

class JavaApiVersionTest {

    @Test
    void getJavaApiVersion() {
        assertThat(JavaApiVersionInfo.getVersion(), startsWith("3.1"));
    }
}
