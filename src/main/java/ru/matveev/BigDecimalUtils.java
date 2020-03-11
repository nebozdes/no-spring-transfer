package ru.matveev;

import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

public final class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static boolean isGreaterOrEqual(@NotNull BigDecimal left, @NotNull BigDecimal right) {
        return left.compareTo(right) >= 0;
    }
}
