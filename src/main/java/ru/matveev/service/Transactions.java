package ru.matveev.service;

import ru.matveev.model.Transaction;
import ru.matveev.web.dto.PageObject;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.math.BigDecimal;

public interface Transactions {

    Transaction create(@NotNull @Positive Long fromAccountId, @NotNull @Positive Long toAccountId, @NotNull @Positive BigDecimal amount);

    PageObject<Transaction> list(int page, int limit);
}
