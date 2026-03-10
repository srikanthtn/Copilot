# CO-STAR Prompt Framework
**Version 2.0.0 | Framework: CO-STAR + PromptML | Domain: BFSI**

---

## What Is This?

The **CO-STAR Prompt** folder contains a complete, production-grade GitHub Copilot prompt and instruction system for **Banking, Financial Services & Insurance (BFSI)** code generation.

Every prompt file and instruction file in this repository is written using the **CO-STAR** framework combined with **PromptML** structured markup, ensuring that Copilot has maximal context, clear objectives, the correct tone, and hard compliance guardrails before generating a single line of code.

---

## CO-STAR Framework

CO-STAR is a prompt engineering methodology that structures every prompt into six dimensions:

| Dimension | Meaning                                                                 |
|-----------|-------------------------------------------------------------------------|
| **C** — Context  | Background information about the system, domain, and codebase     |
| **O** — Objective | The precise goal or task the model must accomplish               |
| **S** — Style    | The writing/coding style, patterns, and architecture to apply      |
| **T** — Tone     | Formal, regulatory-grade, safety-conscious communication style     |
| **A** — Audience | Senior Scala / BFSI engineers; compliance teams; code reviewers    |
| **R** — Response | Exact output format — files, PromptML, tables, reports             |

In every `.prompt.md` file you will find:

```xml
<prompt id="..." version="2.0.0" framework="CO-STAR+PromptML">
  <context>...</context>
  <objective>...</objective>
  <style>...</style>
  <tone>...</tone>
  <audience>...</audience>
  <response>...</response>
  <constraints id="...">
    <rule id="...">...</rule>
  </constraints>
</prompt>
```

---

## Folder Structure

```
CO-STAR_Prompt/
│
├── README.md                           ← This file
│
└── .github/
    ├── prompts/                        ← Active Copilot prompt files (5)
    │   ├── code-generator.prompt.md    ← Full project code generation
    │   ├── code-reviewer.prompt.md     ← Automated code review
    │   ├── unit-test-generator.prompt.md ← ScalaTest + ScalaCheck tests
    │   ├── comments-generator.prompt.md  ← Scaladoc + compliance comments
    │   └── SonarQube.prompt.md         ← Quality gate simulation
    │
    ├── governance/                     ← Policy and process governance (5)
    │   ├── policy-master.md
    │   ├── prompt-usage-policy.md
    │   ├── versioning-strategy.md
    │   ├── approval-workflow.md
    │   └── instruction-change-process.md
    │
    └── instructions/                   ← Domain knowledge base (50+ files)
        ├── code-generation-master.md   ← Universal tech stack baseline
        │
        ├── payments/                   ← ISO 20022, PSD2, SWIFT, SCA
        ├── core-banking/               ← Accounts, ledgers, GL
        ├── risk-compliance/            ← Basel III/IV, AML, KYC, FRTB
        ├── treasury/                   ← Cash management, FX, liquidity
        ├── capital-markets/            ← Trading, settlement, clearing
        ├── accounting-audit/           ← IFRS 9/16/17, audit controls
        ├── fpna/                       ← Budgeting, forecasting, variance
        ├── insurance/                  ← Solvency II, IDD, IFRS 17
        └── public-finance-regulation/  ← SGP, IPSAS, GFS, EDP
```

---

## Technology Stack (Enforced in All Generated Code)

| Component       | Version         |
|-----------------|-----------------|
| Scala           | 2.13.14         |
| Apache Spark    | 3.5.1           |
| Delta Lake      | 3.1.0           |
| sbt             | 1.9.x           |
| Java            | 17 LTS (Temurin)|
| ScalaTest       | 3.2.18          |
| ScalaCheck      | Bundled         |
| Cats            | 2.10.0          |
| Cats-Effect     | 3.5.4           |
| Prometheus      | 0.16.0          |

> **All monetary values use `scala.math.BigDecimal`. `Double` and `Float` are forbidden.**

---

## How to Use Each Prompt

### 1. Code Generator (`code-generator.prompt.md`)
Generates a complete 27-file BFSI Scala project with domain model, services, Spark pipelines, observability, and tests.

**Use when:** Starting a new BFSI microservice or data pipeline.

**Attach instructions:** Relevant domain folder (e.g., `payments/`) + `code-generation-master.md`

---

### 2. Code Reviewer (`code-reviewer.prompt.md`)
Runs 8-phase automated review covering architecture, security, Spark patterns, regulatory compliance, and test coverage.

**Use when:** Reviewing a PR or generated code before merge.

**Output:** APPROVED / CONDITIONAL / REJECTED verdict + findings table.

---

### 3. Unit Test Generator (`unit-test-generator.prompt.md`)
Generates ScalaTest WordSpec + ScalaCheck property tests covering domain logic, compliance scenarios, and security boundaries.

**Use when:** Coverage is below threshold or after new domain logic is added.

---

### 4. Comments Generator (`comments-generator.prompt.md`)
Adds Scaladoc to all public symbols and inline `COMPLIANCE:` / `SECURITY:` / `RULE:` annotations.

**Use when:** Preparing code for regulatory review or knowledge transfer.

---

### 5. SonarQube Gate (`SonarQube.prompt.md`)
Simulates a BFSI-STRICT quality gate: coverage ≥ 90%, no critical issues, OWASP Top 10, CWE mapping, CVE dependency scan.

**Use when:** Pre-commit or CI gate check before deploying to regulated environments.

---

## Domain Instruction Usage

Attach the relevant instruction files as Copilot context when generating code for each domain:

| Domain                      | Instruction Folder              |
|-----------------------------|---------------------------------|
| Payments / SWIFT / PSD2     | `instructions/payments/`        |
| Core Banking / Ledger       | `instructions/core-banking/`    |
| Risk / AML / KYC            | `instructions/risk-compliance/` |
| Treasury / FX / Liquidity   | `instructions/treasury/`        |
| Trading / Settlement        | `instructions/capital-markets/` |
| IFRS / Audit                | `instructions/accounting-audit/`|
| Budgeting / FP&A            | `instructions/fpna/`            |
| Insurance / Solvency II     | `instructions/insurance/`       |
| Sovereign Debt / IPSAS      | `instructions/public-finance-regulation/` |

---

## Version History

| Version | Date       | Change                                       |
|---------|------------|----------------------------------------------|
| 2.0.0   | 2024-01-15 | Full CO-STAR + PromptML rewrite from Boron_v1|
| 1.0.0   | 2024-01-01 | Initial Boron_v1 BFSI prompt chain framework |

---

## Governance & Compliance

- All changes to `instructions/` require a **Change Request** per `governance/instruction-change-process.md`
- All new prompt files require **Tier review** per `governance/approval-workflow.md`
- Versioning follows **SemVer** rules in `governance/versioning-strategy.md`
- Permitted and prohibited use cases are defined in `governance/prompt-usage-policy.md`

---

*This framework is maintained by the BFSI Platform Engineering team. Questions: raise via the standard change request process.*
