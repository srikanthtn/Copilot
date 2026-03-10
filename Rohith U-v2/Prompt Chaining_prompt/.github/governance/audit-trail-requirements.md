# Audit Trail Requirements — BFSI Prompt Chaining Framework
**Version:** 1.0.0
**Authority:** chain-master.md | chain-governance-policy.md

---

## Purpose

Define the minimum audit trail requirements for every BFSI Prompt Chain execution,
ensuring traceability, reproducibility, and regulatory defensibility of all
AI-assisted financial reasoning decisions.

---

## Mandatory Audit Record — Per Chain Execution

Each chain execution MUST produce a structured audit record containing:

| Field                   | Description                                               | Required |
|-------------------------|-----------------------------------------------------------|----------|
| `audit_uuid`            | Unique identifier for this chain execution                | MANDATORY|
| `chain_version`         | Semantic version of the chain coordinator prompt used     | MANDATORY|
| `execution_timestamp`   | ISO 8601 datetime of chain initiation                     | MANDATORY|
| `domain_scope`          | List of BFSI domains activated during Stage 3             | MANDATORY|
| `intent_classification` | Primary and secondary intents from Stage 2                | MANDATORY|
| `stages_executed`       | List of stage IDs executed (e.g., [1, 2, 3, 4, 5, 6, 7]) | MANDATORY|
| `compliance_verdict`    | Stage 4 verdict (COMPLIANT / CONDITIONAL / NON_COMPLIANT) | MANDATORY|
| `violations_detected`   | List of violation IDs from Stage 4 (or NONE)             | MANDATORY|
| `risk_tier`             | Stage 5 risk tier classification                          | MANDATORY|
| `risk_score`            | Stage 5 composite risk score (0–100)                      | MANDATORY|
| `decision_verdict`      | Stage 6 decision verdict                                  | MANDATORY|
| `regulations_evaluated` | List of regulations assessed in Stage 4                  | MANDATORY|
| `instruction_files_used`| List of instruction files ingested during Phase 0         | RECOMMENDED|
| `operator_id`           | Identifier of the operator or system invoking the chain   | RECOMMENDED|
| `channel`               | Channel or system invoking the chain (API, UI, batch)     | RECOMMENDED|

---

## Audit Record Retention

| Record Type               | Minimum Retention Period     | Regulatory Basis        |
|---------------------------|------------------------------|-------------------------|
| All chain executions      | 7 years                      | Basel III / local CBs   |
| SAR-related executions    | 10 years                     | FATF / AMLD6            |
| Compliance BLOCK records  | 10 years                     | AMLD6 / AML Act         |
| High-risk decisions       | 7 years post-relationship end| Basel III / FATF        |
| PEP-related decisions     | 5 years post-relationship end| FATF Rec 12             |

---

## Immutability Requirements

- Audit records MUST be append-only.
- No modification or deletion of audit records is permitted.
- Audit records must be stored with tamper-evident controls.
- All access to audit records must itself be logged.

---

## Audit Accessibility Standards

- Audit records MUST be retrievable by `audit_uuid` within 24 hours.
- Regulatory bodies must be able to receive audit exports within 5 business days of request.
- Audit record schema must be documented and version-controlled alongside the chain framework.

---

## Compliance Reporting Obligations

When a chain execution results in:

| Trigger                                       | Required Action                                        |
|-----------------------------------------------|--------------------------------------------------------|
| Compliance verdict = NON_COMPLIANT            | Retain COMPLIANCE_BLOCK_REPORT for minimum 10 years   |
| Risk tier = CRITICAL + decision = ESCALATE    | Notify MLRO/CRO within 24 hours                        |
| COMP-003 BLOCK (Sanctions hit)                | File with OFAC/EU/UN authority per jurisdiction rules  |
| AML-001 threshold breach (AML-005)            | SAR filing within jurisdiction regulatory deadline     |
| PEP detected (COMP-004)                       | Escalate to senior management; document sign-off       |
