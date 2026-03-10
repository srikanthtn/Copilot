<!-- version: 2.0.0 | domain: insurance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Insurance — Policy Lifecycle

## State Transitions

```
QUOTED    → [bindPolicy()  ] → ACTIVE       — premium received; policy in force
ACTIVE    → [lapsePolicy() ] → LAPSED       — premium missed + grace expired
ACTIVE    → [cancelPolicy()] → CANCELLED    — cancelled by customer or insurer
ACTIVE    → [expirePolicy()] → EXPIRED      — end of policy term
LAPSED    → [reinstate()   ] → ACTIVE       — arrears cleared + re-underwriting
EXPIRED   → [renew()       ] → ACTIVE       — new policy ID; fresh underwriting
CANCELLED → terminal                        — no further transitions permitted
```

## Lifecycle Rules

| Rule ID      | Rule                                                     |
|--------------|----------------------------------------------------------|
| INS-LC-001   | Policy becomes ACTIVE only after first premium confirmed |
| INS-LC-002   | Lapse grace period: 30 days (life) / 15 days (non-life) |
| INS-LC-003   | Cancelled policy: return premium pro-rata from cancel date|
| INS-LC-004   | Expired policy: no claims accepted post-expiry date      |
| INS-LC-005   | Reinstatement: health declaration + arrears full payment |
| INS-LC-006   | Every state transition emits a domain event              |


# Insurance — Data Boundaries

## PII Categories

| Field               | Classification    | Storage           | Log Rule        |
|---------------------|------------------|-------------------|-----------------|
| Policyholder name   | PII              | Encrypted         | Suppress        |
| Date of birth       | PII / Sensitive  | Encrypted         | Suppress        |
| Medical condition   | Special Category | Encrypted + logged| Never log        |
| National ID         | Special Category | Encrypted + logged| Never log        |
| Address             | PII              | Encrypted         | Suppress        |
| Premium amount      | Financial / PII  | Plain BigDecimal  | Authorised only |
| Claim amount        | Financial        | Plain BigDecimal  | Authorised only |

## Retention Schedule

| Data Category          | Retention Period     |
|------------------------|----------------------|
| Active policy records  | Policy term + 7 years|
| Paid claims records    | 7 years              |
| Rejected claims        | 5 years              |
| Underwriting files     | Policy term + 7 years|
| Medical data (health)  | Policy term + 10 years|

## Cross-Domain Rules

| Integration            | Permitted | Condition                            |
|------------------------|-----------|--------------------------------------|
| Insurance → Risk       | YES       | Aggregated loss data, no PII          |
| Insurance → Accounting | YES       | Premium and claims figures, no PII   |
| Insurance → External   | YES       | Explicit consent + DPA required      |
| Insurance → Reinsurer  | YES       | Per reinsurance treaty data schedule |


# Insurance — Regulatory Constraints

| Regulation                    | Key Requirement                                    |
|-------------------------------|----------------------------------------------------|
| Solvency II (EU 2009/138)     | SCR / MCR capital; ORSA; Pillar 3 reporting        |
| IDD (EU 2016/97)              | Insurance Distribution Directive; conduct standards|
| PRIIPs (EU 1286/2014)         | Key Information Document for packaged products     |
| GDPR (EU 2016/679)            | Special category data handling for health policies |
| FCA COBS (UK)                 | Post-Brexit conduct of business standards          |
| Consumer Duty (FCA 2023)      | Good outcomes; value for money; fair treatment     |
| Claims Management (FCA PS20/4)| Claims handling standards; 8-week response rule    |
| Distance Marketing Directive  | 14-day cancellation right for distance sales       |
