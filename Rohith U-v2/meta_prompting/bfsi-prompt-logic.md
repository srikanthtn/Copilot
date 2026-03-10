# BFSI Prompt Logic Framework
## Improved Prompt Logic Derived from Boron_v1 — Advanced Meta Prompting Edition

**Version:** 1.0.0  
**Scope:** All BFSI Sub-Domains  
**Origin:** Boron_v1 `code-generation-master.md` + `code-generator.prompt.md` v13.0.0  
**Placement:** `Rohith U-v2/meta_prompting/`

---

## DOCUMENT PURPOSE

This file defines the **Prompt Logic** that powers `bfsi-meta-prompt.md`.
It is the authoritative specification for:

1. How the Meta Prompt decomposes BFSI tasks into atomic units
2. How each Sub-Prompt Unit (SPU) is structured internally
3. How prompt-to-prompt handoffs are managed (Prompt Chaining Protocol)
4. How domain-conditional routing selects instruction paths
5. How adversarial self-validation prevents hallucination and regulatory drift
6. How confidence is accumulated and thresholded across the session

This file contains **only prompt logic specifications and decision tables** —
no implementation code of any kind.

---

## PART I — PROMPT ARCHITECTURE OVERVIEW

### 1.1 Two-File Meta Prompt Model

The BFSI Meta Prompt is split across exactly two files:

```
meta_prompting/
├── bfsi-meta-prompt.md      ← Primary Meta Prompt (executable directives)
└── bfsi-prompt-logic.md     ← Prompt Logic Framework (this file; design rationale)
```

`bfsi-meta-prompt.md` is the **operational layer**: it contains the `@prompt`,
`@context`, `@instructions`, `@constraints`, and `@metadata` blocks that drive
AI generation at runtime.

`bfsi-prompt-logic.md` (this file) is the **design layer**: it specifies the
reasoning rules, routing logic, and prompt-chaining contracts that `bfsi-meta-prompt.md`
implements. It also records the design decisions made when diverging from or
improving on the Boron_v1 architecture.

---

### 1.2 Meta Technique Stack — Rationale

The eight Meta Prompting techniques employed and the reason each was chosen:

| Technique | Abbreviation | Problem Solved from Boron_v1 |
|-----------|-------------|------------------------------|
| Prompt Decomposition Matrix | PDM | Boron_v1 was monolithic; one large @instructions block caused drift in long sessions |
| Domain-Conditional Routing Engine | DCRE | Boron_v1's domain detection happened inline; isolated routing engine prevents mis-detection |
| Regulatory Awareness Injection | RAI | Boron_v1 relied on instruction-file scan only; baseline @regulations block adds a safety net |
| Adversarial Self-Validation Gate | ASVG | Boron_v1's review loop was post-hoc; ASVG runs before file emission — shift-left validation |
| Prompt Chaining Protocol | PCP | Boron_v1 had no --resume capability; PCP adds explicit checkpoint handoff for multi-turn |
| Template Instantiation Engine | TIE | Boron_v1's default fallback was generic SEPA; TIE provides canonical templates for all 7 domains |
| Chain-of-Thought Financial Scaffold | CTFS | Boron_v1 went straight to code; CTFS forces reasoning about monetary invariants before generation |
| Confidence Declaration System | CDS | Boron_v1 had no per-file confidence; CDS adds transparent scoring with penalty accumulation |

---

## PART II — SUB-PROMPT UNIT SPECIFICATIONS

Each Sub-Prompt Unit (SPU) is a logically encapsulated prompt fragment.
SPUs are self-contained: each one receives a defined input and produces a
defined output. They are sequenced by the Prompt Chaining Protocol.

---

### SPU-1 — Domain Orientation Prompt

**Purpose:** Establish the BFSI sub-domain, load vocabulary, ingest knowledge.

**Input:**
  - Raw task string from user
  - `.github/instructions/` directory tree

**Internal Logic:**
```
1. TOKENISE task string
2. SCORE each domain using vocabulary overlap (§ Domain Router)
3. SELECT max-score domain (ties → prefer more specific child domain)
4. LOAD cross-domain parent vocabularies if applicable
5. INGEST .github/instructions/<domain>/*.md files
6. RECORD domain decision (immutable for session)
```

