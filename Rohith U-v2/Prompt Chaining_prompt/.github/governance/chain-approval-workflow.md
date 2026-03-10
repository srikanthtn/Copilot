# Chain Approval Workflow — BFSI Advanced Prompt Chaining Framework
**Version:** 1.0.0
**Authority:** chain-governance-policy.md

---

## Purpose

Define the approval process for introducing, modifying, or retiring prompt chain
components within the BFSI Advanced Prompt Chaining Framework.

---

## Change Categories and Approval Requirements

### Category A — Stage Logic Changes (MAJOR)
Changes that alter the reasoning mandate, gate behaviour, or output contract of any stage.

**Requires:**
- Architectural Review Board (ARB) sign-off
- CISO review (for any security or data privacy impact)
- Compliance Officer review (for any gate logic changes)
- Minimum 5 business days review period
- Updated version MAJOR increment for affected files
- Full regression test of chain against reference scenarios

---

### Category B — Domain Entity or Rule Changes (MINOR)
Adding new domain entities, compliance checks, fraud signals, or business rules.

**Requires:**
- Lead Architect sign-off
- Domain Owner review (for the affected domain)
- Minimum 2 business days review period
- MINOR version increment for affected files
- Validation run using bfsi-chain-validator.prompt.md

---

### Category C — Formatting and Non-Behavioral Changes (PATCH)
Typo corrections, formatting improvements, documentation updates.

**Requires:**
- Peer review by one senior team member
- PATCH version increment for affected files
- No regression testing required (no behavioral change)

---

## Approval Workflow Diagram

```
Change Request Raised
         │
         ▼
   Categorize Change
   (A / B / C)
         │
    ─────┼─────────────────────
    │         │           │
  CAT A     CAT B       CAT C
    │         │           │
    ▼         ▼           ▼
 ARB +      Lead       Peer
 CISO +    Architect   Review
 Compliance+ Domain
 Officer   Owner
    │         │           │
    ▼         ▼           ▼
 Approved?  Approved?  Approved?
    │         │           │
   Yes       Yes         Yes
    │         │           │
    └────────┬────────────┘
             │
             ▼
    Increment Version
    Update Changelog
    Merge to Main Branch
             │
             ▼
    Run Chain Validator
    (for Cat A and Cat B)
             │
             ▼
    Deploy to Governed
    Chain Environment
```

---

## Emergency Fast-Track Process

For P1 production incidents requiring immediate chain changes:

1. Senior Engineer raises emergency change request with P1 justification.
2. CTO or designate provides emergency approval (verbal + written confirmation within 1 hour).
3. Change is deployed using emergency access.
4. Post-incident report filed within 24 hours.
5. Standard approval workflow retroactively completed within 3 business days.
6. All emergency changes are logged and included in the next ARB governance review.

---

## Prohibited Change Actions

- Merging chain changes without documented approval.
- Relaxing compliance gates without Compliance Officer sign-off.
- Removing fraud signal checks without CRO review.
- Downgrading file versions after publication.
- Bypassing the chain validator for Category A or B changes.
