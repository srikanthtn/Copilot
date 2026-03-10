# Risk & Compliance — Compliance Rules
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Compliance Requirements

- All regulated activities MUST be subject to compliance checks before execution.
- Compliance validation MUST occur **before** any transaction or state change is permitted.
- Compliance violations MUST be explicitly recorded — silent failures are forbidden.
- All SAR (Suspicious Activity Report) filing decisions MUST be documented with a
  reasoning trace referencing the specific signals that triggered the decision.

---

## AML Compliance Rules

| Rule ID    | Description                                                            |
|------------|------------------------------------------------------------------------|
| AML-001    | All transactions above EUR/USD 10,000 equivalent must be monitored     |
| AML-002    | Structuring patterns (splitting to avoid thresholds) must be detected  |
| AML-003    | Beneficial ownership must be established for all legal entity customers|
| AML-004    | High-risk jurisdictions must trigger enhanced monitoring automatically  |
| AML-005    | SAR filing must occur within regulatory deadlines once triggered        |

---

## KYC Compliance Rules

| Rule ID    | Description                                                            |
|------------|------------------------------------------------------------------------|
| KYC-001    | Customer identity must be verified before account opening              |
| KYC-002    | Periodic KYC refresh must occur per risk-based schedule                |
| KYC-003    | PEP (Politically Exposed Person) status must be actively monitored     |
| KYC-004    | Negative news and adverse media must be screened continuously          |
| KYC-005    | KYC downgrade (Tier reduction) must trigger account restrictions       |

---

## Sanctions Screening Rules

| Rule ID    | Description                                                            |
|------------|------------------------------------------------------------------------|
| SAN-001    | All parties must be screened against OFAC, EU, UN, and HMT lists      |
| SAN-002    | Fuzzy matching must be applied (not exact match only)                  |
| SAN-003    | Positive sanctions hits must result in immediate transaction BLOCK     |
| SAN-004    | All sanctions hits must be escalated to the MLRO within 24 hours      |

---

## Enforcement Rules

- Compliance outcomes MUST be deterministic — same inputs must produce same verdict.
- Overrides of compliance controls require explicit authorization with a named approver and documented rationale.
- All compliance overrides must be logged and included in the periodic audit report.

---

## Forbidden Compliance Behaviours

- Skipping AML screening for any transaction above minimum threshold.
- Silent compliance failures (no logging, no flagging).
- Informal exception handling via verbal instruction.
- Allowing a transaction to proceed with an unresolved sanctions hit.
