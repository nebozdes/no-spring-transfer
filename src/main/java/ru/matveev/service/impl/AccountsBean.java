package ru.matveev.service.impl;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import ru.matveev.exceptions.LocalApplicationException;
import ru.matveev.model.Account;
import ru.matveev.service.Accounts;
import ru.matveev.web.dto.PageObject;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.text.MessageFormat;

@ApplicationScoped
public class AccountsBean implements Accounts {

    @Override
    @Transactional
    public Account get(@NotNull @Positive Long accountId) {
        Account account = Account.findById(accountId, LockModeType.PESSIMISTIC_READ);

        if (account == null) {
            throw new LocalApplicationException(MessageFormat.format("Account with id = {0} not found", accountId));
        }

        return account;
    }

    @Override
    public PageObject<Account> list(int page, int limit) {
        PanacheQuery<Account> query = Account.findAll();

        return PageObject.<Account>builder()
                .found(query.count())
                .results(query.page(page, limit).list())
                .build();
    }
}
