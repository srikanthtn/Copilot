package com.bank.payments.infrastructure.persistence;

import com.bank.payments.domain.model.PaymentInstruction;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository {
    List<PaymentInstruction> fetchPendingPayments();

    void save(PaymentInstruction paymentInstruction);

    PaymentInstruction findById(UUID transactionId);

    List<PaymentInstruction> findAll();
}
