package ru.matveev.service;

import ru.matveev.model.Account;
import ru.matveev.web.dto.PageObject;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface Accounts {

    Account get(@NotNull @Positive Long accountId);

    PageObject<Account> list(int page, int limit);
}
