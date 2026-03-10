<!-- version: 2.0.0 | domain: risk-compliance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Risk & Compliance — Prohibited Patterns

> Code that implements any pattern below will be flagged as BLOCKER by the
> code reviewer and must be removed before any deployment.

## AML Prohibited Patterns

| Pattern                                             | Offence / Risk                    |
|-----------------------------------------------------|-----------------------------------|
| Process transfer without sanctions screening        | Legal obligation under 6AMLD      |
| Allow manual override of HOLD without audit trail   | Compliance gate violation         |
| Auto-dismiss AML alert without human review         | FIU reporting obligation          |
| Delete or suppress any AML alert record             | Evidence destruction offence      |
| Reveal SAR existence to subject of investigation    | "Tipping off" criminal offence    |
| Allow PEP without Enhanced Due Diligence            | 5AMLD Article 18-24 mandatory     |
| Base risk score on ethnicity/religion/gender        | Fair lending / ECHR Art. 14       |

## Credit Risk Prohibited Patterns

| Pattern                                             | Offence / Risk                    |
|-----------------------------------------------------|-----------------------------------|
| Grant credit without creditworthiness assessment    | CCD / MCD consumer protection     |
| Override credit limit without 4-eyes approval       | Internal controls breach          |
| Use Double/Float in RWA or VaR calculations         | Precision error in capital figures|
| Apply negative interest to retail savings           | Consumer protection law           |

## Data Prohibited Patterns

| Pattern                                             | Offence / Risk                    |
|-----------------------------------------------------|-----------------------------------|
| Export customer risk profile to unauthorised system | GDPR Art. 5(1)(b) purpose limit   |
| Store biometric data without explicit consent       | GDPR Art. 9 special category      |
| Retain SAR data beyond 5-year statutory limit       | GDPR Art. 5(1)(e) storage         |
