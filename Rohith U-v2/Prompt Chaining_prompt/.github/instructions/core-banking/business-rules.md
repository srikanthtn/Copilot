# Core Banking — Business Rules
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Fundamental Rules

- Account balances MUST always reconcile with the sum of their ledger entries.
- Every balance change MUST originate from a ledger posting — direct manipulation is forbidden.
- Ledger entries are **append-only and immutable** once posted.
- The institution cannot hold a liability without a corresponding asset entry.

---

## Invariants

- Debit and credit symmetry MUST be preserved across every transaction.
- The sum of all postings MUST equal the net balance delta.
- Historical balances MUST remain reproducible from the ledger at any point in time.
- Every `Account` MUST be associated with a verified `CustomerRecord`.

---

## KYC-Linked Business Rules

- An account MUST NOT be opened without at least KYC Tier 1 completion.
- Transactions above regulatory thresholds MUST be blocked if KYC Tier is below the required level.
- Periodic KYC refresh MUST be tracked and enforced according to the customer risk classification.

---

## Prohibited Business Behaviour

- Direct balance manipulation without a corresponding ledger entry.
- Silent corrections to ledger records.
- Retroactive mutation of any ledger entry.
- Posting to an account in `FROZEN` or `CLOSED` status without explicit override authorization.
- Creating transactions with zero or negative amounts (FM-003, FM-005).

---

## Chain Rules Applied in Stage 3

| Rule                        | Application                                            |
|-----------------------------|--------------------------------------------------------|
| Ledger symmetry (FM-006)    | Verify debit/credit pair exists for every transaction  |
| State machine (SE-002)      | Validate account is in an active, non-terminal state   |
| Monetary direction (FM-005) | Confirm DEBIT/CREDIT enum is used, not signed amounts  |
| Temporal precision (FM-007) | All posting dates must be ISO 8601 formatted           |
