# Investment — Domain Overview
**Version:** 1.0.0
**Authority:** chain-master.md

---

## Scope

The Investment domain governs portfolio management, securities analysis, investment
product suitability assessment, and financial instrument lifecycle management for
retail, affluent, and institutional clients.

This domain ensures **investment suitability**, **portfolio risk governance**,
and **MiFID II / SEBI / SEC regulatory compliance** in all investment activities.

---

## Characteristics

- Investment decisions must be suitable for the client's risk profile, financial
  situation, and investment objectives (Know Your Client — KYC in investment context).
- Conflicts of interest must be disclosed and managed.
- Portfolio valuation must use transparent, auditable pricing methodologies.
- All investment recommendations must include a documented suitability rationale.
- Best execution obligations must be met for all order execution.

---

## Key Investment Entities

| Entity                 | Description                                               |
|------------------------|-----------------------------------------------------------|
| `Portfolio`            | Collection of financial assets held by a client           |
| `NetAssetValue (NAV)`  | Total portfolio value net of liabilities                  |
| `SecuritiesPosition`   | Holding of a specific financial instrument                |
| `InvestmentMandate`    | Client's stated investment objectives and constraints     |
| `SuitabilityAssessment`| Formal evaluation of investment product relevance for client|
| `OrderInstruction`     | Trade execution directive                                 |
| `Benchmark`            | Reference index for performance comparison               |

---

## Investment Suitability Framework (MiFID II Aligned)

| Suitability Dimension   | Assessment Question                                       |
|-------------------------|-----------------------------------------------------------|
| Knowledge & Experience  | Does the client understand this product type?             |
| Financial Situation     | Can the client absorb the maximum potential loss?         |
| Investment Objectives   | Does the product match the stated return goals?           |
| Risk Tolerance          | Does the product risk level match the client's profile?  |

**Suitability Verdict:**
- `SUITABLE`        — All dimensions affirmed. Proceed.
- `INAPPROPRIATE`   — Client lacks understanding. Warn + document.
- `UNSUITABLE`      — Product does not match client profile. Do not recommend.

---

## Chain Reasoning Responsibilities

### Stage 3 Triggers
- Resolution of investment entities: `Portfolio`, `NavRecord`, `SuitabilityAssessment`
- Identification of applicable regulation: MiFID II, SEBI LODR, SEC rules
- Conflict of interest and best execution checks

### Stage 4 Compliance Triggers
| Check ID  | Description                                          |
|-----------|------------------------------------------------------|
| COMP-002  | KYC (investor suitability profile) verified          |
| COMP-007  | Investment mandate and consent documented            |
| COMP-008  | Audit trail for all investment recommendations       |
| COMP-010  | Conflict of interest disclosure completed            |

Applicable regulation: **MiFID II**, **SEBI LODR**, **SEC Regulations**

### Stage 5 Risk Triggers
- Market risk (VaR, stress testing, sensitivity)
- Concentration risk (single-name or sector overweight)
- Counterparty risk for bilateral or OTC instruments
- Liquidity risk for illiquid or restricted investment products

---

## Prohibited Operations

- Recommending an `UNSUITABLE` product to a client.
- Churning (excessive trading for fee generation without client benefit).
- Executing trades on insider information.
- Front-running client orders.
- Omitting conflict of interest disclosures from investment advice.
- Using benchmark-relative performance reporting that misrepresents absolute risk.
