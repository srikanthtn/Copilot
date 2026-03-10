# Instruction Change Process — CO-STAR BFSI Framework

> **Framework:** CO-STAR + PromptML v2.0

---

## 1. When to Update an Instruction File

Domain instruction files drive AI code generation. They must be updated when:

- A new regulatory requirement takes effect (e.g., new EBA guideline)
- A business rule changes (new payment limit, updated KYC threshold)
- A security policy changes (new cipher requirement, key rotation policy)
- A domain boundary changes (new product added, old product retired)
- An AI-generated code incident reveals a gap in the instructions

---

## 2. Change Request Process

### Step 1: Raise Change Request
Create a PR with the proposed changes to the instruction file(s).
Include in the PR description:
- **Reason**: Why is this change needed?
- **Regulation/Rule Reference**: Citation (e.g., EBA/GL/2020/08 Section 4.3)
- **Impact Assessment**: Which domains / generated code is affected?
- **Backward Compatibility**: Does this require re-generation of existing code?

### Step 2: Review
| Instruction File Type | Required Reviewers                     |
|-----------------------|----------------------------------------|
| `domain-overview.md`  | Domain SME + Enterprise Architect      |
| `regulatory-constraints.md` | Compliance Officer + Legal     |
| `business-rules.md`   | Domain SME + Product Owner             |
| `forbidden-operations.md` | Compliance + CISO                  |
| `data-boundaries.md`  | Data Privacy Officer + Architect       |

### Step 3: Testing
After merging instruction changes:
1. Re-run `/code-generator <domain>` in a sandbox environment
2. Confirm new rules are reflected in generated code
3. Run `/code-reviewer` to verify no regressions introduced
4. Document re-test results in the change PR

### Step 4: Broadcast
Announce instruction file updates to all teams using the affected domain
generation prompts via the project wiki / Confluence page.

---

## 3. Emergency Instruction Changes

For urgent regulatory changes (e.g., new sanctions list):
1. CISO or Compliance Officer can approve single-approver emergency update
2. Document emergency approval in PR
3. Full review completed within 5 business days
4. Post-update audit added to `policy-master.md` change log

---

## 4. Instruction File Versioning

See `versioning-strategy.md` for version bump rules. Every merged instruction
change must include a version bump in the file's header comment.
