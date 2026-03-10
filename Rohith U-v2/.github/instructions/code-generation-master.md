# Master Instruction File: BFSI Advanced Context Engineering Authority
**Version:** 3.0.0
**Scope:** All Financial Services Domains — Banking, Financial Services & Insurance
**Authority Level:** ABSOLUTE — All generated code and AI outputs MUST comply
**Context Engineering Edition:** Multi-Layer Context Architecture with Dynamic Retrieval

---

## 0. CONTEXT ENGINEERING ARCHITECTURE (NEW IN v3)

### 0.1 What Is Context Engineering in BFSI
Context Engineering is the systematic discipline of constructing, managing, compressing,
and delivering precise context to AI systems to maximize correctness and regulatory compliance.

In BFSI, context engineering goes beyond prompt templating. It encompasses:
- **Memory Layers:** Persistent domain knowledge, session state, and working memory
- **Context Windows:** Token-budget-aware injection of the highest-priority context first
- **Dynamic Retrieval:** RAG-based extraction of relevant rules at inference time
- **Context Compression:** Distilling 100K-token instruction sets into ranked, relevant excerpts
- **Conflict Resolution:** Deterministic tie-breaking when domain rules clash

### 0.2 Four-Layer Context Model
```
┌─────────────────────────────────────────────────────────────────────┐
│  LAYER 0: SYSTEM PERSONA (Always Present, Highest Priority)          │
│  Role definition, authority level, intent lock, output constraints   │
├─────────────────────────────────────────────────────────────────────┤
│  LAYER 1: REGULATORY CONTEXT (Session-Persistent)                    │
│  GDPR, PCI-DSS, PSD2, AML, Basel III/IV, Solvency II, MiFID II      │
│  DORA, IFRS 9/17, SOX — all applicable regulations pre-loaded        │
├─────────────────────────────────────────────────────────────────────┤
│  LAYER 2: DOMAIN CONTEXT (Dynamically Retrieved per Request)         │
│  Payments / Core Banking / Capital Markets / Treasury /               │
│  Risk & Compliance / Insurance / Accounting / FP&A                   │
├─────────────────────────────────────────────────────────────────────┤
│  LAYER 3: WORKING CONTEXT (Per-Task, Ephemeral)                      │
│  Active code under review, current task parameters, runtime state     │
└─────────────────────────────────────────────────────────────────────┘
```

### 0.3 Context Priority Resolution
When context layers conflict, apply:
1. Layer 0 (Persona) > Layer 1 (Regulatory) > Layer 2 (Domain) > Layer 3 (Working)
2. More restrictive rule always wins within the same layer
3. Document every conflict resolution in the output as `[CONFLICT-RESOLVED: reason]`

---

## 1. DOCUMENT HIERARCHY & PRECEDENCE

### 1.1 Instruction File Priority (Highest to Lowest)
1. **This Master File** — Global rules for ALL domains
2. **Context Engineering Framework** (`context-engineering-framework.md`) — CE principles
3. **Domain-Specific Instructions** — Bounded context rules
4. **Prompt Configuration** — Runtime parameters

### 1.2 Available Domain Instruction Directories
```
./payments/                — SEPA, SWIFT, ISO 20022, Instant, Cross-Border
./core-banking/            — Accounts, Ledger, Customer, KYC, Onboarding
./capital-markets/         — Trading, Securities, Derivatives, Clearing
./treasury/                — Liquidity, FX, Cash Management, ALM
./risk-compliance/         — AML, Sanctions, Fraud, Conduct Risk, Model Risk
./insurance/               — Policies, Claims, Underwriting, Reinsurance, Actuarial
./accounting-audit/        — GL, Reconciliation, Close, IFRS, GAAP
./fpna/                    — Budgeting, Forecasting, Management Reporting, Scenario Planning
./public-finance-regulation/ — Regulatory Reporting, Stress Testing, XBRL
```

