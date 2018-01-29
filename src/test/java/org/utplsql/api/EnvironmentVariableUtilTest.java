package org.utplsql.api;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class EnvironmentVariableUtilTest {

    @Test
    public void testGetVariableFromEnvironment() {
        // Let's find an environment variable which is not in Properties list and not empty
        Set<Object> props = System.getProperties().keySet();
        Optional<Map.Entry<String, String>> envVariable = System.getenv().entrySet().stream()
                .filter((e) -> !props.contains(e.getKey()) && e.getValue() != null && !e.getValue().isEmpty())
                .findFirst();

        if ( !envVariable.isPresent() )
            Assert.fail("Can't test for there is no environment variable not overridden by property");

        Assert.assertEquals(envVariable.get().getValue(), EnvironmentVariableUtil.getEnvValue(envVariable.get().getKey()));
    }

    @Test
    public void testGetVariableFromProperty() {
        System.setProperty("PATH", "MyPath");

        Assert.assertEquals("MyPath", EnvironmentVariableUtil.getEnvValue("PATH"));
    }

    @Test
    public void testGetVariableFromDefault() {

        Assert.assertEquals("defaultValue", EnvironmentVariableUtil.getEnvValue("RANDOM"+String.valueOf(System.currentTimeMillis()), "defaultValue"));
    }

}