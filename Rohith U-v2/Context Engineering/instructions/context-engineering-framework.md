# Context Engineering Framework for BFSI AI Systems
**Version:** 1.0.0
**Authority:** Architectural Review Board (ARB) + AI Governance Office
**Scope:** All AI-assisted code generation, review, and analysis in BFSI workloads

---

## 1. WHAT IS CONTEXT ENGINEERING

Context Engineering is the practice of deliberately designing, managing, and optimizing
the information provided to large language models (LLMs) in order to maximize:
- **Correctness** of generated outputs
- **Regulatory compliance** of recommended patterns
- **Consistency** across repeated interactions
- **Auditability** of AI decision chains

In BFSI, this is critical because:
- A hallucinated regulatory threshold can result in a €10M+ fine
- A missing AML check that went undetected due to incomplete context = criminal liability
- Inconsistent rounding behavior injected by an LLM = P&L mismatch across books

---

## 2. CONTEXT LAYERS — DETAILED SPECIFICATION

### Layer 0: System Persona Context
**Persistence:** Always present — injected at session start, never evicted
**Token Budget:** Reserve first 500 tokens of every context window

Content must include:
- Role definition: "You are a Principal BFSI Architect and Compliance Auditor"
- Authority level: ABSOLUTE enforcement of all rules in this layer
- Intent lock: No interaction mode, no assumptions mode
- Output format constraints: File-by-file, citations required, confidence scores required
- Prohibited behaviors: List of things the model must never do

```
[PERSONA BLOCK — ALWAYS FIRST]
Role: Principal BFSI Architect, Regulatory Compliance Authority, Security Auditor
Authority: Absolute — block all violations without exception
Mode: Autonomous execution — no questions, no pauses, no assumptions
Output: File-by-file with [SOURCE:], [ASSUMED:], [CONFLICT-RESOLVED:] inline tags
Prohibited: Hallucinated entity names, uncited regulatory claims, partial generation
```

### Layer 1: Regulatory Context
**Persistence:** Session-persistent, refreshed on regulation version change
**Token Budget:** 1500–2500 tokens depending on applicable framework set
**Retrieval Trigger:** Always injected; further filtered by detected domain

Regulatory bundles by domain:
| Domain | Applicable Regulations |
|--------|------------------------|
| Payments | PSD2, PCI-DSS v4, AML 6AMLD, SEPA rulebooks, ISO 20022 |
| Core Banking | GDPR, PCI-DSS, CRR III, Basel III/IV, FATCA, CRS |
| Capital Markets | MiFID II/MiFIR, EMIR, CSDR, SFTR, MAR |
| Treasury | LCR, NSFR, SA-CCR, G-SIB, FRTB |
| Risk & Compliance | BCBS 239, AML 6AMLD, FATF 40 Recommendations, DORA |
| Insurance | Solvency II, IFRS 17, IDD, GDPR, DORA |
| Accounting | IFRS 9, IFRS 15, IFRS 16, IFRS 17, IAS 39, SOX |
| FP&A | ICAAP, ILAAP, Recovery Planning, Stress Testing (EBA) |

### Layer 2: Domain Context
**Persistence:** Request-scoped, evicted after each generation
**Token Budget:** 1000–3000 tokens (adaptive based on query complexity)
**Retrieval Method:** RAG with cosine similarity threshold ≥ 0.85

Context chunks are ordered by:
1. Direct entity references (exact name match: +1.0 boost)
2. Business rule severity (BLOCKER rules: +0.5 boost)
3. Recency of access in current session (+0.2 boost for recently touched rules)
4. Semantic similarity score (baseline)

### Layer 3: Working Context
**Persistence:** Per-task, ephemeral
**Token Budget:** Remaining tokens after Layers 0–2
**Content:** Active code files, current task parameters, prior output in iterative mode

---

## 3. CONTEXT RETRIEVAL PIPELINE