### 1.3 Context Retrieval Strategy
For each user request:
1. **Detect** the primary domain from vocabulary analysis
2. **Retrieve** the top 5 most relevant rule chunks from target domain (similarity > 0.85)
3. **Inject** regulatory constraints that apply (always include AML, PCI-DSS, GDPR baseline)
4. **Compress** injected context using hierarchical summarization if > 4096 tokens
5. **Validate** no conflicting rules before proceeding to generation

---

## 2. UNIVERSAL BUSINESS INVARIANTS (NON-NEGOTIABLE)

### 2.1 Financial Mathematics — Extended
| Rule ID | Rule Name | Constraint | Enforcement Level |
|---------|-----------|------------|-------------------|
| FM-001 | Money Precision | `Decimal` with 128-bit context | Type system enforcement |
| FM-002 | Banker's Rounding | `ROUND_HALF_EVEN` for all final monetary values | Unit test 100% |
| FM-003 | Non-Negative Amounts | All transaction amounts MUST be `> 0` | Pre-condition check |
| FM-004 | Zero Tolerance | Zero ONLY for Ping/Heartbeat; explicit enum | Semantic validation |
| FM-005 | No Floating Point | NEVER use `float/double` for money | Static analysis blocker |
| FM-006 | Currency Validation | ISO 4217 codes only; sealed enum | Compile-time check |
| FM-007 | Decimal Preservation | Minimum 4 decimal places during intermediate calculations | Property test |
| FM-008 | Arithmetic Overflow | Check for overflow in all monetary operations | Boundary test suite |
| FM-009 | Cross-Currency Rates | FX rates must carry precision metadata and timestamp | Rate staleness check |
| FM-010 | Interest Calculation | Use Actual/360 or Actual/365 convention per product; explicit | Convention registry |

### 2.2 State Machine Transitions — Enhanced
All financial entities MUST follow this lifecycle:

```
                          ┌──────────────────────────────────────────┐
                          │         FINANCIAL ENTITY LIFECYCLE        │
                          └──────────────────────────────────────────┘

 ┌────────────┐   ┌────────────┐   ┌────────────┐   ┌────────────┐   ┌────────────┐
 │ INITIATED  │──▶│ VALIDATED  │──▶│  CLEARED   │──▶│  SETTLED   │──▶│  ARCHIVED  │
 └────────────┘   └────────────┘   └────────────┘   └────────────┘   └────────────┘
       │                │                 │                 │
       ▼                ▼                 ▼                 │
 ┌──────────┐    ┌──────────┐    ┌──────────────┐          │
 │ REJECTED │    │ REJECTED │    │    FAILED    │          │
 └──────────┘    └──────────┘    └──────────────┘          │
                                                    [TERMINAL]◀──────┘
```

**Constraints (v3 additions):**
- Every transition MUST emit a `DomainEvent` for audit completeness
- `SETTLED` and `ARCHIVED` are TERMINAL STATES — immutable after entry
- Reversals are SEPARATE transactions (never rollback a settled state)
- State machine definition MUST be versioned alongside the domain model
- Concurrent transition attempts MUST be serialized via optimistic locking

### 2.3 Monetary Direction Convention
- NEVER use negative amounts for debits
- Use explicit `Direction` enum: `DEBIT | CREDIT`
- Double-entry rule: SUM(DEBITS) = SUM(CREDITS) for every journal entry
- Ledger balance = SUM(CREDIT) - SUM(DEBIT) per account

### 2.4 Idempotency Invariant (v3 — NEW)
- Every write operation MUST carry an `IdempotencyKey`
- Duplicate requests with same `IdempotencyKey` MUST return the original result
- `IdempotencyKey` retention: minimum 24 hours, maximum 7 days
- Idempotency store MUST be distributed and consistent

---

## 3. FORBIDDEN OPERATIONS — ZERO TOLERANCE

