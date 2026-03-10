# Fraud Detection — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Fraud Detection domain governs the real-time and batch identification,
classification, and response to fraudulent activities across all BFSI products,
channels, and customer segments.

This domain ensures **proactive fraud prevention**, **signal-based detection**,
and **timely intervention** before financial harm occurs.

---

## Characteristics

- Fraud detection logic must be explainable — opaque models without documented
  signal weights are prohibited.
- Detection outcomes must be actionable — every fraud signal must map to a
  defined response action (block, flag, review, escalate).
- False positive rates must be actively managed — blanket blocking without
  signal-based justification creates regulatory and reputational risk.
- All fraud verdicts must be traceable to specific detected signals.

---

## BFSI Fraud Signal Taxonomy

| Signal Code | Signal Description                               | Risk Weight | Detection Method           |
|-------------|--------------------------------------------------|-------------|----------------------------|
| FS-001      | Transaction velocity spike (> 3σ from baseline) | HIGH        | Statistical threshold       |
| FS-002      | Geolocation mismatch (device IP vs. card country)| HIGH        | Location cross-reference    |
| FS-003      | Round-amount transaction pattern                 | MEDIUM      | Amount pattern analysis     |
| FS-004      | First-time high-value payee                      | MEDIUM      | Beneficiary history check   |
| FS-005      | Account dormancy breach (> 12 months)            | HIGH        | Account activity history    |
| FS-006      | Rapid fund sweeping pattern                      | CRITICAL    | Balance change velocity     |
| FS-007      | Multiple failed authentication attempts          | HIGH        | Auth log analysis           |
| FS-008      | Split transaction structuring (smurfing)         | CRITICAL    | Threshold-splitting pattern |
| FS-009      | Merchant category anomaly                        | MEDIUM      | MCC baseline deviation      |
| FS-010      | Cross-border transaction mismatch                | HIGH        | Geographic profile deviation|

---

## Chain Reasoning Responsibilities

### Stage 3 Triggers
- Resolution of entities to `FraudSignal`, `RiskProfile`, `AMLAlert`
- Mapping of behavioral anomalies to signal taxonomy codes

### Stage 4 Compliance Triggers
| Check ID  | Description                                      |
|-----------|--------------------------------------------------|
| COMP-001  | AML screening for all flagged transactions        |
| COMP-003  | Sanctions screening when cross-border flag raised |
| COMP-008  | Audit trail entry for all fraud determinations    |

### Stage 5 Direct Triggers
- Full fraud signal scan (FS-001 through FS-010) is MANDATORY when:
  - Primary intent is `DETECT`
  - Secondary intent is `DETECT`
  - `TRANSACT` intent with HIGH-risk customer profile

---

## Response Actions by Detected Severity

| Composite Score | Risk Tier  | Required Response                                   |
|-----------------|------------|-----------------------------------------------------|
| 0 – 30          | LOW        | Allow. Log event.                                   |
| 31 – 60         | MEDIUM     | Allow with monitoring flag. Notify risk team.       |
| 61 – 85         | HIGH       | Soft block. Require additional authentication.      |
| 86 – 100        | CRITICAL   | Hard block. Freeze account. Escalate to MLRO.       |

---

## Prohibited Operations

- Processing a transaction flagged at CRITICAL tier without escalation.
- Ignoring CRITICAL fraud signals based on customer tenure or relationship.
- Blocking customers based on non-signal factors (demographic profiling).
- Suppressing fraud detection findings from the audit trail.
- Applying fraud verdicts without documented signal evidence.
