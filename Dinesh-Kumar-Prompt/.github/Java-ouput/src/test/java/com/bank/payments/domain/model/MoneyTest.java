package com.bank.payments.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Currency;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    @Test
    void testMoneyCreationAndAddition() {
        Money m1 = new Money(new BigDecimal("100.00"), Currency.getInstance("EUR"));
        Money m2 = new Money(new BigDecimal("50.00"), Currency.getInstance("EUR"));
        Money sum = m1.add(m2);
        assertEquals(new BigDecimal("150.0000"), sum.getAmount());
        assertEquals("EUR", sum.getCurrency().getCurrencyCode());
    }

    @Test
    void testCurrencyMismatchThrows() {
        Money m1 = new Money(new BigDecimal("100.00"), Currency.getInstance("EUR"));
        Money m2 = new Money(new BigDecimal("50.00"), Currency.getInstance("USD"));
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    void testNegativeAndZero() {
        Money m1 = new Money(new BigDecimal("-10.00"), Currency.getInstance("EUR"));
        Money m2 = new Money(BigDecimal.ZERO, Currency.getInstance("EUR"));
        assertTrue(m1.isNegative());
        assertTrue(m2.isZero());
    }
}