### 3.1 Security Blockers
| Violation ID | Description | Detection | Severity |
|--------------|-------------|-----------|----------|
| SEC-001 | Hardcoded secrets (keys, passwords, tokens) | Regex pattern scan | BLOCKER |
| SEC-002 | PII in logs (IBAN, PAN, Name, DOB) | Log statement AST analysis | BLOCKER |
| SEC-003 | SQL injection via string concatenation | Parameter binding check | BLOCKER |
| SEC-004 | Insecure deserialization | Class allowlist check | BLOCKER |
| SEC-005 | Missing authentication on endpoints | Coverage analysis | BLOCKER |
| SEC-006 | Plaintext sensitive data storage | Encryption annotation check | BLOCKER |
| SEC-007 | Missing CSRF protection | Token check on POST/PUT/DELETE | BLOCKER |
| SEC-008 | Unvalidated redirects | URL allowlist check | CRITICAL |
| SEC-009 | Missing rate limiting on auth endpoints | Throttle annotation check | CRITICAL |
| SEC-010 | Cleartext token transmission | TLS enforcement check | BLOCKER |

### 3.2 Technical Blockers
| Violation ID | Description | Correct Alternative | Severity |
|--------------|-------------|---------------------|----------|
| TECH-001 | `None`/`null` as control flow | `Optional[T]` / typed absence | BLOCKER |
| TECH-002 | Bare `raise` / `throw` | `Result[E, T]` / `Either` pattern | BLOCKER |
| TECH-003 | Mutable state in domain entities | Immutable value objects | BLOCKER |
| TECH-004 | Untyped collections | Generic typed collections | MAJOR |
| TECH-005 | `float`/`double` for money | `Decimal` with explicit precision | BLOCKER |
| TECH-006 | Full dataset load into memory | Streaming / pagination / lazy eval | CRITICAL |
| TECH-007 | `datetime.now()` without timezone | UTC-aware timestamps only | BLOCKER |
| TECH-008 | Synchronous blocking I/O in async context | Async I/O with proper await | CRITICAL |
| TECH-009 | Silent exception swallowing | Explicit error propagation | BLOCKER |
| TECH-010 | God class (>500 lines, >20 methods) | Decompose into bounded services | MAJOR |

### 3.3 Financial Blockers
| Violation ID | Description | Business Impact | Severity |
|--------------|-------------|-----------------|----------|
| FIN-001 | Implicit decimal truncation | Monetary mismatch / regulatory breach | BLOCKER |
| FIN-002 | Silent ledger mutation | Audit trail corruption | BLOCKER |
| FIN-003 | Missing idempotency key | Duplicate transactions / P&L error | BLOCKER |
| FIN-004 | Unbalanced ledger entries | Regulatory violation / reconciliation failure | BLOCKER |
| FIN-005 | Missing transaction timestamp | Reconciliation failure / regulatory breach | CRITICAL |
| FIN-006 | Missing AML screening for > threshold | Regulatory fine / criminal liability | BLOCKER |
| FIN-007 | PCI data stored post-authorization | PCI-DSS non-compliance / fine | BLOCKER |
| FIN-008 | Unvalidated FX rate application | Financial loss / incorrect P&L | CRITICAL |
| FIN-009 | Missing credit risk pre-approval | Credit exposure without authorization | BLOCKER |
| FIN-010 | Calculation without MathContext | Precision loss in financial results | BLOCKER |

---

## 4. DATA BOUNDARIES & ACCESS CONTROL — ADVANCED

### 4.1 Data Classification Matrix (Extended)
| Classification | Examples | Encryption | Logging Policy | Retention | Access |
|----------------|----------|------------|----------------|-----------|--------|
| **PCI** | PAN, CVV, Expiry | AES-256-GCM + Tokenization | NEVER | 90 days (tokenized only) | PCI team only |
| **PII** | Name, IBAN, Email, SSN | AES-256-GCM | MASKED (last 4 only) | 7 years | KYC/Ops |
| **PHI** | Health records (insurance) | AES-256-GCM | NEVER | Per HIPAA / local law | Actuarial/Claims |
| **Financial Confidential** | Balances, P&L, Positions | AES-256 at rest, TLS 1.3 transit | Audit events only | 10 years | Front-Office |
| **Trade Secret** | Pricing models, Risk algo | HSM-protected | Restricted audit | Indefinite | Quants only |
| **Internal** | System configs, metrics | Environment-level | INFO level | 1 year | Engineering |
| **Public** | Exchange rates, Calendars | None | Any level | Indefinite | All |

