# Versioning Strategy — CO-STAR BFSI Framework

> **Framework:** CO-STAR + PromptML v2.0

---

## 1. Prompt File Versioning

All prompt files use **Semantic Versioning** (MAJOR.MINOR.PATCH) in their YAML
frontmatter.

| Version Component | When to Increment                                        |
|-------------------|----------------------------------------------------------|
| MAJOR             | Breaking change to expected output format or tool calls  |
| MINOR             | New phase, phase reordering, new constraints added       |
| PATCH             | Typo fixes, clarifications, example updates              |

Current baseline: all prompts at **2.0.0** (CO-STAR + PromptML migration).

---

## 2. Instruction File Versioning

Domain instruction files (`.github/instructions/<domain>/`) carry a version
header comment at the top of each file:

```markdown
<!-- version: 1.0.0 | updated: 2024-01-15 | reviewed-by: <role> -->
```

Instruction file version increments trigger mandatory re-generation of any
application code that ingests that file, per the `approval-workflow.md`.

---

## 3. Breaking vs Non-Breaking Changes

### Breaking Changes (MAJOR bump required)
- Changing the expected file-creation output format
- Renaming or removing a phase
- Changing technology baseline (e.g., Scala 2.13 → 3.x)
- Removing a constraint rule

### Non-Breaking Changes (MINOR bump)
- Adding a new domain to the supported domains table
- Adding a new constraint rule
- Adding a new instruction file to an existing domain
- Extending an existing phase with additional checks

### Patches
- Fixing a broken example code block
- Correcting a regulatory citation
- Updating a CVE version number

---

## 4. Deprecation Policy

1. Mark the deprecated version in frontmatter: `deprecated: true`
2. Add a `deprecation_notice` field explaining why and what replaces it.
3. Maintain the deprecated version for **one quarter** before removal.
4. All downstream references must be updated before archival.

---

## 5. Changelog

All version changes are documented in the `CHANGELOG.md` at the repo root,
following Keep a Changelog format.

| Version | Date       | Type  | Summary                                       |
|---------|------------|-------|-----------------------------------------------|
| 2.0.0   | 2024-01-15 | MAJOR | Migrated to CO-STAR + PromptML framework      |
| 1.0.0   | 2023-06-01 | MAJOR | Initial Boron_v1 prompt framework             |
