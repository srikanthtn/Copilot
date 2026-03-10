# Insurance — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Insurance domain governs the full lifecycle of insurance products, including
policy origination, underwriting, premium management, claims, and actuarial modelling.

This domain ensures **actuarial accuracy**, **claims integrity**, and **regulatory capital compliance**.

---

## Characteristics

- Underwriting decisions must be risk-based, explainable, and non-discriminatory.
- Claims assessments must follow documented claims handling procedures.
- Reserving methodology must comply with Solvency II or applicable actuarial standards.
- Policy lifecycle transitions must be auditable and irreversible without explicit authorization.

---

## Policy Lifecycle State Machine

```
APPLIED ──▶ UNDERWRITING ──▶ ISSUED ──▶ ACTIVE ──▶ RENEWED
                │                          │
                ▼                          ▼
           DECLINED                   LAPSED / CANCELLED
                                           │
                                           ▼
                                       [TERMINAL STATE]
```

---

## Claims Lifecycle State Machine

```
LODGED ──▶ ACKNOWLEDGED ──▶ UNDER_ASSESSMENT ──▶ APPROVED ──▶ PAID
              │                    │                 │
              ▼                    ▼                 ▼
           REJECTED           REFERRED          DISPUTED
```

---

## Chain Reasoning Responsibilities

### Stage 3 Triggers
- Resolution of insurance entities: `InsurancePolicy`, `Claim`, `UnderwritingDecision`,
  `PremiumRecord`, `ActuarialModel`
- Application of policy and claims lifecycle state machine rules
- Coverage scope and exclusions interpretation

### Stage 4 Compliance Triggers
| Check ID  | Description                                     |
|-----------|-------------------------------------------------|
| COMP-002  | KYC/CDD verified for policyholder               |
| COMP-007  | Consent and authorization for policy changes    |
| COMP-008  | Audit trail entry for underwriting decisions    |
| COMP-010  | Conflict of interest checks (broker, advisor)   |

Applicable regulation: **Solvency II**, **IRDAI Regulations**, **Insurance Act**

### Stage 5 Risk Triggers
- Actuarial and underwriting risk assessment
- Concentration risk in portfolio exposure
- Claims fraud indicators (inflated claims, late reporting anomalies)

---

## Prohibited Operations

- Issuing a policy without completing underwriting risk assessment.
- Approving a claim above threshold without documented assessment logic.
- Making underwriting decisions based on prohibited discrimination criteria.
- Releasing claim payment without identity and authorization verification.
- Retroactive policy backdating without regulatory authorization.
