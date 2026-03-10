# BFSI Advanced POT Reasoning Framework
## Prompt of Thoughts Design Layer — Enterprise BFSI Edition

**Version:** 2.0.0
**Scope:** All BFSI Sub-Domains
**Reasoning Model:** Prompt of Thoughts (POT) v2.0
**Origin:** Boron_v1 + bfsi-meta-prompt.md v1.0.0 — Enhanced with POT Architecture
**Placement:** `Rohith U-v2/Pot_prompting/`

---

## DOCUMENT PURPOSE

This file is the **reasoning design layer** for `bfsi-pot-prompt.md`.
It complements the primary prompt the same way `bfsi-prompt-logic.md`
complements `bfsi-meta-prompt.md` — but at a deeper reasoning stratum.

This document specifies:

1. **What POT Prompting is** and how it differs from Meta Prompting and Chain-of-Thought
2. **The POT Thought Node specification** — canonical structure, types, and sequencing
3. **Adversarial Thought Validation (ATV)** — how challenges and resolutions are structured
4. **Regulatory Thought Mapping (RTM)** — how BFSI regulations flow into thought nodes
5. **Thought Gate contracts** — entry and exit conditions for each gate
6. **Confidence Accumulation model** — mathematical scoring and thresholds
7. **Comparison table** — how each POT technique improves on its Meta Prompt predecessor
8. **Design decisions** and ADR-style rationale for architectural choices

This document contains **only reasoning specifications and design rationale** — 
no implementation code of any kind.

---

## PART I — POT ARCHITECTURE OVERVIEW

### 1.1 What is Prompt of Thoughts (POT)?

Prompt of Thoughts is an advanced prompting architecture that extends
Chain-of-Thought (CoT) and Meta Prompting with three key innovations:

| Dimension           | Chain-of-Thought (CoT)          | Meta Prompting                     | POT (Prompt of Thoughts)              |
|---------------------|---------------------------------|------------------------------------|---------------------------------------|
| Reasoning Location  | Inline, implicit                | Pre-generation decomposition       | Explicit, externalised Thought Nodes  |
| Adversarial Review  | None                            | Post-hoc ASVG (after generation)   | Adversarial-THOUGHT per decision      |
| Confidence Tracking | None                            | Per-file CDS scoring               | Per-thought accumulation; gate-gated  |
| Reasoning Auditability | Partial (inline traces)      | Session-level checkpoint registry  | Full THOUGHT_AUDIT.md audit document  |
| Regulatory Mapping  | None                            | Instruction file scan only         | Active REGULATORY-THOUGHT per rule    |
| Domain Routing      | None                            | DCRE vocabulary scoring            | DOMAIN-THOUGHT with adversarial check |
| Resumability        | None                            | --resume via checkpoint            | THOUGHT-CHECKPOINT per TN + TCRP      |

**POT Core Principle:**
> Every significant decision must be externalised as a Thought Node
> BEFORE it is acted upon. No reasoning is permitted to remain implicit.

This principle separates POT from CoT (where reasoning stays internal)
and from Meta Prompting (where the reasoning engine is structural, not explicit).

---

### 1.2 Three-File POT Model

The BFSI POT framework is distributed across three files with distinct responsibilities:

```
Pot_prompting/
├── bfsi-pot-prompt.md              ← Operational layer (executable directives)
├── bfsi-pot-reasoning-framework.md ← Design layer (this file; reasoning specs)
└── bfsi-pot-thought-chains.md      ← Thought chain library (per-domain templates)
```

| File                             | Role                                                          |
|----------------------------------|---------------------------------------------------------------|
| `bfsi-pot-prompt.md`             | Runtime execution: @prompt, @context, @thought_node_engine, @generation_thought_loop |
| `bfsi-pot-reasoning-framework.md`| Design rationale: POT theory, technique comparisons, ADRs     |
| `bfsi-pot-thought-chains.md`     | Domain thought libraries: pre-built TN sequences per sub-domain |

This expands the two-file model of `bfsi-meta-prompt.md` + `bfsi-prompt-logic.md`
by adding a dedicated **thought chain library** that can be extended without
touching the primary prompt.

---

### 1.3 POT Technique Stack — Rationale

Each POT technique and the problem it solves from its Meta Prompt predecessor:

