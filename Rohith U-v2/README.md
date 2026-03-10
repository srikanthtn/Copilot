# Advanced BFSI Context Engineering System — v2.0.0

> **Rohith U-v2** — A production-grade Context Engineering framework for AI-assisted Banking, Financial Services, and Insurance (BFSI) code generation.

---

## Overview

This folder contains the complete implementation of an advanced **Context Engineering** system designed to produce consistently correct, regulatory-compliant, and audit-ready BFSI code using GitHub Copilot.

It supersedes the Boron_v1 prompt engineering system with a fully redesigned architecture featuring:

| Feature | Boron_v1 | Rohith U-v2 |
|---|---|---|
| Language | Scala 2.13 first | **Python 3.12 first** |
| Architecture | Flat instruction files | **4-Layer Context Model** |
| Retrieval | None | **RAG (cosine ≥ 0.85)** |
| Anti-hallucination | None | **Closed-World + Citations** |
| Code Reviewer | 100-point | **120-point (Field 1 = CE Quality)** |
| Regulatory coverage | PSD2, GDPR, AML | + **DORA, IFRS 17, Basel IV, FRTB** |
| Conflict resolution | None | **Priority-tree arbitration** |
| Token management | None | **Hierarchical compression** |
| Financial precision | Scala BigDecimal | **Python Decimal(prec=28, HALF_EVEN)** |

---

## Architecture

```
┌──────────────────────────────────────────────────────────┐
│           BFSI Context Engineering — 4-Layer Model       │
├──────────────────────────────────────────────────────────┤
│  Layer 0 │ PERSONA         │ Always present │  500 tokens │
│  Layer 1 │ REGULATORY      │ Session-persist │ 1,500 tok  │
│  Layer 2 │ DOMAIN (RAG)    │ Request-scoped  │ 2,000 tok  │
│  Layer 3 │ WORKING         │ Ephemeral       │ Remaining  │
└──────────────────────────────────────────────────────────┘
                           │
                 ┌─────────▼─────────┐
                 │  Context Engine   │
                 │  ─────────────    │
                 │  DomainRouter     │  detect_domain()
                 │  RagRetriever     │  cosine ≥ 0.85
                 │  Compressor       │  hierarchical
                 │  ConflictResolver │  priority-tree
                 │  PromptBuilder    │  layer-ordered
                 └─────────┬─────────┘
                           │
              ┌────────────▼────────────┐
              │  Quality Gates (5)      │
              │  Gate 1: CE Quality     │
              │  Gate 2: Security       │
              │  Gate 3: Compliance     │
              │  Gate 4: Code Quality   │
              │  Gate 5: CE Output      │
              └─────────────────────────┘
```

---

## Folder Structure

```
Rohith U-v2/
├── .github/
│   ├── instructions/
│   │   ├── code-generation-master.md         ← ABSOLUTE authority (v3.0.0)
│   │   ├── context-engineering-framework.md  ← CE architecture (v1.0.0) [NEW]
│   │   ├── payments/domain-master.md
│   │   ├── core-banking/domain-master.md
│   │   ├── capital-markets/domain-master.md
│   │   ├── treasury/domain-master.md
│   │   ├── risk-compliance/domain-master.md
│   │   ├── insurance/domain-master.md
│   │   └── accounting-audit/domain-master.md
│   ├── prompts/
│   │   ├── bfsi-architect.prompt.md           ← Primary generation prompt (v3.0.0)
│   │   ├── code-reviewer.prompt.md            ← 120-point scorecard (v5.0.0)
│   │   ├── unit-test-generator.prompt.md      ← Hypothesis + pytest (v5.0.0)
│   │   ├── SonarQube.prompt.md                ← Static analysis rules (v5.0.0)
│   │   └── comments-generator.prompt.md       ← Documentation (v3.0.0)
│   └── governance/
│       └── policy-master.md                   ← AI governance (v2.0.0)
│
└── python/
    ├── __init__.py
    ├── models/
    │   ├── context_models.py     ← ContextPackage, ContextChunk, enums
    │   ├── financial_models.py   ← Money, IbanNumber, BicCode, DomainEvent
    │   └── domain_models.py      ← PaymentInstruction, BankAccount, TradeOrder…
    ├── context_engine/
    │   ├── bfsi_context_manager.py   ← Orchestrator
    │   ├── domain_router.py          ← Domain detection
    │   ├── rag_retriever.py          ← RAG retrieval
    │   ├── context_compressor.py     ← Hierarchical compression
    │   ├── conflict_resolver.py      ← Priority-tree arbitration
    │   └── prompt_builder.py         ← Prompt assembly
    ├── validators/
    │   ├── financial_validator.py    ← FM-001…FM-010 enforcement
    │   ├── regulatory_validator.py   ← GDPR, PCI-DSS, PSD2, AML checks
    │   └── security_validator.py     ← SEC-001…SEC-008 checks
    ├── pipeline/
    │   ├── bfsi_pipeline.py          ← PySpark 3.5 + Delta Lake
    │   └── context_pipeline.py       ← Context-aware pipeline wrapper
    └── tests/
        ├── test_context_engine.py    ← Context engine unit + integration tests
        ├── test_financial_validator.py  ← Hypothesis property-based tests
        └── test_domain_models.py     ← Domain aggregate tests
```

---

## Quick Start

### Prerequisites

