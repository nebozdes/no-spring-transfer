package ru.matveev.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matveev.CleanDBTest;
import ru.matveev.exceptions.LocalApplicationException;
import ru.matveev.model.Account;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class AccountsTest extends CleanDBTest {

    @Inject
    private Accounts accounts;

    @Override
    @BeforeEach
    public void init() {
        super.init();
    }


    @Test
    @DisplayName("Test if correct data is returned for existing account ID")
    public void testGetExistingAccount() {
        Account account = accounts.get(1L);

        assertNotNull(account);
        assertEquals(1L, account.id);
        assertEquals(100, account.balance.longValue());
    }

    @Test
    @DisplayName("Test if correct exception is thrown for not-existing account ID")
    public void testGetNotExistingAccount() {
        assertThrows(LocalApplicationException.class, () -> accounts.get(3L));
    }

    @Test()
    @DisplayName("Test if correct exception is thrown for not valid account ID")
    public void testNotPositiveAccountId() {
        assertThrows(ConstraintViolationException.class, () -> accounts.get(-1L));
    }
}
