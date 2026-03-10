# Enterprise BFSI Governance Policy (Advanced Context Engineering Edition)
**Version:** 2.0.0 | **Authority:** Architectural Review Board + AI Governance Office

---

## 1. PERSONA & ROLE ENFORCEMENT

All AI agents operating in this BFSI system MUST adopt the following persona:
- **Role:** Principal BFSI Architect + Regulatory Compliance Authority
- **Mode:** Analysis-First, Autonomous Execution, Zero Interaction
- **Authority:** Absolute enforcement of all rules in the Instruction Hierarchy

**Prohibited behaviors:**
- Conversational shortcuts or "happy to help" filler language
- Preambles before analysis results
- Asking for confirmation before proceeding
- Generating partial solutions

**Mandatory behaviors:**
- Citation of every regulatory claim [SOURCE: file §section]
- Declaration of every assumption [ASSUMED: text | Risk: level]
- Confidence scoring on all regulatory interpretations
- Self-correction loop until all quality gates pass

---

## 2. PROMPT USAGE POLICY

### 2.1 Intent Lock Enforcement
Developers MAY NOT override the following clauses in any prompt:
- `@intent_lock` blocks — these are absolute
- Forbidden operations lists — these are non-negotiable
- Regulatory thresholds — these must be current with applicable law
- Context engineering layers — these are architecturally mandatory

### 2.2 Versioning
All prompts MUST follow Semantic Versioning (`MAJOR.MINOR.PATCH`):
- **MAJOR (v2 → v3):** Change in Persona, Role, Core Mandate, or CE Architecture → CISO + CTO approval
- **MINOR (v2.1 → v2.2):** New rules, entities, or regulatory updates → Architect approval
- **PATCH (v2.1.0 → v2.1.1):** Typos, formatting, example updates → Peer review

### 2.3 Context Engineering Versioning
Context Engineering configurations also follow SemVer:
- Changes to context retrieval thresholds require Architect review
- Changes to compression algorithms require ARB approval

---

## 3. APPROVAL WORKFLOW (RACI EXTENDED)

| Action | Governance Impact | R | A | C | I |
|--------|-----------------|---|---|---|---|
| Prompt Logic Change | AI behavior change | Lead Dev | Architect | Compliance, Security | Team |
| New Domain Entity | Data model change | Data Steward | Domain Owner | Architect | Team |
| Rule Relaxation | Safety reduction | Architect | **CISO** | Compliance, Risk | Auditors |
| New Regulatory Rule | Compliance scope | Compliance Officer | General Counsel | ARB | All |
| CE Threshold Change | Retrieval behavior | AI Engineer | Architect | ML Platform | Team |
| Emergency Override | Bypasses controls | Ops Lead | CTO | Risk, Compliance | Team |
| Hallucination Flag | AI quality issue | AI Engineer | AI Gov Officer | Compliance | All |

---

## 4. AI GOVERNANCE REQUIREMENTS (NEW IN v2)

### 4.1 AI Output Classification
Every AI-generated output MUST be classified:
| Class | Description | Review Required |
|-------|-------------|----------------|
| **Regulatory Code** | Directly implements compliance logic | Compliance Officer review |
| **Financial Logic** | Monetary calculations, P&L, positions | Finance Controller review |
| **Security Code** | Encryption, auth, access control | CISO review |
| **Infrastructure Code** | DB, messaging, deployment | Senior Engineer review |
| **Test Code** | Unit, integration, property tests | Lead Developer review |

### 4.2 Hallucination Detection Controls
- All AI outputs scanned for uncited regulatory claims (automated)
- Confidence score < 0.85 → mandatory human review before merge
- Any `[ASSUMED: ... Risk: HIGH]` annotation → review gate in CI/CD pipeline

### 4.3 AI Audit Trail Retention
- All context packages used for generation retained for 90 days
- All AI-generated code commits tagged with: model version, prompt version, confidence score
- AI audit logs are immutable (WORM storage)

---

## 5. EMERGENCY OVERRIDE PROTOCOL

In **P1 Critical Incident** (Production Outage):
1. `@intent_lock` and context engineering constraints MAY be suspended by Staff Engineer only
2. All manual code changes must be logged in `EmergencyChangeRecord`
3. **Post-Mortem:** Filed within 24 hours
4. **Resync:** Manual changes back-ported to instruction files within 3 business days
5. **AI Re-training trigger:** If override reveals gap in instruction files → update and re-embed

---

## 6. CONTEXT ENGINEERING GOVERNANCE

### 6.1 Instruction File Management
- All changes to instruction files require pull request with mandatory reviewer
- Instruction files are version-controlled alongside source code
- Embedding re-generation triggered automatically on any instruction file change
- Stale embeddings (> 30 days without re-generation check) flagged in monitoring

### 6.2 Domain Knowledge Quality SLO
| Metric | Target | Alert |
|--------|--------|-------|
| Instruction File Coverage (entities) | 100% of domain entities referenced | Any gap |
| Regulatory Rule Freshness | < 90 days from last review date | > 90 days |
| Retrieval Precision (automated) | ≥ 0.90 | < 0.80 |
| Context Conflict Rate | < 2 conflicts per 100 requests | > 5 |
