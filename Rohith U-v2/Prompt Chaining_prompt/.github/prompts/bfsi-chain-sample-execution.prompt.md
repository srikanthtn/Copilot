---
name: BFSI Prompt Chain — Sample Execution Walkthrough
version: 1.0.0
description: >
  A complete worked example of the BFSI Advanced Prompt Chaining Framework
  executing all 7 reasoning stages against a real-world BFSI scenario.
  This file demonstrates how the chain coordinator processes a financial
  task from raw input through to a final auditable response.
scenario: High-value cross-border wire transfer with suspicious activity signals
chain_version: 1.0.0
---

# ═══════════════════════════════════════════════════════════════════════════════
# SAMPLE CHAIN EXECUTION
# Scenario: High-Value Cross-Border Wire Transfer with Suspicious Signals
# ═══════════════════════════════════════════════════════════════════════════════

## RAW INPUT (User Query)

```
A customer has initiated a USD 185,000 wire transfer to a bank account in
a high-risk jurisdiction. The customer's account was dormant for 14 months
before this transaction. The customer completed KYC Tier 1 only. Please
assess and process the request.
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# PHASE 0 — KNOWLEDGE INGESTION
# ═══════════════════════════════════════════════════════════════════════════════

**Instruction files ingested:**
- `chain-master.md` — Universal business invariants loaded
- `instructions/payments/domain-overview.md` — Payment domain rules loaded
- `instructions/risk-compliance/domain-overview.md` — AML/KYC rules loaded
- `instructions/risk-compliance/compliance-rules.md` — Compliance rules loaded
- `instructions/risk-compliance/risk-frameworks.md` — Risk frameworks loaded
- `instructions/fraud-detection/domain-overview.md` — Fraud signal taxonomy loaded
- `instructions/core-banking/business-rules.md` — Core banking rules loaded
- `chain-stage-definitions.md` — Stage execution contracts loaded

**Domain routing:** `payments` + `risk-compliance` + `fraud-detection`

---

# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 1 — CONTEXT UNDERSTANDING
# Stage ID: STAGE-01
# ═══════════════════════════════════════════════════════════════════════════════

**Entities Identified:**

- **Financial Actor — Originator:** Unnamed customer (internal account holder)
- **Financial Actor — Beneficiary:** Unnamed recipient at a foreign bank
- **Transaction Amount:** USD 185,000
- **Currency:** USD (ISO 4217 code confirmed)
- **Transaction Type:** Wire transfer (cross-border)
- **Destination Jurisdiction:** High-risk jurisdiction (specific country not stated)
- **KYC Status of Originator:** Tier 1 only (Basic CDD — name, DOB, address, document)
- **Account State:** Previously dormant — reactivated within this transaction window
- **Dormancy Period:** 14 months

**Operational Scope:**
- Request is to **assess and process** a cross-border payment instruction.
- System boundary involves: Payments processing, AML monitoring, KYC compliance gatekeeping.
- Data surfaces in scope: Customer account records, KYC status, transaction history, sanctions lists.

**Ambiguity Register:**
- [AMBIGUITY: "High-risk jurisdiction" — specific country not named. Cannot perform exact sanctions screening without jurisdiction identification.]
- [AMBIGUITY: "Customer" identity not provided — cannot verify KYC record directly.]
- [AMBIGUITY: "Process the request" is ambiguous — this could mean approve, verify, or simply route. Interpreted conservatively as: evaluate eligibility for processing.]

```
[CONTEXT_ARTIFACT:
  stage              : STAGE-01
  entities           : [
    "Customer (originator) — identity unspecified",
    "Beneficiary — identity unspecified",
    "Amount: USD 185,000",
    "Transaction type: Cross-border wire transfer",
    "Destination: High-risk jurisdiction (unspecified)",
    "KYC Tier of originator: Tier 1 (Basic CDD only)",
    "Account dormancy: 14 months prior to this transaction"
  ]
  scope              : "Assess eligibility and process a cross-border wire transfer"
  temporal_markers   : [ "Current transaction date", "14 months dormancy period" ]
  amounts            : [ "USD 185,000" ]
  jurisdiction       : "High-risk (unspecified country — AMBIGUITY registered)"
  ambiguities        : [
    "AMBIGUITY-01: High-risk jurisdiction not named — exact sanctions screening blocked",
    "AMBIGUITY-02: Customer identity not provided",
    "AMBIGUITY-03: 'Process the request' scope is ambiguous"
  ]
  raw_input_summary  : "Cross-border USD 185K wire to high-risk jurisdiction from a dormant account with only KYC Tier 1 verification."
]
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 2 — INTENT IDENTIFICATION
# Stage ID: STAGE-02
# ═══════════════════════════════════════════════════════════════════════════════

