# Policy Master — CO-STAR BFSI Prompt Framework

> **Framework:** CO-STAR + PromptML v2.0
> **Scope:** All AI-generated code targeting BFSI production environments
> **Authority:** Enterprise Architecture / CISO / Chief Compliance Officer

---

## 1. Purpose

This policy governs the use of all CO-STAR prompt files in this repository for
generating, reviewing, testing, and documenting Scala/Spark BFSI applications.
Any AI-assisted code generation in a regulated financial environment **must**
operate under these rules.

---

## 2. Mandatory Regulatory Baseline

All generated code must comply with the following regulations. Compliance is
non-optional and verified by the `code-reviewer.prompt.md` and
`SonarQube.prompt.md` prompts before any deployment.

| Regulation       | Scope                          | Key Requirements                         |
|------------------|--------------------------------|------------------------------------------|
| PCI-DSS v4.0     | Any code handling PANs/CHDs    | Req 3 (storage), Req 4 (transit), Req 6 (dev security) |
| GDPR 2016/679    | EU personal data processing    | Art 25 (privacy by design), Art 32 (security) |
| PSD2 2015/2366   | Payment initiation services    | Strong Customer Authentication (Art 97)  |
| AML 5AMLD/6AMLD  | Fund transfer services         | Sanctions screening, SAR reporting       |
| Basel III / CRR  | Risk and capital calculations  | Accuracy requirements for risk models    |
| MiFID II         | Capital markets, trading       | Best execution, record retention 7 yrs   |
| SOC 2 Type II    | All cloud-hosted services      | CC6–CC9 logical access & incident resp.  |
| ISO/IEC 27001    | All environments               | A.12 operations, A.14 secure development |

---

## 3. Technology Policy

| Component     | Mandated Version | Rationale                              |
|---------------|-----------------|----------------------------------------|
| Scala         | 2.13.14         | Active LTS; immutable-first ecosystem  |
| Spark         | 3.5.1           | Supports AQE; compatible with Delta 3  |
| Java          | 17 LTS          | Min for TLS 1.3 native support         |
| Delta Lake    | 3.1.0           | ACID transactions required for ledger  |
| sbt           | 1.9.x           | Pin all dependency versions exactly    |
| ScalaTest     | 3.2.18          | Corporate standard test framework      |
| Cats-Effect   | 3.5.4           | Functional IO for safe side effects    |

Exceptions to the above require written sign-off from the Enterprise Architect
and must be documented in `versioning-strategy.md`.

---

## 4. Code Quality Gates

All generated code must pass ALL quality gates before CI/CD promotion:

| Gate                     | Threshold        | Enforced By                    |
|--------------------------|-----------------|--------------------------------|
| SonarQube Bugs           | 0 new           | SonarQube.prompt.md            |
| SonarQube Vulnerabilities| 0 new           | SonarQube.prompt.md            |
| Test Coverage (domain)   | ≥ 95%           | unit-test-generator.prompt.md  |
| Test Coverage (app)      | ≥ 85%           | unit-test-generator.prompt.md  |
| Code Review Approval     | Required        | code-reviewer.prompt.md        |
| Scaladoc Coverage        | 100% public API | comments-generator.prompt.md  |
| No hardcoded secrets     | Zero tolerance   | All prompts enforce            |
| No PII in logs           | Zero tolerance   | code-reviewer.prompt.md        |

---

## 5. Prohibited Patterns (Absolute — never generate)

The following code patterns are **absolutely prohibited** in all generated code.
The code reviewer will reject any submission containing these.

1. `Double` or `Float` used for any monetary value
2. `null` returned or stored anywhere in domain layer
3. `throw` in domain or application layer (use `Either`)
4. `var` for any mutable state
5. Hardcoded passwords, API keys, or encryption keys
6. PAN or IBAN in plain text in logs or storage
7. AES in ECB mode
8. MD5 or SHA-1 for cryptographic hashing
9. `DataFrame` (untyped) in domain or application layer
10. `.collect()` on unbounded Spark datasets

---

## 6. Domain Access Control

Each domain instruction folder (`payments/`, `core-banking/`, etc.) constitutes
a bounded context boundary. Code generators must load ONLY the instruction files
for the target domain unless explicitly directed otherwise.

Cross-domain data sharing requires:
- An explicit Anti-Corruption Layer (ACL) in the infrastructure layer
- A documented data contract in the domain instruction file
- Compliance sign-off for any PII crossing domain boundaries

---

## 7. Audit and Traceability

Every AI-generated code session must produce a `GENERATION_REPORT.md` that records:
- Timestamp of generation (UTC)
- Domain instructions ingested
- Files created (with SHA-256 checksums)
- Assumptions made (flagged as `[ASSUMPTION]`)
- Regulatory constraints applied
- Review verdict from `code-reviewer.prompt.md`

This report is retained for 7 years per MiFID II audit trail requirements.
