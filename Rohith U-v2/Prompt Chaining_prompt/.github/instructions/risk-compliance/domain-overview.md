# Risk & Compliance — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Risk & Compliance domain governs the identification, assessment, monitoring,
and enforcement of financial, operational, regulatory, and reputational risks
across all BFSI operations.

This domain ensures **institutional safety**, **regulatory adherence**, and
**controlled risk exposure** at all times.

---

## Characteristics

- Cross-domain oversight — this domain audits and constrains all other domains.
- Strong independence from execution logic — risk and compliance MUST NOT be
  embedded in operational workflows without explicit separation.
- High audit and reporting obligations across all functions.
- Conservative decision principles — when in doubt, the more restrictive interpretation applies.

---

## Domain Authority

This domain is authoritative for:
- Risk assessment frameworks (credit, market, operational, fraud)
- Compliance enforcement rules (AML, KYC, sanctions, reporting)
- Control validation outcomes
- Regulatory obligation tracking and SAR/STR filing decisions

Other domains MUST comply with this domain's constraints but MUST NOT redefine
or override its controls.

---

## Chain Reasoning Responsibilities

In Stage 3 (BFSI Domain Interpretation), Risk & Compliance context triggers:
- Resolution of all risk-related entities: `RiskProfile`, `AMLAlert`, `FraudSignal`, `KycStatus`
- Application of AML monitoring business rules
- Sanctions screening obligation identification
- SAR/STR filing obligation identification

In Stage 4 (Compliance Evaluation), Risk & Compliance operations trigger ALL mandatory checks:
- COMP-001 through COMP-010

In Stage 5 (Risk Assessment), this domain drives:
- Full fraud signal taxonomy evaluation (FS-001 through FS-010)
- Operational risk assessment
- Customer risk profile evaluation