| POT Technique                          | Abbreviation | Problem Resolved vs. Meta Prompt |
|----------------------------------------|--------------|----------------------------------|
| Thought Node Chain (TNC)               | §POT-1       | Meta Prompt reasoned implicitly; TNC forces every decision to be a traceable node |
| Adversarial Thought Validation (ATV)   | §POT-2       | Meta ASVG ran post-generation; ATV challenges each thought BEFORE the decision is acted upon |
| Regulatory Thought Mapping (RTM)       | §POT-3       | Meta RAI injected regulations structurally; RTM creates an active REGULATORY-THOUGHT per regulation per file |
| Domain-Conditional Thought Router      | §POT-4       | Meta DCRE was a scoring function; DCTR emits an explicit DOMAIN-THOUGHT with adversarial peer review |
| Financial Invariant Thought Gate       | §POT-5       | Meta CTFS ran pre-generation as a scaffold; FITG is a hard gate — generation cannot proceed until the gate passes |
| Confidence-Weighted Thought Accumulator| §POT-6       | Meta CDS scored per-file; POT-6 scores per-thought and applies penalties at the thought level for precision |
| Thought Chain Resumption Protocol      | §POT-7       | Meta PCP was checkpoint-based; TCRP is thought-aware — resumes at the exact Thought Node, not just a phase |
| POT Architecture Instantiation Engine  | §POT-8       | Meta TIE provided canonical entity lists; POT AIE attaches an ARCHITECTURE-THOUGHT to every entity instantiation |

---

## PART II — THOUGHT NODE SPECIFICATIONS

### 2.1 Canonical Thought Node Structure

Every Thought Node follows this exact format:

```
[TN-<ID> | <THOUGHT-TYPE> | Conf: <0.00–1.00>]
┌─ Input State  : <what is established before this thought executes>
├─ Reasoning    : <the explicit step-by-step reasoning process>
│                 Step R1: <first reasoning step>
│                 Step R2: <second reasoning step>
│                 Step R3: <third reasoning step — minimum required>
├─ Adversarial  : <the strongest possible objection to the primary reasoning>
│                 [ADV-TN-<ID>: <objection text>]
├─ Resolution   : <how the adversarial challenge is refuted or integrated>
│                 [RES-TN-<ID>: <resolution text>]
├─ Output State : <what is established after this thought completes>
├─ Rule-IDs     : <@regulations rule IDs verified, or [ASSUMED: ...] tag>
└─ Conf-Delta   : <± confidence adjustment (see scoring model)>
[TN-<ID> CLOSED | Final-Conf: <running total> | Chains-To: TN-<next-ID>]
[THOUGHT-CHECKPOINT: TN-<ID>]
```

**Annotation Protocol:**

| Annotation Tag                   | Meaning                                                   |
|----------------------------------|-----------------------------------------------------------|
| `[TN-<ID>: OPEN]`                | Thought Node has been declared but not yet resolved       |
| `[TN-<ID> CLOSED]`               | Thought Node has completed; output state is fixed         |
| `[THOUGHT-CHECKPOINT: TN-<ID>]`  | Resumption point marker                                   |
| `[THOUGHT-FAULT: HALLUCINATION]` | A fact was emitted without traceable source; chain halted |
| `[THOUGHT-FAULT: BLOCKER]`       | A BLOCKER regulation was violated; chain halted           |
| `[ASSUMED: <fact> | Risk: <H/M/L>]` | Explicit assumption annotation within a thought        |
| `[ADV-TN-<ID>: <objection>]`     | Adversarial challenge within a parent Thought Node        |
| `[RES-TN-<ID>: <resolution>]`    | Resolution of an adversarial challenge                    |
| `[THOUGHT-GATE-<NAME>: PASSED]`  | Gate evaluation succeeded; generation may proceed         |
| `[THOUGHT-GATE-<NAME>: FAILED]`  | Gate evaluation failed; corrective thought required       |

---

### 2.2 Thought Type Taxonomy

Nine thought types govern the complete reasoning process:

#### DOMAIN-THOUGHT
- **Emitted:** Once per session at session start (TN-001)
- **Triggers:** Any user input regardless of format
- **Contract:** Must produce a domain lock before any subsequent node
- **Adversarial requirement:** Mandatory — must challenge domain selection with at least one alternative candidate domain
- **Gate dependency:** Produces THOUGHT-GATE-DOMAIN

