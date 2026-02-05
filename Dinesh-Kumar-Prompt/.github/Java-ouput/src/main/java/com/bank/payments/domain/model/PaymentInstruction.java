package com.bank.payments.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class PaymentInstruction {
    private final UUID transactionId;
    private final Iban debtorIban;
    private final Iban creditorIban;
    private final Money amount;
    private final LocalDateTime instructedAt;
    private final String currency;
    private final String status;

    public PaymentInstruction(UUID transactionId, Iban debtorIban, Iban creditorIban, Money amount,
            LocalDateTime instructedAt, String currency, String status) {
        if (transactionId == null || debtorIban == null || creditorIban == null || amount == null
                || instructedAt == null || currency == null || status == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }
        this.transactionId = transactionId;
        this.debtorIban = debtorIban;
        this.creditorIban = creditorIban;
        this.amount = amount;
        this.instructedAt = instructedAt;
        this.currency = currency;
        this.status = status;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Iban getDebtorIban() {
        return debtorIban;
    }

    public Iban getCreditorIban() {
        return creditorIban;
    }

    public Money getAmount() {
        return amount;
    }

    public LocalDateTime getInstructedAt() {
        return instructedAt;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PaymentInstruction that = (PaymentInstruction) o;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
