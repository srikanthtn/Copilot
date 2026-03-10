<!-- version: 2.0.0 | domain: core-banking | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Core Banking — Business Rules

## Account Opening Rules

| Rule ID     | Rule                                                   | Error                       |
|-------------|--------------------------------------------------------|-----------------------------|
| CB-RULE-001 | Customer must pass KYC before account opening          | `KycNotPassedError`         |
| CB-RULE-002 | Initial deposit ≥ product minimum (varies by type)     | `InsufficientOpeningDepositError` |
| CB-RULE-003 | Customer cannot hold > 5 active current accounts       | `MaxAccountLimitReached`    |
| CB-RULE-004 | IBAN assigned from bank's certified IBAN range only    | `InvalidIbanAllocationError`|

## Debit / Credit Posting Rules

| Rule ID     | Rule                                                   | Error                       |
|-------------|--------------------------------------------------------|-----------------------------|
| CB-RULE-010 | Debit must not exceed availableBalance + overdraftLimit| `InsufficientFundsError`    |
| CB-RULE-011 | LedgerEntry direction must be DEBIT or CREDIT — no negatives | `InvalidDirectionError` |
| CB-RULE-012 | Both debit and credit legs must be posted atomically   | `AtomicPostingFailure`      |
| CB-RULE-013 | Currency of entry must match account currency          | `CurrencyMismatchError`     |
| CB-RULE-014 | Value date must not be more than D-1 business day      | `InvalidValueDateError`     |

## Interest Rules

| Rule ID     | Rule                                                   |
|-------------|--------------------------------------------------------|
| CB-RULE-020 | Interest accrued daily; compounded monthly             |
| CB-RULE-021 | Negative interest applied on excess reserves (ECB policy) |
| CB-RULE-022 | Interest calculation: ACT/360 day count convention     |
| CB-RULE-023 | Interest posting triggers `InterestAccrued` domain event |

## Account Status Transition Rules

```
ACTIVE → DORMANT (no activity 12+ months, regulatory requirement)
ACTIVE → BLOCKED  (fraud/sanction/court order — must log reason)
BLOCKED → ACTIVE  (authorised unblock with 4-eyes approval)
ACTIVE → CLOSED   (zero balance; no pending transactions; customer request)
DORMANT → CLOSED  (after dormancy reclaim process)
CLOSED  → any     (FORBIDDEN — closed is terminal)
```