#### REGULATORY-THOUGHT
- **Emitted:** Before any file with regulatory implications; at THOUGHT-GATE-REGS
- **Triggers:** Any component that handles money, personal data, or external interfaces
- **Contract:** Must cite at minimum the applicable Rule-IDs from @regulations
- **Adversarial requirement:** Must challenge whether any BLOCKER rule is being silently bypassed
- **Gate dependency:** Contributes to THOUGHT-GATE-REGS and THOUGHT-GATE-FILE-<N>

#### FINANCIAL-THOUGHT
- **Emitted:** Before any monetary arithmetic, precision decision, or ledger entry modelling
- **Triggers:** Any component with BigDecimal fields or currency operations
- **Contract:** Must declare MathContext, RoundingMode, scale, and commutativity
- **Adversarial requirement:** Must challenge edge cases (division, multi-currency, rounding cascade)
- **Gate dependency:** Produces THOUGHT-GATE-FINANCIAL

#### SECURITY-THOUGHT
- **Emitted:** Before any external interface design or sensitive data field classification
- **Triggers:** Any HTTP endpoint, Kafka topic, database column with PII/PCI/PHI
- **Contract:** Must complete a STRIDE analysis fragment for the component
- **Adversarial requirement:** Must challenge whether the threat model is exhaustive
- **Gate dependency:** Produces THOUGHT-GATE-SECURITY

#### ARCHITECTURE-THOUGHT
- **Emitted:** Before every new file, class, and pattern assignment
- **Triggers:** Any structural decision — new file, new package, pattern selection
- **Contract:** Must declare the layer assignment, dependency direction, and periodic-table pattern
- **Adversarial requirement:** Mandatory only when the file introduces a new dependency direction
- **Gate dependency:** Contributes to THOUGHT-GATE-ARCH and THOUGHT-GATE-FILE-<N>

#### COMPLIANCE-THOUGHT
- **Emitted:** At every Thought Gate checkpoint; once per file post-generation
- **Triggers:** Thought Gate evaluation; post-generation file review
- **Contract:** Must produce a pass/fail verdict with explicit evidence for each check
- **Adversarial requirement:** Must challenge whether any gap has been rationalised away rather than fixed
- **Gate dependency:** Determines all gate outcomes

#### ASSUMPTION-THOUGHT
- **Emitted:** Whenever a fact cannot be traced to an instruction file or @regulations
- **Triggers:** Any design decision relying on best-practice inference rather than a cited rule
- **Contract:** Must declare: fact, basis, risk level, impact, mitigation, and confidence delta
- **Adversarial requirement:** Must always challenge whether the assumption creates a compliance gap
- **Gate dependency:** Confidence delta applies immediately; accumulated total checked at every gate

#### ADVERSARIAL-THOUGHT
- **Emitted:** In --challenge mode or whenever a BLOCKER-level regulation is implicated
- **Triggers:** Any BLOCKER regulation, any ARCHITECTURE-THOUGHT in --challenge mode
- **Contract:** Must raise the strongest possible objection; cannot be a strawman argument
- **Resolution requirement:** A RESOLUTION-THOUGHT must follow within the same Thought Node

#### RESOLUTION-THOUGHT
- **Emitted:** As part of the parent Thought Node, immediately after ADVERSARIAL-THOUGHT
- **Triggers:** Every ADVERSARIAL-THOUGHT
- **Contract:** Must either (a) fully refute the adversarial challenge with evidence, or (b) integrate the objection into the Output State, modifying the decision accordingly
- **Gate dependency:** Unresolved adversarial thoughts = −0.10 confidence penalty

---

### 2.3 Mandatory Thought Sequences

#### Greenfield Generation Sequence

