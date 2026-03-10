<!-- version: 2.0.0 | domain: accounting-audit | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Accounting & Audit — Domain Overview

| Field         | Value                               |
|---------------|-------------------------------------|
| Domain Name   | Accounting and Audit                |
| Package       | `com.bank.accounting`               |
| Bounded Context | Financial Reporting, Audit Trail |

## Core Entities

| Entity               | Type           | Key Fields                                     |
|----------------------|----------------|------------------------------------------------|
| `JournalEntry`       | Aggregate Root | entryId, date, debitLeg, creditLeg, reference  |
| `GeneralLedgerAccount` | Entity       | accountCode, name, category, currency          |
| `TrialBalance`       | Entity         | period, accounts, totalDebits, totalCredits    |
| `AuditTrail`         | Entity         | recordId, action, userId, timestamp, before, after |
| `FinancialStatement` | Value Object   | period, type (PL/BS/CF), figures               |
| `AccountCode`        | Value Object   | chart-of-accounts code, natural balance        |

## Domain Events

| Event                      | Trigger                                    |
|----------------------------|--------------------------------------------|
| `JournalEntryPosted`       | Double-entry bookkeeping entry created     |
| `PeriodClosed`             | Month/quarter/year-end close completed     |
| `RestatementPosted`        | Prior period error corrected via restate   |
| `AuditFindingRaised`       | Internal/external audit exception raised   |
| `ReconciliationBreak`      | GL vs sub-ledger reconciliation fails      |
| `ImpairmentRecognised`     | IFRS 9 expected credit loss provision      |

## Double-Entry Rules

Every JournalEntry must satisfy:
```
sum(debitAmounts) == sum(creditAmounts)   ← algebraic equality
all amounts > 0 (BigDecimal, DECIMAL128)  ← direction via GL account type
```

### Natural Balance by Account Category

| Category       | Natural Balance | DEBIT Effect  | CREDIT Effect  |
|----------------|-----------------|---------------|----------------|
| Asset          | DEBIT           | Increase      | Decrease       |
| Liability      | CREDIT          | Decrease      | Increase       |
| Equity         | CREDIT          | Decrease      | Increase       |
| Revenue        | CREDIT          | Decrease      | Increase       |
| Expense        | DEBIT           | Increase      | Decrease       |
