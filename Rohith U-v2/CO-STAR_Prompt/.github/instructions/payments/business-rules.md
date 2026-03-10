<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Business Rules

> **CO-STAR Objective Block** — Ingested by code-generator.prompt.md Phase 0
> These rules map directly to `Specification[T]` implementations in the domain layer.

## Payment Amount Rules

| Rule ID          | Rule                                           | Error If Violated                |
|------------------|------------------------------------------------|----------------------------------|
| PAYMENTS-RULE-001| Amount must be > 0.00                          | `NegativeOrZeroAmountError`      |
| PAYMENTS-RULE-002| SEPA SCT max = EUR 999,999,999.99              | `AmountExceedsSchemeLimit`       |
| PAYMENTS-RULE-003| SEPA Inst max = EUR 100,000.00                 | `AmountExceedsInstantLimit`      |
| PAYMENTS-RULE-004| Amount scale must be ≤ 2 decimal places        | `InvalidAmountPrecisionError`    |
| PAYMENTS-RULE-005| Debit and credit amounts must match exactly    | `AmountMismatchError`            |

## IBAN Validation Rules

| Rule ID          | Rule                                           | Error If Violated                |
|------------------|------------------------------------------------|----------------------------------|
| PAYMENTS-RULE-010| IBAN must pass ISO 13616 checksum (mod-97)     | `InvalidIbanChecksumError`       |
| PAYMENTS-RULE-011| IBAN country code must be in allowed SEPA zone | `UnsupportedCountryError`        |
| PAYMENTS-RULE-012| IBAN length must match country specification   | `InvalidIbanLengthError`         |
| PAYMENTS-RULE-013| IBAN must not be on internal blocked list      | `BlockedAccountError`            |

## BIC / Routing Rules

| Rule ID          | Rule                                           | Error If Violated                |
|------------------|------------------------------------------------|----------------------------------|
| PAYMENTS-RULE-020| BIC must be 8 or 11 characters                 | `InvalidBicFormatError`          |
| PAYMENTS-RULE-021| BIC must be reachable via TARGET2 for SEPA     | `UnreachableBicError`            |
| PAYMENTS-RULE-022| BIC country must match IBAN country            | `CountryMismatchError`           |

## Duplicate Detection Rules

| Rule ID          | Rule                                           | Error If Violated                |
|------------------|------------------------------------------------|----------------------------------|
| PAYMENTS-RULE-030| End-to-end ID must be unique per originator    | `DuplicatePaymentReferenceError` |
| PAYMENTS-RULE-031| Same IBAN + amount + date = suspected duplicate| `SuspectedDuplicateError`        |
| PAYMENTS-RULE-032| Duplicate window = 24 hours                    | `DuplicateWindowViolationError`  |

## SCA Rules (PSD2 Art. 97)

| Rule ID          | Rule                                           |
|------------------|------------------------------------------------|
| PAYMENTS-RULE-040| SCA required for electronic payments > EUR 30  |
| PAYMENTS-RULE-041| SCA required for remote payments unless exemption applies |
| PAYMENTS-RULE-042| Trusted beneficiary exemption applies after 5 transactions |

## Reference Format Rules

| Rule ID          | Rule                                           |
|------------------|------------------------------------------------|
| PAYMENTS-RULE-050| End-to-end reference: max 35 chars [a-zA-Z0-9] |
| PAYMENTS-RULE-051| Instruction ID must be globally unique (UUID-based) |
| PAYMENTS-RULE-052| Remittance info: max 140 chars (structured or unstructured) |
