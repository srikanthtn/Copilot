<!-- version: 2.0.0 | domain: treasury | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Treasury — Cash Management

## Daily Cash Management Process

```
06:00 UTC  → Nostro balance ingestion from correspondents (SWIFT MT940)
07:00 UTC  → Net liquidity position calculated per currency
08:00 UTC  → Cash forecast updated with overnight repo/reverse-repo
09:00 UTC  → FX positions marked to market
10:00 UTC  → LCR / NSFR computed and validated
16:00 UTC  → End-of-day position cut with central bank
18:00 UTC  → RTGS settlement confirmation reconciled
20:00 UTC  → Next-day cash forecast generated and reviewed
```

## Cash Position Rules

| Rule ID     | Rule                                                     | Error                     |
|-------------|----------------------------------------------------------|---------------------------|
| CASH-001    | Nostro balance must reconcile within ±EUR 10 to MT940    | `ReconciliationBreachError` |
| CASH-002    | Intraday liquidity must not drop below stress buffer     | `IntraLiquidityAlert`     |
| CASH-003    | FX spot settlement (T+2) must be prefunded at T          | `PrefundingMissingError`  |
| CASH-004    | Cash forecast net position must be net positive at EOD   | `CashShortfallAlert`      |
| CASH-005    | All BigDecimal amounts use DECIMAL128 + HALF_EVEN        | Code constraint (GEN-001) |

## Nostro Reconciliation

- Automated via MT940 ingest (SWIFT / ISO 20022 CAMT.053)
- Tolerance: ±EUR 10 for same-day breaks
- Breaks > EUR 10,000 trigger automatic escalation
- All nostro entries matched against internal payment ledger
- Unmatched entries → `UnmatchedNostroEntry` domain event
