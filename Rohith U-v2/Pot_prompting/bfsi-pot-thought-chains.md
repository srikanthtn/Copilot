# BFSI POT Thought Chain Library
## Per-Domain Thought Templates — Prompt of Thoughts v2.0

**Version:** 2.0.0
**Scope:** All BFSI Sub-Domains
**Change Log:**
  - v2.0.0 (2026-03-10): Initial release — 7 domain libraries + universal chains

---

## DOCUMENT PURPOSE

This library provides **pre-built Thought Node sequences** for each BFSI
sub-domain. These templates accelerate session initialisation by supplying
canonical TN-001 through TN-005 skeletons that can be instantiated with
minimal deviation for standard use cases.

Each domain library contains:
1. The canonical DOMAIN-THOUGHT (TN-001) for the domain
2. The canonical REGULATORY-THOUGHT set (TN-002)
3. The canonical FINANCIAL-THOUGHT pre-flight (TN-003)
4. The canonical SECURITY-THOUGHT STRIDE fragment (TN-004)
5. The canonical ARCHITECTURE-THOUGHT skeleton (TN-005)
6. Domain-specific thought extensions and invariant declarations

**Usage Protocol:**
- Instantiate the relevant domain library at session start
- Replace `<PLACEHOLDER>` fields with session-specific values
- Extend canonical nodes with instruction-file-sourced facts
- Never REDUCE the canonical node content; only EXTEND

---

## PART I — UNIVERSAL THOUGHT CHAIN (ALL DOMAINS)

These Thought Nodes apply regardless of detected domain.
They are emitted as a prefix to every domain-specific chain.

---

### UTN-0 — Session Initialisation Thought

```
[UTN-0 | ARCHITECTURE-THOUGHT | Conf: 1.00]
┌─ Input State  : New BFSI POT session started. No domain locked yet.
│                 User input: <USER_INPUT_TEXT>
│
├─ Reasoning    :
│   R1: Identify the session mode from user input:
│       - Keywords: /bfsi-pot → explicit invocation
│       - --trace, --greenfield, --audit, --challenge, --resume → mode flags
│       - Free-form text → autonomous thought chain
│   R2: Determine if --resume is active; if so, scan for last THOUGHT-CHECKPOINT.
│   R3: Set base confidence to 1.00 (new session) or inherited value (--resume).
│
├─ Adversarial  :
│   [ADV-UTN-0: Is the session mode misidentified? Could a free-form input
│    be confused for a domain keyword invocation?]
│
├─ Resolution   :
│   [RES-UTN-0: Mode detection is additive — explicit flags override inferred
│    mode. Free-form text is never confused with slash-command invocation.]
│
├─ Output State : Session mode = <MODE>. Base confidence = <C>. Proceed to TN-001.
└─ Conf-Delta   : 0.00
[UTN-0 CLOSED | Final-Conf: 1.00 | Chains-To: TN-001]
[THOUGHT-CHECKPOINT: UTN-0]
```

---

## PART II — PAYMENTS DOMAIN THOUGHT CHAIN

### P-TN-001 — DOMAIN-THOUGHT (Payments)

```
[P-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
┌─ Input State  : Session started. User input tokenised.
│
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       payments tokens found: sepa, swift, iban, settlement, clearing, ...
│       score(payments)        = <SCORE>  ← highest
│       score(core-banking)    = <SCORE>
│       score(risk-compliance) = <SCORE>
│   R2: Domain with highest score = payments (score ≥ 1.0 → CONFIRMED).
│   R3: Cross-domain inheritance: payments → ALSO LOADS core-banking + risk-compliance.
│       Package root: com.bank.payments
│       Ingesting .github/instructions/payments/*.md
│
├─ Adversarial  :
│   [ADV-P-TN-001: Could the input describe a treasury or capital-markets
│    use case that incidentally mentions payment terms?]
│
├─ Resolution   :
│   [RES-P-TN-001: The primary domain indicators (IBAN, SEPA, settlement cycle)
│    are unambiguous payments vocabulary. Treasury terms (tenor, DV01, ALM)
│    are absent. Domain = payments CONFIRMED.]
│
├─ Output State : Domain LOCKED = payments.
│                 Cross-loaded: core-banking + risk-compliance.
│                 Package root: com.bank.payments
│
└─ Rule-IDs     : N/A (domain detection step)
   Conf-Delta   : 0.00
[P-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: P-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: P-TN-001]
```

---

### P-TN-002 — REGULATORY-THOUGHT (Payments)

