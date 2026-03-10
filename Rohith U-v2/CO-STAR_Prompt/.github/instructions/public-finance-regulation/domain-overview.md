<!-- version: 2.0.0 | domain: public-finance-regulation | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Public Finance & Regulation — Domain Overview

## Purpose

Public finance and regulation covers sovereign debt management, municipal finance, fiscal policy enforcement, public-sector accounting, and government grant management. Code generated in this domain must comply with constitutional fiscal laws, EU treaty obligations, and international public-sector accounting standards.

## Core Aggregates

| Aggregate              | Responsibility                                    |
|------------------------|---------------------------------------------------|
| `SovereignDebt`        | Issuance, redemption, coupon tracking of government bonds |
| `MunicipalBond`        | Local authority financing; yield, term, repayment schedule |
| `FiscalBudget`         | Annual government budget: revenue, expenditure, deficit   |
| `PublicGrant`          | Grant allocation, disbursement, eligibility, audit trail  |
| `TaxRevenue`           | Tax receipt aggregation; VAT, income, corporate tax flows  |
| `PublicPension`        | Defined-benefit public sector pensions; actuarial liabilities|
| `CentralBankAccount`   | Government's account with central bank; reserve management |

## Domain Events

| Event                         | Trigger                                      |
|-------------------------------|----------------------------------------------|
| `BondIssued`                  | New sovereign / municipal bond placed        |
| `BondRedeemed`                | Maturity reached; principal returned         |
| `CouponPaymentMade`           | Scheduled interest payment processed         |
| `FiscalBudgetApproved`        | Legislature approves annual budget           |
| `FiscalBudgetRevised`         | Mid-year supplementary budget enacted        |
| `DeficitThresholdBreached`    | Budget deficit exceeds statutory limit       |
| `GrantDisbursed`              | Grant funds transferred to recipient         |
| `GrantAuditFailed`            | Grant auditor rejects expenditure evidence   |
| `TaxRevenueRecorded`          | Tax receipt collected and classified         |
| `DebtCeilingApproaching`      | Outstanding debt > 95% of statutory ceiling  |

## Key Metrics & Formulas

| Metric                    | Formula                                                 |
|---------------------------|---------------------------------------------------------|
| Deficit-to-GDP Ratio      | (Expenditure − Revenue) / GDP × 100                    |
| Debt-to-GDP Ratio         | Total Outstanding Debt / GDP × 100                     |
| Primary Balance           | Revenue − Non-Interest Expenditure                      |
| Structural Deficit        | Cyclically-adjusted deficit (OECD methodology)          |
| Debt Service Ratio        | (Principal + Interest Payments) / Total Revenue         |
| Grant Utilisation Rate    | Disbursed / Allocated × 100                             |
| Tax Buoyancy              | % Change in Tax Revenue / % Change in GDP               |

## Technology Constraints

| Parameter    | Value                                     |
|--------------|-------------------------------------------|
| Language     | Scala 2.13.14                             |
| Framework    | Apache Spark 3.5.1, Delta Lake 3.1.0      |
| Build Tool   | sbt 1.9.x                                 |
| Java Runtime | Java 17 LTS (Temurin)                     |
| Arithmetic   | `scala.math.BigDecimal` — all monetary values |
| Testing      | ScalaTest 3.2.18 + ScalaCheck             |
| FP Libraries | Cats 2.10.0, Cats-Effect 3.5.4            |
| Observability| Prometheus 0.16.0                         |
