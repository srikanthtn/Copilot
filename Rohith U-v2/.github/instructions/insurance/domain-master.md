# Domain Instruction: Insurance — Policies, Claims, Underwriting & Actuarial
**Version:** 2.0.0 | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY
- **Domain:** Insurance (P&C, Life, Health, Reinsurance)
- **Authority:** Authoritative for all contractual risk transfer obligations
- **Regulatory Frameworks:** Solvency II, IFRS 17, IDD, GDPR, DORA

---

## 2. CORE BUSINESS INVARIANTS

### 2.1 Policy Lifecycle (State Machine)
```
QUOTED → UNDERWRITTEN → ACTIVE → RENEWED / LAPSED / CANCELLED / EXPIRED
                                         │
                                         ▼
                                  CLAIM POSSIBLE (while ACTIVE)
```
- Only ACTIVE policies can generate valid claims
- CANCELLED and EXPIRED are TERMINAL policy states
- Policy terms (premium, coverage, period) are immutable once `UNDERWRITTEN`

### 2.2 Premium Calculation Invariants
- Premium MUST use `Decimal` arithmetic (actuarial precision required)  [FM-001]
- Loading factors (expenses, profit, reinsurance) MUST be separately tracked
- Premium = Pure Risk Premium × (1 + Loading Factor)
- All premium calculations MUST cite the actuarial rating table version used

### 2.3 Claims Processing Rules
- Claims can only be filed against ACTIVE policies
- `ClaimAssessment` must consider policy exclusions, deductibles, and sub-limits
- Payment MUST NOT exceed `Sum Insured` (with reinsurance recovery calculated separately)
- Fraudulent claim flags: inconsistency between claim date and incident date, repeat small claims

### 2.4 IFRS 17 Compliance (NEW)
- Insurance contracts MUST be measured using `General Measurement Model (GBB)` or `PAA`
- `ContractualServiceMargin (CSM)` must be tracked per contract group
- CSM is released over the coverage period (not at inception)
- `Risk Adjustment` for non-financial risk must be calculated separately from best estimate

---

## 3. REGULATORY CONSTRAINTS
[SOURCE: code-generation-master.md §5.7]

- **Solvency II SCR:** Solvency Capital Requirement computed as 99.5th percentile VaR
- **Solvency II MCR:** Minimum Capital Requirement — floor for SCR; breach → regulatory action
- **Solvency II ORSA:** Own Risk and Solvency Assessment — annual prospective capital adequacy
- **IFRS 17 (eff. 2023):** Insurance contract measurement; CSM, RA, fulfilment cash flows
- **IDD (Insurance Distribution Directive):** Suitability and appropriateness for products
- **GDPR:** Health/PHI data (health insurance) encrypted with AES-256; NEVER logged

---

## 4. ACTUARIAL DATA REQUIREMENTS

- Loss Development Tables (LDT) stored in `ActuarialDataStore` (read-only, versioned)
- Expected Loss Ratio (ELR) updated quarterly, with version tagged on each calculation run
- Reinsurance treaties stored in `ReinsuranceTreatyRepository` (immutable after binding)
- Reserve estimates carry `ActuarySignOffRecord` with actuarial stamp and date

---

## 5. FORBIDDEN OPERATIONS

- ⛔ Claim payment against non-ACTIVE policy — BLOCKER
- ⛔ Premium calculation without citing actuarial table version — MAJOR
- ⛔ PHI (health data) logged or stored without AES-256 encryption — BLOCKER [SEC-006]
- ⛔ CSM recognized at inception (must be amortized over coverage period) — BLOCKER
- ⛔ Reinsurance treaty mutation post-binding — BLOCKER
- ⛔ Missing fraud flag evaluation on large claims (> €50,000) — CRITICAL

---

## 6. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Type | Purpose |
|--------|------|---------|
| `InsurancePolicy` | Aggregate | Policy contract |
| `PolicyCoverage` | Value Object | Coverage terms |
| `InsurancePremium` | Value Object | Premium with components |
| `ClaimRecord` | Aggregate | Filed claim |
| `ClaimAssessment` | Entity | Adjudicator assessment |
| `UnderwritingDecision` | Entity | Risk acceptance |
| `ReinsuranceTreaty` | Entity (immutable) | Reinsurance agreement |
| `ContractualServiceMargin` | Value Object | IFRS 17 CSM |
| `RiskAdjustment` | Value Object | IFRS 17 RA |
| `ActuarialReserveEstimate` | Entity | Reserve calc |
| `ActuarySignOffRecord` | Value Object | Actuarial approval |
| `SolvencyCapitalRequirement` | Value Object | SCR output |
| `PolicyholderProfile` | Entity | Customer (insured) |
| `FraudIndicatorRecord` | Value Object | Fraud signal |
