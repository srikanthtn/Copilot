<!-- version: 2.0.0 | domain: risk-compliance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Risk & Compliance — Domain Overview

| Field         | Value                              |
|---------------|------------------------------------|
| Domain Name   | Risk and Compliance                |
| Package       | `com.bank.risk`                    |
| Bounded Context | AML, Fraud, Sanctions, Credit Risk|

## Core Entities

| Entity               | Type           | Key Fields                                      |
|----------------------|----------------|-------------------------------------------------|
| `RiskProfile`        | Aggregate Root | customerId, riskScore, category, reviewDate     |
| `SanctionsCheck`     | Entity         | checkId, entityName, status, listVersion        |
| `AmlAlert`           | Entity         | alertId, transactionId, ruleId, severity        |
| `CreditExposure`     | Value Object   | limit, utilised, riskWeightedAmount             |
| `RiskCategory`       | Sealed Trait   | LOW, MEDIUM, HIGH, PROHIBITED                   |
| `ComplianceStatus`   | Sealed Trait   | PASSED, FAILED, PENDING_REVIEW, ESCALATED       |

## Domain Events

| Event                     | Trigger                                  |
|---------------------------|------------------------------------------|
| `SanctionsHit`            | Name/entity matched on OFAC/HMT/EU list  |
| `AmlAlertRaised`          | Transaction matched an AML rule          |
| `RiskProfileUpdated`      | Periodic or trigger-based reassessment   |
| `SuspiciousActivityFiled` | SAR submitted to FIU                     |
| `ExposureLimitBreached`   | Credit exposure exceeds approved limit   |
| `PepDesignated`           | Customer identified as Politically Exposed Person |

## AML Rule Categories

| Category           | Examples                                               |
|--------------------|--------------------------------------------------------|
| Structuring        | Multiple just-below-threshold transactions (smurfing)  |
| Velocity           | Unusual transaction frequency for account type         |
| Geographic         | High-risk jurisdiction flows                           |
| Counterparty       | Transactions with flagged or dormant accounts          |
| Behavioural        | Deviation from established transaction pattern         |