```
USER REQUEST
     │
     ▼
[1. INTENT CLASSIFIER]
     │ Detects: domain, action_type, entities_mentioned
     ▼
[2. REGULATORY SELECTOR]
     │ Maps domain → applicable regulation bundle
     ▼
[3. DOMAIN RETRIEVER]
     │ Queries vector store: top-K chunks where similarity > 0.85
     │ Applies entity boost and recency boost
     ▼
[4. CONTEXT COMPOSER]
     │ Assembles Layer 0 + Layer 1 + Layer 2 + Layer 3
     │ Checks total token count
     ▼
[5. COMPRESSION ENGINE] (conditional)
     │ IF total_tokens > budget THEN compress Layer 1 and 2
     │ Method: Hierarchical extractive summarization
     ▼
[6. CONFLICT DETECTOR]
     │ Checks for contradicting rules across layers
     │ Applies priority resolution: higher layer wins
     ▼
[7. CONTEXT PACKAGE]
     │ Final assembled context with provenance tags
     ▼
[LLM GENERATION]
     │
     ▼
[8. OUTPUT VALIDATOR]
     │ Checks: citations present, confidence scores present
     │ Scans: forbidden patterns, uncited claims
     ▼
FINAL OUTPUT
```

---

## 4. CONTEXT COMPRESSION SPECIFICATION

### 4.1 When Compression Is Required
Trigger compression when assembled context exceeds 80% of the available token budget.

### 4.2 Compression Algorithm
```
INPUTS:
  - context_chunks: List[{text, source, relevance_score, layer}]
  - token_budget: int
  - reserved_tokens: int (for generation output)

ALGORITHM:
  available = token_budget - reserved_tokens - layer_0_tokens
  
  Step 1: Always include Layer 0 (Persona) fully
  Step 2: Sort Layer 1 (Regulatory) chunks by relevance descending
  Step 3: Sort Layer 2 (Domain) chunks by (severity_score * relevance) descending
  Step 4: Fit chunks greedily until available budget is 90% used
  Step 5: For remaining chunks, apply extractive summarization
          - Extract 1 sentence per 10 lines
          - Preserve: thresholds, identifiers, forbidden operations verbatim
  Step 6: Append [COMPRESSED: N chunks summarized] tag to compressed sections
  
OUTPUT: context_package with total_tokens ≤ token_budget
```

### 4.3 Sacrosanct Content — Never Compress
The following content must ALWAYS be included verbatim regardless of token budget:
- All BLOCKER-severity rules from master instruction file
- PCI-DSS CHD storage prohibitions
- AML threshold values
- GDPR lawful basis requirements
- All `FORBIDDEN OPERATIONS` sections

---

## 5. CONFLICT RESOLUTION SPECIFICATION

### 5.1 Rule Conflict Types
| Type | Example | Resolution |
|------|---------|------------|
| **Same-layer contradiction** | Two domain files give different AML thresholds | Take the LOWER (more restrictive) threshold |
| **Cross-layer contradiction** | Domain rule relaxes a master-file blocker | Master file wins; log `[CONFLICT-RESOLVED: master]` |
| **Regulatory version conflict** | PCI-DSS v3.2.1 vs v4.0 rule difference | Apply the NEWER version; log version |
| **Jurisdiction conflict** | EU rule vs US rule for cross-border | Apply STRICTER of the two; flag for legal review |
| **Domain overlap** | Payment in insurance context | Apply primary domain first, supplement with secondary |

### 5.2 Conflict Resolution Log Format
Every conflict must be documented inline:
```
[CONFLICT-RESOLVED: {rule_a_source} vs {rule_b_source} → applied {winner} because {reason}]
```

---

## 6. ANTI-HALLUCINATION FRAMEWORK

### 6.1 Closed-World Assumption
The model MUST operate under the closed-world assumption:
- If an entity, rule, or threshold is not found in the instruction files → it does not exist
- Model must NOT invent domain entities, regulatory thresholds, or technical standards
- When encountering unknown concepts → output `[UNKNOWN: concept not found in context; halted]`

### 6.2 Citation Requirements
Every regulatory claim, threshold, or pattern in generated output must carry:
```
[SOURCE: {filename}.md §{section_number}]
```
Examples:
- `[SOURCE: code-generation-master.md §5.3]` for AML rule
- `[SOURCE: payments/regulatory-constraints.md §4.1]` for SEPA rule
- `[SOURCE: risk-compliance/compliance-rules.md §2]` for compliance enforcement

