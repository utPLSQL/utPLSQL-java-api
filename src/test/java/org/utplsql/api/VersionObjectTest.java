package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.exception.InvalidVersionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class VersionObjectTest {

    @Test
    public void versionPatternRecognitionFull() {
        Version v = new Version("v3.1.3.1234");

        assertEquals(3, (long)v.getMajor());
        assertEquals(1, (long)v.getMinor());
        assertEquals(3, (long)v.getBugfix());
        assertEquals(1234, (long)v.getBuild());
        assertEquals(true, v.isValid());
        assertEquals("3.1.3.1234", v.getNormalizedString());
    }

    @Test
    public void versionPatternRecognitionPartial() {
        Version v = new Version("3.1.etc");

        assertEquals(3, (long)v.getMajor());
        assertEquals(1, (long)v.getMinor());
        assertNull(v.getBugfix());
        assertNull(v.getBuild());
        assertEquals(true, v.isValid());
        assertEquals("3.1", v.getNormalizedString());
    }

    @Test
    public void versionPatternRecognitionInvalid() {
        Version v = new Version("adseef");

        assertNull(v.getMajor());
        assertNull(v.getMinor());
        assertNull(v.getBugfix());
        assertNull(v.getBuild());
        assertEquals(false, v.isValid());
        assertEquals("invalid", v.getNormalizedString());
    }

    @Test
    public void versionCompareTo()
    {
        Version base = new Version("2.3.4.5");

        // Less than
        assertEquals(-1, base.compareTo(new Version("3")));
        assertEquals(-1, base.compareTo(new Version("3.2")));
        assertEquals(-1, base.compareTo(new Version("2.4.1")));
        assertEquals(-1, base.compareTo(new Version("2.3.9.1")));
        assertEquals(-1, base.compareTo(new Version("2.3.4.9")));

        // Greater than
        assertEquals(1, base.compareTo(new Version("1")));
        assertEquals(1, base.compareTo(new Version("1.6")));
        assertEquals(1, base.compareTo(new Version("2.2.4")));
        assertEquals(1, base.compareTo(new Version("2.3.3")));
        assertEquals(1, base.compareTo(new Version("2.3.4.1")));
        assertEquals(1, base.compareTo(new Version("2.3.4")));

        // Equal
        assertEquals(0, base.compareTo(new Version("2.3.4.5")));
    }

    @Test
    public void isGreaterOrEqualThan()
    {
        Version base = new Version("2.3.4.5");

        try {
            assertEquals(true, base.isGreaterOrEqualThan(new Version("1")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2.2")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.4")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.3")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.4.5")));
            assertEquals(true, base.isGreaterOrEqualThan(new Version("2.3.4.4")));

            assertEquals(false, base.isGreaterOrEqualThan(new Version("2.3.4.6")));
            assertEquals(false, base.isGreaterOrEqualThan(new Version("2.3.5")));
            assertEquals(false, base.isGreaterOrEqualThan(new Version("2.4")));
            assertEquals(false, base.isGreaterOrEqualThan(new Version("3")));
        }
        catch ( InvalidVersionException e )
        {
            fail(e);
        }
    }

    @Test
    public void isGreaterOrEqualThanFails()
    {
        // Given version is invalid
        try {
            new Version("2.3.4.5").isGreaterOrEqualThan(new Version("aerfvf"));
            fail("Given Version is invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }

        // Base version is invalid
        try {
            new Version("erefs").isGreaterOrEqualThan(new Version("1.2.3"));
            fail("Base version is invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }

        // Both versions are invalid
        try {
            new Version("erefs").isGreaterOrEqualThan(new Version("aerfvf"));
            fail("Both versions are invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }
    }
}