**Output Contract:**
```
[DOMAIN-DETECTED: <domain> | Confidence: 0.xx | Tokens: <list>]
[CROSS-DOMAIN-LOADED: <parent-domains>]
[PACKAGE-ROOT: com.bank.<domain-slug>]
[KNOWLEDGE-INGESTED: <file> | Concepts: <n>] × M
```

**Failure Modes:**
| Condition | Action |
|-----------|--------|
| Vocabulary overlap < 0.5 for all domains | Default to `payments`; emit [DOMAIN-ASSUMED:] |
| .github/instructions/ absent | Activate FALLBACK baseline; emit [FALLBACK-ACTIVATED:] |
| Conflicting domain signals (tie) | Choose child domain over parent; document in [CONFLICT-RESOLVED:] |

---

### SPU-2 — Threat & Compliance Modeller

**Purpose:** Apply STRIDE threat model and map regulations to code components
before any code is generated. Prevents security afterthoughts.

**Input:**
  - Domain decision from SPU-1
  - Package topology (abstract, pre-generation)
  - @regulations block from Meta Prompt

**Internal Logic — STRIDE Application:**
```
For each external interface / component:
  S — Spoofing       : Is authentication required? → AuthenticationToken
  T — Tampering      : Is integrity check required? → Immutable case class + event log
  R — Repudiation    : Is audit trail required? → NitrogenAuditObserver
  I — Info Disclosure: Is encryption required? → TransitionEncryptionDecorator
  D — Denial of Srv  : Rate limiting needed? → Token-bucket rate limiter
  E — Elevation      : RBAC/ABAC required? → RBAC enforcement
```

**Regulatory Mapping Table (produced by SPU-2):**

| Active Regulation | Clause | Scala Component Assigned |
|-------------------|--------|--------------------------|
| GDPR Art.32       | Encryption at rest + transit | TransitionEncryptionDecorator |
| GDPR Art.17       | Right to Erasure | ActinideRetentionRegistry |
| PCI-DSS Req.3.4   | PAN masking | TransitionPanMaskDecorator |
| AML-001 (6AMLD)   | €10 000 threshold | HalogenAmlValidatorChain |
| KYC-001           | ACTIVE + VERIFIED | PostTransitionAccountLifecycle |
| PSD2-SCA RTS      | Authentication token | AuthenticationToken value object |
| DORA Art.9        | Incident response | NitrogenIncidentObserver |
| FM-001            | BigDecimal precision | Money.scala DECIMAL128 |
| FM-005            | Double-entry balance | HalogenLedgerBalanceChain |

**Output Contract:**
```
[THREAT-MODEL: <n> threats identified | <n> controls assigned]
[REGULATORY-MAP: <n> regulations | <n> components]
```

---

### SPU-3 — Architecture Blueprint Prompt

**Purpose:** Define the full project skeleton — package topology, layer
boundaries, bounded context names, and DDD aggregate map.

**Input:**
  - Domain from SPU-1
  - Canonical template for that domain (§ Template Instantiation Engine)
  - Architectural constraints from @architecture block

**Internal Logic:**
```
1. SELECT canonical entity list from TIE template for detected domain
2. ASSIGN each entity to a DDD layer (domain/application/infrastructure)
3. DEFINE bounded context package: com.bank.<domain>/
4. VERIFY dependency direction: domain ← application ← infrastructure
5. ASSIGN ≥ 2 design patterns per bounded context
6. GENERATE folder tree with all path names (no code yet)
```

**Output Contract:**
```
[ARCHITECTURE-DEFINED: <n> packages | <n> aggregates | <n> patterns]
[FOLDER-TREE: ... <tree listing> ...]
```

**Design Pattern Assignment Rules:**
```
domain/model/     → AlkaliFactory + BoronBuilder (creation cluster; mandatory)
domain/services/  → HalogenChain (validation) + CarbonStrategy (pluggable logic)
application/      → LanhanidePipeline + AlkalineEarthSaga
infrastructure/   → ChalcogenRepository + TransitionDecorator (encryption)
security/         → TransitionDecorator + NitrogenObserver
```

---

