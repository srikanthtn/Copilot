<!-- version: 2.0.0 | domain: core-banking | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Core Banking — Domain Overview

| Field         | Value                               |
|---------------|-------------------------------------|
| Domain Name   | Core Banking                        |
| Package       | `com.bank.corebanking`              |
| Bounded Context | Account Management and Ledger    |

## Core Entities

| Entity              | Type           | Key Fields                                   |
|---------------------|----------------|----------------------------------------------|
| `Account`           | Aggregate Root | id, iban, balance, currency, status, type    |
| `LedgerEntry`       | Entity         | id, accountId, amount, direction, timestamp  |
| `AccountHolder`     | Value Object   | customerId, name (encrypted), dateOfBirth    |
| `Balance`           | Value Object   | availableBalance, ledgerBalance (BigDecimal) |
| `AccountStatus`     | Sealed trait   | ACTIVE, DORMANT, CLOSED, BLOCKED             |
| `AccountType`       | Sealed trait   | CURRENT, SAVINGS, OMNIBUS, NOSTRO, VOSTRO    |

## Domain Events

| Event                    | Trigger                            |
|--------------------------|------------------------------------|
| `AccountOpened`          | New account successfully created   |
| `AccountCredited`        | Credit entry posted to ledger      |
| `AccountDebited`         | Debit entry posted to ledger       |
| `AccountBlocked`         | Freeze order applied               |
| `AccountClosed`          | Account closed with zero balance   |
| `OverdraftLimitChanged`  | Approved limit modification        |
| `InterestAccrued`        | Daily interest calculation posted  |

## Invariants (never violate)

1. Balance = sum of all posted LedgerEntries (immutable history)
2. Debit cannot exceed availableBalance + approvedOverdraft
3. Closed account accepts no further entries
4. Blocked account: debit forbidden; credit permitted for regulatory receipts
5. LedgerEntry once posted is immutable (no update/delete)

## Ubiquitous Language

| Term            | Definition                                          |
|-----------------|-----------------------------------------------------|
| Ledger          | Complete immutable ordered history of account entries |
| Posting         | Recording an entry against an account              |
| Available balance | Balance minus holds, plus authorised overdraft   |
| Ledger balance  | Balance of all settled entries only                |
| Nostro          | Bank's account at correspondent — "our account"    |
| Omnibus         | Pooled account holding funds for multiple customers|
