<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Regulatory Constraints

> **CO-STAR Constraints Block** — Ingested by code-generator.prompt.md Phase 0
> Every constraint listed here must be enforced in generated code.

## PSD2 (EU) 2015/2366

| Article   | Requirement                                        | Implementation                        |
|-----------|----------------------------------------------------|---------------------------------------|
| Art. 94   | Payment data processed only for payment services   | `dataProcessingScope` validation      |
| Art. 97   | SCA for electronic / remote payments               | `ScaValidator.require(auth)`          |
| Art. 98   | SCA exemptions (low-value, TRA, trusted list)      | `ScaExemptionEvaluator`               |
| Art. 73   | Liability for unauthorised transactions            | `UnauthorisedTxAuditLogger`           |

## SEPA Rulebooks (EPC)

| Rule Set              | Version | Constraint                                         |
|-----------------------|---------|----------------------------------------------------|
| SCT Rulebook          | v5.1    | ISO 20022 message format mandatory                 |
| SCT Inst Rulebook     | v3.0    | 10-second processing window mandatory              |
| SEPA Data Protection  | v1.0    | Customer data retention max 5 years post-execution |

## AML / CTF

| Regulation            | Requirement                                        |
|-----------------------|----------------------------------------------------|
| 6AMLD (EU 2018/1673)  | Sanctions screening before processing any payment  |
| FATF Recommendation 16| Originator + beneficiary data on all wire transfers|
| SAR Threshold         | Submit SAR for suspicious activity — no threshold  |

## PCI-DSS v4.0

| Requirement    | Scope                                    | Implementation                 |
|----------------|------------------------------------------|--------------------------------|
| PCI Req 3.4    | Mask PAN in any display/log              | `PiiMasker.maskPan()`          |
| PCI Req 4      | Encrypt cardholder data in transit       | TLS 1.3 minimum                |
| PCI Req 7      | Restrict access to cardholder data       | RBAC in security layer         |

## GDPR Art. 6, 25, 32

| Article      | Requirement                                        |
|--------------|----------------------------------------------------|
| Art. 6       | Lawful basis for processing (contract)             |
| Art. 25      | Privacy by design — minimise PII in domain model   |
| Art. 32      | Encryption of PII at rest (AES-256-GCM)            |
| Art. 17      | Right to erasure — support for anonymisation       |
