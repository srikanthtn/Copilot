# Prompt Chain Versioning Strategy
**Version:** 1.0.0
**Authority:** Architectural Review Board (ARB)

---

## Purpose

Ensure traceability, reproducibility, and auditability of all BFSI Prompt Chain
instruction file and prompt file evolution.

---

## Versioning Rules

- All instruction files and prompt files MUST carry an explicit version header.
- Version changes MUST be documented in the change log section of the file.
- Backward compatibility impact must be assessed for every MINOR and MAJOR change.
- No unversioned files may be used in a production chain execution context.

---

## Version Semantics

| Version Kind | Trigger Condition                                          | Impact Assessment Required |
|--------------|------------------------------------------------------------|----------------------------|
| MAJOR (X.0.0)| Stage structure change, gate logic alteration, persona change | Yes — full ARB review    |
| MINOR (x.Y.0)| New rule, new entity, new domain, new checklist item       | Yes — architect sign-off   |
| PATCH (x.y.Z)| Typo corrections, formatting, non-behavioral clarifications| Peer review sufficient     |

---

## Prohibited Versioning Behaviours

- Overwriting a file without incrementing its version.
- Applying a MINOR-level change under a PATCH version.
- Reusing version identifiers across different files.
- Removing rules or constraints under a PATCH version increment.
- Deploying a chain with instruction files at mismatched major versions.

---

## Version Compatibility Matrix

| Chain Version | chain-master.md | stage-definitions.md | Domain Instructions |
|---------------|-----------------|----------------------|--------------------|
| 1.x.x         | 1.x.x           | 1.x.x                | 1.x.x              |

All components within a chain execution MUST share the same MAJOR version.
MINOR and PATCH versions may differ across components within the same execution.
