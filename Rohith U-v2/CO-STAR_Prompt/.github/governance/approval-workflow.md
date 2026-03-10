# Approval Workflow — CO-STAR BFSI Framework

> **Framework:** CO-STAR + PromptML v2.0

---

## 1. Code Generation Approval Flow

```
Developer invokes /code-generator
         │
         ▼
┌────────────────────────┐
│  Phase 0: Instruction  │  ← Must complete before any code is generated
│  File Ingestion        │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│  Generation runs       │  ← 27 files created on disk
│  GENERATION_REPORT.md  │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│  /code-reviewer        │  ← Developer runs immediately after generation
│  Review Report         │
└────────────┬───────────┘
             │
     ┌───────┴───────┐
     │               │
  APPROVED      REJECTED / CONDITIONAL
     │               │
     ▼               ▼
Create PR        Fix all BLOCKER +
to feature       CRITICAL findings
branch           → Re-run /code-reviewer
     │
     ▼
┌────────────────────────┐
│  /sonarqube pre-flight │  ← Must PASS gate
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│  Senior Engineer       │  ← Mandatory peer review
│  Peer Review in PR     │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│  Merge to develop/main │  ← CI runs sonar + tests
└────────────────────────┘
```

---

## 2. Change Approval Tiers

| Change Type                            | Approvers Required            |
|----------------------------------------|-------------------------------|
| New generated feature in feature branch | Dev + one Senior Engineer    |
| Change to prompt file (PATCH)          | Dev + Tech Lead               |
| Change to prompt file (MINOR/MAJOR)    | Dev + Tech Lead + Architect   |
| Change to domain instruction file      | Dev + Domain SME + Compliance |
| New domain instruction folder          | Architect + Compliance + CISO |
| Change to `policy-master.md`           | CISO + Chief Compliance Officer |

---

## 3. Emergency Hotfix Path

For production incidents requiring immediate code change:
1. Create hotfix branch from `main`
2. Apply minimal fix
3. Run `/code-reviewer` and `/sonarqube` — document in PR
4. Emergency approval: Tech Lead + Compliance (same day)
5. Post-incident review within 5 business days
6. Update instruction files to prevent recurrence

---

## 4. Audit Trail Requirements

Every merged PR must include:
- Link to `GENERATION_REPORT.md` from the generation session
- Code reviewer output (pass/reject + finding count)
- SonarQube pre-flight gate result
- Peer review sign-off from required approvers

Retained for 7 years per MiFID II Article 25.
