[![Build Status](https://img.shields.io/travis/utPLSQL/utPLSQL-java-api/develop.svg?label=develop-branch)](https://travis-ci.org/utPLSQL/utPLSQL-java-api)
[![Build Status](https://img.shields.io/travis/utPLSQL/utPLSQL-java-api/master.svg?label=master-branch)](https://travis-ci.org/utPLSQL/utPLSQL-java-api)

# utPLSQL-java-api
This is a collection of classes, that makes easy to access the [utPLSQL v3](https://github.com/utPLSQL/utPLSQL/) database objects using Java.

* Uses [ut_runner.run](https://github.com/utPLSQL/utPLSQL/blob/develop/docs/userguide/running-unit-tests.md#ut_runnerrun-procedures) methods to execute tests.
* Can gather results asynchronously from multiple reporters.

## Downloading
This is a Maven Library project, you can add on your Java project as a dependency. 

The library is hosted on ![[packagecloud](https://packagecloud.io/utPLSQL/utPLSQL-java-api)](https://packagecloud.io/images/packagecloud-badge.png)


You install this Maven repository by adding it to the <repositories> section of your pom.xml. No special plugins or extensions are required.

```xml
<repositories>
  <repository>
    <id>utplsql-java-api</id>
    <url>
      https://packagecloud.io/utplsql/utplsql-java-api/maven2
    </url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

To use the java-api library, add this to the `<dependencies>` section of your `pom.xml`.
```xml
<dependency>
    <groupId>org.utplsql</groupId>
    <artifactId>java-api</artifactId>
    <version>3.0.4</version>
    <scope>compile</scope>
</dependency>
```

## Compatibility
The latest Java-API is always compatible with all database frameworks of the same major version.
For example API-3.0.4 is compatible with database framework 3.0.0-3.0.4 but not with database framework 2.x.

## Usage

Executing tests using default parameters:
```java
try (Connection conn = DriverManager.getConnection(url)) {
    new TestRunner().run(conn);
} catch (SQLException e) {
    e.printStackTrace();
}
```

Executing tests and printing results to screen:
```java
try (Connection conn = DriverManager.getConnection(url)) {
    Reporter documentationReporter = new DocumentationReporter().init(conn);
    
    new TestRunner()
        .addReporter(documentationReporter)
        .run(conn);
    
    new OutputBuffer(documentationReporter)
        .printAvailable(conn, System.out);
} catch (SQLException e) {
    e.printStackTrace();
}
```

## Contributing
To develop it locally, you need to setup your maven environment.

### Maven Installation
That's the easy part, you just need to download the Maven binaries and extract it somewhere, then put the maven/bin folder on your PATH.

https://maven.apache.org/install.html

*Don't forget to configure your JAVA_HOME environment variable.*

### Oracle Maven Repository
The library uses OJDBC Driver to connect to the database, it's added as a maven dependency. To be able to download the Oracle dependencies, you need to configure your access to Oracle's Maven Repository:

http://docs.oracle.com/middleware/1213/core/MAVEN/config_maven_repo.htm#MAVEN9010

*Sections 6.1 and 6.5 are the more important ones, and the only ones you need if you're using the latest Maven version.*

After configuring your access to Oracle's Maven repository, you will be able to successfully build this API.

```bash
cd utPLSQL-java-api
mvn clean package install -DskipTests
```

The cmd above is ignoring unit tests because it needs a database connection with the latest utPLSQL v3 installed. Please take a look on .travis.yml and .travis folder to see how testing was configured.
