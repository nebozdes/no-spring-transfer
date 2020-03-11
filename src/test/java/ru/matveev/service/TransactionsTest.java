package ru.matveev.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matveev.CleanDBTest;
import ru.matveev.exceptions.LocalApplicationException;
import ru.matveev.model.Account;
import ru.matveev.model.Transaction;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class TransactionsTest extends CleanDBTest {

    @Inject
    private Transactions transactions;

    @Inject
    private Accounts accounts;

    @Override
    @BeforeEach
    public void init() {
        super.init();
    }

    @Test
    @DisplayName("Test if correct transaction is done and account data is updated")
    @Transactional
    public void testCorrectMoneyTransfer() {
        Account fromAccount = accounts.get(1L);
        Account toAccount = accounts.get(2L);

        BigDecimal initialFromBalance = fromAccount.balance;
        BigDecimal initialToBalance = toAccount.balance;

        assertEquals(100, initialFromBalance.longValue());
        assertEquals(200, initialToBalance.longValue());

        Transaction transaction = transactions.create(fromAccount.id, toAccount.id, BigDecimal.valueOf(100));

        assertNotNull(transaction);
        assertNotNull(transaction.id);
        assertEquals(100, transaction.amount.longValue());
        assertEquals(0, transaction.fromAccount.balance.longValue());
        assertEquals(300, transaction.toAccount.balance.longValue());

        Account newFromAccount = accounts.get(1L);
        Account newToAccount = accounts.get(2L);

        BigDecimal finalFromBalance = fromAccount.balance;
        BigDecimal finalToBalance = toAccount.balance;

        assertEquals(0, finalFromBalance.longValue());
        assertEquals(300, finalToBalance.longValue());
    }

    @Test
    @DisplayName("Test if correct exception is thrown when account doesn't exist")
    public void testExceptionWhenNotExistingAccount() {
        assertThrows(LocalApplicationException.class, () -> transactions.create(1L, 3L, BigDecimal.valueOf(100)));
    }

    @Test
    @DisplayName("Test if correct exception is thrown when account doesn't have enough money")
    public void testExceptionWhenNotEnoughMoney() {
        assertThrows(LocalApplicationException.class, () -> transactions.create(1L, 2L, BigDecimal.valueOf(500)));
    }

    @Test
    @DisplayName("Test if correct exception is thrown when amount to transfer is not positive")
    public void testExceptionWhenAmountIsNotPositive() {
        assertThrows(ConstraintViolationException.class, () -> transactions.create(1L, 2L, BigDecimal.valueOf(-500)));
    }
}
