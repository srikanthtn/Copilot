<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Overview

> **CO-STAR Context Block** — Ingested by code-generator.prompt.md Phase 0

## Domain Identity

| Field         | Value                                   |
|---------------|-----------------------------------------|
| Domain Name   | Payments                                |
| Package       | `com.bank.payments`                     |
| Bounded Context | Payment Initiation and Processing     |
| Default Scheme | SEPA Credit Transfer (SCT)            |

## Supported Payment Schemes

| Scheme        | Type     | Max Amount        | Settlement    |
|---------------|----------|-------------------|---------------|
| SEPA SCT      | Batch    | EUR 999,999,999.99| D+1 (TARGET2) |
| SEPA Inst     | Real-time| EUR 100,000.00    | < 10 seconds  |
| SWIFT MT103   | Single   | No limit (AML)    | D+1 to D+3    |
| CHAPS         | RTGS     | No limit          | Same day      |
| BACS          | Batch    | GBP 20,000,000    | D+3           |

## Core Domain Entities

| Entity              | Type           | Key Fields                              |
|---------------------|----------------|-----------------------------------------|
| `Payment`           | Aggregate Root | id, amount, currency, status, iban      |
| `PaymentInstruction`| Entity         | reference, beneficiaryIban, chargesCode |
| `BeneficiaryAccount`| Value Object   | iban, bic, name, address                |
| `Money`             | Value Object   | amount (BigDecimal/DECIMAL128), currency|
| `PaymentReference`  | Value Object   | end-to-end ID, instruction ID           |
| `PaymentStatus`     | State FSM      | CREATED→VALIDATED→SUBMITTED→SETTLED     |

## Domain Events

| Event                    | Trigger                              |
|--------------------------|--------------------------------------|
| `PaymentCreated`         | New payment instruction received     |
| `PaymentValidated`       | Business + compliance rules passed   |
| `PaymentSubmittedToScheme` | Sent to clearing scheme            |
| `PaymentSettled`         | Confirmation received from scheme    |
| `PaymentRejected`        | Validation or scheme rejection       |
| `SanctionsCheckPassed`   | OFAC/HMT/EU sanctions cleared        |

## Ubiquitous Language

| Term          | Definition                                           |
|---------------|------------------------------------------------------|
| IBAN          | International Bank Account Number (ISO 13616)        |
| BIC           | Bank Identifier Code (ISO 9362)                      |
| SCT           | SEPA Credit Transfer                                 |
| Inst          | Instant (real-time) payment                          |
| Clearing      | Multilateral netting before settlement               |
| Settlement    | Final, irrevocable transfer of funds                 |
| SCA           | Strong Customer Authentication (PSD2 Art.97)         |
| Nostro        | Bank's own account at a correspondent bank           |
| Vostro        | Correspondent bank's account held at our bank        |