```
[P-TN-002 | REGULATORY-THOUGHT | Conf: 1.00]
┌─ Input State  : Domain = payments. @regulations block active.
│
├─ Reasoning    :
│   R1: Enumerate BLOCKER rules for payments domain:
│       FM-001 → Money.scala (BigDecimal DECIMAL128)
│       FM-002 → Money.scala (HALF_EVEN rounding)
│       FM-004 → Direction enum (DEBIT/CREDIT)
│       FM-005 → HalogenLedgerBalanceChain (double-entry)
│       SEC-001 → TransitionEncryptionDecorator (AES-256-GCM)
│       SEC-002 → All serialisers: PII masked
│       AML-001 → HalogenAmlValidatorChain (€10 000 threshold)
│       KYC-001 → PostTransitionAccountLifecycle (ACTIVE+VERIFIED)
│       PSD2-001→ PhosphorusSCAComplianceThoughtGate (auth enforcement)
│       PCI-002 → TransitionPanMaskDecorator (last 4 digits only)
│
│   R2: Map each BLOCKER to its Scala enforcement artefact (as above).
│
│   R3: Verify no BLOCKER is unmapped. Count: 10 BLOCKER rules → 10 artefacts.
│       All BLOCKER rules mapped. CRITICAL rules mapped to test assertions.
│
├─ Adversarial  :
│   [ADV-P-TN-002: Is PSD2-001 (SCA) correctly scoped? SCA has exemptions
│    for low-value payments (< €30) and recurring transactions. Is the
│    enforcement artefact aware of these exemptions?]
│
├─ Resolution   :
│   [RES-P-TN-002: PhosphorusSCAComplianceThoughtGate must implement
│    the exemption logic as a CarbonSCAExemptionStrategy. Exemptions are
│    logged as ASSUMPTION-THOUGHTs in the absence of specific instruction
│    file guidance on exemption thresholds.]
│
├─ Output State : All BLOCKER regulations mapped. SCA exemption strategy declared.
│                 10 BLOCKER artefacts assigned. CRITICAL rules test-asserted.
│
└─ Rule-IDs     : FM-001, FM-002, FM-004, FM-005, SEC-001, SEC-002,
│                 AML-001, KYC-001, PSD2-001, PCI-002
   Conf-Delta   : +0.02 × 10 = +0.20 (10 BLOCKERs correctly mapped)
[P-TN-002 CLOSED | Final-Conf: 1.20 (capped at 1.00) | Chains-To: P-TN-003]
[THOUGHT-GATE-REGS: PASSED]
[THOUGHT-CHECKPOINT: P-TN-002]
```

---

### P-TN-003 — FINANCIAL-THOUGHT (Payments)

```
[P-TN-003 | FINANCIAL-THOUGHT | Conf: 1.00]
┌─ Input State  : Domain = payments. Regulatory mapping complete.
│
├─ Reasoning    :
│   R1: Monetary precision declaration:
│       MathContext : DECIMAL128 (34 significant digits)
│       RoundingMode: HALF_EVEN at every intermediate step
│       Minimum scale: 4 dp intermediate; 2 dp final display
│       Commutative : Addition (a+b = b+a) ✔  |  Division: NOT commutative ✗
│
│   R2: Direction convention:
│       DEBIT  = money leaving account (payment out, fee deduction)
│       CREDIT = money entering account (incoming transfer, refund)
│       NEVER use negative amounts; ALWAYS use Direction enum.
│
│   R3: State machine for SepaCreditTransfer:
│       INITIATED → VALIDATED → CLEARED → SETTLED [terminal]
│           ↓           ↓           ↓
│        REJECTED    REJECTED    FAILED [terminal]
│       Cut-off time (CutOffTime spec) governs INITIATED → VALIDATED gate.
│       AML screening (HalogenAmlValidatorChain) governs VALIDATED gate.
│
│   R4: Ledger integrity:
│       Every debit MUST have matching credit (double-entry).
│       HalogenLedgerBalanceChain verifies zero-sum at posting time.
│
│   R5: Instant payment special constraint:
│       SepaInstantPayment: processing window ≤ 10 seconds.
│       TIMEOUT at 10s → FAILED state transition event emitted.
│       No partial settlement permitted on timeout.
│
├─ Adversarial  :
│   [ADV-P-TN-003: Multi-currency SEPA edge case: EUR-only rule (MetalloidEurOnlySpec).
│    What happens when a cross-border payment with intermediate conversion is submitted?
│    Is the BigDecimal precision maintained across FX conversion steps?]
│
├─ Resolution   :
│   [RES-P-TN-003: SEPA (Single Euro Payments Area) is EUR-only by definition.
│    Cross-currency payments use SWIFT, not SEPA. MetalloidEurOnlySpec REJECTS
│    non-EUR payment instructions at the VALIDATED gate. FX precision is a
│    treasury domain concern, not a payments domain concern. Boundary confirmed.]
│
├─ Output State : Monetary invariants declared. State machine declared.
│                 Ledger integrity protocol declared. Instant timeout constraint declared.
│
└─ Rule-IDs     : FM-001, FM-002, FM-003, FM-004, FM-005, FM-006, FM-007
   Conf-Delta   : +0.02 (gate passed)
[P-TN-003 CLOSED | Final-Conf: 1.00 | Chains-To: P-TN-004]
[THOUGHT-GATE-FINANCIAL: PASSED]
[THOUGHT-CHECKPOINT: P-TN-003]
```

---

### P-TN-004 — SECURITY-THOUGHT / STRIDE (Payments)