**Consuming:** [CONTEXT_ARTIFACT from STAGE-01]

**Primary Intent Classification:**

The request asks to both assess AND process a financial transaction.
"Assess" maps to `ASSESS`. "Process" maps to `TRANSACT`.
Since the transaction has not yet been executed and assessment is the gating step:
→ **Primary Intent: `ASSESS`** (credit / risk evaluation before action)
→ **Secondary Intent: `TRANSACT`** (conditional on assessment outcome)

**Urgency Tier:**
- Cross-border wire transfers in most real-time systems have regulatory reporting
  obligations within defined windows. The large amount (USD 185,000 > USD 10,000
  FinCEN/FATF threshold) elevates processing urgency.
→ **Urgency Tier: `P1`** (regulatory SLA-bound, threshold reporting obligations apply)

**Operation Sensitivity:**
- Customer financial data is in scope.
- Cross-border transaction details involve a potentially sanctioned jurisdiction.
→ **Sensitivity: `RESTRICTED`**
→ **PII Flag: `PII_PRESENT`** (customer identity and account data involved)

```
[INTENT_ARTIFACT:
  stage              : STAGE-02
  primary_intent     : ASSESS
  secondary_intent   : TRANSACT
  urgency_tier       : P1
  sensitivity        : RESTRICTED
  pii_flag           : PII_PRESENT
  intent_rationale   : "Assessment gates the transaction. Both intents are active but ASSESS must resolve first before TRANSACT can be permitted."
]
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 3 — BFSI DOMAIN INTERPRETATION
# Stage ID: STAGE-03
# ═══════════════════════════════════════════════════════════════════════════════

**Consuming:** [CONTEXT_ARTIFACT + INTENT_ARTIFACT from STAGE-01, STAGE-02]

**Domain Entity Resolution:**

| Raw Input Term                        | Canonical BFSI Entity                              |
|---------------------------------------|-----------------------------------------------------|
| "Wire transfer to foreign bank"       | `PaymentInstruction` → type: CrossBorderTransfer   |
| "Customer"                            | `CustomerRecord` → KYC Tier 1, dormant account     |
| "Account dormant for 14 months"       | `Account` → state: DORMANCY_BREACH (signal FS-005) |
| "USD 185,000"                         | `Money(amount: 185000.00, currency: USD)`          |
| "High-risk jurisdiction"              | `CrossBorderTransferRequest` + `SanctionsScreening` required |
| "KYC Tier 1 only"                     | `KycStatus` → Tier: 1, status: BELOW_REQUIRED_THRESHOLD |
| "Assess and process"                  | `RiskScoreComputation` + `PaymentInstruction` dispatch |

**Business Rules Applied:**

- **FM-001:** Amount USD 185,000.00 with explicit currency confirmed. ✓
- **FM-005:** DEBIT direction on originator account; CREDIT on beneficiary. ✓
- **FM-007:** Transaction date confirmed as current. ✓
- **KYC-001 (from compliance-rules.md):** Cross-border wire of this value requires
  minimum KYC Tier 2. Current status is Tier 1. → **BELOW THRESHOLD — Rule violation identified.**
- **AML-001 (from compliance-rules.md):** USD 185,000 is above USD 10,000 equivalent AML
  monitoring threshold. → **AML monitoring mandatory.**
- **AML-004:** High-risk jurisdiction triggers enhanced monitoring automatically.
- **State Machine:** Account was DORMANT. Reactivation without verified reactivation
  event violates the state machine constraint (SE-002). → **Anomalous state transition.**

**BFSI Terminology Normalizations:**

| Input Term                     | Normalized BFSI Term                                |
|--------------------------------|------------------------------------------------------|
| "Wire transfer to foreign bank" | `CrossBorderCreditTransfer`                        |
| "High-risk jurisdiction"        | `HighRiskJurisdiction: EDD (Enhanced Due Diligence) required` |
| "Assess and process"            | `RiskScoreComputation` + `ComplianceGating`        |
| "Dormant account"               | `AccountStatusTransition: DORMANT → ACTIVE (unverified)` |

**Domains in Scope:**
- `payments` — CrossBorderCreditTransfer instruction
- `risk-compliance` — AML monitoring, KYC verification, sanctions screening
- `fraud-detection` — Dormancy breach + cross-border mismatch signals active

**Cross-Domain Dependencies:**
- `payments` depends on `risk-compliance` for AML clearance before dispatch
- `risk-compliance` depends on `core-banking` for account state and KYC records
- `fraud-detection` signals feed into `risk-compliance` risk scoring

```
[DOMAIN_ARTIFACT:
  stage                      : STAGE-03
  resolved_entities          : [
    { raw: "Wire transfer to foreign bank", canonical: "CrossBorderCreditTransfer" },
    { raw: "Customer", canonical: "CustomerRecord (KYC Tier 1, dormant account)" },
    { raw: "USD 185,000", canonical: "Money(185000.00, USD)" },
    { raw: "High-risk jurisdiction", canonical: "HighRiskJurisdiction — EDD required" },
    { raw: "Dormant 14 months", canonical: "Account DORMANCY_BREACH — FS-005 triggered" }
  ]
  business_rules_applied     : [
    "FM-001 — Monetary precision confirmed",
    "FM-005 — DEBIT/CREDIT direction confirmed",
    "KYC-001 — KYC Tier 1 BELOW required Tier 2 for this transaction value",
    "AML-001 — Amount exceeds AML monitoring threshold",
    "AML-004 — High-risk jurisdiction triggers EDD"
  ]
  domains_in_scope           : [ "payments", "risk-compliance", "fraud-detection" ]
  cross_domain_dependencies  : [
    "payments depends on risk-compliance for AML clearance",
    "risk-compliance depends on core-banking for KYC and account state"
  ]
  terminology_normalizations : [
    { input: "wire transfer to foreign bank", normalized: "CrossBorderCreditTransfer" },
    { input: "dormant account", normalized: "AccountStatusTransition: DORMANT → ACTIVE (unverified)" }
  ]
  domain_boundary_violations : []
]
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 4 — COMPLIANCE EVALUATION  [GATE STAGE]
# Stage ID: STAGE-04
# ═══════════════════════════════════════════════════════════════════════════════

