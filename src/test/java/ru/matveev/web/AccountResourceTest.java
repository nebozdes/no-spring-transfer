package ru.matveev.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matveev.CleanDBTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class AccountResourceTest extends CleanDBTest {

    @Override
    @BeforeEach
    public void init() {
        super.init();
    }

    @Test
    @DisplayName("Test if correct data is returned for all existing accounts")
    public void testGetAllExistingAccounts() {
        given()
                .when().get("/account")
                .then()
                .statusCode(200)
                .log()
                .all()
                .body("found", equalTo(2))
                .body("results.size()", equalTo(2))
                .body("results.get(0).id", equalTo(1))
                .body("results.get(0).balance", equalTo(100f))
                .body("results.get(1).id", equalTo(2))
                .body("results.get(1).balance", equalTo(200f))
        ;
    }

    @Test
    @DisplayName("Test if correct data is returned for existing account ID")
    public void testGetExistingAccount() {
        given()
                .when().get("/account/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("balance", equalTo(100f))
        ;
    }

    @Test
    @DisplayName("Test if correct exception is thrown for not-existing account ID")
    public void testGetNotExistingAccount() {
        given()
                .when().get("/account/3")
                .then()
                .statusCode(400)
                .body("error", equalTo("Account with id = 3 not found"))
        ;
    }

    @Test
    @DisplayName("Test if correct exception is thrown for not valid account ID")
    public void testNotPositiveAccountId() {
        given()
                .when().get("/account/-1")
                .then()
                .statusCode(400)
        ;
    }
}