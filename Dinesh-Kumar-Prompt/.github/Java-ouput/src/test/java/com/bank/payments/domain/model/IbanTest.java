package com.bank.payments.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IbanTest {
    @Test
    void testValidIban() {
        Iban iban = new Iban("DE89370400440532013000");
        assertEquals("DE****3000", iban.getMasked());
    }

    @Test
    void testInvalidIbanThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Iban("INVALID_IBAN"));
    }

    @Test
    void testToStringIsMasked() {
        Iban iban = new Iban("FR7630006000011234567890189");
        assertEquals("FR****0189", iban.toString());
    }
}