### 4.2 Context-Aware Access Control (v3 — NEW)
```
REQUEST ──► [Context Validator] ──► [Domain Router] ──► [Data Zone]
                │                          │
                ▼                          ▼
        [Auth Token Check]        [Least-Privilege Scope]
                │                          │
                ▼                          ▼
        [Consent Validation]      [Field-Level Masking]
```
- Context includes: `userId`, `roleId`, `requestedScope`, `consentVersion`, `sessionId`
- Domain Router applies ABAC (Attribute-Based Access Control) rules
- Data exposure is gated by both role AND the active consent record

---

## 5. REGULATORY CONSTRAINTS — COMPREHENSIVE (v3)

### 5.1 GDPR (EU 2016/679)
| Article | Requirement | Implementation |
|---------|-------------|----------------|
| Art. 5 | Data Minimization | Collect only fields with documented lawful basis |
| Art. 6 | Lawful Basis | Consent management with versioned records |
| Art. 17 | Right to Erasure | Crypto-shredding with audit trail |
| Art. 20 | Portability | Export API in JSON-LD and CSV formats |
| Art. 25 | Privacy by Design | Encryption and pseudonymization in all new features |
| Art. 32 | Security | AES-256 at rest, TLS 1.3 in transit, RBAC |
| Art. 33 | Breach Notification | Automated alert within 72 hours to DPA |

### 5.2 PSD2 / Open Banking (EU 2015/2366 + RTS)
| Requirement | Constraint | Implementation |
|-------------|------------|----------------|
| SCA | All customer-initiated payments carry `AuthenticationToken` | FIDO2 / OTP |
| API Security | OAuth2 + MTLS for all third-party access | mTLS termination at gateway |
| Consent Refresh | 90-day re-authentication for AIS | Consent expiry calendar |
| Transaction Monitoring | Real-time fraud screening < 100ms | ML scoring pipeline |

### 5.3 AML / CTF — Anti-Money Laundering
| Rule | Threshold | Trigger | Action |
|------|-----------|---------|--------|
| Large Transaction Reporting | > €10,000 | Single transaction | `AmlScreeningEvent` + SAR |
| Structuring Detection | Multiple < €10,000 within 24h | Pattern analysis | Automatic escalation |
| Sanctions Screening | All cross-border + domestic high-risk | Real-time | OFAC / EU / UN list check |
| PEP Screening | Account opening + annual review | Name + DOB match | Enhanced Due Diligence |
| Beneficial Ownership | Corporate accounts > 25% ownership | KYB verification | Registry cross-check |

### 5.4 PCI-DSS v4.0
| Requirement | Implementation |
|-------------|----------------|
| 3.2.1 | Never store SAD (Sensitive Authentication Data) post-authorization |
| 3.5.1 | PAN rendered unreadable via tokenization + masking (first 6, last 4) |
| 6.4.1 | WAF in active blocking mode for all web-facing PCI endpoints |
| 8.4.2 | MFA required for all non-console administrative access |
| 10.3.3 | Audit logs protected from destruction (immutable, WORM storage) |
| 12.3.3 | Annual cryptographic algorithm review and upgrade plan |

### 5.5 Basel III/IV — Capital Adequacy
| Requirement | Constraint | Code Impact |
|-------------|------------|-------------|
| Credit RWA | Risk weight calculation per asset class | `CreditRwaCalculator` with Basel formulae |
| Market Risk (FRTB) | IMA/SA boundary determination | `MarketRiskEngine` with P&L attribution |
| Operational Risk (SMA) | Business Indicator Component calculation | `OperationalRiskCalculator` |
| Leverage Ratio | Min 3% (Tier 1 / Total Exposure) | `LeverageRatioMonitor` real-time |
| LCR | Liquidity Coverage Ratio > 100% | `LiquidityCoverageEngine` daily |