### SPU-4 — Domain Model Generator

**Purpose:** Generate all domain model files — value objects, aggregates,
domain events, and specification implementations.

**Generation Order (dependency-safe):**
```
1. Sealed trait enumerations (Currency, Direction, AccountStatus, DomainError)
2. Value objects (Money, Iban, Bic, AccountId, ...)
3. Domain Events (sealed trait hierarchy)
4. Specification interfaces and implementations
5. Aggregate root entities (BankAccount, SepaCreditTransfer, ...)
```

**Per-File Checklist (CTFS pre-flight, SPU-4 scope):**
```
□ F1: Business invariants identified and mapped to require()/Either
□ F2: All active @regulations rules cited with Rule-ID in file header
□ F3: Monetary arithmetic declared with MathContext + RoundingMode
□ F4: State machine transition graph declared before ADT generation
□ F5: All assumption risks catalogued with [ASSUMED:] + Conf-Δ
□ QA-all: Self-validation gate (SPU-8 equiv.) run before file emission
```

---

### SPU-5 — Application & Pipeline Generator

**Purpose:** Generate CQRS command/query handlers, Spark batch/streaming jobs,
saga orchestrators, and workflow coordinators.

**CQRS Implementation Contract:**
```
Commands (write side):
  → Each command: case class + Either[DomainError, Event] return
  → Idempotency key on every command that mutates state (FM-006)
  → Delegate all business rules to domain services (no logic in handlers)

Queries (read side):
  → Read-optimised; no mutation
  → Return Option[A] or List[A]; never throw

Spark Jobs:
  → Dataset[T] with explicit Encoder[T]; NO DataFrame without type
  → AQE enabled; Delta Lake for sink writes
  → Partitioning declared before job generation
  → Error handling: Either-based row-level errors accumulated in accumulator
```

**Saga Specification:**
```
Pattern: AlkalineEarthSaga (Group 2 — Alkaline-Earth metals)
Saga steps are compensatable; each step declares:
  → forward action (normal path)
  → compensation action (rollback on downstream failure)
Saga emits domain events at each step boundary for audit trail.
```

---

### SPU-6 — Infrastructure Generator

**Purpose:** Generate repository implementations, Spark I/O adapters,
Kafka producers/consumers, external API clients, and configuration.

**Resilience Contracts:**
```
External calls: Circuit Breaker (Resilience4j) + exponential backoff + jitter
Repository:     ChalcogenRepository interface (port) + concrete adapter (adaptor)
Kafka:          Exactly-once semantics via idempotency key (FM-006)
Configuration:  Typesafe Config / Pureconfig; no hardcoded values (SEC-004)
```

**Spark I/O Rules:**
```
Reader: Explicit schema (StructType); no schema inference in production
Writer: Parquet/ORC + Snappy; Delta Lake MERGE for idempotent upserts
Partitioning: Hash by domain key (customerId, transactionDate)
Error handling: Dead-letter topic or quarantine table for malformed records
```

---

### SPU-7 — Security & Compliance Layer Generator

**Purpose:** Generate encryption decorators, PII masking, audit observers,
authentication/authorisation, and rate limiters.

**Security Artefact Map:**
```
TransitionEncryptionDecorator → wraps any aggregate field with AES-256-GCM
TransitionPanMaskDecorator    → masks PAN to last 4 digits in toString/toJson
NitrogenAuditObserver         → appends immutable audit record on every mutation
ActinideRegulatoryRuleRegistry→ singleton: loads active @regulations rules at startup
AlkaliAuthServiceFactory      → creates OAuth2 token validators per domain
```

**PII Classification & Treatment:**
```
┌────────────────┬────────────────┬─────────────────┬────────────────┬──────────┐
│ Classification │ Examples       │ Encryption      │ Logging Rule   │ Retention│
├────────────────┼────────────────┼─────────────────┼────────────────┼──────────┤
│ PCI            │ PAN, CVV, Exp. │ Tokenise        │ NEVER log      │ 90 days  │
│ PII            │ Name, IBAN     │ AES-256-GCM     │ Masked only    │ 7 years  │
│ PHI            │ Health records │ AES-256-GCM     │ NEVER log      │ Per regs │
│ Confidential   │ Balances, Txns │ TLS in transit  │ Audit only     │ 10 years │
│ Internal       │ System config  │ Env-based       │ INFO level     │ 1 year   │
│ Public         │ Exchange rates │ None required   │ Any level      │ Indef.   │
└────────────────┴────────────────┴─────────────────┴────────────────┴──────────┘
```

