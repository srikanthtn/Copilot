package com.bank.payments.domain.specifications;

import com.bank.payments.domain.model.PaymentInstruction;
import java.util.function.Predicate;

public final class PaymentSpecifications {
    public static Predicate<PaymentInstruction> isValidAmount() {
        return pi -> pi.getAmount().getAmount().compareTo(java.math.BigDecimal.ZERO) > 0;
    }

    public static Predicate<PaymentInstruction> isSupportedCurrency() {
        return pi -> "EUR".equals(pi.getCurrency());
    }

    public static Predicate<PaymentInstruction> isRecent() {
        return pi -> pi.getInstructedAt().isAfter(java.time.LocalDateTime.now().minusDays(30));
    }

    public static Predicate<PaymentInstruction> isNotDuplicate(java.util.Set<java.util.UUID> seenIds) {
        return pi -> seenIds.add(pi.getTransactionId());
    }

    public static Predicate<PaymentInstruction> isValidIban() {
        return pi -> pi.getDebtorIban() != null && pi.getCreditorIban() != null;
    }
}
