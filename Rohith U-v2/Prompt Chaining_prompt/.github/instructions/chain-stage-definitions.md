# Chain Stage Definitions — BFSI Advanced Prompt Chaining
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Purpose

This file defines the execution contract, responsibility boundary, input/output
specification, and success criteria for each of the 7 reasoning stages in the
BFSI Advanced Prompt Chain. All stages MUST conform to these definitions.

---

## Stage 1 — Context Understanding

**Stage ID:** `STAGE-01`
**Execution Order:** First — no prior stage outputs required
**Gate Status:** Non-Gate (does not block chain execution)

### Responsibility
Extract all structural and factual context from the raw input. This stage does
NOT interpret, classify, or evaluate — it only observes and documents.

### Input
- Raw user query or financial task description

### Mandatory Output: [CONTEXT_ARTIFACT]
```
[CONTEXT_ARTIFACT:
  stage              : STAGE-01
  entities           : [ <list of identified financial and non-financial actors> ]
  scope              : <description of the operation or event in scope>
  temporal_markers   : [ <all dates, times, durations identified> ]
  amounts            : [ <all monetary amounts with currency where present> ]
  jurisdiction       : <country/regulatory zone if identifiable>
  ambiguities        : [ <unresolved terms, tagged [AMBIGUITY: ...]> ]
  raw_input_summary  : <concise rephrasing of the original input>
]
```

### Success Criteria
- All entities are named but not interpreted.
- All ambiguities are registered.
- No compliance, risk, or domain conclusions appear in this artifact.

---

## Stage 2 — Intent Identification

**Stage ID:** `STAGE-02`
**Execution Order:** Second — requires [CONTEXT_ARTIFACT]
**Gate Status:** Non-Gate (does not block chain execution)

### Responsibility
Classify the request according to the BFSI Intent Taxonomy. Determine operational
urgency, data sensitivity, and PII exposure level.

### Input
- [CONTEXT_ARTIFACT] from Stage 1

### Mandatory Output: [INTENT_ARTIFACT]
```
[INTENT_ARTIFACT:
  stage              : STAGE-02
  primary_intent     : < QUERY | TRANSACT | ASSESS | COMPLY | DETECT | REPORT | MANAGE | ESCALATE >
  secondary_intent   : < intent code or NONE >
  urgency_tier       : < P1 | P2 | P3 >
  sensitivity        : < RESTRICTED | CONFIDENTIAL | INTERNAL >
  pii_flag           : < PII_PRESENT | PII_ABSENT >
  intent_rationale   : <one-sentence explanation of why this classification was chosen>
]
```

### Success Criteria
- Exactly one primary intent is assigned.
- Secondary intent is explicitly stated (including NONE).
- Urgency and sensitivity are assigned with rationale.

---

## Stage 3 — BFSI Domain Interpretation

**Stage ID:** `STAGE-03`
**Execution Order:** Third — requires [CONTEXT_ARTIFACT] + [INTENT_ARTIFACT]
**Gate Status:** Non-Gate (does not block chain execution)

### Responsibility
Map all raw entities and intents to canonical BFSI domain constructs. Apply
business rules. Normalize financial terminology. Declare domain scope.

### Input
- [CONTEXT_ARTIFACT] from Stage 1
- [INTENT_ARTIFACT] from Stage 2

### Mandatory Output: [DOMAIN_ARTIFACT]
```
[DOMAIN_ARTIFACT:
  stage                      : STAGE-03
  resolved_entities          : [ { raw: "<input term>", canonical: "<BFSI entity>" } ]
  business_rules_applied     : [ "<rule ID> — <rule description>" ]
  domains_in_scope           : [ < core-banking | payments | risk-compliance | insurance | credit-assessment | investment | fraud-detection > ]
  cross_domain_dependencies  : [ "<domain A> depends on <domain B> for <reason>" ]
  terminology_normalizations : [ { input: "<term>", normalized: "<BFSI standard term>" } ]
  domain_boundary_violations : [ <any cross-domain authority violations detected> ]
]
```

### Success Criteria
- Every entity from Stage 1 is mapped to a canonical construct.
- All BFSI terminology is normalized.
- Domain boundaries are declared without violations.
- No compliance or risk assessment appears in this artifact.

---

## Stage 4 — Compliance Evaluation  [GATE STAGE]

**Stage ID:** `STAGE-04`
**Execution Order:** Fourth — requires all prior artifacts
**Gate Status:** GATE — NON_COMPLIANT verdict HALTS chain execution

### Responsibility
Evaluate all prior reasoning against applicable financial regulations and
compliance controls. Produce a compliance verdict. Halt the chain if violations
are detected.

### Input
- [CONTEXT_ARTIFACT], [INTENT_ARTIFACT], [DOMAIN_ARTIFACT] from Stages 1–3

### Mandatory Output: [COMPLIANCE_ARTIFACT]
```
[COMPLIANCE_ARTIFACT:
  stage                  : STAGE-04
  regulations_evaluated  : [ "<regulation name>" ]
  checklist_results      : [
    { check_id: "COMP-001", description: "...", result: "PASS|WARN|BLOCK" },
    ...
  ]
  violations_detected    : [ { violation_id: "VIO-XXX", description: "...", severity: "..." } ]
  compliance_verdict     : < COMPLIANT | CONDITIONAL | NON_COMPLIANT >
  audit_trail_reference  : "<UUID>"
  mandatory_notifications: [ "<regulatory body> — <notification type>" ]
  block_reason           : <populated ONLY if compliance_verdict = NON_COMPLIANT>
]
```

