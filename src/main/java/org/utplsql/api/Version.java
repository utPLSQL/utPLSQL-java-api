package org.utplsql.api;

import org.utplsql.api.exception.InvalidVersionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Simple class to parse utPLSQL Version-information and provide the separate version-numbers
 *
 * @author pesse
 */
public class Version implements Comparable<Version> {
    private String origString;
    private Integer major;
    private Integer minor;
    private Integer bugfix;
    private Integer build;
    private boolean valid = false;

    public Version( String versionString ) {
        assert versionString != null;
        this.origString = versionString;
        parseVersionString();
    }

    private void parseVersionString()
    {
        Pattern p = Pattern.compile("([0-9]+)\\.?([0-9]+)?\\.?([0-9]+)?\\.?([0-9]+)?");

        Matcher m = p.matcher(origString);

        try {
            if (m.find()) {
                if ( m.group(1) != null )
                    major = Integer.valueOf(m.group(1));
                if ( m.group(2) != null )
                    minor = Integer.valueOf(m.group(2));
                if ( m.group(3) != null )
                    bugfix = Integer.valueOf(m.group(3));
                if ( m.group(4) != null )
                    build = Integer.valueOf(m.group(4));

                if ( major != null ) // We need a valid major version as minimum requirement for a Version object to be valid
                    valid = true;
            }
        }
        catch ( NumberFormatException e )
        {
            valid = false;
        }
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
            sb.append(String.valueOf(major));
            if ( minor != null )
                sb.append("." + String.valueOf(minor));
            if ( bugfix != null )
                sb.append("." + String.valueOf(bugfix));
            if ( build != null )
                sb.append("." + String.valueOf(build));

            return sb.toString();
        }
        else
            return "invalid";
    }

    private int compareToWithNulls( Integer i1, Integer i2 ) {
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

    /** Compares this version to a given version and returns true if this version is greater or equal than the given one
     * Throws an InvalidVersionException if either this or the given version are invalid
     *
     * @param v Version to compare with
     * @return
     * @throws InvalidVersionException
     */
    public boolean isGreaterOrEqualThan( Version v ) throws InvalidVersionException {
        if ( !isValid() )
            throw new InvalidVersionException(this);

        if ( !v.isValid() )
            throw new InvalidVersionException(v);

        if ( compareTo(v) >= 0 )
            return true;
        else
            return false;
    }
}