### 5.6 DORA (Digital Operational Resilience Act) — NEW in v3
| Pillar | Requirement | Implementation |
|--------|-------------|----------------|
| ICT Risk Management | Documented risk taxonomy | `IctRiskRegister` with severity matrix |
| Incident Reporting | Major incident to competent authority < 4h | `DoraIncidentReporter` automated |
| Operational Resilience Testing | TLPT (Threat-Led Penetration Testing) | Annual red team integration |
| Third-Party Risk | ICT concentration risk monitoring | `ThirdPartyRiskDashboard` |
| Information Sharing | Threat intelligence sharing | `ThreatIntelligenceFeed` integration |

### 5.7 Solvency II / IFRS 17 (Insurance)
| Standard | Requirement | Code Constraint |
|----------|-------------|-----------------|
| SCR | Solvency Capital Requirement calculation | `ScrCalculator` with standard formula |
| Technical Provisions | Best Estimate + Risk Margin | `TechnicalProvisionEngine` |
| IFRS 17 GMM | General Measurement Model (BBA) | `InsuranceContractMeasurement` |
| IFRS 17 PAA | Premium Allocation Approach | `PaaEligibilityChecker` |
| Reinsurance | Separate CSM tracking | `ReinsuranceContractLedger` |

---

## 6. ADVANCED ARCHITECTURAL CONSTRAINTS (v3)

### 6.1 Hexagonal Architecture with Context Ports
```
                    ┌──────────────────────────────────────────┐
                    │             DOMAIN CORE                   │
                    │   (Pure Business Logic + State Machines)  │
                    └─────────────────┬────────────────────────┘
                                      │
              ┌───────────────────────┼──────────────────────┐
              │                       │                      │
    ┌─────────▼──────────┐  ┌────────▼─────────┐  ┌────────▼─────────┐
    │   INBOUND PORTS    │  │  CONTEXT PORTS   │  │  OUTBOUND PORTS  │
    │  (Driving Adapters)│  │  (NEW in v3)     │  │ (Driven Adapters)│
    ├────────────────────┤  ├──────────────────┤  ├──────────────────┤
    │ REST API           │  │ Domain Context   │  │ DB Repository    │
    │ Event Consumer     │  │ Regulatory Store │  │ Message Broker   │
    │ Batch Job          │  │ Memory Layer     │  │ External API     │
    │ CLI                │  │ RAG Retriever    │  │ Audit Sink       │
    └────────────────────┘  └──────────────────┘  └──────────────────┘
```

### 6.2 Context Ports (v3 — NEW)
Context Ports are a new architectural primitive for BFSI AI-assisted systems:
- **Domain Context Port:** Provides live access to domain instruction embeddings
- **Regulatory Context Port:** Streams applicable regulation snippets per operation
- **Memory Context Port:** Maintains conversation/session state for multi-step operations
- **Conflict Resolution Port:** Arbitrates between competing domain rules

### 6.3 Event Sourcing + CQRS + Context Streaming
```
COMMAND ──► [Context Enricher] ──► [Command Handler] ──► [Event Store]
                                                               │
READ ◄────── [Read Model] ◄──── [Projection] ◄────────────────┘
                 │
                 ▼
         [Context-Aware Query] ──► [Regulatory Filter] ──► [Masked Response]
```

---

## 7. CONTEXT ENGINEERING PATTERNS (v3 — NEW)

