package org.utplsql.api.reporter;

import org.utplsql.api.Version;
import org.utplsql.api.exception.InvalidVersionException;

/**
 * This enum defines default reporters, added and maintained by the utPLSQL team,
 * and information since (and maybe until) which version they exist
 *
 * @author pesse
 */
public enum CoreReporters {

    UT_COVERAGE_COBERTURA_REPORTER(Version.V3_1_0, null),
    UT_COVERAGE_HTML_REPORTER(Version.V3_0_0, null),
    UT_COVERAGE_SONAR_REPORTER(Version.V3_0_0, null),
    UT_COVERALLS_REPORTER(Version.V3_0_0, null),
    UT_DEBUG_REPORTER(Version.V3_1_4, null),
    UT_DOCUMENTATION_REPORTER(Version.V3_0_0, null),
    UT_JUNIT_REPORTER(Version.V3_1_0, null),
    UT_REALTIME_REPORTER(Version.V3_1_4, null),
    UT_SONAR_TEST_REPORTER(Version.V3_0_0, null),
    UT_TEAMCITY_REPORTER(Version.V3_0_0, null),
    UT_TFS_JUNIT_REPORTER(Version.V3_1_0, null),
    UT_XUNIT_REPORTER(Version.V3_0_0, null);

    private final Version since;
    private final Version until;

    CoreReporters(Version since, Version until) {
        this.since = since;
        this.until = until;
    }

    public Version getSince() {
        return since;
    }

    public Version getUntil() {
        return until;
    }

    /**
     * Checks whether a CoreReporter is valid for the given databaseVersion
     *
     * @param databaseVersion Database-Version
     * @return true or false
     */
    public boolean isAvailableFor(Version databaseVersion) {
        try {
            if ((since == null || databaseVersion.isGreaterOrEqualThan(since))
                    && (until == null || databaseVersion.isLessOrEqualThan(until))) {
                return true;
            }
        } catch (InvalidVersionException ignored) {
        }

        return false;
    }
}
