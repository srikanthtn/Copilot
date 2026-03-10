<!-- version: 2.0.0 | domain: fpna | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# FP&A — Data Usage Boundaries

## Data Classification

| Data Item               | Classification | Who May Access                  |
|-------------------------|---------------|---------------------------------|
| Actual P&L figures      | CONFIDENTIAL  | Finance + Board only            |
| Budget by cost centre   | CONFIDENTIAL  | Finance + department heads      |
| Headcount plan          | CONFIDENTIAL  | HR + Finance + C-suite          |
| Capex project pipeline  | RESTRICTED    | Finance + project owners        |
| Forecast vs actual      | CONFIDENTIAL  | Finance + C-suite               |
| Published earnings guide| PUBLIC        | All — post board approval only  |

## Source System Restrictions

- Actuals: sourced from General Ledger only; no shadow spreadsheet books
- Rate inputs: read from Treasury domain via published port interface
- Volume drivers: fetched from business domain APIs — no direct DB reads
- No FP&A model reads production customer PII under any circumstance
- Statistical models run on anonymised, aggregated datasets

## Data Retention

| Record Type             | Retention Period  |
|-------------------------|-------------------|
| Approved budgets        | 7 years           |
| Rolling forecasts       | 5 years           |
| Board papers (financials)| 10 years         |
| Variance analysis       | 5 years           |
