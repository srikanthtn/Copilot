# BFSI Advanced Prompt Chaining Framework
**Version:** 1.0.0
**Architecture Basis:** Boron_v1
**Domain:** Banking, Financial Services, and Insurance (BFSI)

---

## Overview

The **BFSI Advanced Prompt Chaining Framework** is an enterprise-grade, production-ready
prompt engineering system designed for AI-assisted financial reasoning in regulated
BFSI environments.

This framework implements a **structured 7-stage reasoning chain** that enforces
domain precision, regulatory compliance, and risk-aware decision logic before
any response is generated.

It is architecturally derived from and fully consistent with the **Boron_v1** prompt
engineering foundation, enhanced with Advanced Prompt Chaining reasoning techniques.

---

## Architecture

```
Prompt Chaining_prompt/
├── README.md                                   ← This file
└── .github/
    ├── prompts/
    │   ├── bfsi-chain-coordinator.prompt.md    ← MAIN: 7-stage reasoning chain
    │   └── bfsi-chain-validator.prompt.md      ← AUDIT: 7-dimension validation scorecard
    ├── instructions/
    │   ├── chain-master.md                     ← AUTHORITY: Global rules & entity registry
    │   ├── chain-stage-definitions.md          ← Execution contracts per stage
    │   ├── core-banking/
    │   │   ├── domain-overview.md
    │   │   ├── business-rules.md
    │   │   └── regulatory-constraints.md
    │   ├── payments/
    │   │   └── domain-overview.md
    │   ├── risk-compliance/
    │   │   ├── domain-overview.md
    │   │   ├── compliance-rules.md
    │   │   └── risk-frameworks.md
    │   ├── insurance/
    │   │   └── domain-overview.md
    │   ├── fraud-detection/
    │   │   └── domain-overview.md
    │   ├── credit-assessment/
    │   │   └── domain-overview.md
    │   └── investment/
    │       └── domain-overview.md
    └── governance/
        ├── chain-governance-policy.md          ← Enterprise governance policy
        ├── chain-versioning-strategy.md        ← SemVer rules
        ├── chain-approval-workflow.md          ← Change approval RACI
        └── audit-trail-requirements.md        ← Regulatory audit obligations
```

---

## The 7-Stage Reasoning Chain

| Stage | Name                       | Responsibility                                        | Gate |
|-------|----------------------------|-------------------------------------------------------|------|
| 1     | Context Understanding      | Extract entities, scope, actors, ambiguities          | No   |
| 2     | Intent Identification      | Classify intent type, urgency, sensitivity            | No   |
| 3     | BFSI Domain Interpretation | Map to canonical BFSI constructs, apply business rules| No   |
| 4     | Compliance Evaluation      | Evaluate against 10 compliance checks + regulations   | YES  |
| 5     | Risk Assessment            | Score risk dimensions and fraud signals               | YES  |
| 6     | Decision Reasoning         | Synthesize all artifacts into a traceable verdict     | No   |
| 7     | Final Response Generation  | Produce structured, auditable enterprise response     | No   |

**Gate Stages** enforce hard stops:
- Stage 4: `NON_COMPLIANT` verdict halts the entire chain immediately.
- Stage 5: `CRITICAL` tier mandates mitigations before any positive decision.

---

## Supported BFSI Domains

| Domain              | Regulatory Coverage                        |
|---------------------|--------------------------------------------|
| Core Banking        | Basel III, Central Bank Guidelines         |
| Payments            | PSD2, ISO 20022, SEPA, SWIFT               |
| Risk & Compliance   | FATF, AMLD6, KYC/AML                       |
| Insurance           | Solvency II, IRDAI                         |
| Fraud Detection     | PCI-DSS, Internal Risk Frameworks          |
| Credit Assessment   | IFRS 9, Basel III, Consumer Credit Laws    |
| Investment          | MiFID II, SEBI, SEC                        |

---

## How to Invoke

**Standard Chain Execution:**
```
/bfsi-chain <domain-keyword>
/bfsi-chain payments
/bfsi-chain risk-compliance
/bfsi-chain kyc-aml
/bfsi-chain credit-assessment
```

**With Mode Flags:**
```
/bfsi-chain --audit        (Stages 1–5 only; compliance gap report)
/bfsi-chain --explain      (Reasoning trace alongside every stage)
/bfsi-chain --risk-only    (Stages 1, 3, 5 rapid triage)
/bfsi-chain --strict       (Maximum regulatory strictness)
```

**Validation Run:**
```
/bfsi-chain-validator      (Audits last chain execution output)
```

---

## Relationship to Boron_v1

| Aspect                    | Boron_v1                          | BFSI Prompt Chaining Framework         |
|---------------------------|-----------------------------------|----------------------------------------|
| Base Architecture         | Governance-driven instruction files | Same — aligned with Boron_v1 structure |
| Prompt Design Style       | Annotated blocks with @context, @intent_lock | Same style maintained            |
| Domain Coverage           | Multi-domain BFSI                 | Same domains; deepened chain reasoning |
| Reasoning Technique       | Single-pass code generation       | 7-stage sequential reasoning chain     |
| Gate Enforcement          | Code-level forbidden operations   | Stage-level reasoning gates            |
| Compliance Model          | Rule tables in instruction files  | Chain-enforced compliance evaluation   |
| Output Type               | Scala/Spark code files            | Structured BFSI reasoning responses    |

---

## Governance

This framework operates under enterprise governance defined in:
- [chain-governance-policy.md](.github/governance/chain-governance-policy.md)
- [chain-approval-workflow.md](.github/governance/chain-approval-workflow.md)
- [chain-versioning-strategy.md](.github/governance/chain-versioning-strategy.md)
- [audit-trail-requirements.md](.github/governance/audit-trail-requirements.md)

---

## Regulatory Compliance

All reasoning chain stages are designed for compliance with:

| Regulation     | Coverage                                        |
|----------------|-------------------------------------------------|
| FATF Rec 10    | KYC / Customer Due Diligence obligations        |
| EU AMLD6       | Anti-Money Laundering reasoning                 |
| PSD2           | Payment authorization and SCA requirements     |
| GDPR / DPDPA   | Personal data protection in reasoning outputs  |
| Basel III      | Capital adequacy and credit risk assessment     |
| IFRS 9         | Expected credit loss staging                    |
| Solvency II    | Insurance capital and reserving                 |
| PCI-DSS        | Payment card data security                      |
| MiFID II       | Investment suitability reasoning                |
| ISO 20022      | Payment messaging standard alignment            |

---

*Framework Version 1.0.0 — Derived from Boron_v1 Architecture*