### 7.1 Mandatory Context Engineering Patterns
| Pattern | Use Case | Implementation |
|---------|----------|----------------|
| **Sliding Window Context** | Long transaction histories | Keep last N + summary of prior |
| **Hierarchical Summarization** | Compress large domain docs | Multi-level extractive summaries |
| **Retrieval-Augmented Generation (RAG)** | Domain rule lookup | Vector similarity > 0.85 threshold |
| **Chain-of-Thought Injection** | Complex compliance checks | Step-by-step reasoning scaffolds |
| **Instruction Hierarchy Enforcement** | Rule conflict resolution | Priority queue-based injection |
| **Context Provenance Tracking** | Audit trail for AI decisions | Each context chunk tagged with source |
| **Negative Space Prompting** | Prevent hallucination | Explicit "you MUST NOT assume" clauses |
| **Role-Conditioned Context** | RBAC-aware context delivery | Filter context by requestor's role |

### 7.2 Context Compression Algorithm
When token budget < required context:
1. Rank all context chunks by relevance score (cosine similarity to query)
2. Retain top-K chunks that fit within 80% of token budget
3. Summarize remaining chunks using extractive summarization (1 sentence per 10 lines)
4. Prepend priority-ordered: Regulatory > Domain-Specific > Working Context
5. Always preserve the complete "FORBIDDEN OPERATIONS" section

### 7.3 Anti-Hallucination Controls
- Provide explicit citation references for every domain rule used
- Use `[SOURCE: file.md §Section]` notation in generated code comments
- Enforce "closed-world assumption": if not in instruction files, it does not exist
- Flag all assumptions made during generation with `[ASSUMED: justification]`
- Require confidence scores for any regulatory interpretation (< 0.9 → flag for human review)

---

## 8. PYTHON-SPECIFIC IMPLEMENTATION STANDARDS (v3 — NEW)

### 8.1 Python Type System Requirements
```python
# MANDATORY: All financial models use Pydantic v2 with strict mode
from decimal import Decimal, ROUND_HALF_EVEN
from pydantic import BaseModel, field_validator, model_validator
from typing import Final, Literal, Annotated, NewType

# Money type: Never use float
MoneyAmount = Annotated[Decimal, "Financial amount with DECIMAL128 precision"]
CurrencyCode = Literal["EUR", "USD", "GBP", "JPY", "CHF", "SGD"]  # Extend as needed

class Money(BaseModel):
    model_config = {"frozen": True, "strict": True}
    amount: Decimal
    currency: CurrencyCode

    @field_validator("amount")
    @classmethod
    def validate_amount(cls, v: Decimal) -> Decimal:
        if v < 0:
            raise ValueError("Amount cannot be negative")
        return v.quantize(Decimal("0.0001"), rounding=ROUND_HALF_EVEN)
```

### 8.2 Python Domain Event Pattern
```python
from dataclasses import dataclass, field
from datetime import datetime, timezone
from uuid import UUID, uuid4
from typing import ClassVar

@dataclass(frozen=True)
class DomainEvent:
    event_id: UUID = field(default_factory=uuid4)
    occurred_at: datetime = field(default_factory=lambda: datetime.now(timezone.utc))
    correlation_id: UUID = field(default_factory=uuid4)
    event_version: ClassVar[str] = "1.0"
```

### 8.3 Python Error Handling — Result Pattern
```python
from typing import Generic, TypeVar, Union
from dataclasses import dataclass

T = TypeVar("T")
E = TypeVar("E")

@dataclass(frozen=True)
class Ok(Generic[T]):
    value: T

@dataclass(frozen=True)
class Err(Generic[E]):
    error: E

Result = Union[Ok[T], Err[E]]
```

---

## 9. TESTING REQUIREMENTS (ENHANCED v3)

### 9.1 Coverage Thresholds
| Component | Min Coverage | Critical Path | Financial Calc | AI Output |
|-----------|-------------|---------------|----------------|-----------|
| Domain Logic | 100% | 100% | 100% | N/A |
| Application Services | 85% | 100% | 100% | 90% |
| Infrastructure | 75% | 95% | N/A | N/A |
| Security Controls | 100% | 100% | N/A | N/A |
| Context Engine | 90% | 100% | N/A | 95% |

