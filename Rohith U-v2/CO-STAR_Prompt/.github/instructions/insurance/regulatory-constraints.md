<!-- version: 2.0.0 | domain: insurance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Insurance — Regulatory Constraints

## Core Regulatory Framework

| Regulation                         | Scope              | Key Obligations                                                     |
|------------------------------------|--------------------|----------------------------------------------------------------------|
| Solvency II (EU 2009/138/EC)       | EU insurers        | SCR / MCR capital; ORSA; Pillar 2 governance; Pillar 3 reporting     |
| IDD (EU 2016/97/EU)                | EU distributors    | Product oversight; demands-and-needs test; fair remuneration         |
| PRIIPs (EU 1286/2014)              | Packaged products  | Key Information Document (IPID); 3-page limit; scenario analysis     |
| GDPR (EU 2016/679)                 | All EU operations  | Art. 9 health data; DPO required; breach notification 72 hr         |
| IFRS 17                            | Global listed firms| Insurance contract measurement; CSM; PAA for short-duration         |
| FCA ICOBS (UK COBS equiv.)         | UK retail insurers | Claims handling; fair presentation; no suitability churning          |
| Consumer Duty (FCA PS22/9, 2023)   | UK FCA-authorised  | Good outcomes; value; understanding; support; fair price             |
| PRA Rulebook (Insurance — General) | UK PRA firms       | Capital adequacy; risk management; supervisory review                |
| Distance Marketing Directive       | EU distance sales  | 14-day cooling-off cancellation right for retail customers           |
| Anti-Money Laundering (AMLD 6)     | EU insurance       | CDD for single premium > €10k; suspicious transaction reporting      |

## Capital Requirements

| Metric | Formula / Constraint |
|--------|----------------------|
| SCR (Solvency Capital Requirement) | VaR 99.5% over 1-year horizon |
| MCR (Minimum Capital Requirement)  | 25%–45% of SCR; absolute floor €2.5M |
| SCR Ratio | Available Own Funds / SCR ≥ 100% |
| MCR Ratio | Available Own Funds / MCR ≥ 100% |
| ORSA | Own Risk and Solvency Assessment — annual + after material change |

## Code Enforcement Rules

| Rule ID      | Rule                                                                  |
|--------------|-----------------------------------------------------------------------|
| INS-REG-001  | SCR and MCR computations must use BigDecimal arithmetic only         |
| INS-REG-002  | Premium calculations must apply actuarial rate loaded by risk class  |
| INS-REG-003  | Claims > €50,000 require senior underwriter approval event           |
| INS-REG-004  | All policy quotes must record demands-and-needs assessment reference |
| INS-REG-005  | IPID generation required for every new retail product launch         |
| INS-REG-006  | Health / medical data (Art. 9 GDPR) must be stored in Vault only    |
| INS-REG-007  | No claim can be rejected without generating a rejection domain event |
| INS-REG-008  | Cancellation right of 14 days must be enforced for distance sales    |
| INS-REG-009  | AML screening mandatory for single premium payments above £10,000   |
| INS-REG-010  | IFRS 17 CSM must be initialised at contract inception; no retroaction|

## Compliance Reporting Requirements

| Report              | Frequency  | Recipient      |
|---------------------|------------|----------------|
| SFCR (Solvency II)  | Annual     | Regulator + Public|
| RSR (Solvency II)   | Annual     | Regulator only |
| QRT Templates       | Quarterly  | EIOPA / PRA    |
| ORSA Report         | Annual + ad hoc | Board + Regulator|
| IFRS 17 Disclosures | Half-year + annual | Investors  |
| AML Suspicious Transaction | Event-driven | FCA / NCA  |
| GDPR Breach Report  | ≤ 72 hours | ICO / local DPA|

## Prohibited Patterns

| Anti-Pattern                                            | Reason                    |
|---------------------------------------------------------|---------------------------|
| Storing SCR/MCR as `Double` or `Float`                  | Precision loss in capital  |
| Backdating policy start to before underwriting approval  | Fraudulent inception date  |
| Approving claim without fraud-check gate                | Regulatory non-compliance  |
| Issuing policy with no demands-and-needs record          | IDD violation              |
| Processing distance sale without 14-day cooling period  | DMD violation              |
| Exporting Art. 9 health data outside EU                 | GDPR Chapter V violation   |
