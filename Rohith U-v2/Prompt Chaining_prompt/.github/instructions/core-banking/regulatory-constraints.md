# Core Banking — Regulatory Constraints
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Mandatory Regulatory Constraints

- All monetary operations MUST be auditable and traceable to an explicit authorization.
- All customer-impacting changes MUST be traceable to a specific customer or operator action.
- Ledger state MUST be reconstructable at any historical point in time.
- Dormant account handling MUST comply with applicable unclaimed deposits regulations.

---

## Regulatory Expectations

| Requirement                           | Applicable Regulation         |
|---------------------------------------|-------------------------------|
| Deterministic transaction execution   | Basel III / Central Bank      |
| Full transaction lineage              | Basel III / BCBS              |
| Customer identity verification        | FATF Rec 10 / AMLD6           |
| Data retention and audit records      | GDPR / DPDPA / Basel III      |
| Threshold reporting obligations       | FinCEN / FATF / RBI Guidelines|
| Anti-money laundering monitoring      | AMLD6 / FATF / AML Rules      |

---

## Forbidden Practices

- Non-deterministic posting logic (outcome must not vary for the same inputs).
- Unlogged financial state changes.
- Account status changes without a corresponding authorization and audit record.
- Bypassing threshold-based reporting obligations.

---

## Chain Stage 4 Compliance Triggers

When Core Banking domain is in scope, the following compliance checks are MANDATORY:

| Check ID  | Description                                       |
|-----------|---------------------------------------------------|
| COMP-002  | KYC/CDD status verified and current               |
| COMP-005  | Transaction threshold reporting obligations met   |
| COMP-008  | Audit trail entry created for this operation      |
| COMP-007  | Consent and authorization verified for account ops|
