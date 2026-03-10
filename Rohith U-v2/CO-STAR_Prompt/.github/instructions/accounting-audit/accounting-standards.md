<!-- version: 2.0.0 | domain: accounting-audit | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Accounting & Audit — Accounting Standards

## IFRS Standards Applied

| Standard   | Area                                 | Key Code Requirement                    |
|------------|--------------------------------------|-----------------------------------------|
| IFRS 9     | Financial Instruments / Impairment   | ECL model: Stage 1/2/3 provisioning    |
| IFRS 13    | Fair Value Measurement               | Level 1/2/3 fair value hierarchy       |
| IFRS 15    | Revenue Recognition                  | 5-step model; no revenue without POB   |
| IFRS 16    | Leases                               | Right-of-use asset on balance sheet    |
| IAS 1      | Presentation of Financial Statements | Comparative period mandatory           |
| IAS 8      | Accounting Policies, Errors          | Retrospective restatement rules        |
| IAS 36     | Impairment of Assets                 | Annual impairment test for goodwill    |

## Chart of Accounts Structure

```
1000-1999  → Assets (liquid: 1000-1499; illiquid: 1500-1999)
2000-2999  → Liabilities (deposits: 2000-2499; borrowed: 2500-2999)
3000-3999  → Equity (share capital: 3000; retained earnings: 3999)
4000-4999  → Revenue (interest income: 4000; fee income: 4500)
5000-5999  → Cost of Funds (interest expense: 5000)
6000-6999  → Operating Expenses (staff: 6000; IT: 6500)
7000-7999  → Provisions and Impairment (ECL: 7000; litigation: 7500)
```

## Financial Math Rules

- All monetary figures: BigDecimal with MathContext.DECIMAL128
- Rounding: HALF_EVEN (Banker's rounding) on all calculations
- Interest accrual: ACT/365 or ACT/360 per instrument specification
- FX translation: closing rate for P&L; average rate for flow items
- ECL discount rate: effective interest rate of the instrument


# Accounting & Audit — Audit Controls

## Internal Audit Requirements

| Control                       | Implementation                           |
|-------------------------------|------------------------------------------|
| Segregation of duties         | No single user: authorise + post + approve |
| 4-eyes on manual journals      | JournalEntry.authorisedBy ≠ JournalEntry.postedBy |
| Immutable audit trail          | Append-only Delta Lake; no UPDATE/DELETE |
| Periodic reconciliation        | GL vs sub-ledger daily automated check  |
| Exception reporting            | `ReconciliationBreak` event auto-escalates |
| Management account sign-off    | TrialBalance approved before month-close |

## External Audit Evidence

All evidence provided to external auditors must:
1. Be extracted from immutable audit trail (Delta Lake, no modifications)
2. Include: record ID, timestamp, user ID, action, before/after values
3. Be produced without the ability to filter out unfavourable records
4. Be time-stamped with UTC and stored with HMAC-SHA256 integrity signature

## Forbidden Controls

| Pattern                                   | Reason                        |
|-------------------------------------------|-------------------------------|
| Delete or update posted journal entries   | IFRS / accounting law         |
| Allow single person to post + approve     | Segregation of duties failure |
| Reconcile GL to sub-ledger with tolerance > 0 | Accounting accuracy       |
| Generate financial reports without period close | Completeness control    |
