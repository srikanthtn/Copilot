<!-- version: 2.0.0 | domain: accounting-audit | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Accounting & Audit — Audit Controls

## Segregation of Duties Matrix

| Action                       | Role Required            | Second Approver Needed |
|------------------------------|--------------------------|------------------------|
| Post manual journal entry    | Accountant               | Finance Manager        |
| Month-end close              | Finance Manager          | CFO / Controller       |
| Post impairment provision    | Risk Accounting          | Chief Risk Officer     |
| Restatement of prior period  | Senior Accountant        | External Audit sign-off|
| Access to full GL report     | Authorised Finance only  | N/A (read-only)        |

## Automated Controls (generated in code)

| Control ID | Control                                         | Implementation              |
|------------|-------------------------------------------------|-----------------------------|
| ACCT-CTL-001 | Debit total must equal credit total per entry | JournalEntry.validate()    |
| ACCT-CTL-002 | Posting user ≠ approval user                  | FourEyesValidator.check()  |
| ACCT-CTL-003 | No posting to closed period                   | PeriodStatusChecker.check()|
| ACCT-CTL-004 | GL vs sub-ledger reconciliation daily          | ReconciliationJob.run()    |
| ACCT-CTL-005 | All journals timestamped in UTC (Instant)      | Code standard              |
| ACCT-CTL-006 | Audit record for every state change            | AuditLogger.record()       |


# Accounting & Audit — Regulatory Constraints

| Regulation              | Key Requirement                                        |
|-------------------------|--------------------------------------------------------|
| IFRS (IAS/IFRS Suite)   | Basis of preparation for all financial statements      |
| CRD IV / FINREP (EBA)   | Quarterly prudential financial statements in XBRL      |
| Sarbanes-Oxley (SOX)    | Internal controls over financial reporting (if US-listed) |
| Companies Act 2006      | Statutory accounts; directors' duties                  |
| FRS 101 / 102           | UK-reduced IFRS for subsidiary entities                |
| Statutory Audit Directive (EU 2006/43) | External audit independence rules        |
| GDPR Art. 58            | Data subject rights in audit-held personal data        |
| MiFID II record-keeping | 7-year retention for trade-related accounting records  |


# Accounting & Audit — Reporting Boundaries

## Financial Statement Scope

| Statement             | Scope                              | Regulatory Audience         |
|-----------------------|------------------------------------|-----------------------------|
| Consolidated P&L      | Group entities — IFRS              | Shareholders, Regulators    |
| Consolidated Balance Sheet | Group entities — IFRS         | Shareholders, Regulators    |
| Segment Reporting      | Material business segments        | Shareholders (IFRS 8)       |
| FINREP Quarterly      | Prudential consolidated group     | ECB / National CA            |
| Solo for each entity  | Entity-level statutory accounts   | Companies House / NCA        |

## Data Boundaries

- Accounting data must NOT be shared outside finance/audit without approval
- Trial balance figures: CONFIDENTIAL; not to be shared with clients
- Audit exceptions and findings: CONFIDENTIAL; audit team only
- Financial statements once published: PUBLIC (annual report)
- Provisioning model parameters: RESTRICTED; credit risk team + audit

## PII in Accounting Records

- Customer names in journal narratives: avoid where possible
- If present: encrypt; access limited to finance and audit
- GDPR right of erasure: cannot apply to records required for legal retention