| Tool | Version | Notes |
|---|---|---|
| Python | ≥ 3.12 | `python --version` |
| uv / pip | latest | Dependency management |
| Java | ≥ 11 | Required only for PySpark pipeline |
| PySpark | ≥ 3.5 | Optional — install `[spark]` extra |

### Installation

```bash
# Clone and install dev dependencies
pip install -e ".[dev]"

# Optional: with PySpark support
pip install -e ".[dev,spark]"
```

### Running Tests

```bash
# All tests
pytest

# With coverage
pytest --cov=python --cov-report=term-missing

# Property-based tests only
pytest python/tests/test_financial_validator.py -v

# Context engine tests
pytest python/tests/test_context_engine.py -v
```

### Using the Context Manager

```python
import asyncio
from uuid import uuid4
from python.context_engine.bfsi_context_manager import BfsiContextManager
from python.context_engine.prompt_builder import ContextAwarePromptBuilder
from python.models.context_models import BfsiDomain, SessionId

async def main() -> None:
    manager = BfsiContextManager(
        session_id=SessionId(uuid4()),
        total_token_budget=8192,
    )
    package = await manager.build_context_package(
        domain=BfsiDomain.PAYMENTS,
        task_description="Generate a SEPA Credit Transfer validator with IBAN Mod-97 check",
    )
    builder = ContextAwarePromptBuilder()
    prompt = builder.build(
        package=package,
        task="Generate a SEPA Credit Transfer validator with IBAN Mod-97 check",
    )
    print(f"Confidence: {package.overall_confidence:.1%} [{package.confidence_grade.value}]")
    print(f"Layers: L0={len(package.layer_0)} L1={len(package.layer_1)} "
          f"L2={len(package.layer_2)} L3={len(package.layer_3)}")

asyncio.run(main())
```

---

## GitHub Copilot Integration

All `.github/instructions/` files are automatically picked up by GitHub Copilot as instruction files. The `.github/prompts/` files are available as reusable prompt templates in Copilot Chat.

### Primary Prompt Files

| File | Use In Copilot Chat | Purpose |
|---|---|---|
| `bfsi-architect.prompt.md` | `#bfsi-architect` | Generate new BFSI features |
| `code-reviewer.prompt.md` | `#code-reviewer` | 120-point code review |
| `unit-test-generator.prompt.md` | `#unit-test-generator` | Generate test suites |
| `SonarQube.prompt.md` | `#SonarQube` | Static analysis review |
| `comments-generator.prompt.md` | `#comments-generator` | Generate documentation |

---

## Regulatory Coverage

| Regulation | Domains | Key Controls |
|---|---|---|
| GDPR (EU 2016/679) | All | Consent, retention, PII masking |
| PCI-DSS v4.0 | Payments | PAN tokenization, AES-256-GCM |
| PSD2 + RTS | Payments | SCA >€30, X-Request-ID, idempotency |
| AML 6AMLD | Risk, Core Banking | SAR, PEP screening, €10K threshold |
| Basel III/IV | Capital Markets, Treasury | CET1, LCR≥100%, NSFR≥100%, FRTB |
| MiFID II / EMIR | Capital Markets | Best execution, trade reporting |
| Solvency II | Insurance | SCR/MCR, ORSA |
| IFRS 9 | Accounting, Core Banking | ECL (3-stage), PD/LGD/EAD |
| IFRS 17 | Insurance | CSM, VFA, PAA, BBA |
| DORA | All | Operational resilience, ICT risk |
| SOX 302/404 | Accounting | Control attestation, audit trail |
| FATCA / CRS | Core Banking | AEOI reporting |

---

## Key Engineering Decisions

### 1. Decimal over float — FM-001
All monetary values use `decimal.Decimal` with `prec=28` and `ROUND_HALF_EVEN`. `float` is rejected by all Pydantic validators with a `TypeError`.

### 2. Frozen models for value objects
`Money`, `IbanNumber`, `BicCode`, `ContextChunk` are all `frozen=True` Pydantic models — they are immutable after creation.

### 3. Append-only audit trail
`LedgerEntry` is a frozen dataclass; `JournalEntry` validates double-entry balance at construction time. Nothing is ever mutated.

### 4. PII protection by default
- `IbanNumber.__str__` always returns masked output (`DE89 **** **** 3000`)
- `Money.__str__` returns `EUR [MASKED]`
- `CustomerProfile.__repr__` contains no raw personal data
- SAR narrative is scanned for raw IBAN, email, credit card patterns before acceptance

### 5. Anti-Hallucination via Closed-World Assumption
Every regulatory claim in generated code must cite `[SOURCE: file §section]`. Any claim without a source is flagged as `[VERIFY:]`. The confidence scoring system gates low-confidence output.

---

## Contributing

1. All new domain rules go in `.github/instructions/<domain>/domain-master.md`
2. New Python code must pass: `ruff check .` `mypy python/` `bandit -r python/` `pytest`
3. All monetary fields must use `Decimal` — never `float`
4. All new validators must return `ValidationResult` — never raise directly from business logic
5. All audit events must be immutable and append-only

---

## Version History

| Version | Date | Author | Change |
|---|---|---|---|
| 2.0.0 | 2025-01 | Rohith U | Initial v2 — Context Engineering edition |
| 1.0.0 | — | Boron_v1 | Baseline Scala + Spark edition |