---

### SPU-8 — Test & Documentation Generator

**Purpose:** Generate ScalaTest/ScalaCheck test suites, COMPLIANCE_MATRIX.md,
ADRs, README.md, SECURITY.md, and RUNBOOK.md.

**Test Generation Priority Order:**
```
1. BLOCKER rule tests (AML threshold, money precision, PAN masking)
2. State machine transition tests (including forbidden transitions)
3. Property-based tests for monetary operations
4. Domain aggregate unit tests
5. Spark job integration tests
6. Security decorator tests
7. Contract tests (schema compatibility)
8. Mutation testing edge cases
```

---

## PART III — PROMPT CHAINING PROTOCOL (PCP)  [§META-5]

### 3.1 Handoff Contract

Each SPU receives a **Prompt Context Bundle** from its predecessor and
produces a **Result Bundle** for its successor.

```
┌──────┬───────────────────────────────────────────────────────────────────────┐
│ SPU  │ Receives from Predecessor                                              │
├──────┼───────────────────────────────────────────────────────────────────────┤
│ SPU-1│ Raw task string (from user)                                            │
│ SPU-2│ DomainDecision + PackageRoot + KnowledgeBundle from SPU-1             │
│ SPU-3│ ThreatModel + RegulatoryMap + DomainDecision from SPU-2               │
│ SPU-4│ ArchitectureBlueprint + PatternAssignments from SPU-3                 │
│ SPU-5│ GeneratedDomainModels + CommandQueryMap from SPU-4                    │
│ SPU-6│ PortInterfaces + SparkJobSpecs from SPU-5                             │
│ SPU-7│ AllGeneratedFiles + SecurityRequirements from SPU-6                   │
│ SPU-8│ CompleteCodebase + RegulatoryMap + AssumptionLedger from SPU-7        │
└──────┴───────────────────────────────────────────────────────────────────────┘
```

### 3.2 Checkpoint Registry

Each SPU records its outputs in the **Checkpoint Registry** — a sequential log
of completed artefacts. The registry enables `--resume` mode.

```
[CHECKPOINT: SPU-<n> complete | Artefacts: <list> | Timestamp: <utc>]
```

**Registry Entry Types:**
```
[GENERATED-FILE: <path> | Domain: <domain> | Confidence: 0.xx]
[REGULATORY-MAP: <n> rules | Version: <date>]
[THREAT-MODEL: complete | Threats: <n> | Controls: <n>]
[ARCHITECTURE-DEFINED: <domain> | Packages: <n>]
[SESSION-RESUMED: from checkpoint | Skipped: <n> files]
```

### 3.3 Multi-Turn Session Rules

```
Rule PCP-1: Domain decision locked in SPU-1 CANNOT change mid-session.
Rule PCP-2: Checkpoint registry is APPEND-ONLY; entries never deleted.
Rule PCP-3: --resume skips all registered [GENERATED-FILE:] entries.
Rule PCP-4: On domain change request mid-session → start new session; do not
            mutate the current session's DomainDecision.
Rule PCP-5: Assumption ledger accumulates across ALL turns in a session.
Rule PCP-6: Confidence values from prior turns inform current file scoring.
```

---

## PART IV — CONFIDENCE DECLARATION SYSTEM (CDS)  [§META-8]

### 4.1 Scoring Model

Every generated file carries a confidence score computed as follows:

```
Base score           : 1.00
Penalties (cumulative):
  −0.05 per HIGH-risk assumption
  −0.02 per MED-risk assumption
  −0.00 per LOW-risk assumption
  −0.05 per stale regulatory reference (> 18 months)
  −0.03 per unresolvable cross-domain rule overlap
  −0.10 if domain stays UNKNOWN after full routing cascade
  −0.02 per public method missing type annotation

Confidence cannot exceed the score of the lowest-confidence source used.
```