```
Session Start
    │
    ▼
TN-001: DOMAIN-THOUGHT
    │
    ├──── THOUGHT-GATE-DOMAIN
    │
    ▼
TN-002: REGULATORY-THOUGHT
    │
    ├──── THOUGHT-GATE-REGS
    │
    ▼
TN-003: FINANCIAL-THOUGHT
    │
    ├──── THOUGHT-GATE-FINANCIAL
    │
    ▼
TN-004: SECURITY-THOUGHT (STRIDE)
    │
    ├──── THOUGHT-GATE-SECURITY
    │
    ▼
TN-005: ARCHITECTURE-THOUGHT
    │
    ├──── THOUGHT-GATE-ARCH
    │
    ▼
THOUGHT-GATE-ENTRY (all pre-gen gates passed?)
    │
    ├──── NO  → corrective TN emitted; loop back
    │
    ▼  YES
Per-file generation loop:
    │
    ├──── TN-<N>: ARCHITECTURE-THOUGHT (before each file)
    │       ├──── COMPLIANCE-THOUGHT (regulatory pre-flight)
    │       ├──── Generate file content
    │       └──── THOUGHT-GATE-FILE-<N>
    │
    ▼
TN-FIN: COMPLIANCE-THOUGHT (post-generation review)
    │
    ├──── THOUGHT-GATE-FINAL
    │
    ▼
Emit THOUGHT_AUDIT.md + COMPLIANCE_MATRIX.md
Session End
```

#### Review Mode Sequence

```
Session Start
    │
    ▼
TN-001: DOMAIN-THOUGHT (detect domain from existing codebase)
    │
    ▼
TN-002: REGULATORY-THOUGHT (enumerate applicable regulations)
    │
    ▼
TN-REV-*: COMPLIANCE-THOUGHT × N (one per file reviewed)
    │
    ▼
TN-FIN: COMPLIANCE-THOUGHT (aggregate review findings)
    │
    ▼
Emit 110-Point POT Compliance Report
```

#### Audit Mode Sequence

```
TN-001: DOMAIN-THOUGHT
TN-002: REGULATORY-THOUGHT
TN-AUD-*: REGULATORY-THOUGHT per regulation (gap analysis)
Emit: Regulatory Gap Analysis with thought-chain evidence
```

---

## PART III — ADVERSARIAL THOUGHT VALIDATION (ATV)  [§POT-2]

### 3.1 ATV Design Rationale

The Meta Prompt's Adversarial Self-Validation Gate (ASVG) ran **post-generation**:
the code was produced first, then reviewed for violations. This is a shift-right
approach that allows regulatory violations to propagate into generated artefacts
before detection.

ATV shifts this validation to the thought level, making it **shift-left**:
the adversarial challenge must be resolved BEFORE the decision is acted upon.
No file may be generated whose ARCHITECTURE-THOUGHT contains an unresolved adversarial challenge.

### 3.2 Adversarial Challenge Taxonomy

| Challenge Type             | Scope                                     | When Raised                          |
|----------------------------|-------------------------------------------|--------------------------------------|
| Regulatory Bypass Challenge | Any REGULATORY-THOUGHT                   | When a BLOCKER rule is mapped        |
| Architectural Validity Challenge | Any ARCHITECTURE-THOUGHT            | When a new layer or dependency is introduced |
| Financial Precision Challenge | Any FINANCIAL-THOUGHT                  | When a monetary formula is declared  |
| Assumption Validity Challenge | Any ASSUMPTION-THOUGHT                 | Always — every assumption is challenged |
| Domain Correctness Challenge | TN-001 DOMAIN-THOUGHT                  | When vocabulary score is < 0.8      |
| Pattern Justification Challenge | Any ARCHITECTURE-THOUGHT with patterns | Always                            |

### 3.3 Challenge Strength Standard

An adversarial challenge is valid only if it meets the **Steel Man Standard**:
the objection raised must be the strongest possible version of the counter-argument,
not a strawman. A challenge that can be trivially dismissed is itself a THOUGHT-FAULT.

Minimum challenge strength criteria:
1. The challenge must identify a specific failure mode (not a generic "could be wrong")
2. The challenge must reference a specific regulation, design principle, or failure scenario
3. The challenge must propose an alternative or identify what would need to be true for the primary reasoning to fail

### 3.4 Resolution Quality Standard

A RESOLUTION-THOUGHT is valid only if it:
1. Directly addresses the specific failure mode raised
2. Either provides evidence that the failure mode is prevented, OR
3. Modifies the Output State of the parent Thought Node to guard against the failure mode

A resolution that simply re-asserts the primary reasoning without addressing the
adversarial challenge is a THOUGHT-FAULT → −0.10 confidence penalty applied.

---