```
[P-TN-004 | SECURITY-THOUGHT | Conf: 1.00]
┌─ Input State  : Domain, regulations, and financial invariants are locked.
│
├─ Reasoning    :
│   R1: Identify external interfaces in the payments domain:
│       - REST API (payment submission endpoint)
│       - Kafka topic (payment events)
│       - SWIFT/SEPA network adapter
│       - Database (payment state + audit trail)
│       - Reporting dashboard
│
│   R2: Apply STRIDE to each interface:
│
│    REST API:
│      S → OAuth2 + JWT authentication required (PSD2-001)
│      T → Immutable SepaCreditTransfer case class; event log
│      R → NitrogenAuditObserver; append-only; HMAC-signed
│      I → AES-256-GCM for PAN/IBAN fields; TLS 1.3 in transit
│      D → Token-bucket rate limiter per TPP (PSD2-003)
│      E → RBAC: PAYMENT_INITIATOR role required
│
│    Kafka Topic:
│      S → mTLS between producers/consumers
│      T → Domain event schema validation; Avro schema registry
│      R → Event sourcing with event IDs; NitrogenAuditObserver
│      I → Sensitive field encryption before message publication
│      D → Consumer group lag alerting via NitrogenMetricsObserver
│      E → Topic-level ACL; only authorised producers/consumers
│
│    SWIFT Adapter:
│      S → Certificate-based authentication (SWIFT CSP)
│      T → Message authentication code (SWIFT MAC)
│      R → Full SWIFT message archive; ISO 20022 traceability
│      I → PAN never in SWIFT message body; reference token only
│      D → Circuit breaker; retry with exponential backoff
│      E → Least-privilege SWIFT service account
│
│   R3: Assign control components:
│       AuthenticationToken (unforgeable value object)
│       TransitionEncryptionDecorator (AES-256-GCM field-level)
│       NitrogenAuditObserver (immutable event log)
│       CarbonRbacEnforcementStrategy (role check)
│
├─ Adversarial  :
│   [ADV-P-TN-004: Is the payment submission endpoint protected against
│    CSRF attacks in addition to OAuth2? OAuth2 does not inherently prevent
│    CSRF on endpoints that accept browser-based requests.]
│
├─ Resolution   :
│   [RES-P-TN-004: SCA context tokens (PSD2-001) with PKCE (PSD2-002) and
│    the use of Authorization header (not cookies) eliminates the CSRF attack
│    vector on API endpoints. Browser-based flows must use PKCE per RTS-SCA.
│    This is documented in the SECURITY-THOUGHT output.]
│
├─ Output State : STRIDE complete. 3 interfaces modelled. Controls assigned.
│                 CSRF vector addressed via PSD2-002 PKCE.
│
└─ Rule-IDs     : SEC-001, SEC-002, SEC-004, PSD2-001, PSD2-002, PCI-002
   Conf-Delta   : +0.02 (gate passed)
[P-TN-004 CLOSED | Final-Conf: 1.00 | Chains-To: P-TN-005]
[THOUGHT-GATE-SECURITY: PASSED]
[THOUGHT-CHECKPOINT: P-TN-004]
```

---

### P-TN-005 — ARCHITECTURE-THOUGHT (Payments)

```
[P-TN-005 | ARCHITECTURE-THOUGHT | Conf: 1.00]
┌─ Input State  : All pre-generation gates pending confirmation.
│
├─ Reasoning    :
│   R1: Bounded context = payments (com.bank.payments).
│       No need to split: payments volume does not warrant sub-contexts initially.
│       ASSUMPTION-THOUGHT emitted if instruction files specify sub-contexts.
│
│   R2: Layer topology confirms dependency direction:
│       domain/ ← application/ ← infrastructure/
│       security/ and observability/ are cross-cutting (inbound only)
│       routing/ and compliance/ are POT-specific additions
│
│   R3: Pattern assignments per layer:
│       domain/model/       → AlkaliPaymentFactory + BoronTransactionBuilder
│       domain/services/    → HalogenAmlValidatorChain + CarbonInstantPaymentStrategy
│       domain/specs/       → MetalloidValidIbanSpec + MetalloidEurOnlySpec
│                            + MetalloidCutOffTimeSpec + MetalloidAmountPositiveSpec
│       application/        → LanthanidePaymentBatchPipeline
│                            + AlkalineEarthPaymentSaga
│       infrastructure/     → ChalcogenPaymentRepository
│                            + TransitionEncryptionDecorator
│       security/           → NitrogenAuditObserver + TransitionPanMaskDecorator
│       routing/            → SiliconPaymentDomainThoughtRouter
│       compliance/         → PhosphorusPaymentComplianceThoughtGate
│
│   R4: File generation order declared (dependency-safe):
│       01 Currency.scala → Direction.scala → DomainError.scala
│       02 Money.scala → Iban.scala → Bic.scala → TransactionId.scala
│       03 PaymentEvents.scala (sealed trait hierarchy)
│       04 PaymentSpecifications.scala (MetalloidXxx impls)
│       05 SepaCreditTransfer.scala (aggregate; creation cluster)
│       06 SepaInstantPayment.scala → PaymentBatch.scala
│       07 SepaPaymentValidator.scala → HalogenAmlValidatorChain.scala
│       08 PaymentCommands.scala → PaymentQueries.scala
│       09 LanthanidePaymentBatchPipeline.scala
│       10 AlkalineEarthPaymentSaga.scala
│       11 ChalcogenPaymentRepository.scala (infrastructure)
│       12 TransitionEncryptionDecorator.scala → NitrogenAuditObserver.scala
│       13 SiliconPaymentDomainThoughtRouter.scala
│       14 PhosphorusPaymentComplianceThoughtGate.scala
│       15 PaymentTests.scala → ComplianceMatrixTests.scala
│
├─ Adversarial  :
│   [ADV-P-TN-005: Is a single bounded context for payments scalable?
│    High-volume banks separate SEPA Credit, Instant, and Batch into
│    independent microservices. Is this architecture suitable for Tier-1?]
│
├─ Resolution   :
│   [RES-P-TN-005: The package topology uses sub-packages that can be
│    promoted to independent bounded contexts without structural refactoring.
│    The initial single-context implementation is appropriate for greenfield;
│    the ADR documents the path to microservice decomposition.
│    This is architecturally conservative and defensible.]
│
├─ Output State : Package topology confirmed. Pattern assignments declared.
│                 File generation order declared. ADR for future decomposition noted.
│
└─ Rule-IDs     : N/A (architectural)
   Conf-Delta   : +0.03 (gate passed)
[P-TN-005 CLOSED | Final-Conf: 1.00 | Chains-To: THOUGHT-GATE-ENTRY]
[THOUGHT-GATE-ARCH: PASSED]
[THOUGHT-GATE-ENTRY: PASSED — all pre-generation gates satisfied]
[THOUGHT-CHECKPOINT: P-TN-005]
```

