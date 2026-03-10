<!-- version: 2.0.0 | domain: risk-compliance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Risk & Compliance — Compliance Rules

## AML Rules (generate as Specification[T] implementations)

| Rule ID      | Rule                                                   | AML Category    |
|--------------|--------------------------------------------------------|-----------------|
| AML-001      | Transaction exactly at EUR 9,999 = structuring flag    | Structuring     |
| AML-002      | 3+ transactions just below EUR 10,000 in 24h = ALERT  | Structuring     |
| AML-003      | Transfer to high-risk jurisdiction (FATF grey/black)  | Geographic      |
| AML-004      | Unusual spike > 3x 90-day average transaction value   | Velocity        |
| AML-005      | Transaction with dormant account (>6 months inactive) | Counterparty    |
| AML-006      | Cash withdrawal pattern: daily max, multiple branches | Behavioural     |
| AML-007      | New account with immediate large transfer out          | Behavioural     |
| AML-008      | Round-number transfers (multiple of EUR 5,000)        | Structuring     |

## Sanctions Screening Rules

| Rule ID      | Rule                                                   | List            |
|--------------|--------------------------------------------------------|-----------------|
| SANC-001     | Screen originator name + IBAN against OFAC SDN         | OFAC            |
| SANC-002     | Screen beneficiary name + IBAN against HMT list        | HMT UK          |
| SANC-003     | Screen against EU Consolidated Sanctions List          | EU              |
| SANC-004     | Screen against UN Security Council list                | UN              |
| SANC-005     | Fuzzy match threshold: ≥ 85% name similarity = HOLD   | All lists       |
| SANC-006     | Country-level block: Iran, DPRK, Myanmar (OFAC/SVR)   | All lists       |

## Fraud Detection Rules

| Rule ID      | Rule                                                   |
|--------------|--------------------------------------------------------|
| FRAUD-001    | Card not present + international + new device = HIGH  |
| FRAUD-002    | Velocity: > 10 card transactions per hour             |
| FRAUD-003    | Transaction from impossible geolocation pair          |
| FRAUD-004    | Amount pattern matching known card testing attack      |
