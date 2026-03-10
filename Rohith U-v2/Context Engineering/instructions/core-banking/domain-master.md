# Domain Instruction: Core Banking — Accounts, Ledger & Customer Management
**Version:** 2.0.0 | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY
- **Domain:** Core Banking (System of Record)
- **Core Principle:** This domain maintains financial truth — every balance, every ledger entry
- **Consistency Model:** STRONG consistency mandatory (no eventual consistency for balances)

---

## 2. CORE BUSINESS INVARIANTS

### 2.1 Double-Entry Accounting (Fundamental Law)
Every transaction MUST create balanced journal entries:
- `SUM(all DEBIT entries) == SUM(all CREDIT entries)` for every journal
- ANY imbalanced journal entry is a REGULATORY VIOLATION — BLOCKER
- Ledger is APPEND-ONLY — no updates, no deletes [FIN-002]

### 2.2 Account Balance Rules
- Account balance = `SUM(CREDIT postings) - SUM(DEBIT postings)` since account opening
- Balance may go negative ONLY if account has an approved overdraft facility
- Balance changes are ATOMIC (all-or-nothing across all affected accounts)
- Balance must be recalculated from ledger history for audit purposes (no stale cache)

### 2.3 Account Lifecycle States
```
PENDING_KYC → ACTIVE → DORMANT → SUSPENDED → CLOSED [TERMINAL]
                │                    │
                └────────────────────┘ (reactivation path)
```

### 2.4 Customer Identification
- Customer MUST complete KYC before account activation
- Identity documents MUST be cryptographically verified
- Beneficial ownership MUST be established for corporate accounts (> 25% ownership threshold)
- PEP status check MANDATORY at onboarding and annually

---

## 3. REGULATORY CONSTRAINTS
[SOURCE: code-generation-master.md §5.1, §5.3, §5.5]

- **GDPR Art.17:** Customer PII stored in isolated vault (crypto-shredding on erasure request)
- **FATCA/CRS:** US persons and exchange-relevant accounts must be identified and reported
- **Basel III CET1:** Capital adequacy ratios calculated from core banking data daily
- **BCBS 239:** Data lineage and aggregation risk — every balance traceable to source transactions
- **KYC (AML 6AMLD):** Enhanced Due Diligence for high-risk customers; ongoing monitoring
- **IFRS 9:** Expected Credit Loss (ECL) staging — Stage 1/2/3 based on delinquency

---

## 4. FORBIDDEN OPERATIONS

- ⛔ Mutable ledger entries (READ: append-only) — BLOCKER [FIN-002]
- ⛔ Balance computed outside of ledger (no cached balance without recalculation audit) — BLOCKER
- ⛔ Account activated without KYC completion — BLOCKER
- ⛔ Customer PII logged in plaintext — BLOCKER [SEC-002]
- ⛔ Negative amount in ledger posting — use Direction enum — BLOCKER [FM-003]
- ⛔ Cross-account balance without atomic transaction — BLOCKER

---

## 5. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Type | Purpose |
|--------|------|---------|
| `BankAccount` | Aggregate | Account with lifecycle |
| `LedgerEntry` | Entity (append-only) | Single posting |
| `JournalEntry` | Aggregate | Balanced set of postings |
| `CustomerProfile` | Aggregate | KYC-verified customer |
| `KycVerificationRecord` | Value Object | KYC evidence |
| `AccountBalance` | Value Object | Point-in-time balance |
| `OverdraftFacility` | Entity | Approved overdraft |
| `AccountStatementPeriod` | Value Object | Statement range |
| `ReconciliationResult` | Entity | Reconciliation outcome |
| `PepScreeningResult` | Value Object | PEP check result |
| `BeneficialOwnerRecord` | Entity | UBO record |
| `AccountClosureRequest` | Command | Close account |

---

## 6. DATA CLASSIFICATION
- `CustomerProfile.name`, `CustomerProfile.dob`, `CustomerProfile.address` → PII (AES-256)
- `BankAccount.iban`, `BankAccount.balance` → Financial Confidential
- `LedgerEntry` → Append-only; retention 10 years (regulatory)
- `KycVerificationRecord` → Retain 5 years post account closure
