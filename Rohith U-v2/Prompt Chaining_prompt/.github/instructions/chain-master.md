# Master Instruction File: BFSI Advanced Prompt Chaining Authority
**Version:** 1.0.0
**Scope:** All BFSI Domains — Banking, Financial Services, and Insurance
**Authority Level:** ABSOLUTE — All chain reasoning MUST comply

---

## 1. DOCUMENT HIERARCHY & PRECEDENCE

### 1.1 Instruction File Priority (Highest to Lowest)
1. **This Master File** — Global rules that apply to ALL domains and ALL chain stages
2. **Domain-Specific Instructions** — Rules for specific BFSI bounded contexts
3. **Prompt Chain Configuration** — Runtime parameters from the coordinator prompt

### 1.2 Conflict Resolution
- If domain-specific rules conflict with this master file → **Master wins**
- If domain-specific rules conflict with each other → **More restrictive rule wins**
- If no explicit rule exists → **Apply FATF, Basel III, PCI-DSS, ISO 27001 best practices**

### 1.3 Available Domain Instruction Directories
The chain coordinator MUST scan and ingest rules from ALL of these directories:
- `./core-banking/`        — Accounts, ledger, customer management
- `./payments/`            — SEPA, SWIFT, RTGS, ISO 20022 transactions
- `./risk-compliance/`     — AML, KYC, sanctions, fraud detection
- `./insurance/`           — Policies, claims, underwriting, actuarial
- `./credit-assessment/`   — Scoring, loan processing, LTV, IFRS 9
- `./investment/`          — Portfolio, NAV, securities, MiFID II
- `./fraud-detection/`     — Behavioral signals, pattern analysis

---

## 2. CHAIN STAGE EXECUTION CONTRACT (NON-NEGOTIABLE)

### 2.1 Stage Identity and Sequencing
| Stage | Name                        | Gate Stage | Artifacts Produced      | Input From                      |
|-------|-----------------------------|------------|-------------------------|---------------------------------|
| 1     | Context Understanding       | No         | [CONTEXT_ARTIFACT]      | Raw input                       |
| 2     | Intent Identification       | No         | [INTENT_ARTIFACT]       | Stage 1                         |
| 3     | BFSI Domain Interpretation  | No         | [DOMAIN_ARTIFACT]       | Stages 1–2                      |
| 4     | Compliance Evaluation       | YES        | [COMPLIANCE_ARTIFACT]   | Stages 1–3                      |
| 5     | Risk Assessment             | YES        | [RISK_ARTIFACT]         | Stages 1–4                      |
| 6     | Decision Reasoning          | No         | [DECISION_ARTIFACT]     | Stages 1–5                      |
| 7     | Final Response Generation   | No         | Final Output            | Stages 1–6                      |

### 2.2 Stage Execution Rules
| Rule ID | Rule Name               | Constraint                                               |
|---------|-------------------------|----------------------------------------------------------|
| SE-001  | Sequential Execution    | Stages MUST execute in order 1 → 7. No skipping.        |
| SE-002  | Artifact Propagation    | Each stage MUST receive and consume prior artifacts.     |
| SE-003  | Gate Halt               | Stage 4 NON_COMPLIANT verdict HALTS chain immediately.   |
| SE-004  | Gate Escalation         | Stage 5 CRITICAL tier MUST trigger escalation in Stage 6.|
| SE-005  | No Phantom Stages       | Only Stages 1–7 exist. No ad-hoc stages may be inserted. |
| SE-006  | Explicit Tagging        | Every stage output MUST be tagged with its stage ID.     |
| SE-007  | No Retroactive Updates  | Completed stages are immutable. No backward rewrites.    |

---

## 3. UNIVERSAL BFSI BUSINESS INVARIANTS (NON-NEGOTIABLE)

