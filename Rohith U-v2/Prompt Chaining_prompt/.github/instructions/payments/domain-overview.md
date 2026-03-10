# Payments — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Payments domain governs all financial value transfers between parties, including
retail payments, corporate payments, cross-border transfers, clearing, and settlement.

This domain ensures **payment integrity**, **regulatory compliance**, and **timely settlement**.

---

## Characteristics

- Every payment instruction must be authorised, validated, and traceable.
- Payment messages must comply with applicable messaging standards (ISO 20022).
- SLA adherence is regulatory in real-time payment schemes (e.g., SEPA Inst < 10s).
- Payment finality must be explicit — a settled payment cannot be unilaterally reversed.

---

## Supported Payment Types

| Type                  | Standard    | Settlement Window          |
|-----------------------|-------------|----------------------------|
| SEPA Credit Transfer  | ISO 20022   | D+0 (standard) / < 10s (Inst)|
| SEPA Direct Debit     | ISO 20022   | D+2 / D+5                  |
| SWIFT MT103           | SWIFT MT    | D+1 to D+3                 |
| RTGS (Domestic)       | Country-specific | Near-real-time         |
| Card Payments         | ISO 8583 / payment network | < 3 seconds  |
| ACH / NEFT / IMPS     | Country-specific | Varies                 |

---

## Chain Reasoning Responsibilities

### Stage 3 Triggers
- Resolution of payment entities to `PaymentInstruction`, `ClearingMessage`, `SettlementRecord`
- Application of ISO 20022 message structure rules
- Identification of SEPA vs. cross-border vs. domestic routing logic

### Stage 4 Compliance Triggers
| Check ID  | Description                                   |
|-----------|-----------------------------------------------|
| COMP-001  | AML screening on all payment parties          |
| COMP-002  | KYC/CDD status verified for originating party |
| COMP-003  | Sanctions screening on all counterparties     |
| COMP-005  | Threshold reporting obligations met           |
| COMP-007  | Strong Customer Authentication (PSD2 SCA)     |
| COMP-008  | Audit trail entry created                     |
| COMP-009  | Cross-border regulatory requirements assessed |

### Stage 5 Fraud Signal Triggers
- FS-001, FS-002, FS-003, FS-004, FS-006, FS-008, FS-010 are all active for payments.

---

## Prohibited Operations

- Initiating a payment without completed AML and sanctions screening.
- Bypassing PSD2 Strong Customer Authentication for regulated payment types.
- Processing payments to or from sanctioned entities or jurisdictions.
- Allowing settlement finality reversal without an explicit reversal instruction.
- Using informal payment routing (not aligned with ISO 20022 schema requirements).
