<!-- version: 2.0.0 | domain: core-banking | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Core Banking — Data Boundaries

## PII Classification

| Field               | Classification    | Storage Rule              | Log Rule         |
|---------------------|------------------|---------------------------|------------------|
| Account IBAN        | PII / Financial  | Plain (scheme identifier) | Mask last 4 only |
| Account Holder Name | PII              | Encrypted (AES-256-GCM)  | Suppress         |
| Date of Birth       | PII / Sensitive  | Encrypted                 | Suppress         |
| National ID / NIN   | Special Category | Encrypted + access-logged | Suppress         |
| Account Balance     | Confidential     | Plain BigDecimal          | Authorised only  |
| Overdraft Limit     | Confidential     | Plain BigDecimal          | Authorised only  |
| Address             | PII              | Encrypted                 | Suppress         |
| Phone / Email       | PII              | Encrypted                 | Suppress         |

## Data Retention

| Category                  | Retention    | Action at Expiry         |
|---------------------------|-------------|--------------------------|
| Active account records    | Duration + 7 years | Archive              |
| Closed account ledger     | 7 years post-close | Archive read-only    |
| KYC documents             | 5 years post-close | Secure delete        |
| Overdraft approvals       | 7 years     | Archive                  |
| Interest calculations     | 7 years     | Archive                  |

## Cross-Domain Rules

| Integration Point           | Allowed | Condition                       |
|-----------------------------|---------|----------------------------------|
| Core Banking → Payments     | YES     | Balance check via port interface |
| Core Banking → Risk         | YES     | Aggregated patterns only         |
| Core Banking → Treasury     | YES     | Nostro/Vostro position data only |
| Core Banking → FP&A         | YES     | Anonymised aggregates only       |
| Core Banking → External     | NO      | No direct external data sharing  |


# Core Banking — Regulatory Constraints

## Key Regulations

| Regulation              | Requirement                                          |
|-------------------------|------------------------------------------------------|
| CRD IV / CRR (EU 575/2013) | Capital adequacy reflected in risk-weighted assets |
| IFRS 9                  | Impairment provisions; expected credit loss model    |
| Basel III Liquidity     | LCR and NSFR driven by account balance data          |
| 5AMLD/6AMLD             | KYC before account opening; screening ongoing        |
| Deposit Guarantee (DGSD)| Coverage up to EUR 100k; data format for FOLTF       |
| FATCA / CRS             | Tax residency classification on account holder       |
| Consumer Credit Directive| APR disclosure on all overdraft products            |
| GDPR Art. 6             | Contract as lawful basis for account processing      |