### 3.1 Financial Reasoning Mathematics
| Rule ID | Rule Name             | Constraint                                                      |
|---------|-----------------------|-----------------------------------------------------------------|
| FM-001  | Monetary Precision    | All amounts must state currency, value, and precision context   |
| FM-002  | Rounding Convention   | Banker's rounding (HALF_EVEN) for all reported final values     |
| FM-003  | Non-Negative Amounts  | Transaction amounts MUST be > 0; zero requires explicit context |
| FM-004  | Currency Standard     | ISO 4217 codes only (USD, EUR, GBP, INR, SGD, etc.)            |
| FM-005  | Directional Clarity   | Use DEBIT/CREDIT direction enum; NEVER use negative values      |
| FM-006  | Ledger Symmetry       | Every debit entry must have a corresponding credit entry        |
| FM-007  | Temporal Precision    | All dates and timestamps in ISO 8601 format                     |

### 3.2 BFSI Entity Lifecycle State Machine
All financial entities MUST follow this lifecycle pattern:

```
┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐
│   INITIATED     │──▶│   VALIDATED     │──▶│    CLEARED      │──▶│    SETTLED      │
└─────────────────┘   └─────────────────┘   └─────────────────┘   └─────────────────┘
         │                     │                     │                     │
         ▼                     ▼                     ▼                     │
┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐           │
│    REJECTED     │   │    REJECTED     │   │     FAILED      │           │
└─────────────────┘   └─────────────────┘   └─────────────────┘           │
                                                                           │
                                               [TERMINAL STATE] ◀──────────┘
```

**Constraints:**
- Transitions are **FORWARD-ONLY** (no rollback except via explicit reversal event)
- `SETTLED` is a **TERMINAL STATE** (immutable after entry)
- Every state transition MUST produce a domain event for audit trail

### 3.3 KYC/CDD Tier Requirements
| KYC Tier | Customer Type            | Required Verification Level                     |
|----------|--------------------------|-------------------------------------------------|
| Tier 0   | Anonymous/Pre-onboarding | No transaction permitted above regulatory minimum|
| Tier 1   | Basic CDD                | Name, DOB, address, document verification        |
| Tier 2   | Standard CDD             | Tier 1 + income source + account purpose         |
| Tier 3   | Enhanced CDD (EDD)       | Tier 2 + beneficial ownership + risk narrative  |
| PEP      | Politically Exposed Person | Tier 3 + senior management sign-off            |

---

## 4. FORBIDDEN OPERATIONS (ZERO TOLERANCE)

### 4.1 Reasoning Integrity Blockers
| Violation ID | Description                                        | Severity |
|--------------|----------------------------------------------------|----------|
| RI-001       | Citing a regulation not validated in Stage 4       | BLOCKER  |
| RI-002       | Producing a positive decision over a BLOCK verdict | BLOCKER  |
| RI-003       | Skipping a mandatory reasoning stage               | BLOCKER  |
| RI-004       | Fabricating domain entities not in instruction files| BLOCKER |
| RI-005       | Silent ambiguity resolution without documentation  | MAJOR    |
| RI-006       | Informal or conversational tone in output          | MAJOR    |
| RI-007       | Omitting risk mitigations for HIGH/CRITICAL tier   | BLOCKER  |

### 4.2 Security and Privacy Blockers
| Violation ID | Description                                        | Severity |
|--------------|----------------------------------------------------|----------|
| SP-001       | PII exposed in reasoning chain or output           | BLOCKER  |
| SP-002       | Unmasked IBAN, PAN, or account numbers in output   | BLOCKER  |
| SP-003       | Credential or secret references in any output      | BLOCKER  |
| SP-004       | Sanctioned entity processed without BLOCK          | BLOCKER  |
| SP-005       | Missing authentication verification for TRANSACT   | BLOCKER  |

### 4.3 Compliance Blockers
| Violation ID | Description                                        | Severity |
|--------------|----------------------------------------------------|----------|
| CB-001       | AML screening bypassed                             | BLOCKER  |
| CB-002       | KYC status not verified before transaction         | BLOCKER  |
| CB-003       | Regulatory threshold reporting suppressed          | BLOCKER  |
| CB-004       | Audit trail record omitted                         | BLOCKER  |
| CB-005       | Consent or authorization chain incomplete          | BLOCKER  |

