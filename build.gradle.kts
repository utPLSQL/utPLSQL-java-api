import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

val deployerJars by configurations.creating

group = "org.utplsql"
val mavenArtifactId = "java-api"
version = "3.1.3-SNAPSHOT"

val coverageResourcesVersion = "1.0.1"
val ojdbcVersion = "12.2.0.1"

plugins {
    `java-library`
    `maven-publish`
    maven
    id("de.undercouch.download") version "3.4.3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// In this section you declare where to find the dependencies of your project
repositories {
    maven {
        url = uri("https://www.oracle.com/content/secure/maven/content")
        credentials {
            // you may set this properties using gradle.properties file in the root of the project or in your GRADLE_HOME
            username = if (project.hasProperty("ORACLE_OTN_USER")) project.property("ORACLE_OTN_USER") as String? else System.getenv("ORACLE_OTN_USER")
            password = if (project.hasProperty("ORACLE_OTN_PASSWORD")) project.property("ORACLE_OTN_PASSWORD") as String? else System.getenv("ORACLE_OTN_PASSWORD")
        }
    }
    mavenCentral()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("com.google.code.findbugs:jsr305:3.0.2")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("com.oracle.jdbc:ojdbc8:$ojdbcVersion") {
        exclude(group = "com.oracle.jdbc")
    }
    implementation("com.oracle.jdbc:orai18n:$ojdbcVersion")

    // Use Jupiter test framework
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("org.hamcrest:hamcrest:2.1")
    deployerJars("io.packagecloud.maven.wagon:maven-packagecloud-wagon:0.0.6")
}

tasks {
    test {
        useJUnitPlatform()
        exclude("**/*IT.class")
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStackTraces = true
        }
    }

    val intTest = create<Test>("intTest") {
        dependsOn(test)
        doFirst {
            environment("DB_URL", System.getenv("DB_URL") ?: "localhost:1521/XE")
            environment("DB_USER", System.getenv("DB_USER") ?: "app")
            environment("DB_PASS", System.getenv("DB_PASS") ?: "app")
        }
        useJUnitPlatform()
        include("**/*IT.class")
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStackTraces = true
            showStandardStreams = true
        }
    }

    // add integration tests to the whole check
    named("check") {
        dependsOn(intTest)
    }

    val coverageResourcesDirectory = "${project.buildDir}/resources/main/CoverageHTMLReporter"
    val coverageResourcesZip = "${project.buildDir}/utPLSQL-coverage-html-$coverageResourcesVersion.zip"

    // download Coverage Resources from web
    val downloadResources = create<Download>("downloadCoverageResources") {
        src("https://codeload.github.com/utPLSQL/utPLSQL-coverage-html/zip/$coverageResourcesVersion")
        dest(File(coverageResourcesZip))
        overwrite(true)
    }

    withType<ProcessResources> {
        dependsOn(downloadResources)

        val properties = project.properties.toMutableMap()
        properties.putIfAbsent("travisBuildNumber", System.getenv("TRAVIS_BUILD_NUMBER") ?: "local")
        expand(properties)

        doLast {
            copy {
                // extract assets folder only from downloaded archive
                // https://github.com/gradle/gradle/pull/8494
                from(zipTree(coverageResourcesZip)) {
                    include("*/assets/**")
                    eachFile {
                        relativePath = RelativePath(true, *relativePath.segments.drop(2).toTypedArray())  // <2>
                    }
                    includeEmptyDirs = false
                }
                into(coverageResourcesDirectory)
            }
        }
    }

    withType<Jar> {
        dependsOn("generatePomFileForMavenPublication")
        manifest {
            attributes(
                    "Built-By" to System.getProperty("user.name"),
                    "Created-By" to "Gradle ${gradle.gradleVersion}",
                    "Build-Jdk" to "${System.getProperty("os.name")} ${System.getProperty("os.arch")} ${System.getProperty("os.version")}"
            )
        }
        into("META-INF/maven/${project.group}/$mavenArtifactId") {
            from("$buildDir/publications/maven")
            rename(".*", "pom.xml")
        }

    }

    named<Upload>("uploadArchives") {
        repositories.withGroovyBuilder {
            "mavenDeployer" {
                setProperty("configuration", deployerJars)
                "repository"("url" to "packagecloud+https://packagecloud.io/utPLSQL/utPLSQL-java-api") {
                    "authentication"("password" to System.getenv("PACKAGECLOUD_TOKEN"))
                }
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = mavenArtifactId
            pom {
                name.set("utPLSQL-java-api")
                url.set("https://github.com/utPLSQL/utPLSQL-java-api")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
            from(components["java"])
        }
    }
}