### 4.2 Score Bands & Gate Behaviour

```
┌──────────────┬────────────────────┬──────────────────────────────────────────┐
│ Score Range  │ Grade              │ Gate Behaviour                           │
├──────────────┼────────────────────┼──────────────────────────────────────────┤
│ 0.90 – 1.00  │ HIGH   (GREEN)     │ Generation proceeds normally             │
│ 0.75 – 0.89  │ MEDIUM (AMBER)     │ Proceeds; all [ASSUMED:] tags mandatory  │
│ 0.70 – 0.74  │ LOW    (RED)       │ Proceeds with [ENRICHMENT-WARNING:]      │
│ 0.00 – 0.69  │ HALT   (BLACK)     │ Generation stopped; [ENRICHMENT-REQUIRED:│
│              │                    │   <gap-description>] emitted before stop │
└──────────────┴────────────────────┴──────────────────────────────────────────┘
```

### 4.3 Confidence Transparency Rule

Every generated file header block MUST include:
```scala
/** ...
  * Confidence : 0.xx | Grade: GREEN/AMBER/RED | Basis: <source §section>
  * Penalties  : [HIGH-assumption ×n (−0.05×n)] [stale-rule ×m (−0.05×m)]
  */
```

This makes the Model's uncertainty visible and auditable.

---

## PART V — DOMAIN-SPECIFIC FINANCIAL REASONING RULES

### 5.1 Payments Domain — Specialised Logic

```
SEPA ZONE RULES:
  Currency must be EUR for all SEPA transactions (intra-zone)
  SCT Inst (Instant) end-to-end latency: < 10 seconds SLA
  BIC validation: must resolve in ECB BIC directory
  IBAN checksum: MOD-97 validation mandatory on all IBAN inputs
  Batch payments: process date respects TARGET2 (EBA STEP2) calendar

PAYMENT STATE MACHINE:
  INITIATED → VALIDATED → CLEARED → SETTLED [TERMINAL]
               ↓             ↓
            REJECTED      FAILED   [TERMINAL]
  Forbidden: SETTLED → any state
  Forbidden: REJECTED → any state (reversal = new INITIATED payment)

IDEMPOTENCY PROTOCOL:
  Payment instruction carries UUID idempotency key (FM-006)
  ChalcogenPaymentRepository deduplicates on idempotency key
  Duplicate detection window: 24 hours (configurable)
```

### 5.2 Core Banking Domain — Specialised Logic

```
DOUBLE-ENTRY INVARIANT (FM-005):
  Every JournalEntry satisfies: ΣDebit == ΣCredit
  HalogenLedgerBalanceChain validates post-posting, pre-ledger-commit
  Unbalanced entries: BLOCKED — emit [FM-005-VIOLATION: entry-id]

ACCOUNT LIFECYCLE:
  PENDING_VERIFICATION → ACTIVE → DORMANT → CLOSED [TERMINAL]
  Constraint: ACTIVE requires VERIFIED KYC status (KYC-001)
  DORMANT trigger: 730 days without customer-initiated transaction (DORM-001)
  CLOSED accounts: no withdrawals; balance transfer mandatory

BALANCE CALCULATION:
  RunningBalance = Σ(CREDIT entries) − Σ(DEBIT entries) over account's lifetime
  Overdraft check: Balance must not fall below −overdraft_limit_amount
  Overdraft_limit: Optional[Money]; None = no overdraft permitted
```

### 5.3 Risk & Compliance Domain — Specialised Logic

```
AML SCORING FRAMEWORK:
  Tier-1 Rule (AML-001): Single transaction > €10 000 → AmlAlert
  Tier-2 Rule (AML-003): Structuring detection —
    N transactions < €10 000 within 24h window summing > €10 000 → AmlAlert
  Tier-3 Rule: Velocity check — >X transactions per hour per customer
  SAR FILING: AML-002 — SAR must be filed within 24 hours of suspicious flag

SANCTIONS SCREENING:
  Real-time screening on: customer name, counterparty name, IBAN owner
  Screening lists: OFAC SDN, EU Consolidated, UK HM Treasury, UN Security Council
  HIT action: Payment BLOCKED; manual review queue entry created
  Partial match threshold: Jaro-Winkler similarity ≥ 0.85 → escalate

FRAUD SCORING:
  CarbonFraudScoringStrategy: pluggable ML model interface
  Score range: 0.0 (no fraud) to 1.0 (confirmed fraud)
  Threshold: score ≥ 0.80 → automatic block; 0.50–0.79 → manual review
  Feature vector: amount, velocity, geo-distance, device fingerprint, time-of-day
```

