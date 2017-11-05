package org.utplsql.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Simple class to parse utPLSQL Version-information and provide the separate version-numbers
 *
 * @author pesse
 */
public class Version {
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

    public String getOrigString() {
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
    public String getVersionString()
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
}
