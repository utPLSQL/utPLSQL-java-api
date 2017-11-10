package org.utplsql.api;

import org.junit.Assert;
import org.junit.Test;

public class VersionObjectTest {

    @Test
    public void versionPatternRecognitionFull() {
        Version v = new Version("v3.1.3.1234");

        Assert.assertEquals(3, (long)v.getMajor());
        Assert.assertEquals(1, (long)v.getMinor());
        Assert.assertEquals(3, (long)v.getBugfix());
        Assert.assertEquals(1234, (long)v.getBuild());
        Assert.assertEquals(true, v.isValid());
        Assert.assertEquals("3.1.3.1234", v.getNormalizedString());
    }

    @Test
    public void versionPatternRecognitionPartial() {
        Version v = new Version("3.1.etc");

        Assert.assertEquals(3, (long)v.getMajor());
        Assert.assertEquals(1, (long)v.getMinor());
        Assert.assertNull(v.getBugfix());
        Assert.assertNull(v.getBuild());
        Assert.assertEquals(true, v.isValid());
        Assert.assertEquals("3.1", v.getNormalizedString());
    }

    @Test
    public void versionPatternRecognitionInvalid() {
        Version v = new Version("adseef");

        Assert.assertNull(v.getMajor());
        Assert.assertNull(v.getMinor());
        Assert.assertNull(v.getBugfix());
        Assert.assertNull(v.getBuild());
        Assert.assertEquals(false, v.isValid());
        Assert.assertEquals("invalid", v.getNormalizedString());
    }
}
