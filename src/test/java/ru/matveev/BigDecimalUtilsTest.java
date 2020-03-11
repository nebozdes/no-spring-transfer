package ru.matveev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalUtilsTest {

    @Test
    @DisplayName("Test if left value is lesser and result is false")
    void testLeftIsLesser() {
        Assertions.assertFalse(BigDecimalUtils.isGreaterOrEqual(BigDecimal.ZERO, BigDecimal.TEN));
    }

    @Test
    @DisplayName("Test if left value is lesser and result is true")
    void testEqual() {
        assertTrue(BigDecimalUtils.isGreaterOrEqual(BigDecimal.TEN, BigDecimal.TEN));
    }

    @Test
    @DisplayName("Test if left value is lesser and result is true")
    void testLeftIsGreater() {
        assertTrue(BigDecimalUtils.isGreaterOrEqual(BigDecimal.TEN, BigDecimal.ZERO));
    }
}