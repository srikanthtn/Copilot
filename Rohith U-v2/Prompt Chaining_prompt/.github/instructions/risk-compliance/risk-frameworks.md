# Risk & Compliance — Risk Frameworks
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Framework Principles

- Risk evaluation MUST be systematic, reproducible, and auditable.
- Risk models MUST be transparent and explainable — opaque scoring is forbidden.
- Risk frameworks MUST be consistently applied across all domains and all transactions.
- Risk scores MUST be traceable to the specific inputs that generated them.

---

## Credit Risk Framework (IFRS 9 Aligned)

| Parameter | Description                                               |
|-----------|-----------------------------------------------------------|
| PD        | Probability of Default — borrower-level estimate          |
| LGD       | Loss Given Default — collateral and recovery estimate     |
| EAD       | Exposure at Default — outstanding balance at default point|
| ECL       | Expected Credit Loss = PD × LGD × EAD                    |

**Stage Classification:**
| Stage | Description                              | ECL Provisioning          |
|-------|------------------------------------------|---------------------------|
| 1     | Performing — no significant credit risk  | 12-month ECL              |
| 2     | Significant increase in credit risk      | Lifetime ECL              |
| 3     | Credit-impaired                          | Lifetime ECL (actual)     |

---

## Market Risk Framework

| Method              | Description                                         |
|---------------------|-----------------------------------------------------|
| VaR (99%, 10-day)   | Value at Risk — maximum expected loss               |
| Stress Testing      | Scenario-based loss under extreme market conditions |
| Sensitivity Analysis| Delta, gamma, vega for derivatives                  |

---

## Operational Risk Framework

| Assessment Method | Description                                           |
|-------------------|-------------------------------------------------------|
| BIA               | Business Impact Analysis — criticality of processes   |
| Scenario Analysis  | Forward-looking loss estimation for rare events      |
| Loss Event History | Historic loss data analysis for recurring risks      |
| RCSA              | Risk and Control Self-Assessment                      |

---

## Fraud Risk Framework

Applied in Stage 5 using the BFSI Fraud Signal Taxonomy (FS-001 to FS-010).
Composite fraud risk score is derived by weighted sum of triggered signals.

| Risk Weight | Score Contribution |
|-------------|-------------------|
| CRITICAL    | 30 points per signal|
| HIGH        | 20 points per signal|
| MEDIUM      | 10 points per signal|

Maximum possible raw fraud score: 300 (all 10 signals at CRITICAL).
Normalized to 0–100 scale before risk tier classification.

---

## Risk Framework Rules

| Rule ID   | Description                                                             |
|-----------|-------------------------------------------------------------------------|
| RF-001    | Risk inputs MUST be explicitly defined; no implicit assumptions         |
| RF-002    | Risk outputs MUST be traceable to specific inputs                       |
| RF-003    | Model assumptions MUST be documented inline in the risk output          |
| RF-004    | Domain-specific risk shortcuts are PROHIBITED                           |
| RF-005    | Opaque risk scoring (non-explainable) is PROHIBITED                     |
| RF-006    | Undocumented model changes are PROHIBITED                               |

---

## Audit Requirements for Risk Decisions

- Every risk assessment output MUST include a version reference for the risk model used.
- All CRITICAL tier decisions MUST be reviewed by a named risk officer.
- Risk score history must be retained for the regulatory-mandated period.
- Model validation must be conducted at minimum annually.