---

## PART III — CORE BANKING DOMAIN THOUGHT CHAIN

### CB-TN-001 — DOMAIN-THOUGHT (Core Banking)

```
[CB-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       core-banking tokens: account, ledger, balance, overdraft, journal,
│                            reconciliation, statement, dormant, ...
│       score(core-banking) = <SCORE> — highest
│   R2: Domain = core-banking (CONFIRMED).
│   R3: Cross-domain: core-banking is a ROOT domain (no parent loads).
│       Package root: com.bank.corebanking
│       Ingesting .github/instructions/core-banking/*.md
│
├─ Adversarial  :
│   [ADV-CB-TN-001: Does the input include payment vocabulary that suggests
│    payments domain should be the primary context?]
│
├─ Resolution   :
│   [RES-CB-TN-001: Account/ledger/balance cluster is core-banking.
│    If payment processing keywords are present, cross-load payments
│    sub-templates. Core-banking remains the primary bounded context.]
│
├─ Output State : Domain LOCKED = core-banking.
│                 Package root: com.bank.corebanking
[CB-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: CB-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: CB-TN-001]
```

---

### CB-TN-002 — REGULATORY-THOUGHT (Core Banking)

```
[CB-TN-002 | REGULATORY-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Core Banking BLOCKER regulation set:
│       FM-001 → Balance.scala (BigDecimal DECIMAL128)
│       FM-004 → Direction enum on all ledger entries
│       FM-005 → HalogenDoubleEntryValidatorChain (SUM(CR)−SUM(DR)=0)
│       DORM-001→ PostTransitionAccountLifecycle (730-day inactivity flag)
│       GDPR-001→ ActinideRetentionRegistry (right-to-erasure)
│       KYC-001 → PostTransitionAccountLifecycle (ACTIVE+VERIFIED gate)
│       SEC-002 → NitrogenAuditObserver (PII masked in all logs)
│       SEC-005 → Option[T] for all nullable domain fields
│
│   R2: All BLOCKER rules mapped to enforcement artefacts.
│
├─ Adversarial  :
│   [ADV-CB-TN-002: GDPR right-to-erasure conflicts with financial
│    data retention obligations (7-year rule under EU accounting directive).
│    How is this conflict resolved?]
│
├─ Resolution   :
│   [RES-CB-TN-002: Logical deletion with crypto-shredding resolves the
│    conflict: personal data is rendered inaccessible (effectively erased)
│    while the immutable transaction record is retained without PII.
│    The ActinideRetentionRegistry implements TTL + crypto-key deletion.
│    This satisfies both GDPR Art.17 and the 7-year ledger retention rule.]
│
└─ Rule-IDs     : FM-001, FM-004, FM-005, DORM-001, GDPR-001, KYC-001, SEC-002, SEC-005
[CB-TN-002 CLOSED | Final-Conf: 1.00 | Chains-To: CB-TN-003]
[THOUGHT-GATE-REGS: PASSED]
[THOUGHT-CHECKPOINT: CB-TN-002]
```

---

## PART IV — RISK & COMPLIANCE DOMAIN THOUGHT CHAIN

### RC-TN-001 — DOMAIN-THOUGHT (Risk & Compliance)

```
[RC-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       risk-compliance tokens: aml, sanctions, kyc, sar, suspicious, fatf,
│                               ofac, pep, watchlist, screening, 6amld, ...
│       score(risk-compliance) = <SCORE> — highest
│   R2: Domain = risk-compliance (CONFIRMED).
│   R3: ROOT domain. Package root: com.bank.risk
│       Ingesting .github/instructions/risk-compliance/*.md
│
├─ Output State : Domain LOCKED = risk-compliance.
│                 Package root: com.bank.risk
[RC-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: RC-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: RC-TN-001]
```

---

### RC-TN-002 — REGULATORY-THOUGHT (Risk & Compliance)

