package org.utplsql.api;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class EnvironmentVariableUtilTest {

    @Test
    void testGetVariableFromEnvironment() {
        // Let's find an environment variable which is not in Properties list and not empty
        Set<Object> props = System.getProperties().keySet();
        Optional<Map.Entry<String, String>> envVariable = System.getenv().entrySet().stream()
                .filter((e) -> !props.contains(e.getKey()) && e.getValue() != null && !e.getValue().isEmpty())
                .findFirst();

        if (!envVariable.isPresent()) {
            fail("Can't test for there is no environment variable not overridden by property");
        }

        assertEquals(envVariable.get().getValue(), EnvironmentVariableUtil.getEnvValue(envVariable.get().getKey()));
    }

    @Test
    void testGetVariableFromProperty() {
        System.setProperty("PATH", "MyPath");

        assertEquals("MyPath", EnvironmentVariableUtil.getEnvValue("PATH"));
    }

    @Test
    void testGetVariableFromDefault() {

        assertEquals("defaultValue", EnvironmentVariableUtil.getEnvValue("RANDOM" + System.currentTimeMillis(), "defaultValue"));
    }

}