## PART IV — REGULATORY THOUGHT MAPPING (RTM)  [§POT-3]

### 4.1 RTM vs. Meta Regulatory Awareness Injection (RAI)

| Dimension              | Meta RAI                              | POT RTM                                                 |
|------------------------|---------------------------------------|---------------------------------------------------------|
| When applied           | Once, via @regulations block scan    | Once per applicable regulation per component            |
| Depth of mapping       | Artefact assignment table             | Active REGULATORY-THOUGHT with adversarial validation   |
| Bypass detection       | Post-generation ASVG                 | Adversarial challenge within the REGULATORY-THOUGHT     |
| Confidence impact      | None                                  | BLOCKER rule correctly applied → +0.02 confidence       |
| Failure mode           | Silent omission possible             | Omission is THOUGHT-FAULT; halts generation             |

### 4.2 Regulatory Thought Node Protocol

For each regulation in @regulations active for the detected domain:

```
[TN-REG-<RULE-ID> | REGULATORY-THOUGHT | Conf: <x>]
┌─ Input State  : Domain confirmed. Component <name> is being designed.
│                 Regulation <RULE-ID> is active for this domain.
│
├─ Reasoning    :
│   R1: What does <RULE-ID> require at the code level?
│   R2: Which Scala component is the primary enforcement point?
│   R3: How does the enforcement prevent silent bypass?
│
├─ Adversarial  :
│   [ADV-REG-<RULE-ID>: Could this regulation be satisfied by contract
│    but violated in practice? What edge case violates the enforcement?]
│
├─ Resolution   :
│   [RES-REG-<RULE-ID>: Guard against the edge case by <specific mechanism>]
│
├─ Output State : Regulation <RULE-ID> mapped to <Scala artefact>.
│                 Bypass guard: <mechanism declared>.
│
└─ Rule-IDs     : <RULE-ID>
   Conf-Delta   : +0.02 (BLOCKER correctly mapped)
[TN-REG-<RULE-ID> CLOSED | Final-Conf: <value>]
```

### 4.3 BFSI Regulatory Thought Coverage Checklist

For each domain, the following regulations warrant dedicated REGULATORY-THOUGHTs:

**All Domains (Universal):**
- FM-001, FM-002, FM-004, FM-005 (monetary invariants)
- SEC-001 through SEC-007 (security blockers)
- GDPR-001, GDPR-003 (data erasure + minimisation)

**Payments Domain (additional):**
- AML-001, AML-002, AML-003, AML-004
- PSD2-001, PSD2-002
- KYC-001, KYC-002

**Core Banking Domain (additional):**
- DORM-001, DORM-002
- KYC-001, KYC-002, KYC-003
- AML-001, AML-003

**Risk & Compliance Domain (additional):**
- All AML-* rules
- All KYC-* rules
- GDPR-001 through GDPR-004

**Capital Markets Domain (additional):**
- DORA-001, DORA-002
- All FM-* rules with FRTB context

**Insurance Domain (additional):**
- IFRS9-01, IFRS9-02
- GDPR-001 through GDPR-004

**Treasury Domain (additional):**
- DORA-001, DORA-002
- FM-003 (multi-currency ISO-4217)

---

## PART V — CONFIDENCE-WEIGHTED THOUGHT ACCUMULATION  [§POT-6]

### 5.1 Confidence Model

Session confidence is a running value C ∈ [0.0, 1.0]:

```
C_start = 1.00

Per Thought Node:
  C_n = C_{n-1} + Σ(Conf-Delta of all events within TN-n)

Events and deltas:
  ASSUMPTION-THOUGHT emitted (HIGH risk)      : −0.08
  ASSUMPTION-THOUGHT emitted (MED risk)       : −0.03
  ASSUMPTION-THOUGHT emitted (LOW risk)       : −0.01
  ADVERSARIAL unresolved (no RESOLUTION-TN)   : −0.10
  ADVERSARIAL resolved with valid resolution  :  0.00 (no penalty)
  BLOCKER regulation correctly mapped         : +0.02
  THOUGHT-GATE passed                         : +0.03
  THOUGHT-GATE failed (corrective TN applied) : −0.15
  HALLUCINATION-FAULT (generation halted)     : −∞ (session terminated)

Thresholds:
  C ≥ 0.85 → CONFIDENT (generation proceeds freely)
  C ≥ 0.70 → CAUTIOUS  (generation proceeds; ASSUMPTION-THOUGHTs flagged)
  C < 0.70 → ENRICHMENT-REQUIRED (generation halted)
  C < 0.50 → CRITICAL  (session terminated; THOUGHT_AUDIT.md emitted immediately)
```

