# Domain Instruction: Risk & Compliance — AML, Fraud, Sanctions, Model Risk
**Version:** 2.0.0 | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY
- **Domain:** Risk & Compliance
- **Authority:** Gates all domains; no transaction may complete without risk clearance
- **Principle:** Risk controls are non-optional and non-bypassable

---

## 2. RISK FRAMEWORKS APPLIED
[SOURCE: code-generation-master.md §5.3, §5.5]

| Framework | Regulation | Scope |
|-----------|-----------|-------|
| AML | EU 6AMLD, FATF 40 | All transactions |
| Sanctions | OFAC SDN, EU Consolidated, UN | Cross-border + high-risk domestic |
| Fraud | PSD2 EBA GL on fraud reporting | Payments, account takeover |
| Conduct Risk | MiFID II suitability | Advisory and investment products |
| Model Risk | SR 11-7 (Fed), SS 1/23 (PRA) | All quantitative models |
| Cyber Risk | DORA, NIST CSF 2.0 | All ICT systems |
| Operational Risk | Basel III SMA | Business-line level |
| Credit Risk | IRB/SA (Basel III) | All credit exposures |
| Liquidity Risk | LCR, NSFR | Treasury and ALM |
| Market Risk | FRTB IMA/SA | Trading book positions |

---

## 3. AML SCREENING RULES (MANDATORY)

### 3.1 Threshold-Based Triggers
| Rule | Threshold | Action | Time |
|------|-----------|--------|------|
| Large Transaction | > €10,000 single transaction | `AmlScreeningEvent` + SAR review | Immediate |
| Structuring Detection | 3+ transactions < €10,000 within 24h for same customer | Auto-escalation | Rolling 24h window |
| Cash Intensity | > €5,000 cash equivalent in retail | Enhanced monitoring | Immediate |
| Cross-Border Remittance | Any amount to high-risk jurisdiction | Enhanced scrutiny | Immediate |
| Wire Transfer | > €1,000 cross-border | Originator/beneficiary info required | Immediate |

### 3.2 Entity Screening
- ALL counterparties screened against: OFAC SDN, EU Consolidated, UN Security Council, HMT
- Screening MUST occur before transaction authorization (not post-settlement)
- PEP (Politically Exposed Person) screening: name, nationality, DOB fuzzy match > 85% score
- False positive rate monitoring: flag if > 2% (indicates model degradation)

---

## 4. FRAUD DETECTION PATTERNS

### 4.1 Real-Time Scoring (< 200ms SLA)
- ML model produces `FraudRiskScore` (0.0–1.0)
- Score > 0.85 → auto-decline with `FraudDeclineEvent`
- Score 0.65–0.85 → `FraudReviewEvent` + SCA challenge
- Score < 0.65 → allow with `FraudPassEvent`

### 4.2 Behavioral Patterns (Rule Engine)
| Pattern | Signal | Action |
|---------|--------|--------|
| Account Takeover | Password change + large transfer within 2h | MFA challenge |
| Card Not Present Fraud | > 3 failed CVV attempts | Card suspension |
| Velocity Anomaly | > 10 transactions per hour | Temporal block |
| Geo Velocity | Transaction in 2 countries < 1h apart | MFA challenge |
| New Payee + Large Amount | First payment > €5,000 to new IBAN | Additional confirmation |

---

## 5. MODEL RISK MANAGEMENT
[SOURCE: code-generation-master.md §5.6 analogous risk standards]

- ALL quantitative models MUST be registered in `ModelInventory`
- Models MUST have documented: purpose, assumptions, limitations, validation date
- Model validation: independent team, annual review minimum
- Model performance: monthly backtesting; alert if AUC/KS drops > 10%
- Model override: MUST be logged with `ModelOverrideRecord` and approver

---

## 6. FORBIDDEN OPERATIONS

- ⛔ Bypassing AML screening for any transaction — BLOCKER (criminal liability)
- ⛔ Silent compliance failure (any risk control exception MUST be logged) — BLOCKER
- ⛔ Hard-coding risk thresholds (must be configurable, versioned) — MAJOR
- ⛔ Using un-validated ML model in production — BLOCKER
- ⛔ Sanctions screening post-settlement — BLOCKER
- ⛔ Non-deterministic compliance outcomes — BLOCKER

---

## 7. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Type | Purpose |
|--------|------|---------|
| `AmlScreeningEvent` | Domain Event | AML trigger |
| `SuspiciousActivityReport` | Aggregate | SAR filing |
| `SanctionsScreeningResult` | Value Object | Screening outcome |
| `FraudRiskScore` | Value Object | ML fraud score |
| `FraudDeclineEvent` | Domain Event | Fraud blockage |
| `PepScreeningResult` | Value Object | PEP check |
| `ModelInventoryRecord` | Entity | Model registry entry |
| `ModelOverrideRecord` | Entity | Override audit |
| `RegulatoryAlertRecord` | Entity | Regulatory alert |
| `RiskPolicyRule` | Entity | Risk rule (versioned) |
| `ComplianceCheckResult` | Value Object | Pass/fail with reason |
| `ConductRiskAssessment` | Entity | Suitability check |