### Gate Behaviour
| Verdict          | Chain Action                                       |
|------------------|----------------------------------------------------|
| `COMPLIANT`      | Proceed to Stage 5 immediately                     |
| `CONDITIONAL`    | Proceed to Stage 5 with escalation flag set        |
| `NON_COMPLIANT`  | HALT. Emit COMPLIANCE_BLOCK_REPORT. Stop at Stage 4|

### Success Criteria
- Every applicable regulation from the Regulatory Reference Registry is evaluated.
- Every compliance checklist item has an explicit PASS/WARN/BLOCK result.
- All BLOCK results are accompanied by a violation record.

---

## Stage 5 — Risk Assessment  [GATE STAGE]

**Stage ID:** `STAGE-05`
**Execution Order:** Fifth — requires all prior artifacts
**Gate Status:** GATE — CRITICAL tier triggers mandatory escalation in Stage 6

### Responsibility
Quantify financial, operational, fraud, and credit risk exposure. Apply the
appropriate risk framework from instruction files. Classify the overall risk tier.

### Input
- All prior artifacts from Stages 1–4

### Mandatory Output: [RISK_ARTIFACT]
```
[RISK_ARTIFACT:
  stage                    : STAGE-05
  risk_dimensions_assessed : [
    { type: "<risk type>", method: "<framework>", score: <0-100>, rationale: "..." },
    ...
  ]
  fraud_signals_evaluated  : [
    { signal_code: "FS-XXX", detected: true|false, weight: "HIGH|MEDIUM|CRITICAL" },
    ...
  ]
  risk_score               : <composite score 0-100>
  risk_tier                : < LOW | MEDIUM | HIGH | CRITICAL >
  model_assumptions        : [ "<assumption>" ]
  escalation_required      : < true | false >
  escalation_reason        : <populated only if escalation_required = true>
]
```

### Gate Behaviour
| Risk Tier  | Chain Action                                                         |
|------------|----------------------------------------------------------------------|
| `LOW`      | Proceed to Stage 6 normally                                          |
| `MEDIUM`   | Proceed to Stage 6 with monitoring flag                              |
| `HIGH`     | Proceed to Stage 6; Stage 6 MUST include risk mitigations            |
| `CRITICAL` | Proceed to Stage 6; Stage 6 MUST include mitigations + escalation    |

### Success Criteria
- All applicable risk dimensions are explicitly assessed.
- Fraud signal scan is executed when intent includes DETECT or TRANSACT.
- Risk tier is derived from the composite score, not assumed.
- All model assumptions are documented.

---

## Stage 6 — Decision Reasoning

**Stage ID:** `STAGE-06`
**Execution Order:** Sixth — requires all prior artifacts
**Gate Status:** Non-Gate (does not block chain execution)

### Responsibility
Synthesize all reasoning artifacts into a single, traceable, defensible decision
recommendation. Resolve contradictions. Map decision options. Produce verdict.

### Input
- All prior artifacts from Stages 1–5

### Mandatory Output: [DECISION_ARTIFACT]
```
[DECISION_ARTIFACT:
  stage              : STAGE-06
  synthesis_points   : [ "<alignment or contradiction identified across prior stages>" ]
  decision_options   : [
    { option: "A", compliance_status: "...", risk_tier: "...", domain_alignment: "...", recommended: true|false },
    ...
  ]
  conditions         : [ "<precondition that must hold for decision to be valid>" ]
  mitigations        : [ "<required mitigation action if HIGH/CRITICAL risk>" ]
  traceability_map   : {
    compliance_basis : [ "Stage 4 — COMP-XXX" ],
    risk_basis       : [ "Stage 5 — FS-XXX" ],
    domain_basis     : [ "Stage 3 — <entity>" ],
    intent_basis     : "Stage 2 — <primary_intent>"
  }
  decision_verdict   : < APPROVE | APPROVE_WITH_CONDITIONS | DEFER | REJECT | ESCALATE >
  escalation_path    : <populated if verdict is ESCALATE or risk_tier is CRITICAL>
]
```

### Success Criteria
- All prior artifacts are explicitly referenced in the synthesis.
- Contradictions are resolved using the More Restrictive Rule Wins principle.
- Decision verdict is unambiguous.
- HIGH/CRITICAL risk tier decisions include mandatory mitigations.

---

## Stage 7 — Final Response Generation

**Stage ID:** `STAGE-07`
**Execution Order:** Seventh and final — requires all prior artifacts
**Gate Status:** Non-Gate (terminal stage — produces final output)

### Responsibility
Compose the enterprise-grade final response integrating all chain reasoning
artifacts. Produce a structured, auditable, regulation-aware output suitable for
senior stakeholder review.

### Input
- All prior artifacts from Stages 1–6

### Mandatory Output: Final Structured Response
Contains:
1. Executive Summary
2. Decision Statement (from [DECISION_ARTIFACT])
3. Compliance Summary (from [COMPLIANCE_ARTIFACT])
4. Risk Summary (from [RISK_ARTIFACT])
5. Action Items (numbered, owner-assigned, time-boxed)
6. Chain Metadata Block

### Success Criteria
- All 6 mandatory response sections are present.
- Response tone is institutional and non-conversational.
- No PII or unmasked sensitive data is present.
- Chain metadata block is appended with accurate values from all stages.
- No fabricated regulatory citations are present.