### 5.4 Insurance Domain — Specialised Logic

```
IFRS 17 CSM / RA COMPUTATION:
  CSM (Contractual Service Margin) = Present Value of unearned profit
  RA (Risk Adjustment) = compensation for non-financial risk uncertainty
  Recognition: CSM released over coverage period proportional to coverage units
  Onerous contracts: immediate loss recognition if CSM < 0

CLAIM STATE MACHINE:
  SUBMITTED → UNDER_REVIEW → APPROVED → PAID [TERMINAL]
                  ↓               ↓
              REJECTED        DISPUTED
  SLA: SUBMITTED → first-review ≤ 5 business days
  SLA: APPROVED  → payment ≤ 3 business days

UNDERWRITING DECISION:
  CarbonUnderwritingStrategy: risk assessment algorithm interface
  Accept criteria: risk_score < threshold AND no exclusion conditions
  Decline criteria: prohibited medical/financial history conditions
```

### 5.5 Capital Markets Domain — Specialised Logic

```
TRADE LIFECYCLE:
  PENDING → SUBMITTED → EXECUTED → CONFIRMED → SETTLED [TERMINAL]
              ↓              ↓           ↓
           REJECTED       FAILED     DISPUTED

FRTB (Fundamental Review of the Trading Book) — Basel IV:
  SA-TB: Standardised Approach for Trading Book
  VaR confidence interval: 97.5% for Expected Shortfall (ES)
  Sensitivity-based risk charges: Delta, Vega, Curvature

MIFID II PRE-TRADE TRANSPARENCY:
  MetalloidMifidPreTradeSpec: validates order against MiFID II pre-trade rules
  Large-in-scale (LIS) waivers must be explicitly flagged
  Best execution: execution policy reference must appear in trade record

SETTLEMENT T+1/T+2 RULES:
  Equities (EU): T+1 (effective 2027 CSDR mandate)
  Bonds/Derivatives: T+2 default; contractual override permitted
  Failed settlement: CSDR mandatory buy-in procedure trigger
```

### 5.6 Treasury Domain — Specialised Logic

```
LIQUIDITY RISK:
  LCR (Liquidity Coverage Ratio) = HQLA / Net Cash Outflows ≥ 100%
  NSFR (Net Stable Funding Ratio) = Available Stable Funding / Required ≥ 100%
  Intraday liquidity position: monitored real-time vs. available limit

FX RISK:
  Open positions reported against VaR limit
  DV01 (Dollar Value of 1bp): sensitivity of fixed-income to 1bp rate move
  Hedge effectiveness test: 80%–125% offset requirement (IAS 39 / IFRS 9)

NOSTRO RECONCILIATION:
  Daily reconciliation of nostro account balance with correspondent bank statement
  Breaks > 24h unresolved: escalation to treasury operations
  Reconciliation tolerance: 0 (no tolerance for unexplained breaks)
```

### 5.7 Accounting & Audit Domain — Specialised Logic

```
IFRS 9 ECL (EXPECTED CREDIT LOSS):
  Stage 1: Performing — 12-month ECL
  Stage 2: Significant increase in credit risk — Lifetime ECL
  Stage 3: Credit-impaired — Lifetime ECL
  Stage transitions: driven by SICR triggers and credit indicators

DOUBLE-ENTRY COMPLETENESS:
  All financial transactions must result in ≥ 2 journal lines
  Chart of Accounts: structured with account code, type, currency
  Trial Balance: sum of all debit balances == sum of all credit balances

AUDIT TRAIL:
  Every ledger mutation emits an immutable AuditTrailRecord
  Records are APPEND-ONLY (no delete, no update) in NitrogenAuditObserver
  Audit log retention: minimum 10 years (regulatory requirement)
```