### 5.2 Confidence Gate Enforcement

| Gate Name              | Minimum Confidence to Pass | Consequence of Failure         |
|------------------------|----------------------------|--------------------------------|
| THOUGHT-GATE-DOMAIN    | 0.90                       | Emit ASSUMPTION-THOUGHT; loosen to 0.75 |
| THOUGHT-GATE-REGS      | 0.85                       | Identify unresolved BLOCKER; correct   |
| THOUGHT-GATE-FINANCIAL | 0.85                       | Emit corrective FINANCIAL-THOUGHT       |
| THOUGHT-GATE-SECURITY  | 0.80                       | Emit additional SECURITY-THOUGHT        |
| THOUGHT-GATE-ARCH      | 0.80                       | Refactor architectural decision         |
| THOUGHT-GATE-ENTRY     | 0.75                       | All pre-gen gates must pass first       |
| THOUGHT-GATE-FILE-<N>  | 0.75                       | Corrective COMPLIANCE-THOUGHT; re-emit  |
| THOUGHT-GATE-FINAL     | 0.85                       | Cannot close session below this value   |

---

## PART VI — THOUGHT CHAIN RESUMPTION PROTOCOL (TCRP)  [§POT-7]

### 6.1 TCRP vs. Meta Prompt Chaining Protocol (PCP)

The Meta Prompt's PCP provided `--resume` by storing checkpoint tags at the
phase level (SPU-1 through SPU-8). Resumption meant starting from the last
uncompleted SPU.

TCRP improves on PCP in three ways:

1. **Granularity:** Resumption is at the Thought Node level, not the phase level.
   A session can resume from TN-023 even within a multi-file generation phase.

2. **Confidence inheritance:** The resuming session inherits the accumulated
   confidence score from all prior thought nodes. It does not reset to 1.00.

3. **Adversarial state preservation:** Open adversarial challenges are preserved
   across resumption. A challenge raised in TN-015 that had not been fully
   resolved before the session ended will be the first item addressed in the
   resumed session.

### 6.2 TCRP Resumption Protocol

On --resume activation:

```
STEP 1: Scan session transcript for all [THOUGHT-CHECKPOINT: TN-<ID>] tags.
STEP 2: Identify the highest TN-ID checkpoint (last completed thought).
STEP 3: Reconstruct C_n (session confidence) by replaying all Conf-Delta values.
STEP 4: Identify any open adversarial challenges ([ADV-TN-<ID>] without matching [RES-TN-<ID>]).
STEP 5: Emit [THOUGHT-RESUME: from TN-<next-ID> | Inherited-Conf: <C_n> | Open-Challenges: <n>]
STEP 6: Resolve any open adversarial challenges first.
STEP 7: Continue the thought chain from the next unprocessed TN.
```

---

## PART VII — DOMAIN-SPECIFIC REASONING EXTENSIONS

### 7.1 Payments Domain Reasoning Extensions

For the payments domain, two additional REGULATORY-THOUGHT nodes are mandatory
that are not present in other domains:

**SCA-THOUGHT (PSD2-001):**
The FINANCIAL-THOUGHT for any payment instruction must include a sub-thought
confirming that Strong Customer Authentication is applied unless an explicit
exemption is declared (amount < €30, recurring, trusted beneficiary).
Every exemption requires an ASSUMPTION-THOUGHT if not covered by instruction files.

**INSTANT-THOUGHT (SEPA Instant):**
For SepaInstantPayment, a specialised FINANCIAL-THOUGHT is required declaring
that the 10-second processing window constraint is modelled as a CutOffTime
specification, and that any timeout path emits a FAILED state transition event.

### 7.2 Risk & Compliance Domain Reasoning Extensions

**VELOCITY-THOUGHT:**
Before generating the HalogenAmlValidatorChain, a dedicated FINANCIAL-THOUGHT
for velocity analysis must declare: the time window (1 h), the count threshold (≥ 5),
and the accumulator strategy (sliding window vs. tumbling window).

