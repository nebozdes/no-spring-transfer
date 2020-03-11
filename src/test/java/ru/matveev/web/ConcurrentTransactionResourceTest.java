package ru.matveev.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matveev.CleanDBTest;

import javax.json.Json;

import java.math.BigDecimal;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
public class ConcurrentTransactionResourceTest extends CleanDBTest {

    @Override
    @BeforeEach
    public void init() {
        super.init();
    }

    @Test
    @DisplayName("Test if 100 of concurrent transactions don't lead to exceptions or inconsistency")
    public void testConcurrentTransactions() {
        //Check state of accounts before the call
        given()
                .when().get("/account/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("balance", equalTo(100f))
        ;

        given()
                .when().get("/account/2")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("balance", equalTo(200f))
        ;

        //Do 100 transactions in 10 threads
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);

        forkJoinPool.submit(() ->
                IntStream.range(0, 100).parallel()
                        .forEach(i -> new Thread(() ->
                                given()
                                        .when()
                                        .contentType(ContentType.JSON)
                                        .body(
                                                Json.createObjectBuilder()
                                                        .add("fromAccountId", 1)
                                                        .add("toAccountId", 2)
                                                        .add("amount", BigDecimal.ONE)
                                                        .build().toString()
                                        )
                                        .post("/transaction")
                                        .then()
                                        .statusCode(200)
                        ).start()))
        ;

        forkJoinPool.submit(() ->
                IntStream.range(0, 100).parallel()
                        .forEach(i -> new Thread(() ->
                                given()
                                        .when()
                                        .contentType(ContentType.JSON)
                                        .body(
                                                Json.createObjectBuilder()
                                                        .add("fromAccountId", 2)
                                                        .add("toAccountId", 1)
                                                        .add("amount", BigDecimal.ONE)
                                                        .build().toString()
                                        )
                                        .post("/transaction")
                                        .then()
                                        .statusCode(200)
                        ).start())
        );

        try {
            forkJoinPool.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
        }

        //Check that all 200 transactions are done
        given()
                .when().get("/transaction?limit=77")
                .then()
                .statusCode(200)
                .body("found", equalTo(200))
                .body("results.size()", equalTo(77))
        ;

        //Check state of accounts after the calls
        given()
                .when().get("/account/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("balance", equalTo(100f))
        ;

        given()
                .when().get("/account/2")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("balance", equalTo(200f))
        ;
    }
}