```
[RC-TN-002 | REGULATORY-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Risk & Compliance BLOCKER regulation set:
│       AML-001 → HalogenAmlValidatorChain (€10 000 threshold trigger)
│       AML-002 → AlkaliSarFactory (SAR within 24h of suspicious flag)
│       AML-003 → CarbonStructuringDetectionStrategy (N×sub-threshold)
│       AML-004 → CarbonVelocityCheckStrategy (≥5 txns/1h)
│       KYC-001 → PostTransitionAccountLifecycle
│       KYC-002 → AlkaliPepScreeningFactory (PEP check at onboarding)
│       KYC-003 → BoronEnhancedDueDiligenceBuilder (high-risk jurisdictions)
│       SEC-002 → NitrogenAuditObserver (PII masked; SAR data protected)
│
│   R2: All BLOCKER rules mapped. AML chain must be comprehensive:
│       threshold check → velocity check → structuring detection → PEP screening
│       All stages of HalogenAmlValidatorChain declared.
│
├─ Adversarial  :
│   [ADV-RC-TN-002: Structuring detection (AML-003) requires lookback period.
│    What is the window? 24h? 48h? 7 days? An incorrect window could miss
│    deliberate structuring patterns.]
│
├─ Resolution   :
│   [RES-RC-TN-002: If instruction files specify a window, use that value.
│    If not, emit ASSUMPTION-THOUGHT: 48h lookback window (MED risk).
│    The window is configurable via ActinideRegulatoryRuleRegistry at runtime
│    to allow compliance team adjustment without code change.]
│
└─ Rule-IDs     : AML-001, AML-002, AML-003, AML-004, KYC-001, KYC-002, KYC-003, SEC-002
[RC-TN-002 CLOSED | Final-Conf: 1.00 | Chains-To: RC-TN-003]
[THOUGHT-GATE-REGS: PASSED]
[THOUGHT-CHECKPOINT: RC-TN-002]
```

---

### RC-TN-VELOCITY — VELOCITY-THOUGHT (Risk & Compliance Extension)

```
[RC-TN-VELOCITY | FINANCIAL-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Velocity analysis parameters:
│       Time window    : 1 hour (configurable via ActinideRegulatoryRuleRegistry)
│       Count threshold: ≥ 5 transactions within the window
│       Window type    : Sliding window (more sensitive than tumbling)
│       Lookback store : Redis in-memory sorted set (TTL = window size)
│
│   R2: Trigger action: ≥ 5 txns → AML-004 rule activated → AmlAlert created
│       Alert severity: MEDIUM (requires analyst review within 4h)
│       Escalation    : If ≥ 10 txns/1h → HIGH severity; account temporarily frozen
│
│   R3: Mathematical invariant:
│       Count(txns, customerId, T−1h, T) ≥ threshold → trigger flag set
│       Count operation is INTEGER; no floating-point risk.
│
├─ Adversarial  :
│   [ADV-RC-TN-VELOCITY: Sliding window in Redis introduces state outside the
│    Spark pipeline. Does this violate the stateless processing principle?]
│
├─ Resolution   :
│   [RES-RC-TN-VELOCITY: Velocity checking is inherently stateful and requires
│    a low-latency state store. Redis TTL-based sorted set is the accepted
│    industry solution. The Spark pipeline delegates velocity checks to the
│    CarbonVelocityCheckStrategy which calls the state store via the
│    ChalcogenVelocityRepository port. The port is swappable for testing.
│    Logical separation is preserved by the hexagonal architecture.]
│
└─ Rule-IDs     : AML-004
   Conf-Delta   : +0.02
[RC-TN-VELOCITY CLOSED | Final-Conf: 1.00 | Chains-To: RC-TN-004]
[THOUGHT-CHECKPOINT: RC-TN-VELOCITY]
```

---

## PART V — INSURANCE DOMAIN THOUGHT CHAIN

### INS-TN-001 — DOMAIN-THOUGHT (Insurance)

```
[INS-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       insurance tokens: policy, premium, claim, ifrs17, csm, underwriting,
│                         actuarial, reinsurance, coverage, peril, ...
│       score(insurance) = <SCORE> — highest
│   R2: Domain = insurance (CONFIRMED).
│   R3: Cross-domain inheritance: insurance → ALSO LOADS accounting-audit.
│       Package root: com.bank.insurance
│       Ingesting .github/instructions/insurance/*.md
│
├─ Output State : Domain LOCKED = insurance.
│                 Package root: com.bank.insurance
[INS-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: INS-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: INS-TN-001]
```

---

### INS-TN-IFRS17 — IFRS17-THOUGHT (Insurance Extension)

