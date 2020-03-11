package ru.matveev.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest {

    private @NotNull @Positive Long fromAccountId;
    private @NotNull @Positive Long toAccountId;

    private @NotNull @Positive BigDecimal amount;
}