---

## 5. DOMAIN ENTITY CANONICAL REGISTRY

### 5.1 Core Banking Entities
| Entity Name             | Description                                    |
|-------------------------|------------------------------------------------|
| `CustomerRecord`        | Authoritative customer identity and KYC record |
| `Account`               | Deposit, current, savings or loan account      |
| `LedgerEntry`           | Immutable, append-only financial record        |
| `AccountBalance`        | Derived from ledger; never directly modified   |
| `TransactionInstruction`| Command to execute a financial operation       |

### 5.2 Payments Entities
| Entity Name                | Description                                 |
|----------------------------|---------------------------------------------|
| `PaymentInstruction`       | Initiation command for a payment            |
| `ClearingMessage`          | Inter-bank clearing communication           |
| `SettlementRecord`         | Final settlement confirmation               |
| `CreditTransfer`           | SEPA/SWIFT credit transfer transaction      |
| `DirectDebitMandate`       | Authorised direct debit mandate             |

### 5.3 Risk and Compliance Entities
| Entity Name                | Description                                 |
|----------------------------|---------------------------------------------|
| `RiskProfile`              | Composite customer or transaction risk score|
| `AMLAlert`                 | Suspicious activity alert record            |
| `SanctionsHit`             | Positive match against sanctions list       |
| `FraudSignal`              | Detected fraud indicator with weight        |
| `KycStatus`                | Customer due diligence completion record    |
| `SuspiciousActivityReport` | SAR filed with regulatory authority         |

### 5.4 Insurance Entities
| Entity Name                | Description                                 |
|----------------------------|---------------------------------------------|
| `InsurancePolicy`          | Active insurance contract                   |
| `Claim`                    | Filed insurance claim record                |
| `UnderwritingDecision`     | Risk-based policy eligibility determination |
| `PremiumRecord`            | Premium payment and schedule                |
| `ActuarialModel`           | Risk pricing and reserve computation model  |

### 5.5 Credit Assessment Entities
| Entity Name                | Description                                 |
|----------------------------|---------------------------------------------|
| `CreditScore`              | Computed creditworthiness indicator         |
| `LoanApplication`          | Active loan origination request             |
| `DebtServiceRatio`         | Debt-to-income ratio assessment             |
| `LtvRatio`                 | Loan-to-value collateral coverage ratio     |
| `ExpectedCreditLoss`       | IFRS 9 provisioning computation             |

---

## 6. REGULATORY REFERENCE REGISTRY

| Regulation     | Full Name                                             | Primary BFSI Domain       |
|----------------|-------------------------------------------------------|---------------------------|
| FATF Rec 10    | FATF Recommendation 10 — Customer Due Diligence       | KYC / AML                 |
| EU AMLD6       | Sixth EU Anti-Money Laundering Directive              | AML / Compliance          |
| PSD2           | Payment Services Directive 2                          | Payments                  |
| GDPR           | General Data Protection Regulation                    | All (where EU PII present)|
| Basel III      | BCBS Capital and Liquidity Framework                  | Core Banking / Credit     |
| IFRS 9         | International Financial Reporting Standard 9          | Credit / Accounting        |
| Solvency II    | EU Insurance Capital Requirements                     | Insurance                 |
| PCI-DSS        | Payment Card Industry Data Security Standard          | Payments / Card           |
| MiFID II       | Markets in Financial Instruments Directive II         | Investment                |
| ISO 20022      | Financial Services Messaging Standard                 | Payments / Clearing        |
| DPDPA 2023     | India Digital Personal Data Protection Act            | All (India-jurisdiction)  |
| RBI Guidelines | Reserve Bank of India Circulars                       | India Banking             |
| IRDAI Regs     | Insurance Regulatory and Development Authority        | India Insurance           |