**Consuming:** [CONTEXT_ARTIFACT + INTENT_ARTIFACT + DOMAIN_ARTIFACT from STAGES 01–03]

**Regulatory Framework Mapping:**

| Regulation     | Triggered | Reason                                              |
|----------------|-----------|-----------------------------------------------------|
| FATF Rec 10    | YES       | Cross-border transaction; KYC verification required |
| EU AMLD6       | YES       | Amount > EUR 10,000 equivalent; high-risk jurisdiction |
| PSD2           | YES       | Digital payment initiation with authentication      |
| GDPR / DPDPA   | YES       | PII_PRESENT flag from Stage 2                       |
| Basel III      | Partial   | Operational risk dimension triggered                |
| FATF AML-001   | YES       | USD 185,000 > USD 10,000 AML reporting threshold    |

**Compliance Gate Checklist:**

| Check ID  | Description                                      | Result  | Reason                                              |
|-----------|--------------------------------------------------|---------|-----------------------------------------------------|
| COMP-001  | AML screening completed for all parties          | BLOCK   | AML screening not confirmed; amount above threshold |
| COMP-002  | KYC/CDD status verified and current              | BLOCK   | KYC Tier 1 only — Tier 2 required for this amount  |
| COMP-003  | Sanctions screening executed (OFAC, EU, UN)      | BLOCK   | Jurisdiction not identified; screening cannot complete |
| COMP-004  | PEP check performed                              | WARN    | Cannot confirm PEP status without full KYC Tier 2  |
| COMP-005  | Transaction threshold reporting obligations met  | WARN    | Threshold reporting obligation exists; not confirmed met |
| COMP-006  | Data retention policy applied                    | PASS    | Standard data handling applies                      |
| COMP-007  | Consent and authorization verified               | WARN    | Authorization present but reactivation of dormant account unverified |
| COMP-008  | Audit trail entry created for this operation     | PASS    | Audit UUID generated at chain initiation            |
| COMP-009  | Cross-border regulatory requirements assessed    | BLOCK   | High-risk jurisdiction unidentified — EDD requirements cannot be confirmed |
| COMP-010  | Conflict of interest checks cleared              | PASS    | No conflict signals in scope                        |

