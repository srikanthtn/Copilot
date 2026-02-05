package com.bank.payments.security.masking;

public final class IdentifierMasker {
    private IdentifierMasker() {
    }

    public static String maskIban(String iban) {
        if (iban == null || iban.length() < 4)
            return "****";
        return iban.substring(0, 2) + "****" + iban.substring(iban.length() - 4);
    }

    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4)
            return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    public static String maskTransactionId(String transactionId) {
        if (transactionId == null || transactionId.length() < 4)
            return "****";
        return transactionId.substring(0, 4) + "****" + transactionId.substring(transactionId.length() - 4);
    }
}
