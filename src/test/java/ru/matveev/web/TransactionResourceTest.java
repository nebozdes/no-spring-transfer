package ru.matveev.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matveev.CleanDBTest;

import javax.json.Json;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class TransactionResourceTest extends CleanDBTest {

    @Override
    @BeforeEach
    public void init() {
        super.init();
    }

    @Test
    @DisplayName("Test if correct transaction is done and account data is updated")
    public void testCorrectMoneyTransfer() {
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

        //Do transaction
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(
                        Json.createObjectBuilder()
                                .add("fromAccountId", 2)
                                .add("toAccountId", 1)
                                .add("amount", 100)
                                .build().toString()
                )
                .post("/transaction")
                .then()
                .statusCode(200)
                .body("amount", equalTo(100))
                .body("fromAccount.id", equalTo(2))
                .body("fromAccount.balance", equalTo(100f))
                .body("toAccount.id", equalTo(1))
                .body("toAccount.balance", equalTo(200f))
        ;

        //Check state of accounts after the call
        given()
                .when().get("/account/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("balance", equalTo(200f))
        ;

        given()
                .when().get("/account/2")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("balance", equalTo(100f))
        ;
    }

    @Test
    @DisplayName("Test if correct exception is thrown when account doesn't exist")
    public void testExceptionWhenNotExistingAccount() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(
                        Json.createObjectBuilder()
                                .add("fromAccountId", 1)
                                .add("toAccountId", 3)
                                .add("amount", 100)
                                .build().toString()
                )
                .post("/transaction")
                .then()
                .statusCode(400)
                .body("error", equalTo("Account with id = 3 not found"))
        ;
    }

    @Test
    @DisplayName("Test if correct exception is thrown when account doesn't have enough money")
    public void testExceptionWhenNotEnoughMoney() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(
                        Json.createObjectBuilder()
                                .add("fromAccountId", 1)
                                .add("toAccountId", 2)
                                .add("amount", 500)
                                .build().toString()
                )
                .post("/transaction")
                .then()
                .statusCode(400)
        ;
    }

    @Test
    @DisplayName("Test if correct exception is thrown when amount to transfer is not positive")
    public void testExceptionWhenAmountIsNotPositive() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(
                        Json.createObjectBuilder()
                                .add("fromAccountId", 1)
                                .add("toAccountId", 2)
                                .add("amount", -100)
                                .build().toString()
                )
                .post("/transaction")
                .then()
                .statusCode(400);
    }
}