**Violations Detected:**

| Violation ID | Description                                                   | Severity      |
|--------------|---------------------------------------------------------------|---------------|
| VIO-003      | Transaction bypasses mandatory AML screening (unconfirmed)    | BLOCK + ALERT |
| VIO-006      | Customer identity unverified above KYC threshold (Tier 1 < Tier 2 required) | BLOCK + HALT |

**Compliance Verdict: `NON_COMPLIANT`**

→ **CHAIN HALTED AT STAGE 4.**
→ Stages 5, 6, 7 will NOT execute.
→ Emitting COMPLIANCE_BLOCK_REPORT below.

```
[COMPLIANCE_ARTIFACT:
  stage                  : STAGE-04
  regulations_evaluated  : [ "FATF Rec 10", "EU AMLD6", "PSD2", "GDPR", "FATF AML-001" ]
  checklist_results      : [
    { check_id: "COMP-001", result: "BLOCK" },
    { check_id: "COMP-002", result: "BLOCK" },
    { check_id: "COMP-003", result: "BLOCK" },
    { check_id: "COMP-004", result: "WARN" },
    { check_id: "COMP-005", result: "WARN" },
    { check_id: "COMP-006", result: "PASS" },
    { check_id: "COMP-007", result: "WARN" },
    { check_id: "COMP-008", result: "PASS" },
    { check_id: "COMP-009", result: "BLOCK" },
    { check_id: "COMP-010", result: "PASS" }
  ]
  violations_detected    : [ "VIO-003", "VIO-006" ]
  compliance_verdict     : NON_COMPLIANT
  audit_trail_reference  : "AUDIT-REF-7F3A9C12-B4E1-4D22-91FA-6BC8E0A2D5F7"
  mandatory_notifications: [
    "MLRO — SAR consideration required under AMLD6",
    "Compliance Officer — KYC deficiency escalation"
  ]
  block_reason           : "Transaction cannot proceed: AML screening incomplete, KYC Tier 1 insufficient for USD 185,000 cross-border transfer, sanctions screening blocked due to unidentified high-risk jurisdiction."
]
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# COMPLIANCE BLOCK REPORT
# Stage 4 compliance_verdict = NON_COMPLIANT — Chain Halted
# ═══════════════════════════════════════════════════════════════════════════════

```
╔═══════════════════════════════════════════════════════════════════════════╗
║  COMPLIANCE BLOCK REPORT                                                 ║
║  Chain execution HALTED at Stage 4 — Compliance Evaluation               ║
╚═══════════════════════════════════════════════════════════════════════════╝

BLOCK REASON:
  The requested cross-border wire transfer of USD 185,000 to a high-risk
  jurisdiction CANNOT be processed in its current state. Three compliance
  checks have returned BLOCK status: AML screening is unconfirmed, KYC
  verification is below the required tier for this transaction value, and
  sanctions screening cannot be completed due to an unidentified destination
  jurisdiction.

VIOLATIONS DETECTED:
  VIO-003 | Transaction bypasses mandatory AML screening        | BLOCK + ALERT
  VIO-006 | Customer KYC Tier 1 < Tier 2 required above $10K   | BLOCK + HALT

