<!-- version: 2.0.0 | domain: risk-compliance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Risk & Compliance — Risk Frameworks

## Credit Risk — Basel III IRB Approach

| Parameter          | Description                              | Implementation               |
|--------------------|------------------------------------------|------------------------------|
| PD (Probability of Default) | 12-month likelihood of default | RiskScoreEngine.pd()        |
| LGD (Loss Given Default) | Expected loss rate on default    | RiskScoreEngine.lgd()       |
| EAD (Exposure At Default) | Exposure at time of default     | ExposureCalculator.ead()    |
| RWA                | Risk-Weighted Asset = EAD × RW factor    | RwaCalculator.compute()     |
| Capital Adequacy   | CET1 ≥ 4.5%, Tier1 ≥ 6%, Total ≥ 8%    | CapitalRatioMonitor         |

## Market Risk — VaR Framework

| Metric            | Method                        | Confidence | Horizon  |
|-------------------|-------------------------------|-----------|----------|
| VaR               | Historical simulation (1Y)    | 99%       | 1 day    |
| Stressed VaR      | 12-month stressed period      | 99%       | 1 day    |
| Expected Shortfall| Average beyond VaR tail       | 97.5%     | 10 days  |

## Operational Risk

| Category           | Capture Method                          |
|--------------------|------------------------------------------|
| Internal fraud     | Loss event database; automated alerts   |
| External fraud     | Real-time transaction scoring           |
| Process failures   | STP exception rate monitoring           |
| IT failures        | Availability SLA breach detection       |


# Risk & Compliance — Audit Requirements

## Internal Audit Evidence

Every compliance evaluation must produce an immutable audit record:

```scala
case class ComplianceAuditRecord(
  evaluationId : java.util.UUID,
  timestamp    : java.time.Instant,      // UTC
  ruleId       : String,                 // e.g. "AML-001"
  entityRef    : String,                 // masked reference
  result       : ComplianceStatus,
  evaluatedBy  : String,                 // system or analyst
  overrideBy   : Option[String],         // 4-eyes override if applicable
  rationale    : String
)
```

## Retention and Access

| Record Type              | Retention | Access           |
|--------------------------|-----------|------------------|
| Sanctions check records  | 5 years   | Compliance only  |
| AML alert evaluations    | 5 years   | Compliance + FIU |
| SAR filings              | 5 years   | Compliance + NCA |
| Risk profile history     | 7 years   | Risk + Audit     |
| Credit decision records  | 7 years   | Credit + Audit   |


# Risk & Compliance — Prohibited Patterns

## Never Generate These Patterns

| Prohibited Pattern                                  | Reason                    |
|-----------------------------------------------------|---------------------------|
| Process transfer without OFAC/HMT/EU sanctions check| Legal obligation          |
| Allow override of sanctions HOLD without audit trail| Compliance gate           |
| Ignore AML alert after 48h without acknowledgement  | FIU reporting obligation  |
| Auto-clear sanctions hit without human review       | Regulatory requirement    |
| Base risk score on protected demographic attributes | Fair lending / ECHR       |
| Suppress or delete AML alert records               | Evidence destruction      |
| Allow PEP customer without Enhanced Due Diligence   | 5AMLD mandatory           |
| Share SAR details with customer being investigated  | "Tipping off" offence     |