---

## PART VI — IMPROVEMENTS OVER BORON_V1

### 6.1 Structural Improvements

| Area | Boron_v1 Approach | BFSI Meta Prompt Improvement |
|------|------------------|-------------------------------|
| Domain scope | 6 domains (no accounting-audit) | 7 domains with full accounting-audit support |
| Pattern set | 10 patterns | 13 patterns (added Saga/Alkaline-Earth, Specification/Metalloid, State Machine/PostTransition) |
| Conflict resolution | Domain-specific, ad-hoc | Formalised 5-level arbitration matrix with annotation |
| Assumptions | Implicit or not flagged | Mandatory [ASSUMED:] with Risk classification and Conf-Δ |
| Multi-turn | No resume capability | Full PCP checkpoint handoff + --resume mode |
| Confidence | Not tracked | Per-file CDS with penalty table and transparency block in file header |
| Domain fallback | Single SEPA template | 7 canonical TIE templates, each with full entity set |
| Financial reasoning | Code-first | CTFS forced pre-flight reasoning before any code |
| Validation timing | Post-generation review loop | ASVG shift-left: validation before file emission |
| Security | OWASP checklist (post-gen) | 4-tier QA gate (Security + Financial + Architectural + Regulatory) |

### 6.2 Regulatory Coverage Expansion

Boron_v1 covered: GDPR, PSD2-SCA, AML, PCI-DSS, SEPA-specific rules.

BFSI Meta Prompt adds:
- DORA Art.9 (Digital Operational Resilience — incident response mapping)
- IFRS 17 CSM/RA (Insurance accounting — Lanthanide pipeline)
- IFRS 9 ECL Stage transitions (Accounting — HalogenIfrs9Chain)
- FATCA §1471-1474 (cross-border tax reporting — Treasury domain)
- MiFID II pre-trade transparency (Capital Markets — MetalloidMifidPreTradeSpec)
- Solvency II Pillar 1-3 (Insurance — reserve calculation)
- FRTB / Basel IV SA-TB (Capital Markets — CarbonFrtbRiskStrategy)

### 6.3 Prompt Logic Improvements

**Improvement L1 — Knowledge Ingestion Protocol:**
Boron_v1 scanned `.github/instructions/` without fallback.
BFSI Meta Prompt adds explicit fallback to universal regulatory baseline with
[FALLBACK-ACTIVATED:] annotation and ADR auto-generation.

**Improvement L2 — Confidence Cascade:**
Boron_v1 lacked confidence tracking.
BFSI Meta Prompt tracks confidence from SPU-1 through SPU-8, accumulating
assumption penalties and emitting per-file confidence blocks for auditability.

**Improvement L3 — Pattern Mandatory Cluster:**
Boron_v1 recommended patterns.
BFSI Meta Prompt mandates the AlkaliFactory+BoronBuilder creation cluster for
ANY file that creates domain aggregates — no partial pattern adoption.

**Improvement L4 — Monetary Pre-Flight:**
No Boron_v1 equivalent.
CTFS Step F3 forces declaration of MathContext, RoundingMode, final scale, and
commutativity/associativity before ANY monetary calculation is written.

**Improvement L5 — State Machine Explicit Declaration:**
Boron_v1 implied state machines.
CTFS Step F4 forces explicit transition graph declaration (with terminal-state
constraints) before the PostTransitionLifecycle ADT is generated.

---

## PART VII — PROMPT VERSION CONTROL POLICY

### 7.1 Versioning Scheme

```
bfsi-meta-prompt.md  : Major.Minor.Patch
bfsi-prompt-logic.md : Must match bfsi-meta-prompt.md Major version
```

### 7.2 Change Categories

| Change Type | Version Bump | Required Approvals |
|-------------|-------------|-------------------|
| New BFSI domain template | Minor | Architecture review |
| New regulatory rule in @regulations | Minor | Compliance review |
| New Meta Technique (new SPU) | Major | Full architecture review |
| Bug fix in routing / validation logic | Patch | Pull request approval |
| Periodic table pattern addition | Minor | Architecture review |
| Technology stack version update | Minor | Security + Arch review |

### 7.3 Supersession Policy

