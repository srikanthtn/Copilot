<!-- version: 2.0.0 | domain: fpna | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# FP&A — Budgeting Guidelines

## Budget Process Rules

| Rule ID       | Rule                                                        |
|---------------|-------------------------------------------------------------|
| FPNA-BUD-001  | All amounts: BigDecimal(DECIMAL128) + HALF_EVEN — no Double |
| FPNA-BUD-002  | Every budget line attributed to GL account + cost centre    |
| FPNA-BUD-003  | Personnel costs phased monthly by headcount plan            |
| FPNA-BUD-004  | Capex separated from Opex per IFRS 16 / IAS 38              |
| FPNA-BUD-005  | Only one APPROVED budget version per period; others DRAFT   |
| FPNA-BUD-006  | Variance threshold alert: >15% or >EUR 500K absolute        |

## Budget Approval Workflow

```
DRAFT → Department submission
     → Finance team review
     → Finance Director sign-off
     → Board / ALCO approval
     → LOCKED (CFO only can initiate replan)
```

## Zero-Based Budgeting Rules

- Every cost line requires documented business justification
- No automatic roll-forward of prior year figures
- New headcount: requires HR Business Partner + Finance sign-off
- Technology investments: require architecture review


# FP&A — Forecasting Rules

## Rolling Forecast Standards

| Rule ID       | Rule                                                   |
|---------------|--------------------------------------------------------|
| FPNA-FC-001   | 12-month rolling horizon; updated first week of month  |
| FPNA-FC-002   | Base + upside + downside scenarios mandatory           |
| FPNA-FC-003   | NII forecast aligned to Treasury rate scenario output  |
| FPNA-FC-004   | ECL provision forecast aligned to Risk IFRS 9 model   |
| FPNA-FC-005   | All amounts: BigDecimal(DECIMAL128), HALF_EVEN         |

## Statistical Engine Requirements

| Method               | Application                                |
|----------------------|--------------------------------------------|
| Linear regression    | Revenue trend with macro variable drivers  |
| 3-month moving avg   | Operating expense baseline smoothing       |
| Exponential smoothing| Seasonal cost patterns                    |
| Driver-based model   | Volume × unit cost; headcount × rate       |


# FP&A — Data Usage Boundaries

| Data Item             | Classification | Who May Access               |
|-----------------------|---------------|-------------------------------|
| Actual P&L figures    | CONFIDENTIAL  | Finance + Board only          |
| Budget by cost centre | CONFIDENTIAL  | Finance + department heads    |
| Headcount plan        | CONFIDENTIAL  | HR + Finance + C-suite        |
| Capex project list    | RESTRICTED    | Finance + project owners      |
| Published guidance    | PUBLIC        | All — post board approval     |

## Data Source Restrictions

- Actuals: General Ledger only (no shadow spreadsheet books)
- Rate inputs: Treasury domain via published port interface
- Volume drivers: business domain APIs (not direct DB reads)
- No FP&A model accesses production customer PII
- All planning models use aggregated, anonymised datasets only
