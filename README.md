# Challenge

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

## Explicit requirements:

1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

## Implicit requirements:

1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

#How to build

```
./mvnw clean package
```

#How to run

```
java -jar target/no-spring-transfer-1.0.0-SNAPSHOT-runner.jar
```

## Tests

```
./mvnw clean test
```

# Stack

1. Java 11
2. Quarkus framework: https://quarkus.io/
3. H2 database (in-memory)
4. Project Lombok 
5. Flyway migration engine

# API entry-points

* GET http://localhost:8080/account - get information about all accounts in system
* GET http://localhost:8080/account/{id} - get information about specific account
* POST http://localhost:8080/transaction - create new transaction
* GET http://localhost:8080/transaction?limit=200 - get list of transactions