```
[INS-TN-IFRS17 | FINANCIAL-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: IFRS 17 measurement model:
│       Three building blocks per insurance contract:
│         (a) Present Value of Future Cash Flows (PVFCF)
│             → discounted at market-consistent rates
│         (b) Risk Adjustment (RA) for non-financial risk
│             → 75th percentile confidence interval (industry standard)
│         (c) Contractual Service Margin (CSM)
│             → represents unearned profit; released over coverage period
│
│   R2: Cohort grouping rules (IFRS 17.22):
│       Contracts issued in the same annual cohort AND
│       With the same expected profitability class are grouped.
│       Cohort boundaries cannot cross annual reporting periods.
│
│   R3: Mathematical precision requirements:
│       Discount factors: BigDecimal(MathContext.DECIMAL128)
│       CSM release pattern: pro-rata over coverage units
│       RA calculation: Monte Carlo simulation (external actuarial engine)
│       All intermediate results: minimum 6 decimal places
│
├─ Adversarial  :
│   [ADV-INS-TN-IFRS17: The RA calculation uses Monte Carlo simulation.
│    This is computationally expensive. Is this in scope for the Spark pipeline
│    or delegated to an external actuarial engine?]
│
├─ Resolution   :
│   [RES-INS-TN-IFRS17: The Spark pipeline (LanthanideIfrs17ValuationPipeline)
│    receives pre-computed RA values from an external actuarial system.
│    The pipeline stores and applies these values; it does not recalculate them.
│    This is the NobleClearingFacade integration pattern.
│    If instruction files specify in-pipeline RA calculation, emit ASSUMPTION-THOUGHT
│    and implement a CarbonMonteCarloRaStrategy as a delegatable component.]
│
└─ Rule-IDs     : IFRS9-01, IFRS9-02  (applied by analogy to IFRS 17 measurement)
   Conf-Delta   : +0.02
[INS-TN-IFRS17 CLOSED | Final-Conf: 1.00 | Chains-To: INS-TN-004]
[THOUGHT-CHECKPOINT: INS-TN-IFRS17]
```

---

## PART VI — CAPITAL MARKETS DOMAIN THOUGHT CHAIN

### CM-TN-001 — DOMAIN-THOUGHT (Capital Markets)

```
[CM-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       capital-markets tokens: trade, order, security, frtb, var, mifid,
│                               isin, derivatives, mark-to-market, netting, ...
│       score(capital-markets) = <SCORE> — highest
│   R2: Domain = capital-markets (CONFIRMED).
│   R3: Cross-domain: capital-markets → ALSO LOADS risk-compliance + accounting-audit.
│       Package root: com.bank.markets
│       Ingesting .github/instructions/capital-markets/*.md
│
├─ Output State : Domain LOCKED = capital-markets.
│                 Package root: com.bank.markets
[CM-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: CM-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: CM-TN-001]
```

---

### CM-TN-FRTB — FRTB-THOUGHT (Capital Markets Extension)

```
[CM-TN-FRTB | FINANCIAL-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: FRTB (Fundamental Review of the Trading Book) context:
│       Approach selection: Standardised Approach (SA) by default.
│       IMA (Internal Models Approach) requires regulatory approval.
│       ASSUMPTION-THOUGHT emitted if instruction files specify IMA.
│
│   R2: SA sensitivity risk factor mapping:
│       Delta risk      → linear sensitivity to price changes
│       Vega risk       → sensitivity to implied volatility
│       Curvature risk  → non-linear residual risk
│       Each mapped to a CarbonXxxSensitivityStrategy implementation.
│
│   R3: Capital calculation precision:
│       All risk weights: BigDecimal(MathContext.DECIMAL128)
│       Correlation matrices: read from ActinideRegulatoryRuleRegistry
│       Netting sets: computationally verified for aggregation correctness
│
├─ Adversarial  :
│   [ADV-CM-TN-FRTB: FRTB correlation matrices change with Basel IV phased
│    implementation. Are the matrices hardcoded or configurable?]
│
├─ Resolution   :
│   [RES-CM-TN-FRTB: Correlation matrices are stored in ActinideRegulatoryRuleRegistry
│    as externally configurable parameters, NOT hardcoded. This honours the
│    SEC-004 (no hardcoded values) rule and allows regulatory updates without
│    redeployment. Version-tagged matrices enable point-in-time capital calculation.]
│
└─ Rule-IDs     : FM-001, FM-002, SEC-004  (no hardcoding of regulatory data)
   Conf-Delta   : +0.02
[CM-TN-FRTB CLOSED | Final-Conf: 1.00 | Chains-To: CM-TN-004]
[THOUGHT-CHECKPOINT: CM-TN-FRTB]
```

---

## PART VII — TREASURY DOMAIN THOUGHT CHAIN

### TR-TN-001 — DOMAIN-THOUGHT (Treasury)

```
[TR-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       treasury tokens: fx, liquidity, swap, dv01, alm, tenor, duration,
│                        hedge, cash-management, yield-curve, basis-point, ...
│       score(treasury) = <SCORE> — highest
│   R2: Domain = treasury (CONFIRMED).
│   R3: Cross-domain: treasury → ALSO LOADS core-banking + accounting-audit.
│       Package root: com.bank.treasury
│       Ingesting .github/instructions/treasury/*.md
│
├─ Output State : Domain LOCKED = treasury.
│                 Package root: com.bank.treasury
[TR-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: TR-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: TR-TN-001]
```

---

### TR-TN-FX — FX-THOUGHT (Treasury Extension)

