package org.utplsql.api;

import org.junit.jupiter.api.Test;
import org.utplsql.api.exception.InvalidVersionException;

import static org.junit.jupiter.api.Assertions.*;

class VersionObjectTest {

    @Test
    void versionPatternRecognitionFull() {
        Version v = Version.create("v3.1.3.1234");

        assertEquals(3, (long)v.getMajor());
        assertEquals(1, (long)v.getMinor());
        assertEquals(3, (long)v.getBugfix());
        assertEquals(1234, (long)v.getBuild());
        assertEquals(true, v.isValid());
        assertEquals("3.1.3.1234", v.getNormalizedString());
    }

    @Test
    void versionPatternRecognitionDevelop() {
        Version v = Version.create("v3.1.3.2140-develop");

        assertEquals(3, (long)v.getMajor());
        assertEquals(1, (long)v.getMinor());
        assertEquals(3, (long)v.getBugfix());
        assertEquals(2140, (long)v.getBuild());
        assertTrue(v.isValid());
        assertEquals("3.1.3.2140", v.getNormalizedString());
    }

    @Test
    void versionPatternRecognitionPartial() {
        Version v = Version.create("3.1.etc");

        assertEquals(3, (long)v.getMajor());
        assertEquals(1, (long)v.getMinor());
        assertNull(v.getBugfix());
        assertNull(v.getBuild());
        assertEquals(true, v.isValid());
        assertEquals("3.1", v.getNormalizedString());
    }

    @Test
    void versionPatternRecognitionInvalid() {
        Version v = Version.create("adseef");

        assertNull(v.getMajor());
        assertNull(v.getMinor());
        assertNull(v.getBugfix());
        assertNull(v.getBuild());
        assertEquals(false, v.isValid());
        assertEquals("invalid", v.getNormalizedString());
    }

    @Test
    void versionCompareTo()
    {
        Version base = Version.create("2.3.4.5");

        // Less than
        assertEquals(-1, base.compareTo(Version.create("3")));
        assertEquals(-1, base.compareTo(Version.create("3.2")));
        assertEquals(-1, base.compareTo(Version.create("2.4.1")));
        assertEquals(-1, base.compareTo(Version.create("2.3.9.1")));
        assertEquals(-1, base.compareTo(Version.create("2.3.4.9")));

        // Greater than
        assertEquals(1, base.compareTo(Version.create("1")));
        assertEquals(1, base.compareTo(Version.create("1.6")));
        assertEquals(1, base.compareTo(Version.create("2.2.4")));
        assertEquals(1, base.compareTo(Version.create("2.3.3")));
        assertEquals(1, base.compareTo(Version.create("2.3.4.1")));
        assertEquals(1, base.compareTo(Version.create("2.3.4")));

        // Equal
        assertEquals(0, base.compareTo(Version.create("2.3.4.5")));
    }

    @Test
    void isGreaterOrEqualThan()
    {
        Version base = Version.create("2.3.4.5");

        try {
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("1")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2.3")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2.2")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2.3.4")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2.3.3")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2.3.4.5")));
            assertEquals(true, base.isGreaterOrEqualThan(Version.create("2.3.4.4")));

            assertEquals(false, base.isGreaterOrEqualThan(Version.create("2.3.4.6")));
            assertEquals(false, base.isGreaterOrEqualThan(Version.create("2.3.5")));
            assertEquals(false, base.isGreaterOrEqualThan(Version.create("2.4")));
            assertEquals(false, base.isGreaterOrEqualThan(Version.create("3")));
        }
        catch ( InvalidVersionException e )
        {
            fail(e);
        }
    }

    @Test
    void isGreaterOrEqualThanFails()
    {
        // Given version is invalid
        try {
            Version.create("2.3.4.5").isGreaterOrEqualThan(Version.create("aerfvf"));
            fail("Given Version is invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }

        // Base version is invalid
        try {
            Version.create("erefs").isGreaterOrEqualThan(Version.create("1.2.3"));
            fail("Base version is invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }

        // Both versions are invalid
        try {
            Version.create("erefs").isGreaterOrEqualThan(Version.create("aerfvf"));
            fail("Both versions are invalid - not recognized");
        }
        catch ( InvalidVersionException e ) {
        }
    }
}
