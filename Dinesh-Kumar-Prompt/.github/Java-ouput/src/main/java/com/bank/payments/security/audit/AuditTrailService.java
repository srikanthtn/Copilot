package com.bank.payments.security.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuditTrailService {
    private static final Logger logger = LoggerFactory.getLogger(AuditTrailService.class);

    public void logAction(String action, UUID transactionId, String actor, String details) {
        String maskedDetails = details == null ? "" : details.replaceAll("[A-Z]{2}[0-9A-Z]{13,30}", "****");
        logger.info("AUDIT | time={} | action={} | txId={} | actor={} | details={}",
                Instant.now(), action, maskTransactionId(transactionId), actor, maskedDetails);
    }

    private String maskTransactionId(UUID transactionId) {
        if (transactionId == null)
            return "****";
        String tx = transactionId.toString();
        return tx.substring(0, 4) + "****" + tx.substring(tx.length() - 4);
    }
}