```
[TR-TN-FX | FINANCIAL-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: FX rate arithmetic:
│       All FX rates: BigDecimal(MathContext.DECIMAL128)
│       Cross-rate calculation: (EUR/USD) × (USD/JPY) = EUR/JPY
│       Cross-rate is NOT commutative; order of operations matters.
│       BigDecimal multiplication preserves precision; intermediate
│       results must not be rounded until the final conversion amount.
│
│   R2: Bid/Ask spread modelling:
│       Mid-rate = (Bid + Ask) / 2  using HALF_EVEN rounding
│       Spread   = Ask − Bid (always positive; validated by require)
│
│   R3: Settlement date convention:
│       Spot: T+2 (standard); T+1 or T+0 for supported currencies
│       Forward: tenor-based (1W, 1M, 3M, 6M, 1Y) from MetalloidTenorSpec
│       Value date validated against CutOffTime spec and currency calendar
│
├─ Adversarial  :
│   [ADV-TR-TN-FX: Holiday calendars affect value-date calculation.
│    Is the holiday calendar embedded in the system or fetched from
│    an external service? What happens if the calendar service is unavailable?]
│
├─ Resolution   :
│   [RES-TR-TN-FX: The holiday calendar is loaded from
│    ActinideRegulatoryRuleRegistry (cached at startup; refreshed daily).
│    If the external calendar feed is unavailable, the system uses the
│    cached version with a [THOUGHT-FALLBACK-ACTIVATED] warning.
│    The Circuit Breaker pattern (DORA-002 resilience requirement) protects
│    the external calendar service call.]
│
└─ Rule-IDs     : FM-001, FM-002, FM-003, FM-007, DORA-002
   Conf-Delta   : +0.02
[TR-TN-FX CLOSED | Final-Conf: 1.00 | Chains-To: TR-TN-004]
[THOUGHT-CHECKPOINT: TR-TN-FX]
```

---

## PART VIII — ACCOUNTING & AUDIT DOMAIN THOUGHT CHAIN

### AA-TN-001 — DOMAIN-THOUGHT (Accounting & Audit)

```
[AA-TN-001 | DOMAIN-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: Vocabulary scoring:
│       accounting-audit tokens: ifrs9, ecl, impairment, ledger, trial-balance,
│                                consolidation, audit-trail, reconciliation, ...
│       score(accounting-audit) = <SCORE> — highest
│   R2: Domain = accounting-audit (CONFIRMED).
│   R3: ROOT domain. Package root: com.bank.accounting
│       Ingesting .github/instructions/accounting-audit/*.md
│
├─ Output State : Domain LOCKED = accounting-audit.
│                 Package root: com.bank.accounting
[AA-TN-001 CLOSED | Final-Conf: 1.00 | Chains-To: AA-TN-002]
[THOUGHT-GATE-DOMAIN: PASSED]
[THOUGHT-CHECKPOINT: AA-TN-001]
```

---

### AA-TN-IFRS9 — IFRS9-THOUGHT (Accounting Extension)

```
[AA-TN-IFRS9 | FINANCIAL-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: IFRS 9 ECL staging model:
│       Stage 1: 12-month ECL (performing; no SICR observed)
│       Stage 2: Lifetime ECL (SICR observed; not credit-impaired)
│       Stage 3: Lifetime ECL (credit-impaired; NPL classification)
│
│   R2: SICR trigger conditions (from instruction files; else ASSUMPTION-THOUGHT):
│       - > 30 days past due with material outstanding balance
│       - Credit rating downgrade of ≥ 2 notches
│       - Forbearance measure applied
│       - Borrower on watchlist
│
│   R3: ECL mathematical components:
│       ECL = PD × LGD × EAD × Discount Factor
│       All components: BigDecimal(MathContext.DECIMAL128)
│       PD (Probability of Default): derived from rating migration matrix
│       LGD (Loss Given Default): collateral-adjusted (min 10%, max 100%)
│       EAD (Exposure at Default): outstanding balance + off-balance sheet add-on
│       Discount Factor: instrument effective interest rate
│
├─ Adversarial  :
│   [ADV-AA-TN-IFRS9: SICR determination uses judgement-based criteria that
│    vary across banking groups. Hardcoded SICR thresholds could result in
│    material misstatement if the wrong thresholds are applied.]
│
├─ Resolution   :
│   [RES-AA-TN-IFRS9: SICR thresholds are stored in ActinideRegulatoryRuleRegistry
│    as auditable, configurable parameters. The CarbonIfrs9EclCalculationStrategy
│    reads thresholds from the registry at calculation time. The registry is
│    version-controlled; every parameter change creates an audit record.
│    This satisfies both IFRS9-01 and the external audit trail requirement.]
│
└─ Rule-IDs     : IFRS9-01, IFRS9-02, FM-001, FM-002
   Conf-Delta   : +0.02
[AA-TN-IFRS9 CLOSED | Final-Conf: 1.00 | Chains-To: AA-TN-004]
[THOUGHT-CHECKPOINT: AA-TN-IFRS9]
```

---

## PART IX — CROSS-DOMAIN THOUGHT TEMPLATES

### XD-TN-DORA — DORA Resilience Thought (All Domains)