REGULATORY BASIS:
  - FATF Recommendation 10 — Customer Due Diligence obligations
  - EU AMLD6 — Mandatory AML screening for transactions > EUR 10,000 equivalent
  - FinCEN / Local AML Act — Threshold reporting obligations for amounts > USD 10,000
  - FATF AML-004 — High-risk jurisdiction requires Enhanced Due Diligence (EDD)

MANDATORY ACTIONS:
  1. Halt the CrossBorderCreditTransfer instruction immediately. Do NOT dispatch.
  2. Place the customer account under temporary processing hold.
  3. Contact the customer to initiate KYC Tier 2 upgrade (Enhanced Due Diligence):
     — Collect source of funds documentation
     — Verify account purpose for cross-border transfers
     — Obtain destination counterparty details (full name, bank, country)
  4. Identify the exact destination country and run full sanctions screening
     against OFAC SDN List, EU Consolidated Sanctions List, and UN Sanctions List.
  5. Conduct AML case review for the dormant account reactivation pattern
     (14-month dormancy breach + immediate high-value cross-border transfer).
  6. MLRO to assess whether a Suspicious Activity Report (SAR) is required
     under applicable AML legislation.
  7. Log all actions taken against Audit Reference below.

ESCALATION REQUIRED:
  Owner       : MLRO (Money Laundering Reporting Officer)
  Secondary   : Compliance Officer — KYC Deficiency
  Deadline    : SAR assessment within 24 hours of detection (AMLD6 obligation)
  Case Ref    : AUDIT-REF-7F3A9C12-B4E1-4D22-91FA-6BC8E0A2D5F7

CHAIN EXECUTION STATUS : HALTED
STAGES COMPLETED       : 1, 2, 3, 4
STAGES NOT EXECUTED    : 5, 6, 7
GENERATED AT           : 2026-03-10T00:00:00Z
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# STAGES 5, 6, 7 — NOT EXECUTED
# Reason: Stage 4 compliance_verdict = NON_COMPLIANT
# These stages will execute upon re-submission after compliance remediation.
# ═══════════════════════════════════════════════════════════════════════════════

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHAIN METADATA
# ═══════════════════════════════════════════════════════════════════════════════

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  CHAIN METADATA                                                              │
│  Chain Version     : 1.0.0                                                  │
│  Stages Executed   : [1, 2, 3, 4]                                           │
│  Stages Halted At  : Stage 4 — NON_COMPLIANT verdict                        │
│  Compliance Verdict: NON_COMPLIANT                                           │
│  Risk Tier         : NOT ASSESSED (chain halted before Stage 5)             │
│  Decision Verdict  : NOT ISSUED   (chain halted before Stage 6)             │
│  Domains In Scope  : payments, risk-compliance, fraud-detection             │
│  Audit Reference   : AUDIT-REF-7F3A9C12-B4E1-4D22-91FA-6BC8E0A2D5F7       │
│  Regulatory Basis  : FATF Rec 10, EU AMLD6, PSD2, GDPR, FinCEN AML-001     │
│  Generated At      : 2026-03-10T00:00:00Z                                   │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# RE-SUBMISSION CONDITIONS
# What must be resolved before the chain can be re-run and proceed to Stage 5
# ═══════════════════════════════════════════════════════════════════════════════

| Condition                                        | Owner              | Required Before Stage |
|--------------------------------------------------|--------------------|-----------------------|
| KYC upgraded to Tier 2 with EDD documentation    | KYC Operations     | Stage 4 (COMP-002)    |
| Destination country confirmed and identified     | Customer / Ops     | Stage 4 (COMP-003)    |
| Full sanctions screening completed (OFAC, EU, UN)| Compliance         | Stage 4 (COMP-003)    |
| AML case reviewed and SAR decision documented    | MLRO               | Stage 4 (COMP-001)    |
| Dormant account reactivation authorization       | Relationship Mgr   | Stage 4 (COMP-007)    |
| Source of funds documentation obtained           | KYC Operations     | Stage 4 (COMP-002)    |
