<!-- version: 2.0.0 | domain: insurance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Insurance — Data Boundaries

## PII Categories

| Field               | Classification    | Storage            | Log Rule         |
|---------------------|-------------------|--------------------|------------------|
| Policyholder name   | PII               | Encrypted AES-256  | Suppress         |
| Date of birth       | PII / Sensitive   | Encrypted AES-256  | Suppress         |
| Medical condition   | Special Category  | Encrypted + audited| Never log        |
| National ID / NIN   | Special Category  | Encrypted + audited| Never log        |
| Address             | PII               | Encrypted AES-256  | Suppress         |
| Bank account / IBAN | Financial / PII   | Tokenised          | Authorised only  |
| Premium amount      | Financial PII     | Plain BigDecimal   | Authorised only  |
| Claim amount        | Financial         | Plain BigDecimal   | Authorised only  |
| Beneficiary details | PII               | Encrypted AES-256  | Suppress         |

## Retention Schedule

| Data Category               | Retention Period           | Basis                   |
|-----------------------------|----------------------------|-------------------------|
| Active policy records        | Policy term + 7 years      | FCA ICOBS / Solvency II |
| Paid claims records          | 7 years                    | Limitation Act          |
| Rejected claims              | 5 years                    | Internal policy         |
| Underwriting files           | Policy term + 7 years      | Solvency II Pillar 3    |
| Medical / health data        | Policy term + 10 years     | NHS Code of Practice    |
| AML / sanctions screening    | 5 years from closure       | POCA 2002               |
| Marketing consent records    | Until consent withdrawn    | GDPR Art. 7             |

## Cross-Domain Data Transfer Rules

| Integration                  | Permitted | Condition                                        |
|------------------------------|-----------|--------------------------------------------------|
| Insurance → Risk             | YES       | Aggregated loss statistics only; no PII          |
| Insurance → Accounting       | YES       | Premium & claims amounts; no personal data       |
| Insurance → Capital Markets  | YES       | Investment fund values; no policy-holder data    |
| Insurance → External API     | YES       | Explicit consent + signed DPA + TLS 1.3          |
| Insurance → Reinsurer        | YES       | Per reinsurance treaty data schedule             |
| Insurance → Third-party DB   | FORBIDDEN | Unless explicit regulatory exemption approved    |
| Insurance → Analytics        | YES       | Anonymised / pseudonymised only; k-anonymity ≥ 5|

## Prohibited Data Operations

| Rule ID      | Rule                                                         |
|--------------|--------------------------------------------------------------|
| INS-DB-001   | Never log raw PII in application logs or Spark SQL outputs   |
| INS-DB-002   | Never store medical data outside encrypted Vault secrets     |
| INS-DB-003   | Never export Special Category data without explicit consent  |
| INS-DB-004   | Never batch-copy policyholder records to non-EU zones        |
| INS-DB-005   | Never retain expired policy data beyond retention schedule   |
| INS-DB-006   | Never use live policyholder data in non-production environments|
