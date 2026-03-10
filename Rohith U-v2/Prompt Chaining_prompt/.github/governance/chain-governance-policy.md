# Chain Governance Policy — BFSI Advanced Prompt Chaining
**Authority:** Architectural Review Board (ARB) & Chief Compliance Officer
**Version:** 1.0.0

---

## 1. Persona & Role Enforcement

All instances of the BFSI Prompt Chain MUST adopt the **"Principal BFSI Reasoning Architect"** persona.

- **Prohibited:** Conversational shortcuts, "Happy-to-help" filler, informal tone.
- **Mandatory:** Chain-First execution, Analysis-before-response workflow, institutional tone.

---

## 2. Chain Usage Policy

- **Sequential Lock:** Developers and operators may NOT override stage sequencing.
- **Gate Enforcement:** Compliance and Risk gates (Stages 4–5) may NOT be bypassed.
- **Versioning:** All prompt chain files must follow Semantic Versioning (v1.0.0).
- **Audit Obligation:** All chain executions must produce an audit trail reference UUID.
- **Intent Lock:** Users may NOT override safety gates embedded in chain stages.

---

## 3. Approval Workflow (RACI Matrix)

| Action                      | Governance Impact           | Responsible     | Accountable   | Consulted   | Informed  |
|-----------------------------|-----------------------------|-----------------| --------------|-------------|-----------|
| Stage Logic Change          | Changes reasoning behavior  | Lead Architect  | ARB           | Compliance  | Team      |
| New Domain Entity Added     | Changes domain model        | Domain Owner    | Data Steward  | Architect   | Team      |
| Compliance Rule Relaxation  | Lowers safety standard      | Compliance Dept | MLRO / CISO   | Legal       | Auditors  |
| Risk Framework Change       | Alters risk posture         | CRO             | ARB           | Compliance  | Risk Dept |
| Emergency Override          | Bypasses chain controls     | Senior Engineer | CTO           | Risk        | Team      |

---

## 4. Prompt Chain Versioning Strategy (Strict SemVer)

All prompt chain files MUST follow Semantic Versioning (`vMAJOR.MINOR.PATCH`).

| Version Kind | Trigger                                                        | Approval Required  |
|--------------|----------------------------------------------------------------|--------------------|
| MAJOR        | Stage structure change, gate logic change, core mandate change | ARB + CISO         |
| MINOR        | New checklist items, domain entity additions, new fraud signals| Architect          |
| PATCH        | Typos, formatting corrections, non-behavioral updates          | Peer Review        |

---

## 5. Emergency Override Protocol

In case of a **P1 Critical Incident** (Production or Regulatory Crisis):

1. Stage 4 and Stage 5 gate halts may be **temporarily suspended** by Staff Engineers only.
2. Manual reasoning intervention is permitted by **Principal Architects** only.
3. **Post-Incident:** A Compliance Override Report must be filed within 24 hours.
4. **Resync:** All manual overrides must be retroactively modelled into updated instruction files within 3 business days.
5. All emergency overrides must be logged with: timestamp, actor ID, reason code, and ARB notification confirmation.

---

## 6. Data Handling During Chain Execution

| Data Type          | Handling Requirement                                          |
|--------------------|---------------------------------------------------------------|
| PII                | Must not appear in any stage output — mask or redact always  |
| IBAN / PAN         | Must be masked in all reasoning outputs                       |
| Credentials        | Never referenced in any stage or output                       |
| Regulatory IDs     | Must be cited accurately; fabricated IDs are strictly BLOCKED |
| Audit UUIDs        | Must be unique per chain execution; never reused              |

---

## 7. Compliance with Applicable Regulatory Standards

This governance policy operates within the following regulatory framework:

- FATF Recommendations (Anti-Money Laundering)
- EU AMLD6 (Sixth Anti-Money Laundering Directive)
- PSD2 (Payment Services Directive 2)
- GDPR / DPDPA 2023 (Data Protection)
- Basel III / BCBS Framework
- Solvency II (Insurance Capital)
- PCI-DSS (Payment Card Security)
- MiFID II (Investment Suitability)
- ISO 27001 (Information Security Management)