When a new version is released:
- Old version file is archived (not deleted) with `_deprecated_vX.Y.Z` suffix
- COMPLIANCE_MATRIX.md must be regenerated with new version
- All active sessions referencing the old version must be flagged for migration

---

## PART VIII — GOVERNANCE & AUTHORITY CHAIN

### 8.1 Instruction File Authority (Highest to Lowest)

```
1. bfsi-meta-prompt.md @regulations block   ─── ABSOLUTE baseline (all domains)
2. .github/instructions/<domain>/*.md        ─── Domain-specific rules
3. bfsi-prompt-logic.md (this file)         ─── Design rationale and decision tables
4. [ASSUMED:] declarations in session       ─── Lowest; always explicitly flagged
```

Conflict resolution between levels uses the 5-Level Arbitration Matrix (§ASVG).
The @regulations block WINS over domain instruction files in Level-2 (Source Authority).
Domain instruction files may ADD restrictions but NEVER relax @regulations rules.

### 8.2 Forbidden Overrides

The following CANNOT be overridden by any instruction file, assumption, or
user request. They are hardcoded into the Meta Prompt and this Logic file:

```
ABSOLUTE PROHIBITIONS (override attempts → HALLUCINATION-FAULT):
  ✗ Double/Float for monetary values (FM-001)
  ✗ null in domain code (SEC-005)
  ✗ var in domain/application layers (SEC-007)
  ✗ PII in log statements (SEC-002)
  ✗ Hardcoded credentials (SEC-004)
  ✗ Domain pattern class without periodic table prefix
  ✗ Regulatory rule asserted without source citation
  ✗ Domain layer importing infrastructure packages
```

---

## PART IX — GLOSSARY OF FINANCIAL TERMS

| Term | Definition | Governing Rule |
|------|-----------|----------------|
| IBAN | International Bank Account Number; 34-char alphanumeric | FM-003 |
| BIC | Business Identifier Code (SWIFT); 8 or 11 chars | FM-003 |
| SEPA | Single Euro Payments Area; EUR-only zone | FM-003 |
| SCT Inst | SEPA Credit Transfer Instant; < 10 seconds | TIE-payments |
| AML | Anti-Money Laundering; financial crime prevention | AML-001 |
| SAR | Suspicious Activity Report; filed with FIU | AML-002 |
| PEP | Politically Exposed Person; enhanced due diligence | KYC-002 |
| KYC | Know Your Customer; identity verification | KYC-001 |
| PSD2 | Payment Services Directive 2; EU payments regulation | @regulations |
| SCA | Strong Customer Authentication; PSD2 requirement | @regulations |
| GDPR | General Data Protection Regulation; EU data law | GDPR-001 |
| PCI-DSS | Payment Card Industry Data Security Standard | PCI-001 |
| DORA | Digital Operational Resilience Act; EU ICT risk | @regulations |
| ECL | Expected Credit Loss; IFRS 9 impairment model | SPU-4-accounting |
| CSM | Contractual Service Margin; IFRS 17 insurance | SPU-4-insurance |
| RA | Risk Adjustment; IFRS 17 insurance | SPU-4-insurance |
| FRTB | Fundamental Review of the Trading Book; Basel IV | SPU-4-capital-markets |
| LCR | Liquidity Coverage Ratio; Basel III metric | SPU-4-treasury |
| NSFR | Net Stable Funding Ratio; Basel III metric | SPU-4-treasury |
| DV01 | Dollar Value of 01; rate sensitivity measure | SPU-4-treasury |
| VaR | Value at Risk; probabilistic loss metric | SPU-4-capital-markets |
| OFAC | US Office of Foreign Assets Control; sanctions | AML-001 |
| FIU | Financial Intelligence Unit; SAR recipient | AML-002 |
| HACR | Hedge Accounting Coverage Ratio; IAS 39/IFRS 9 | SPU-4-treasury |

---

**END OF BFSI PROMPT LOGIC FRAMEWORK**

*This file is the design authority for `bfsi-meta-prompt.md`.  
Domain-specific instruction files in `.github/instructions/` may extend  
the logic described here but may not contradict the absolute prohibitions  
in Part VIII §8.2.*
