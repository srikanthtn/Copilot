# Domain Instruction: Accounting & Audit — GL, Reconciliation & Close
**Version:** 2.0.0 | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY
- **Scope:** General Ledger, Period-End Close, External Audit, IFRS/GAAP Reporting
- **Principle:** Accounting is the final arbiter of financial truth — all values are immutable once posted

---

## 2. CORE INVARIANTS

### 2.1 General Ledger Rules
- All GL accounts must be structured in a Chart of Accounts (CoA) hierarchy
- Every GL posting must be balanced: Total Debits = Total Credits
- Posts are IMMUTABLE — corrections are new offsetting entries (not updates)
- All intercompany transactions eliminated on consolidation

### 2.2 Period-End Close Process
```
OPEN → IN_PROGRESS → SOFT_CLOSE → HARD_CLOSE [TERMINAL]
```
- SOFT_CLOSE: Adjustments allowed with approval
- HARD_CLOSE: No further entries; reports filed; period sealed
- Close window: ≤ 5 business days from period end (internal); ≤ 45 days for IFRS annual

### 2.3 Reconciliation Requirements
- Reconciliation performed: daily (intraday), monthly (GL vs sub-ledger), quarterly (detailed)
- Unreconciled items > 5 business days → escalation to Finance Controller
- Reconciliation items include source reference, amount, status, aging bucket

### 2.4 IFRS Standards Applied
| Standard | Requirement | Code Impact |
|----------|-------------|-------------|
| IFRS 9 | ECL staging (Stage 1/2/3) | `ExpectedCreditLossCalculator` |
| IFRS 15 | Revenue recognition over time / at point | `RevenueRecognitionEngine` |
| IFRS 16 | Right-of-use asset and lease liability | `LeaseAccountingEngine` |
| IFRS 17 | Insurance contract measurement | `InsuranceContractMeasurement` |
| IAS 39 | Hedge accounting (where grandfathered) | `HedgeAccountingValidator` |

---

## 3. REGULATORY CONSTRAINTS

- **SOX Section 302/404:** CEO/CFO certification of financial statements; ITGC controls
- **BCBS 239:** Data lineage for risk aggregation — every balance traceable
- **GDPR Art.17:** Right to erasure does NOT override accounting retention requirements
- **Retention:** Financial records MUST be retained for 7–10 years per jurisdiction

---

## 4. FORBIDDEN OPERATIONS

- ⛔ Modifying or deleting a posted GL entry — BLOCKER [FIN-002]
- ⛔ Posting to a HARD_CLOSED period — BLOCKER
- ⛔ Revenue recognized before performance obligation met (IFRS 15) — BLOCKER
- ⛔ Consolidation without intercompany elimination — BLOCKER
- ⛔ Financial records deleted before retention period — BLOCKER (regulatory)

---

## 5. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Purpose |
|--------|---------|
| `GeneralLedgerAccount` | CoA account definition |
| `JournalEntry` | Balanced set of GL postings |
| `TrialBalance` | Pre-close balance summary |
| `AccountingPeriod` | Open/closed period |
| `ReconciliationRecord` | Reconciliation item |
| `FinancialStatement` | P&L, Balance Sheet, Cash Flow |
| `ConsolidationElimination` | IC elimination entry |
| `AuditFinding` | Auditor observation |
| `SoxControlTest` | ITGC / application control test |
| `ExpectedCreditLossRecord` | IFRS 9 ECL calculation |