### 9.2 Context Engineering Test Requirements (v3 — NEW)
- **Context Retrieval Tests:** Verify relevant chunks are retrieved (precision > 0.90)
- **Context Compression Tests:** Verify compressed context retains critical rules
- **Conflict Resolution Tests:** Verify deterministic winner when rules conflict
- **Hallucination Detection Tests:** Verify AI outputs cite valid sources
- **Token Budget Tests:** Verify generation stays within budget at various sizes
- **Regulatory Completeness Tests:** Verify all applicable regulations are injected

---

## 10. OBSERVABILITY (ENHANCED v3)

### 10.1 Context Engineering Observability
| Metric | Description | Alert Threshold |
|--------|-------------|-----------------|
| `context_retrieval_latency_ms` | Time to retrieve domain context | > 200ms |
| `context_relevance_score` | Average cosine similarity of injected context | < 0.75 |
| `context_compression_ratio` | Input tokens / output tokens after compression | < 0.3 |
| `hallucination_rate` | % outputs with uncited regulatory claims | > 1% |
| `conflict_resolution_count` | Daily rule conflicts resolved | Spike > 2x baseline |
| `token_budget_exceeded_count` | Times token budget was exhausted | Any occurrence |

### 10.2 Standard Financial Metrics (Retained from v2)
| Metric | Type | Labels |
|--------|------|--------|
| `transactions_processed_total` | Counter | domain, scheme, currency |
| `transaction_amount_sum` | Gauge | domain, direction |
| `settlement_latency_seconds` | Histogram | scheme, corridor |
| `rejected_transactions_total` | Counter | domain, reason_code |
| `aml_screening_triggers_total` | Counter | rule_id, outcome |

---

## 11. DOCUMENTATION REQUIREMENTS (ENHANCED v3)

### 11.1 Mandatory Files
| File | Purpose | New in v3 |
|------|---------|-----------|
| `README.md` | Project overview, architecture, quick start | No |
| `CONTEXT_ENGINEERING.md` | Context layer documentation, retrieval strategy | YES |
| `SECURITY.md` | Threat model, controls, incident response | No |
| `COMPLIANCE_MATRIX.md` | Regulation → code mapping | No |
| `AI_GOVERNANCE.md` | AI output review policy, hallucination controls | YES |
| `ADR/` | Architecture Decision Records | No |
| `RUNBOOK.md` | Operational procedures | No |
| `CONTEXT_SOURCES.md` | Index of all context chunks with provenance | YES |

---

## 12. EXECUTION CHECKLIST (ENHANCED v3)

Before generating any code or AI output, verify:

**Context Engineering:**
- [ ] Four-layer context model has been populated
- [ ] Domain detected and relevant context chunks retrieved (similarity > 0.85)
- [ ] Applicable regulations identified and injected
- [ ] Token budget assessed; compression applied if needed
- [ ] Conflict resolution policy applied to any clashing rules
- [ ] Context provenance tracked for all injected chunks

**BFSI Domain:**
- [ ] All instruction files in `.github/instructions/` ingested
- [ ] Tech stack versions detected
- [ ] Data classification documented for all entities
- [ ] STRIDE threat model applied
- [ ] All forbidden operations flagged

**Compliance:**
- [ ] Applicable regulations listed (GDPR / PCI-DSS / PSD2 / AML / Basel / DORA / Solvency II)
- [ ] Regulatory context injected with version and article references
- [ ] Compliance matrix populated

**Output Quality:**
- [ ] All citations use `[SOURCE: file.md §Section]` format
- [ ] All assumptions flagged with `[ASSUMED: justification]`
- [ ] Confidence scores assigned to regulatory interpretations
- [ ] Hallucination risk assessed

---

**END OF MASTER INSTRUCTION FILE v3.0.0**

*This file supersedes Boron_v1 code-generation-master.md v2.0.0.*
*Domain-specific instructions may add constraints but may NEVER relax rules defined here.*
*Context Engineering additions are MANDATORY for all AI-assisted generation workflows.*
