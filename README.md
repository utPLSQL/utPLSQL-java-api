[![Build Status](https://img.shields.io/travis/utPLSQL/utPLSQL-java-api/develop.svg?label=develop-branch)](https://travis-ci.org/utPLSQL/utPLSQL-java-api)
[![Build Status](https://img.shields.io/travis/utPLSQL/utPLSQL-java-api/master.svg?label=master-branch)](https://travis-ci.org/utPLSQL/utPLSQL-java-api)

# utPLSQL-java-api
This is a collection of classes, that makes it easy to access the [utPLSQL v3](https://github.com/utPLSQL/utPLSQL/) database objects using Java.

* Uses [ut_runner.run](https://github.com/utPLSQL/utPLSQL/blob/develop/docs/userguide/running-unit-tests.md#ut_runnerrun-procedures) methods to execute tests.
* Can gather results asynchronously from multiple reporters.
* Handles compatibility for all 3.x versions of utPLSQL

## Downloading
This is a Maven Library project, you can add on your Java project as a dependency. 

The project is hosted on [packagecloud.io](https://packagecloud.io/utPLSQL/utPLSQL-java-api)

[![packagecloud](https://packagecloud.io/images/packagecloud-badge.png)](https://packagecloud.io/utPLSQL/utPLSQL-java-api)


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
    <version>3.1.0</version>
    <scope>compile</scope>
</dependency>
```

## Compatibility
The latest Java-API is always compatible with all database frameworks of the same major version.
For example API-3.0.4 is compatible with database framework 3.0.0-3.1.0 but not with database framework 2.x.

## Usage

You can find examples for many features of the java-api in the Unit- and Integration tests.

### Test-Runner

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
    
    documentationReporter
        .getOutputBuffer()
        .setFetchSize(1)
        .printAvailable(conn, System.out);
} catch (SQLException e) {
    e.printStackTrace();
}
```

### Optional Features

There might be some features which are not available in previous versions of utPLSQL. These "optional features" are listed in the enum org.utplsql.api.compatibility.OptionalFeatures and their availability can be checked against a connection or Version-object:

```OptionalFeatures.CUSTOM_REPORTERS.isAvailableFor( databaseConnection );```

### Compatibility-Proxy
To handle downwards-compatibility, java-api provides an easy to use CompatiblityProxy, which is instantiated with a connection.
It will check the database-version of utPLSQL and is used in several other features like `TestRunner` and `ReporterFactory`.
You can also ask it for the database-version.

```java
try (Connection conn = DriverManager.getConnection(url)) {
   CompatiblityProxy proxy = new CompatibilityProxy( conn );
   Version version = proxy.getDatabaseVersion();
} catch (SQLException e) {
    e.printStackTrace();
}
```

### Reporter-Factory

The java-api provides a ReporterFactory you can use to inject your own implementations of (java-side) reporters or reporter-handlers.
It also provides a more generic approach to Reporter-handling.

If you request the Reporter-Factory for a Reporter it has no specific implementation for it will just
return an instance of a `DefaultReporter` with the given name as SQL-Type, assuming
that it exists in the database. Therefore you can address custom reporters without the need 
of a specific java-side implementation.

```java
try (Connection conn = DriverManager.getConnection(url)) {
    ReporterFactory reporterFactory = ReporterFactory.createDefault( new CompatibilityProxy( conn ));
    reporterFactory.registerReporterFactoryMethod(CoreReporters.UT_DOCUMENTATION_REPORTER.name(), MyCustomReporterImplementation::new, "Custom handler for UT_DOCUMENTATION_REPORTER");
    
    Reporter reporter = reporterFactory.createReporter(CreateReporters.UT_DOCUMENTATION_REPORTER.name());
} catch (SQLException e) {
    e.printStackTrace();
}
```


## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md)