<!-- version: 2.0.0 | domain: insurance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Insurance — Domain Overview

| Field         | Value                      |
|---------------|----------------------------|
| Domain Name   | Insurance                  |
| Package       | `com.bank.insurance`       |
| Bounded Context | Policy and Claims Management |

## Core Entities

| Entity            | Type           | Key Fields                                      |
|-------------------|----------------|-------------------------------------------------|
| `Policy`          | Aggregate Root | policyId, holder, product, premium, status      |
| `Claim`           | Aggregate Root | claimId, policyId, incidentDate, amount, status |
| `PolicyHolder`    | Value Object   | customerId, name (encrypted), address           |
| `Premium`         | Value Object   | amount (BigDecimal/DECIMAL128), frequency       |
| `Coverage`        | Value Object   | type, limit, deductible, exclusions             |
| `PolicyStatus`    | Sealed Trait   | QUOTED, ACTIVE, LAPSED, CANCELLED, EXPIRED      |
| `ClaimStatus`     | Sealed Trait   | NOTIFIED, ASSESSED, APPROVED, REJECTED, PAID    |

## Domain Events

| Event                    | Trigger                                    |
|--------------------------|--------------------------------------------|
| `PolicyIssued`           | New policy bound and activated             |
| `PremiumReceived`        | Premium payment recorded                   |
| `PolicyLapsed`           | Missed premium payment — grace period over |
| `ClaimNotified`          | Policyholder notifies claim event          |
| `ClaimAssessed`          | Claims handler completes assessment        |
| `ClaimApproved`          | Assessment concludes valid claim           |
| `ClaimRejected`          | Claim declined with documented reason      |
| `ClaimPaid`              | Settlement payment made to policyholder    |
| `ReinsuranceCeded`       | Risk shared with reinsurance counterparty  |

## Key Calculations

| Metric              | Formula                                        |
|---------------------|------------------------------------------------|
| Net Premium         | Gross Premium - Reinsurance Premium            |
| Loss Ratio          | Claims Incurred / Net Premium Earned × 100     |
| Combined Ratio      | Loss Ratio + Expense Ratio                     |
| IBNR Reserve        | Incurred But Not Reported reserve (actuarial)  |
| Solvency II SCR     | Solvency Capital Requirement per SF or IM      |
