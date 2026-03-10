# Core Banking — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Core Banking domain governs the system of record for customer accounts,
balances, ledger postings, and financial product lifecycle management.

This domain is responsible for maintaining **financial truth** across the institution.

---

## Characteristics

- Strong consistency is mandatory — no eventual consistency for ledger operations.
- All transactions must be atomic, consistent, isolated, and durable (ACID).
- All state changes must be traceable to an explicit, authorised originating event.
- Auditability is non-negotiable — every mutation must produce an audit record.
- Historical balances must be reproducible at any point in time.

---

## Domain Authority

Core Banking is the **authoritative source** for:
- Account state and status lifecycle
- Balance calculation and ledger integrity
- Customer financial identity and product holdings

Other domains MAY reference Core Banking data but MUST NOT redefine or mutate it.

---

## Chain Reasoning Responsibilities

In Stage 3 (BFSI Domain Interpretation), Core Banking context triggers:
- Resolution of account entities to `Account`, `LedgerEntry`, `AccountBalance`
- Application of ledger symmetry business rules (FM-006)
- State machine constraints for account and transaction lifecycle
- Regulatory constraints from RBI, Basel III, or applicable central bank guidelines

In Stage 4 (Compliance Evaluation), Core Banking operations trigger:
- Audit trail obligation check (COMP-008)
- KYC status verification (COMP-002)
- Transaction threshold reporting obligations (COMP-005)
