package com.bank.payments.application.commands;

import com.bank.payments.domain.model.PaymentInstruction;

public final class ProcessPaymentCommand {
    private final PaymentInstruction paymentInstruction;

    public ProcessPaymentCommand(PaymentInstruction paymentInstruction) {
        if (paymentInstruction == null) {
            throw new IllegalArgumentException("PaymentInstruction must not be null");
        }
        this.paymentInstruction = paymentInstruction;
    }

    public PaymentInstruction getPaymentInstruction() {
        return paymentInstruction;
    }
}