**STRUCTURING-THOUGHT:**
The CarbonStructuringDetectionStrategy requires a FINANCIAL-THOUGHT that
declares the sub-threshold window (e.g., 5 × transactions < €10 000 over 48 h = structuring flag)
and the lookback period used for pattern detection.

### 7.3 Insurance Domain Reasoning Extensions

**IFRS17-THOUGHT:**
The LanthanideIfrs17ValuationPipeline requires a specialised FINANCIAL-THOUGHT
declaring the CSM (Contractual Service Margin) and RA (Risk Adjustment) calculation
basis, the discount rate source, and the cohort grouping strategy.

**PREMIUM-THOUGHT:**
Before any premium calculation component, a FINANCIAL-THOUGHT must declare
the actuarial basis (loss ratio, expense ratio, combined ratio), rounding
methodology, and reserve adequacy test threshold.

### 7.4 Capital Markets Domain Reasoning Extensions

**FRTB-THOUGHT:**
Before the LanthanideFrtbCapitalCalculationPipeline, a specialised
FINANCIAL-THOUGHT must declare the SA (Standardised Approach) vs.
IMA (Internal Model Approach) selection, sensitivity risk factor mapping,
and the back-testing bucket classification logic.

**MTM-THOUGHT:**
For mark-to-market valuation, a FINANCIAL-THOUGHT declares the pricing
hierarchy (Level 1 / Level 2 / Level 3 under IFRS 13), the day-count
convention, and the fair value adjustment (CVA/DVA) modelling basis.

---

## PART VIII — THOUGHT AUDIT DOCUMENT (THOUGHT_AUDIT.md)

### 8.1 Purpose and Relationship to COMPLIANCE_MATRIX.md

`THOUGHT_AUDIT.md` is the POT equivalent of `COMPLIANCE_MATRIX.md`.
They serve different audiences:

| Document               | Audience                  | Content                                              |
|------------------------|---------------------------|------------------------------------------------------|
| `COMPLIANCE_MATRIX.md` | Compliance Officers        | Regulation → Code artefact mapping table             |
| `THOUGHT_AUDIT.md`     | AI Governance Auditors     | Full reasoning trace: every thought, every decision  |

Both documents must be emitted at session end. `THOUGHT_AUDIT.md` contains
`COMPLIANCE_MATRIX.md` as a sub-section (with thought-chain provenance added).

### 8.2 Thought Audit Quality Standard

The THOUGHT_AUDIT.md is considered high-quality if:

1. **Completeness:** Every decision visible in the generated files has a traceable
   Thought Node. No file artefact exists whose generation is not authorised by
   an ARCHITECTURE-THOUGHT in the audit trail.

2. **Regulatory provenance:** Every BLOCKER and CRITICAL regulation applied
   has a REGULATORY-THOUGHT that cites the specific Rule-ID AND demonstrates
   the enforcement mechanism.

3. **Assumption transparency:** Every ASSUMPTION-THOUGHT has a risk level,
   an impact statement, and a mitigation. No assumption is buried.

4. **Adversarial coverage:** Every ADVERSARIAL-THOUGHT raised during the session
   is present in the audit, whether resolved or escalated.

5. **Confidence traceability:** Every confidence delta is recorded; the final
   session confidence can be independently recalculated from the audit trail.

---

## PART IX — IMPROVEMENT RATIONALE VS. BORON_V1 AND META PROMPT

### 9.1 Key Improvements Over Boron_v1

| Boron_v1 Limitation                       | POT Resolution                                          |
|-------------------------------------------|---------------------------------------------------------|
| Monolithic @instructions block            | Decomposed into atomic Thought Nodes per decision       |
| Post-generation review loop               | Adversarial Thought Validation before each file is emitted |
| Implicit domain knowledge acquisition     | Explicit DOMAIN-THOUGHT with vocabulary scoring trace   |
| CDS per-file confidence only              | POT-6 confidence at thought level; compound penalties   |
| No resumption within a generation phase   | TCRP resumes at Thought Node granularity                |
| ASVG ran once post-generation             | ATV ran per decision, before action                     |
| Pattern selection undocumented reasoning  | ARCHITECTURE-THOUGHT documents pattern rationale        |
| Regulation mapping was a static table     | REGULATORY-THOUGHT per rule per file; adversarially checked |

