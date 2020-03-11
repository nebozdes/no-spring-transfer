package ru.matveev.service.impl;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import ru.matveev.exceptions.LocalApplicationException;
import ru.matveev.model.Account;
import ru.matveev.model.Transaction;
import ru.matveev.service.Accounts;
import ru.matveev.service.Transactions;
import ru.matveev.web.dto.PageObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;

import static ru.matveev.BigDecimalUtils.isGreaterOrEqual;

@ApplicationScoped
public class TransactionsBean implements Transactions {

    private final Accounts accounts;

    @Inject
    public TransactionsBean(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    @Transactional
    public Transaction create(@NotNull @Positive Long fromAccountId, @NotNull @Positive Long toAccountId, @NotNull @Positive BigDecimal amount) {
        if (Objects.equals(fromAccountId, toAccountId)) {
            throw new LocalApplicationException("From and To accounts are the same!");
        }

        boolean fromIdIsFirst = fromAccountId < toAccountId;

        Account from;
        Account to;

        //To avoid dead-locks
        if (fromIdIsFirst) {
            from = accounts.get(fromAccountId);
            to = accounts.get(toAccountId);
        } else {
            to = accounts.get(toAccountId);
            from = accounts.get(fromAccountId);
        }

        BigDecimal fromBalance = from.balance;

        if (isGreaterOrEqual(from.balance, amount)) {
            BigDecimal toBalance = to.balance;

            from.balance = fromBalance.subtract(amount);
            to.balance = toBalance.add(amount);
        } else {
            throw new LocalApplicationException("Not enough money to transfer!");
        }

        Transaction transaction = new Transaction(from, to, amount);

        transaction.persist();

        return transaction;
    }

    @Override
    public PageObject<Transaction> list(int page, int limit) {
        PanacheQuery<Transaction> query = Transaction.findAll();

        return PageObject.<Transaction>builder()
                .found(query.count())
                .results(query.page(page, limit).list())
                .build();
    }
}
