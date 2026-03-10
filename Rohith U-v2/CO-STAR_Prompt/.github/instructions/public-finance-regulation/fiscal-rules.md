<!-- version: 2.0.0 | domain: public-finance-regulation | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Public Finance — Fiscal Rules

## Numeric Thresholds

| Rule                                    | Threshold             | Legal Basis           |
|-----------------------------------------|-----------------------|-----------------------|
| Deficit-to-GDP Ceiling                  | 3.0% GDP              | SGP Protocol          |
| Debt-to-GDP Reference Value             | 60.0% GDP             | SGP Protocol          |
| Structural Balance Floor                | −0.5% GDP             | Fiscal Compact Art. 3 |
| Medium-Term Objective (MTO) minimum     | −1.0% GDP             | SGP Preventive Arm    |
| Expenditure Growth Benchmark            | ≤ potential GDP growth| EU Reg 1175/2011      |
| Minimum Correction per Year (EDP)       | ≥ 0.5% GDP structural | SGP Corrective Arm    |
| Debt Reduction Benchmark (if Debt > 60%)| 1/20th excess p.a.    | SGP Art. 2 Reg 1467/97|

## Budget Execution Rules

| Rule ID    | Rule                                                                      |
|------------|---------------------------------------------------------------------------|
| PFR-FR-001 | No expenditure commitment without parliamentary appropriation             |
| PFR-FR-002 | Supplementary budget required if over-run exceeds 5% of original line     |
| PFR-FR-003 | Emergency spending requires Finance Minister sign-off within 48 hours     |
| PFR-FR-004 | Deficit cannot exceed ceiling in any single fiscal year                   |
| PFR-FR-005 | Debt issuance requires pre-authorisation maturity and coupon schedule     |
| PFR-FR-006 | Primary balance must be positive before structural deficit is reduced     |
| PFR-FR-007 | Carry-forward of unspent appropriations limited to 10% of line                |
| PFR-FR-008 | Year-end supplementary estimates must be presented before March 31        |

## Fiscal Code Enforcement

```scala
// Example guard — enforce in domain service
def validateDeficitCeiling(
  projectedRevenue: BigDecimal,
  projectedExpenditure: BigDecimal,
  gdp: BigDecimal
): Either[FiscalPolicyViolation, Unit] = {
  val deficit = projectedExpenditure - projectedRevenue
  val deficitRatio = deficit / gdp * 100
  if (deficitRatio > BigDecimal("3.0"))
    Left(FiscalPolicyViolation(s"Deficit ratio $deficitRatio% exceeds 3.0% GDP ceiling"))
  else
    Right(())
}

def validateDebtCeiling(
  totalDebt: BigDecimal,
  gdp: BigDecimal
): Either[FiscalPolicyViolation, Unit] = {
  val debtRatio = totalDebt / gdp * 100
  if (debtRatio > BigDecimal("60.0"))
    Left(FiscalPolicyViolation(s"Debt ratio $debtRatio% exceeds 60.0% GDP reference value"))
  else
    Right(())
}
```

## Excessive Deficit Procedure (EDP) Triggers

| Stage              | Trigger                                          | Consequence                         |
|--------------------|--------------------------------------------------|-------------------------------------|
| Warning            | Deficit forecast to breach 3% GDP               | Commission early warning issued     |
| EDP Opening        | Actual deficit > 3% GDP                          | Council Decision; correction path   |
| Financial Sanction | Non-compliance with correction recommendation    | 0.2% GDP non-interest-bearing deposit|
| Fine               | Continued non-compliance                         | Up to 0.5% GDP fine                 |
| EDP Abrogation     | Deficit sustainably below 3% GDP                | Excessive deficit procedure closed  |
