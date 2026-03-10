<!-- version: 2.0.0 | domain: fpna | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# FP&A — Forecasting Rules

## Rolling Forecast Requirements

| Rule ID      | Rule                                                    |
|--------------|---------------------------------------------------------|
| FPNA-FC-001  | 12-month rolling horizon; refreshed first week of month |
| FPNA-FC-002  | Base case + upside + downside scenarios required        |
| FPNA-FC-003  | NII forecast uses Treasury-provided interest rate paths |
| FPNA-FC-004  | ECL provision forecast aligned with Risk IFRS 9 model  |
| FPNA-FC-005  | All amounts: BigDecimal(DECIMAL128) + HALF_EVEN         |

## Statistical Methods (ForecastingEngine)

| Method               | Use Case                               |
|----------------------|----------------------------------------|
| Linear regression    | Revenue trend with macro variable inputs |
| 3-month moving avg   | OpEx smoothing for stable cost lines   |
| Exponential smoothing| Seasonal patterns on variable costs   |
| Driver-based         | Volume × unit cost; headcount × rate  |

## Quality Controls

- Every forecast output reviewed by Finance Business Partner
- Forecast accuracy tracked: actual vs forecast delta recorded monthly
- Outliers (>20% variance) auto-trigger assumption review
- All forecast records retained 5 years for management audit trail
