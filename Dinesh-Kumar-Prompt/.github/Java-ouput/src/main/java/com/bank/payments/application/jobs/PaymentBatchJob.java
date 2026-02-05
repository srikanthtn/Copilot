package com.bank.payments.application.jobs;

import com.bank.payments.domain.model.PaymentInstruction;
import com.bank.payments.domain.specifications.PaymentSpecifications;
import com.bank.payments.infrastructure.persistence.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentBatchJob {
    private static final Logger logger = LoggerFactory.getLogger(PaymentBatchJob.class);
    private final PaymentRepository paymentRepository;

    public PaymentBatchJob(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void processBatch() {
        List<PaymentInstruction> batch = paymentRepository.fetchPendingPayments();
        Set<UUID> seenIds = batch.stream().map(PaymentInstruction::getTransactionId).collect(Collectors.toSet());
        long validCount = batch.stream()
                .filter(PaymentSpecifications.isValidAmount())
                .filter(PaymentSpecifications.isSupportedCurrency())
                .filter(PaymentSpecifications.isRecent())
                .filter(PaymentSpecifications.isNotDuplicate(seenIds))
                .filter(PaymentSpecifications.isValidIban())
                .count();
        logger.info("Processed batch: total={}, valid={}", batch.size(), validCount);
    }
}