### 9.2 Key Improvements Over bfsi-meta-prompt.md

| Meta Prompt Limitation                    | POT Resolution                                          |
|-------------------------------------------|---------------------------------------------------------|
| SPU decomposition was structural only     | POT decomposes into reasoning units (thought nodes)     |
| CTFS scaffold ran pre-generation once     | FINANCIAL-THOUGHT runs per monetary component           |
| ASVG was post-hoc                         | ATV is pre-action (before each file)                    |
| Confidence was a session-level metric     | Confidence accumulates per thought; gates enforce floors|
| Thought chain was implicit in session     | Explicit thought chain produces THOUGHT_AUDIT.md        |
| --resume was phase-level                  | TCRP is thought-node-level                              |
| No domain-wide adversarial pass           | --challenge mode activates adversarial check on every TN|
| RAI was a block-level injection           | RTM is an active thought per regulation per component   |

### 9.3 Architecture Decision Records (ADRs)

**ADR-001: Three-File Model over Two-File Model**
- Context: Meta Prompt uses two files (operational + design). POT adds complexity.
- Decision: Add a third file (bfsi-pot-thought-chains.md) for per-domain thought libraries.
- Rationale: Domain-specific thought patterns grow over time; separating them from
  the primary prompt prevents the operational file from becoming monolithic.
- Consequence: Thought chain library is independently extensible without modifying
  the primary prompt.

**ADR-002: Per-Decision ATV over Monolithic ASVG**
- Context: Meta ASVG was a single post-generation validation pass.
- Decision: Replace with per-decision Adversarial Thought Validation.
- Rationale: Shift-left principle — detect violations at the reasoning stage,
  not after code has been generated and would need to be discarded.
- Consequence: Slightly higher token cost per session; significantly reduced
  iteration count for multi-turn sessions.

**ADR-003: Confidence as a Thought-Level Metric**
- Context: CDS scored at file level; coarse-grained.
- Decision: Apply confidence deltas at the individual Thought Node level.
- Rationale: Fine-grained confidence allows early detection of sessions trending
  toward the 0.70 threshold before BLOCKER-level failures accumulate.
- Consequence: Sessions are terminated earlier in a deteriorating confidence
  trajectory, preventing generation of low-confidence artefacts.

**ADR-004: Extend Pattern Registry with POT-Specific Patterns**
- Context: Boron_v1 Periodic Table registry covers standard OO/functional patterns.
- Decision: Add SiliconDomainThoughtRouter and PhosphorusComplianceThoughtGate
  as first-class patterns in the registry.
- Rationale: The POT routing and compliance layers are architectural concerns
  with genuine implementation responsibilities; they belong in the registry.
- Consequence: Two new periodic-table prefixes are assigned; all future POT
  prompts in the BFSI framework must honour these prefixes.

---

## PART X — GOVERNANCE & VERSIONING

### 10.1 Document Hierarchy within Rohith U-v2

```
Priority (highest → lowest):
1. bfsi-pot-prompt.md              (runtime operational directives)
2. bfsi-pot-reasoning-framework.md (design rationale; informs prompt changes)
3. bfsi-pot-thought-chains.md      (domain libraries; extensible)
4. .github/instructions/           (domain-specific entity and rule sources)
5. @regulations block              (universal BFSI regulatory baseline)
6. ASSUMPTION-THOUGHTs             (session-specific declared assumptions)
```

### 10.2 Versioning Strategy

| Version Component   | Trigger for increment                                          |
|---------------------|----------------------------------------------------------------|
| Major (X.0.0)       | Change to POT technique stack or mandatory thought sequence    |
| Minor (x.Y.0)       | Addition of new thought types, domain templates, or ADRs       |
| Patch (x.y.Z)       | Clarifications, confidence score adjustments, gate threshold changes |

### 10.3 Change Control Protocol

Changes to `bfsi-pot-prompt.md` require:
- A new ADR in Section 9.3 of this file
- Version bump in the YAML front matter of `bfsi-pot-prompt.md`
- Update to the Thought Audit specification if the change affects THOUGHT_AUDIT.md

Changes to `bfsi-pot-thought-chains.md` do not require ADR entries but do
require an update to the domain template change log at the top of that file.
