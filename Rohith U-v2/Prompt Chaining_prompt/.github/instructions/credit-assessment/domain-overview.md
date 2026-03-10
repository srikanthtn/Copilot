# Credit Assessment — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Credit Assessment domain governs the evaluation, scoring, and decisioning of
creditworthiness for retail customers, small and medium enterprises (SMEs), and
corporate clients across all lending and credit products.

This domain ensures **risk-based lending decisions**, **IFRS 9 compliant provisioning**,
and **fair credit treatment** aligned with applicable consumer credit regulations.

---

## Characteristics

- Credit decisions must be explainable and non-discriminatory.
- IFRS 9 staging (Stage 1, 2, 3) must be applied for all recognized credit exposures.
- Loan-to-Value (LTV) and Debt Service Coverage Ratio (DSCR) limits must be enforced.
- Credit scoring models must be periodically validated and version-tracked.
- All credit approval or rejection decisions must include a documented reasoning reference.

---

## Key Credit Metrics

| Metric              | Description                                              | Regulatory Basis  |
|---------------------|----------------------------------------------------------|-------------------|
| Credit Score        | Composite creditworthiness indicator (0–850 or 300–900) | Internal model    |
| Probability of Default (PD) | Likelihood of default within 12 months          | IFRS 9 / Basel III|
| Loss Given Default (LGD)  | Estimated loss if default occurs                  | IFRS 9 / Basel III|
| Exposure at Default (EAD) | Outstanding balance at point of default           | IFRS 9 / Basel III|
| Debt Service Ratio (DSR)  | Debt obligations / Net monthly income             | Central Bank guidelines|
| Loan-to-Value (LTV) | Loan amount / Collateral market value                    | Central Bank guidelines|

---

## IFRS 9 Stage Classification

| Stage | Criteria                                     | Provisioning Basis     |
|-------|----------------------------------------------|------------------------|
| 1     | No significant increase in credit risk (SICR)| 12-month ECL           |
| 2     | SICR detected                                | Lifetime ECL           |
| 3     | Objective evidence of credit impairment      | Lifetime ECL (actuals) |

---

## Chain Reasoning Responsibilities

### Stage 3 Triggers
- Resolution of entities to `CreditScore`, `LoanApplication`, `DebtServiceRatio`,
  `LtvRatio`, `ExpectedCreditLoss`
- Application of credit scoring business rules and LTV/DSR limits
- IFRS 9 stage assignment based on customer credit history

### Stage 4 Compliance Triggers
| Check ID  | Description                                     |
|-----------|-------------------------------------------------|
| COMP-002  | KYC/CDD verified for loan applicant             |
| COMP-007  | Applicant consent for credit bureau inquiry     |
| COMP-008  | Audit trail for credit decisioning              |

Applicable regulation: **Basel III**, **IFRS 9**, **Consumer Credit Regulations**

### Stage 5 Risk Triggers
- Credit risk assessment using PD × LGD × EAD framework
- Concentration risk (portfolio-level lending limits)
- Collateral adequacy assessment (LTV coverage)

---

## Credit Decisioning Thresholds

| Score Band       | Risk Category | Decision Guidance             |
|------------------|---------------|-------------------------------|
| > 750            | Prime         | Approve — standard terms      |
| 650 – 750        | Near-Prime    | Approve — enhanced monitoring |
| 550 – 649        | Sub-Prime     | Approve with conditions / higher pricing|
| < 550            | High-Risk     | Decline or require collateral |

---

## Prohibited Operations

- Approving a loan without completing credit score and affordability assessment.
- Using demographic factors (ethnicity, gender, religion) in credit decisioning.
- Backdating credit agreements.
- Issuing credit without KYC Tier 2 minimum verification.
- Suppressing credit bureau inquiry from the customer's knowledge (GDPR/CCPA breach).
