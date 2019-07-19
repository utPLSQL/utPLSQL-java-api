package org.utplsql.api.testRunner;

import org.junit.jupiter.api.Test;
import org.utplsql.api.Version;

public class DynamicTestRunnerStatementTest {

    @Test
    void explore() {
        DynamicTestRunnerStatement testRunnerStatement = DynamicTestRunnerStatement.forVersion(Version.V3_1_7, null);
    }
}
