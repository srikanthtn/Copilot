package com.bank.payments.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Iban {
    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}[0-9A-Z]{13,30}$");
    private final String value;

    public Iban(String value) {
        if (value == null || !IBAN_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid IBAN format");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getMasked() {
        if (value.length() < 4)
            return "****";
        return value.substring(0, 2) + "****" + value.substring(value.length() - 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Iban iban = (Iban) o;
        return value.equals(iban.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getMasked();
    }
}