```
[XD-TN-DORA | REGULATORY-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: DORA applies to all financial entities and their ICT service providers.
│       DORA-001: ICT incident classification within 4 hours
│       DORA-002: Critical ICT system RTO ≤ 4h; RPO ≤ 2h
│       DORA-003: Penetration testing at least annually
│
│   R2: DORA enforcement artefacts:
│       DORA-001 → NitrogenIncidentClassificationObserver
│                  (severity taxonomy: high, significant, critical ICT incidents)
│       DORA-002 → AlkalineEarthDRSaga
│                  (DR saga: detects failure, initiates failover, verifies RTO/RPO)
│       DORA-003 → THREAT_MODEL.md generated in project root
│                  (documents attack surface; basis for pen-test scope)
│
│   R3: AlkalineEarthDRSaga steps:
│       Step 1 (forward): Health check → detect failure → log NitrogenObserver
│       Step 2 (forward): Initiate failover to DR region
│       Step 3 (forward): Verify data integrity post-failover (RPO check)
│       Step 4 (forward): Restore service and verify SLA metrics (RTO check)
│       Step C1 (compensation): Revert failover if primary recovers within threshold
│
├─ Adversarial  :
│   [ADV-XD-TN-DORA: RTO of 4h requires DR infrastructure pre-provisioned.
│    Is this an application-layer concern or an infrastructure concern?
│    Should the Spark pipeline include DR logic?]
│
├─ Resolution   :
│   [RES-XD-TN-DORA: DR infrastructure is an infrastructure + DevOps concern.
│    The application layer (AlkalineEarthDRSaga) provides the ORCHESTRATION
│    logic that coordinates application-level recovery steps.
│    Infrastructure provisioning is documented in README.md and in the
│    COMPLIANCE_MATRIX.md as an out-of-scope ASSUMPTION-THOUGHT (LOW risk)
│    with a reference to the infrastructure team's DR runbook.]
│
└─ Rule-IDs     : DORA-001, DORA-002, DORA-003
   Conf-Delta   : +0.02
[XD-TN-DORA CLOSED | Final-Conf: 1.00]
[THOUGHT-CHECKPOINT: XD-TN-DORA]
```

---

### XD-TN-GDPR — GDPR Data Protection Thought (All Domains)

```
[XD-TN-GDPR | REGULATORY-THOUGHT | Conf: 1.00]
├─ Reasoning    :
│   R1: GDPR obligations active for this domain:
│       GDPR-001: Right to Erasure → ActinideRetentionRegistry
│                 Logical deletion: mark record as deleted + crypto-shredding
│                 TTL: 7 years (financial data retention), then purge
│       GDPR-002: Breach notification → NitrogenIncidentClassificationObserver
│                 (72-hour regulatory notification window)
│       GDPR-003: Data minimisation → BoronMinimalDataBuilder
│                 (only collect fields necessary for stated purpose)
│       GDPR-004: Consent withdrawal → CarbonConsentRevocationStrategy
│                 (stop processing within 24h of revocation)
│
│   R2: PII fields for this domain (domain-specific list):
│       payments : IBAN, name, address, BIC end-beneficiary
│       core-banking: name, address, DOB, national ID, account number
│       insurance   : name, DOB, health data (PHI), policy details
│       Encrypt all listed fields with TransitionEncryptionDecorator.
│       Mask in all log outputs via NitrogenAuditObserver.
│
│   R3: Crypto-shredding implementation:
│       Each customer data record has a unique encryption key (key-per-subject).
│       Key stored in HashiCorp Vault (external to application data store).
│       On erasure request: delete the key → data is cryptographically irretrievable.
│
├─ Adversarial  :
│   [ADV-XD-TN-GDPR: Crypto-shredding invalidates backup consistency.
│    Encrypted data in backups cannot be decrypted after key deletion.
│    How are backup restoration requirements reconciled with GDPR erasure?]
│
├─ Resolution   :
│   [RES-XD-TN-GDPR: This is the intended effect of crypto-shredding.
│    A subject's data is effectively erased from backups without needing
│    to modify backup files (operationally impractical). The approach is
│    explicitly endorsed by GDPR Art.17 interpretive guidance and multiple
│    EU Data Protection Authority opinions. The THOUGHT_AUDIT.md records
│    this interpretation with appropriate regulatory citations.]
│
└─ Rule-IDs     : GDPR-001, GDPR-002, GDPR-003, GDPR-004
   Conf-Delta   : +0.02
[XD-TN-GDPR CLOSED | Final-Conf: 1.00]
[THOUGHT-CHECKPOINT: XD-TN-GDPR]
```

---

## THOUGHT CHAIN LIBRARY EXTENSION PROTOCOL

To add a new domain or extend an existing thought chain:

1. **Add a new PART section** (e.g., PART X for a new domain).
2. **Provide at minimum:** TN-001 (DOMAIN-THOUGHT) and TN-002 (REGULATORY-THOUGHT).
3. **Provide domain extensions** for any financially complex calculations.
4. **Follow the canonical Thought Node format** exactly.
5. **Update the Change Log** at the top of this file with version and date.
6. **Cross-reference ADR** in `bfsi-pot-reasoning-framework.md` if the
   new domain introduces a new thought type or new pattern registry entry.

Extension chains may NOT:
- Reduce or override the universal UTN-0 + TN-001 through TN-005 sequence
- Remove regulatory thought coverage for universal @regulations rules
- Introduce domain-specific confidence scoring that conflicts with the
  global confidence model defined in §POT-6
