package org.utplsql.api;

import org.utplsql.api.exception.InvalidVersionException;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/** Simple class to parse utPLSQL Version-information and provide the separate version-numbers
 *
 * @author pesse
 */
public class Version implements Comparable<Version> {

    public final static Version V3_0_0 = new Version("3.0.0", 3,0,0,null, true);
    public final static Version V3_0_1 = new Version("3.0.1", 3,0,1,null, true);
    public final static Version V3_0_2 = new Version("3.0.2", 3,0,2,null, true);
    public final static Version V3_0_3 = new Version("3.0.3", 3,0,3,null, true);
    public final static Version V3_0_4 = new Version("3.0.4", 3,0,4,null, true);
    public final static Version V3_1_0 = new Version("3.1.0", 3,1,0,null, true);
    public final static Version V3_1_1 = new Version("3.1.1", 3,1,1,null, true);
    public final static Version V3_1_2 = new Version("3.1.2", 3,1,2,null, true);
    private final static Map<String, Version> knownVersions =
            Stream.of(V3_0_0, V3_0_1, V3_0_2, V3_0_3, V3_0_4, V3_1_0, V3_1_1, V3_1_2)
                    .collect(toMap(Version::toString, Function.identity()));

    private final String origString;
    private final Integer major;
    private final Integer minor;
    private final Integer bugfix;
    private final Integer build;
    private final boolean valid;

    private Version(String origString, Integer major, Integer minor, Integer bugfix, Integer build, boolean valid) {
        this.origString = origString;
        this.major = major;
        this.minor = minor;
        this.bugfix = bugfix;
        this.build = build;
        this.valid = valid;
    }

    /**
     * Use {@link Version#create} factory method instead
     * For removal
     */
    @Deprecated()
    public Version(String versionString) {
        assert versionString != null;
        Version dummy = parseVersionString(versionString);

        this.origString = dummy.origString;
        this.major = dummy.major;
        this.minor =dummy.minor;
        this.bugfix = dummy.bugfix;
        this.build = dummy.build;
        this.valid = dummy.valid;
    }

    public static Version create(final String versionString) {
        String origString = Objects.requireNonNull(versionString);
        Version version = knownVersions.get(origString);
        return version != null ? version : parseVersionString(origString);
    }

    private static Version parseVersionString(String origString)
    {

        Integer major = null;
        Integer minor = null;
        Integer bugfix = null;
        Integer build = null;
        boolean valid = false;
        Pattern p = Pattern.compile("([0-9]+)\\.?([0-9]+)?\\.?([0-9]+)?\\.?([0-9]+)?");

        Matcher m = p.matcher(origString);

        try {
            if (m.find()) {
                if (m.group(1) != null )
                    major = Integer.valueOf(m.group(1));
                if (m.group(2) != null )
                    minor = Integer.valueOf(m.group(2));
                if (m.group(3) != null )
                    bugfix = Integer.valueOf(m.group(3));
                if (m.group(4) != null )
                    build = Integer.valueOf(m.group(4));

                 // We need a valid major version as minimum requirement for a Version object to be valid
                valid = major != null;
            }
        }
        catch ( NumberFormatException e )
        {
            valid = false;
        }

        return new Version(origString, major, minor, bugfix, build, valid);
    }

    @Override
    public String toString() {
        return origString;
    }

    public Integer getMajor() {
        return major;
    }

    public Integer getMinor() {
        return minor;
    }

    public Integer getBugfix() {
        return bugfix;
    }

    public Integer getBuild() {
        return build;
    }

    public boolean isValid() {
        return valid;
    }

    /** Returns a normalized form of the parsed version information
     *
     * @return
     */
    public String getNormalizedString()
    {
        if ( isValid() ) {
            StringBuilder sb = new StringBuilder();
            sb.append(major);
            if ( minor != null )
                sb.append(".").append(minor);
            if ( bugfix != null )
                sb.append(".").append(bugfix);
            if ( build != null )
                sb.append(".").append(build);

            return sb.toString();
        }
        else
            return "invalid";
    }

    private int compareToWithNulls(@Nullable Integer i1, @Nullable Integer i2 ) {
        if ( i1 == null && i2 == null )
            return 0;
        else if ( i1 == null )
            return -1;
        else if ( i2 == null )
            return 1;
        else return i1.compareTo(i2);
    }

    @Override
    public int compareTo(Version o) {
        int curResult;

        if ( isValid() && o.isValid() ) {

            curResult = compareToWithNulls(getMajor(), o.getMajor());
            if ( curResult != 0 )
                return curResult;

            curResult = compareToWithNulls(getMinor(), o.getMinor());
            if ( curResult != 0 )
                return curResult;

            curResult = compareToWithNulls(getBugfix(), o.getBugfix());
            if ( curResult != 0 )
                return curResult;

            curResult = compareToWithNulls(getBuild(), o.getBuild());
            if ( curResult != 0 )
                return curResult;
        }

        return 0;
    }

    private void versionsAreValid( Version v ) throws InvalidVersionException {
        if ( !isValid() )
            throw new InvalidVersionException(this);

        if ( !v.isValid() )
            throw new InvalidVersionException(v);
    }

    /** Compares this version to a given version and returns true if this version is greater or equal than the given one
     * Throws an InvalidVersionException if either this or the given version are invalid
     *
     * @param v Version to compare with
     * @return
     * @throws InvalidVersionException
     */
    public boolean isGreaterOrEqualThan( Version v ) throws InvalidVersionException {

        versionsAreValid(v);

        return compareTo(v) >= 0;
    }


    public boolean isGreaterThan( Version v) throws InvalidVersionException
    {
        versionsAreValid(v);

        return compareTo(v) > 0;
    }

    public boolean isLessOrEqualThan( Version v ) throws InvalidVersionException
    {

        versionsAreValid(v);

        return compareTo(v) <= 0;
    }

    public boolean isLessThan( Version v) throws InvalidVersionException
    {
        versionsAreValid(v);

        return compareTo(v) < 0;
    }
}