### 6.3 Confidence Scoring
| Score Range | Interpretation | Action |
|-------------|----------------|--------|
| 0.95 – 1.00 | High confidence | Proceed; include score in output |
| 0.85 – 0.94 | Moderate confidence | Proceed; flag as `[VERIFY: score=X.XX]` |
| 0.70 – 0.84 | Low confidence | PAUSE; escalate for human review |
| < 0.70 | Insufficient context | HALT; request context enrichment |

### 6.4 Assumption Declaration
When the model must make an assumption due to missing context:
```
[ASSUMED: {assumption} | Basis: {closest_rule_found} | Risk: HIGH/MEDIUM/LOW]
```
All HIGH-risk assumptions must be highlighted in `README.md` under "Known Assumptions".

---

## 7. CONTEXT MEMORY MANAGEMENT

### 7.1 Session Memory Architecture
```
┌──────────────────────────────────────────────────────────┐
│                    SESSION MEMORY STORE                   │
├──────────────────────────────────────────────────────────┤
│  PERSISTENT LAYER (survives session restart)              │
│  ├── Ingested domain knowledge embeddings                 │
│  ├── Regulatory version index                             │
│  └── Entity registry (approved names and types)           │
├──────────────────────────────────────────────────────────┤
│  SESSION LAYER (survives within active session)           │
│  ├── Entities created/referenced in this session          │
│  ├── Conflict resolutions made                            │
│  └── Decisions and their rationale                        │
├──────────────────────────────────────────────────────────┤
│  WORKING MEMORY (per-turn, ephemeral)                     │
│  ├── Current task parameters                              │
│  ├── Active code under analysis                           │
│  └── Intermediate reasoning steps                         │
└──────────────────────────────────────────────────────────┘
```

### 7.2 Memory Eviction Policy
- Working memory: evict immediately after turn completes
- Session layer: evict after 60 minutes of inactivity OR explicit `reset` command
- Persistent layer: evict only on explicit re-ingestion command OR version bump

### 7.3 Context Provenance Index
Every context chunk injected into the model must be recorded:
```json
{
  "chunk_id": "uuid",
  "source_file": "payments/regulatory-constraints.md",
  "section": "§4.1",
  "relevance_score": 0.91,
  "layer": 2,
  "compressed": false,
  "injected_at": "2026-03-10T10:00:00Z",
  "turn_id": "uuid"
}
```

---

## 8. CONTEXT ENGINEERING GOVERNANCE

### 8.1 Context Audit Trail
All context engineering decisions must be logged to the `AI_AUDIT_LOG`:
- What context chunks were retrieved
- What compression decisions were made
- What conflicts were resolved and how
- What assumptions were declared
- What confidence scores were assigned

### 8.2 Context Engineering Review Gates
| Gate | Trigger | Reviewer | Action |
|------|---------|----------|--------|
| **High-Risk Assumption** | `[ASSUMED: ... Risk: HIGH]` | Lead Architect | Approve/reject before merge |
| **Low Confidence Output** | Score < 0.84 | Compliance Officer | Manual regulatory check |
| **Conflict Resolution** | Any `[CONFLICT-RESOLVED]` | Domain Owner | Verify resolution correctness |
| **New Regulation Detected** | Unknown regulation referenced | Regulatory Team | Update instruction files |

### 8.3 Context Engineering Metrics SLO
| Metric | Target | Critical Threshold |
|--------|--------|--------------------|
| Context Retrieval Precision | ≥ 0.90 | < 0.80 |
| Context Recall (critical rules) | 1.00 | < 0.95 |
| Hallucination Rate | < 0.5% | > 1.0% |
| Token Budget Efficiency | ≥ 0.85 utilization | < 0.60 |
| Conflict Resolution Accuracy | ≥ 0.99 | < 0.95 |

---

**END OF CONTEXT ENGINEERING FRAMEWORK v1.0.0**

*This framework is mandatory for all AI-assisted workflows in the BFSI system.*
*Failure to follow context engineering principles may result in regulatory non-compliance.*
