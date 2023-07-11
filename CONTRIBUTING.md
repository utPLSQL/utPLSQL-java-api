# Contributing
*Don't forget to configure your JAVA_HOME environment variable.*

### Local database with utPLSQL and utPLSQL-demo-project

To usefully contribute you'll have to set up a local database with installed [latest utPLSQL v3](https://github.com/utPLSQL/utPLSQL) and [utPLSQL-demo-project](https://github.com/utPLSQL/utPLSQL-demo-project). 
The demo-project will serve as your test user. See .travis.yml to see an example on how it can be installed. 
By default, tests are executed against `app/app` user of `localhost:1521/XE database`. 

If you want to run tests against another database you may set `DB_URL`, `DB_USER`, `DB_PASS` environment variables.

When you have local database set up you can run the complete build including integration tests by executing 
```bash
./mvnw verify
```

### Skip the local database part

If you want to skip the local database part, just run
```bash
./mvnw test
```
