# Contributing
To develop it locally, you need to setup your maven environment.

### Maven Installation
That's the easy part, you just need to download the Maven binaries and extract it somewhere, then put the maven/bin folder on your PATH.

https://maven.apache.org/install.html

*Don't forget to configure your JAVA_HOME environment variable.*

### Oracle Maven Repository
The library uses OJDBC Driver to connect to the database, it's added as a maven dependency. To be able to download the Oracle dependencies, you need to configure your access to Oracle's Maven Repository:

http://docs.oracle.com/middleware/1213/core/MAVEN/config_maven_repo.htm#MAVEN9010

*Sections 6.1 and 6.5 are the more important ones, and the only ones you need if you're using the latest Maven version.*

### Local database with utPLSQL and utPLSQL-demo-project

To usefully contribute you'll have to setup a local database with installed [latest utPLSQL v3](https://github.com/utPLSQL/utPLSQL) and [utPLSQL-demo-project](https://github.com/utPLSQL/utPLSQL-demo-project). 
The demo-project will serve as your test user. See .travis.yml to see an example on how it can be installed. 

### Maven settings for utPLSQL-local profile

utPLSQL-java-api comes with a preconfigured profile "utPLSQL-local". This profile uses properties to set the correct 
environment variables for DB_URL, DB_USER and DB_PASS which is needed to run the integration tests.
You can set these properties by adding the following to your Maven settings.xml:

```xml
<settings>
    <!-- ... -->
    <profiles>
        <profile>
            <id>utPLSQL-local</id>
            <properties>
                <dbUrl>localhost:1521/XE</dbUrl>
                <dbUser>app</dbUser>
                <dbPass>app</dbPass>
            </properties>
        </profile>
    </profiles>
     
    <activeProfiles>
        <activeProfile>utPLSQL-local</activeProfile>
    </activeProfiles>
</settings>
```

After configuring your access to Oracle's Maven repository, you will be able to successfully build this API.

```bash
cd utPLSQL-java-api
mvn clean package install
```

### Skip the local database part

If you want to skip the local database part, just run ``mvn clean package install -DskipTests``. 
You will still be able to run ``mvn test`` because integration tests are run in the ``verify``-phase.
