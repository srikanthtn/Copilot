<!-- version: 2.0.0 | domain: fpna | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Financial Planning & Analysis — Domain Overview

| Field         | Value                               |
|---------------|-------------------------------------|
| Domain Name   | Financial Planning and Analysis     |
| Package       | `com.bank.fpna`                     |
| Bounded Context | Budgeting, Forecasting, MIS      |

## Core Entities

| Entity              | Type           | Key Fields                                      |
|---------------------|----------------|-------------------------------------------------|
| `Budget`            | Aggregate Root | budgetId, period, version, status, lines        |
| `BudgetLine`        | Entity         | costCentre, glAccount, amount, currency         |
| `Forecast`          | Aggregate Root | forecastId, horizon, scenario, lines            |
| `Variance`          | Value Object   | actual, budget, variance, variancePct           |
| `CostCentre`        | Value Object   | code, name, owner, businessLine                 |

## Domain Events

| Event                   | Trigger                                  |
|-------------------------|------------------------------------------|
| `BudgetSubmitted`       | Department submits annual budget         |
| `BudgetApproved`        | Finance committee approves               |
| `ForecastGenerated`     | Monthly rolling forecast produced        |
| `VarianceAlertRaised`   | Actual vs budget variance exceeds threshold |
| `ReplanInitiated`       | Mid-year replan triggered                |

## Key Forecasting Horizons

| Horizon        | Frequency    | Method                      |
|----------------|-------------|-----------------------------|
| Annual budget  | Yearly      | Zero-based + driver-based   |
| Rolling 12M    | Monthly     | Statistical + management     |
| 3-year plan    | Annual      | Strategic plan drivers       |
| Intra-month    | Weekly      | Trend extrapolation          |


# FP&A — Budgeting Guidelines

## Budget Process Rules

| Rule ID       | Rule                                                   |
|---------------|--------------------------------------------------------|
| FPNA-BUD-001  | Budget amounts in BigDecimal DECIMAL128 — never Double |
| FPNA-BUD-002  | All budget lines attributed to a GL account + cost centre |
| FPNA-BUD-003  | Personnel costs: phased monthly by headcount plan     |
| FPNA-BUD-004  | Capex: separated from Opex in budget taxonomy         |
| FPNA-BUD-005  | Version management: only one APPROVED budget per period |
| FPNA-BUD-006  | Variance threshold alert: >15% or >EUR 500K absolute  |

## Budget Approval Workflow

```
Draft → Department Review → Finance Review → Board Approval → LOCKED
                                                     ↓
                                           Only CFO can unlock for replan
```

## Zero-Based Budgeting Requirements

- Every cost line requires a documented business justification
- No automatic roll-forward of prior year actuals
- Headcount plan drives personnel cost lines


# FP&A — Forecasting Rules

## Rolling Forecast Requirements

| Rule ID       | Rule                                                  |
|---------------|-------------------------------------------------------|
| FPNA-FC-001   | 12-month rolling horizon updated monthly              |
| FPNA-FC-002   | Base case + upside + downside scenarios required      |
| FPNA-FC-003   | NII forecast driven by rate scenarios from Treasury   |
| FPNA-FC-004   | ECL forecast aligned to Risk's IFRS 9 model output   |
| FPNA-FC-005   | Forecast amounts: BigDecimal(DECIMAL128), HALF_EVEN   |

## Statistical Methods (implemented in ForecastingEngine)

| Method              | Use Case                              |
|---------------------|---------------------------------------|
| Linear regression   | Revenue trend with macro drivers      |
| Moving average (3M) | OpEx smoothing                        |
| Exponential smoothing| Cost items with seasonal pattern     |
| Driver-based model  | Headcount / volume × unit cost        |

## Prohibited Forecast Patterns

- Using Double or Float for any financial forecast figure
- Forward projections without documented assumptions
- Sharing detailed forecasts externally before board approval
- Running forecasts on real customer data (use aggregated only)


# FP&A — Data Usage Boundaries

## Data Classification

| Data Item             | Classification | Permitted Use                        |
|-----------------------|---------------|--------------------------------------|
| Actual P&L figures    | CONFIDENTIAL  | Finance team + Board only            |
| Budget by cost centre | CONFIDENTIAL  | Finance + department heads           |
| Headcount plan        | CONFIDENTIAL  | HR + Finance + Management            |
| Capex project list    | RESTRICTED    | Finance + project owners             |
| Forecast vs actual    | CONFIDENTIAL  | Finance + C-suite only               |
| Published guidance    | PUBLIC        | After board approval                 |

## Data Source Rules

- All actuals sourced from General Ledger only (no shadow books)
- Rate inputs sourced exclusively from Treasury domain
- Volume drivers sourced from business domains via approved APIs
- No FP&A model reads production customer data directly
- Anonymised, aggregated data only for all planning models
