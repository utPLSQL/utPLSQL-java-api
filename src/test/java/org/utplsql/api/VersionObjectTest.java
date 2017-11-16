package org.utplsql.api;

import org.junit.Assert;
import org.junit.Test;
import org.utplsql.api.exception.InvalidVersionException;

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

    @Test
    public void versionCompareTo()
    {
        Version base = new Version("2.3.4.5");

        // Less than
        Assert.assertEquals(-1, base.compareTo(new Version("3")));
        Assert.assertEquals(-1, base.compareTo(new Version("3.2")));
        Assert.assertEquals(-1, base.compareTo(new Version("2.4.1")));
        Assert.assertEquals(-1, base.compareTo(new Version("2.3.9.1")));
        Assert.assertEquals(-1, base.compareTo(new Version("2.3.4.9")));

        // Greater than
        Assert.assertEquals(1, base.compareTo(new Version("1")));
        Assert.assertEquals(1, base.compareTo(new Version("1.6")));
        Assert.assertEquals(1, base.compareTo(new Version("2.2.4")));
        Assert.assertEquals(1, base.compareTo(new Version("2.3.3")));
        Assert.assertEquals(1, base.compareTo(new Version("2.3.4.1")));
        Assert.assertEquals(1, base.compareTo(new Version("2.3.4")));

        // Equal
        Assert.assertEquals(0, base.compareTo(new Version("2.3.4.5")));
    }

    @Test
    public void isGreaterOrEqualThan()
    {
        Version base = new Version("2.3.4.5");

        try {
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("1")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2.2")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.4")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.3")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.4.5")));
            Assert.assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.4.4")));

            Assert.assertEquals(false, base.isGreaterOrEqualThan(new Version("2.3.4.6")));
            Assert.assertEquals(false, base.isGreaterOrEqualThan(new Version("2.3.5")));
            Assert.assertEquals(false, base.isGreaterOrEqualThan(new Version("2.4")));
            Assert.assertEquals(false, base.isGreaterOrEqualThan(new Version("3")));
        }
        catch ( InvalidVersionException e )
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void isGreaterOrEqualThanFails()
    {
        // Given version is invalid
        try {
            new Version("2.3.4.5").isGreaterOrEqualThan(new Version("aerfvf"));
            Assert.fail("Given Version is invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }

        // Base version is invalid
        try {
            new Version("erefs").isGreaterOrEqualThan(new Version("1.2.3"));
            Assert.fail("Base version is invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }

        // Both versions are invalid
        try {
            new Version("erefs").isGreaterOrEqualThan(new Version("aerfvf"));
            Assert.fail("Both versions are invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }
    }
}
