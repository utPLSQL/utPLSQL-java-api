# Contributing
To develop it locally, you need to setup your maven environment.

### Maven Installation
That's the easy part, you just need to download the Maven binaries and extract it somewhere, then put the maven/bin folder on your PATH.

https://maven.apache.org/install.html

*Don't forget to configure your JAVA_HOME environment variable.*

### Oracle Maven Repository
The library uses OJDBC Driver to connect to the database, it's added as a maven dependency. To be able to download the Oracle dependencies, you need to configure your access to Oracle's Maven Repository:
Create file `gradle.properties` in the root directory of the repository and place OTN credentials there:
```properties
ORACLE_OTN_USER=user@email.com
ORACLE_OTN_PASSWORD=password
```

After configuring your access to Oracle's Maven repository, you will be able to successfully build this API by disabling integration tests.

```bash
./gradlew build -x intTest
```

### Local database with utPLSQL and utPLSQL-demo-project

To usefully contribute you'll have to setup a local database with installed [latest utPLSQL v3](https://github.com/utPLSQL/utPLSQL) and [utPLSQL-demo-project](https://github.com/utPLSQL/utPLSQL-demo-project). 
The demo-project will serve as your test user. See .travis.yml to see an example on how it can be installed. 
By default tests are executed against `app/app` user of `localhost:1521/XE database`. 

If you want to run tests against another database you may set `DB_URL`, `DB_USER`, `DB_PASS` environment variables.

When you have local database set up you can run the complete build including integration tests by executing 
```bash
./gradlew build
```

### Skip the local database part

If you want to skip the local database part, just run ``./gradlew test``. 
You will be able to run ``./gradle test`` because integration tests are executed in the separate ``intTest`` task as part of overall ``check``.
