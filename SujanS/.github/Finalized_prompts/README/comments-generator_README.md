# Code Comments Generator Prompt

This folder contains the governed **Code Comments Generator** prompt used to add/update documentation in Scala source files in a regulated repository.

## What it is

- **Prompt file:** .github/prompts/code_comments_generator/comments-generator.prompt.md
- **Role:** Code Comments Generator (governed)
- **Scope:** Adds/updates ScalaDoc and minimal inline comments in Scala source files
- **Non-goals:** Any change to executable code (logic, signatures, imports, refactors, behavior)

This prompt is designed to be safe for long-lived maintenance and audit review. It prioritizes **instruction-file compliance** and avoids embedding domain knowledge inside the prompt.

## How it works (high-level)

When used with a Scala file (or a selection), the prompt drives the assistant through a strict workflow:

1) **Governance discovery (internal)**
   - The assistant must treat instruction files as authoritative.
   - It must discover and apply instructions under:
     - .github/instructions/shared-instructions/
     - .github/instructions/instructions/<domain>/
     - .github/instructions/governance/
   - If instruction files conflict, the assistant must refuse.

2) **Pre-generation analysis (internal)**
   - Determines whether the input is empty / partial / complete Scala code.
   - Reads build metadata (for version-aware wording):
     - build.sbt
     - project/build.properties
   - Checks whether Spark is relevant based on dependencies/usages.

3) **Comment generation (output)**
   - Produces high-signal ScalaDoc and only minimal inline comments.
   - Documents intent (“why”), invariants enforced by code, side effects, and failure modes.
   - Avoids unverifiable claims and avoids leaking sensitive details.

## Output contract

The prompt requires a strict, tool-friendly output:

- Output **only Scala source files** with comments added/updated
- No markdown, no diffs, no narrative explanation
- Emits files using the format:

```
===== FILE: <workspace-relative-path> =====
<full file content>
```

This makes it easier to apply changes deterministically and review them in code review.

## Key rules enforced by the prompt

### Comments-only (no behavior change)
The assistant must not change:
- Logic
- Method/class signatures
- Imports
- Refactorings
- Dependency list

### Minimal, high-signal commenting
- Prefer ScalaDoc (`/** ... */`) for public APIs and type-level documentation.
- Use inline comments (`//`) only for non-obvious control flow, tricky correctness constraints, or Spark-specific reasoning.
- Avoid line-by-line narration of code.

### Safety and governance
- Do not include secrets, credentials, or real identifiers in comments.
- Do not reveal sensitive internal control mechanisms.
- Do not claim compliance or guarantees unless explicitly supported by code and applicable instructions.

### Empty/partial code handling
- If code is partial or contains TODO/FIXME, document only what exists today.
- If there is no Scala code and no specific file target is identified, the prompt requires refusal.

## How to use it in VS Code / Copilot

Exact UI labels vary by Copilot version, but the recommended workflow is:

1) Open the Scala file you want documented.
2) Select the portion you want documented (or leave it unselected to document the whole file).
3) Open Copilot Chat.
4) Attach or reference the selection/file as the input context.
5) Run the prompt from the repository prompt library (this file).
6) Review the returned file content and apply it.

Tip: Because the prompt’s output contract returns full file contents, it is best used on a small set of files per run.

## Customization and extension

If you need different domain vocabulary or compliance-specific phrasing:

- Prefer adding/adjusting content in instruction files under:
  - .github/instructions/instructions/<domain>/
  - .github/instructions/shared-instructions/
  - .github/instructions/governance/

This repository follows the governance policy that **domain knowledge must not exist in prompts**, so changes that encode domain rules should be made in instruction files rather than in the prompt.

## Troubleshooting

- **The assistant refuses unexpectedly:**
  - Check for conflicting instruction files, or requests that imply logic changes.
- **Comments are too verbose:**
  - Ensure the target code is actually complex; the prompt is designed to minimize noise.
- **Spark guidance is missing:**
  - Spark sections only apply when Spark APIs are detected in dependencies/usages.
