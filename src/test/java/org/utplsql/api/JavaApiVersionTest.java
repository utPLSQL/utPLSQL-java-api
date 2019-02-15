package org.utplsql.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaApiVersionTest {

    @Test
    void getJavaApiVersion() {
        assertTrue(JavaApiVersionInfo.getVersion().startsWith("3.1"));
    }
}
