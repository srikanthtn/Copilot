<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Data Boundaries

## PII Classification

| Field                  | Classification | Storage Rule              | Log Rule         |
|------------------------|---------------|---------------------------|------------------|
| Debtor IBAN            | PII / PCI     | Encrypted (AES-256-GCM)   | Mask last 4 only |
| Debtor Name            | PII           | Encrypted                 | Suppress         |
| Creditor IBAN          | PII           | Encrypted                 | Mask last 4 only |
| Creditor Name          | PII           | Encrypted                 | Suppress         |
| Payment Amount         | Confidential  | Plain (BigDecimal)        | Permitted        |
| Payment Reference      | Internal      | Plain (UUID)              | Permitted        |
| Remittance Information | Potential PII | Encrypted                 | Suppress         |
| IP Address of initiator| PII (GDPR)    | Encrypted + retention 90d | Suppress         |

## Data Retention Schedule

| Data Category                  | Retention Period | Action at Expiry     |
|-------------------------------|-----------------|----------------------|
| Completed payment records      | 5 years          | Anonymise PII        |
| Failed / rejected payments     | 2 years          | Delete               |
| Suspicious Activity Reports    | 5 years          | Archive (compliance) |
| Audit log entries              | 7 years (MiFID II)| Archive (read-only) |
| SCA audit evidence             | 18 months (PSD2) | Delete               |

## Cross-Domain Data Rules

| Scenario                    | Allowed | Condition                             |
|-----------------------------|---------|---------------------------------------|
| Payments → Risk scoring     | YES     | Anonymised payment patterns only      |
| Payments → Core Banking     | YES     | Internal via ACL; balance check only  |
| Payments → Capital Markets  | NO      | No cross-domain payment data sharing  |
| Payments → External partner | YES     | Explicit consent; contractual DPA     |

## Data Minimisation (GDPR Art. 25)

- Domain model must contain ONLY fields required for payment processing
- Beneficiary address: collect only what scheme mandates
- Do NOT store marketing preferences in payment domain
- Analytical queries must run on anonymised/aggregated datasets only
