# Prompt Usage Policy — CO-STAR BFSI Framework

> **Framework:** CO-STAR + PromptML v2.0
> **Effective:** 2024-01-01
> **Review Cycle:** Quarterly

---

## 1. Authorised Prompts and Their Permitted Uses

| Prompt File                    | Permitted Use                              | Required Permissions |
|--------------------------------|--------------------------------------------|----------------------|
| `code-generator.prompt.md`     | Generate new BFSI Scala applications        | Dev + Arch sign-off  |
| `code-reviewer.prompt.md`      | Review Scala code before merge              | Dev self-service     |
| `unit-test-generator.prompt.md`| Generate test suites for Scala code         | Dev self-service     |
| `comments-generator.prompt.md` | Add Scaladoc + compliance annotations       | Dev self-service     |
| `SonarQube.prompt.md`          | Pre-flight quality gate check               | Dev self-service     |

---

## 2. Pre-Use Requirements

Before invoking any code-generation prompt:

1. **Instruction Files Present**: All `.github/instructions/<domain>/` files
   for the target domain must be in place and reviewed.
2. **Branch Policy**: Generated code must land in a feature branch — never
   directly in `main` or `develop`.
3. **Review Mandatory**: All generated code must pass `code-reviewer.prompt.md`
   before pull request creation.
4. **Secrets Policy**: Verify `application.conf` uses `${?ENV_VAR}` substitution.
   No credentials may be committed.

---

## 3. Prohibited Uses

- Generating code for non-BFSI domains (override the domain context)
- Bypassing the code-reviewer prompt for production changes
- Using generated code without reviewing GENERATION_REPORT.md
- Generating code that processes real customer PII in test environments
- Committing generated code to `main` without pull request and review pipeline

---

## 4. Domain Boundaries

Prompts are domain-scoped. When invoking a prompt:
- Load ONLY the instruction files for the specified domain
- Do NOT mix `payments/` rules with `capital-markets/` rules without an ACL
- Cross-domain data flows require explicit documentation in the instruction file

---

## 5. Incident Response

If AI-generated code causes a production incident:
1. Immediately flag in `GENERATION_REPORT.md` with `[INCIDENT]` prefix
2. Raise a Post-Incident Review following `approval-workflow.md`
3. Update the affected instruction file with the failure root cause
4. Submit a policy change proposal to governance via `instruction-change-process.md`

---

## 6. Compliance Obligations

All users of these prompts are personally responsible for ensuring that
generated code meets the regulatory requirements listed in `policy-master.md`.
Automated prompt execution does not transfer compliance liability from the
developer to the AI system.
