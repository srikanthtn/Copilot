<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Master Reference

> This is the consolidated entry point for the payments bounded context.
> Code generators ingest this file first, then load all other files in this folder.

## Domain Summary

The Payments bounded context handles the complete lifecycle of payment
instructions from initiation through clearing and settlement. It integrates
with SEPA (TARGET2/EBA CLEARING), SWIFT (MT/MX), and domestic RTGS schemes.

## Instruction Files Load Order

```
1. domain-overview.md          ← Entities, events, ubiquitous language
2. business-rules.md           ← Specifications[T] to implement
3. regulatory-constraints.md   ← Compliance controls required
4. allowed-operations.md       ← What the domain may do
5. forbidden-operations.md     ← What the domain must never do
6. data-boundaries.md          ← PII classification + retention
```

## Reference Packages

```
com.bank.payments.domain.model          → Payment, Money, BeneficiaryAccount
com.bank.payments.domain.events         → PaymentCreated, PaymentSettled, ...
com.bank.payments.domain.specifications → AmountSpecification, IbanSpecification
com.bank.payments.domain.services       → PaymentValidator, SanctionsChecker
com.bank.payments.application.commands  → InitiatePaymentCommand
com.bank.payments.application.jobs      → SepaPaymentBatchJob
com.bank.payments.infrastructure        → DeltaLakePaymentRepo, KafkaPublisher
com.bank.payments.security              → PiiMasker, AesGcmCipher, AuditLogger
com.bank.payments.observability         → PaymentMetricsRegistry
```

## Default Synthetic Test Data

For CSV generation (100 records), use:
- Debtor IBANs: GB29NWBK60161331926819 (cycling pattern with variations)
- Creditor IBANs: DE89370400440532013000 (German IBAN pattern)
- Amounts: BigDecimal range EUR 1.00 – EUR 50,000.00 (DECIMAL128)
- BIC: NWBKGB2L (NatWest), DEUTDEDB (Deutsche Bank)
- Timestamps: 2024-01-01T00:00:00Z to 2024-01-31T23:59:59Z
- Status: mix of SETTLED (80%), REJECTED (10%), PENDING (10%)
