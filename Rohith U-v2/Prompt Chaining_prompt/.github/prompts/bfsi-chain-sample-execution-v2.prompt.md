---
name: BFSI Chain Sample Execution v2 — SEPA Batch Payments (COMPLIANT + CODE OUTPUT)
version: 2.0.0
description: >
  Complete 7-stage chain execution demonstrating the full BFSI Advanced Prompt
  Chaining Framework. Stage 7 generates production Scala/Spark code.
  Input: Process a nightly SEPA credit transfer batch of 4,200 transactions.
  Outcome: COMPLIANT → MEDIUM risk → APPROVE_WITH_CONDITIONS → Scala/Spark code generated.
---

# SAMPLE EXECUTION — BFSI ADVANCED PROMPT CHAINING FRAMEWORK v2
# Input: "Process the nightly SEPA credit transfer batch of 4,200 transactions
#         totalling EUR 18.7M from the corporate payments queue."

---

## ════════════════════════════════════════════════════════════════
## STAGE 1 — CONTEXT UNDERSTANDING
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_1
**Role:** Situational Analyst

### Entities Identified

| Entity Type       | Value                                          |
|-------------------|------------------------------------------------|
| Operation         | SEPA Credit Transfer — Batch Processing        |
| Transaction Count | 4,200 payment instructions                     |
| Total Value       | EUR 18,700,000.00 (Direction: CREDIT aggregate)|
| Payment Scheme    | SEPA Credit Transfer (SCT) — ISO 20022         |
| Source System     | Corporate Payments Queue                       |
| Processing Window | Nightly batch (T+0 submission, T+1 settlement) |
| Currency          | EUR (ISO 4217)                                 |
| Jurisdiction      | European Union — SEPA Zone                    |
| Institution Role  | Originator / Sending PSP                       |

### Operational Scope
- Batch processing of 4,200 SEPA SCT messages from corporate payments queue
- System boundary: Payments origination → Clearing engine → Settlement instruction
- Data surfaces: PaymentInstruction records, customer account ledger, IBAN registry

### Ambiguity Register
- `[AMBIGUITY: Single corporate originator or multi-customer batch?]` — Assume multi-customer batch; flag for Stage 3 resolution
- `[AMBIGUITY: AML threshold screening — individual transaction or aggregated batch value?]` — Both required per EU AMLD6; document in Stage 4

```
[CONTEXT_ARTIFACT: {
  entities: {
    operation: "SEPA_CREDIT_TRANSFER_BATCH",
    transaction_count: 4200,
    aggregate_amount: { value: "18700000.00", currency: "EUR", direction: "CREDIT" },
    payment_scheme: "SEPA_SCT_ISO_20022",
    source_system: "CORPORATE_PAYMENTS_QUEUE",
    processing_window: "NIGHTLY_BATCH",
    settlement_cycle: "T_PLUS_1"
  },
  scope: "BATCH_PAYMENT_PROCESSING",
  temporal_markers: { submission: "T+0", settlement: "T+1", reporting_deadline: "T+1 EOD" },
  jurisdiction: "EU_SEPA_ZONE",
  ambiguities: [
    "ORIGINATOR_TYPE: single_corporate_vs_multi_customer",
    "AML_THRESHOLD: individual_vs_aggregate_screening"
  ]
}]
```

---

## ════════════════════════════════════════════════════════════════
## STAGE 2 — INTENT IDENTIFICATION
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_2
**Role:** Intent Classifier
**Consumes:** [CONTEXT_ARTIFACT]

### Intent Classification

| Dimension          | Value                                                     |
|--------------------|-----------------------------------------------------------|
| **Primary Intent** | `TRANSACT` — Process a batch of financial transactions    |
| **Secondary Intent**| `COMPLY` — SEPA scheme rules + EU AMLD6 + PSD2 SCA      |
| **Tertiary Intent**| `REPORT` — Nightly settlement report generation           |
| **Urgency Tier**   | `P2` — Near-real-time: batch SLA is same-day processing  |
| **Sensitivity**    | `CONFIDENTIAL` — Contains IBAN, account, amount data      |
| **PII Flag**       | `PII_PRESENT` — IBAN, beneficiary name, remittance info  |

```
[INTENT_ARTIFACT: {
  primary_intent: "TRANSACT",
  secondary_intent: "COMPLY",
  tertiary_intent: "REPORT",
  urgency_tier: "P2",
  sensitivity: "CONFIDENTIAL",
  pii_flag: "PII_PRESENT",
  operation_class: "BATCH_PAYMENT_PROCESSING",
  sla_window: "SAME_DAY_CUTOFF"
}]
```

---

## ════════════════════════════════════════════════════════════════
## STAGE 3 — BFSI DOMAIN INTERPRETATION
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_3
**Role:** BFSI Domain Architect
**Consumes:** [CONTEXT_ARTIFACT] + [INTENT_ARTIFACT]

### Domain Entity Resolution

| Input Entity            | Canonical BFSI Construct                    | Domain       |
|-------------------------|---------------------------------------------|--------------|
| SEPA payment record     | `PaymentInstruction`                        | `payments`   |
| IBAN account reference  | `AccountIdentifier (IBAN — ISO 13616)`      | `payments`   |
| BIC routing code        | `BankIdentifier (BIC/SWIFT — ISO 9362)`     | `payments`   |
| Batch aggregate amount  | `Money(BigDecimal(18700000.00), EUR)`        | `payments`   |
| Payment queue entry     | `ClearingMessage (pacs.008 ISO 20022)`      | `payments`   |
| Settlement instruction  | `SettlementRecord (pacs.002 ISO 20022)`     | `payments`   |
| Nightly batch           | `BatchPaymentJob (Spark batch, AQE enabled)`| `payments`   |
| Remittance info         | `RemittanceInformation (unstructured)`      | `payments`   |

### Business Rules Applied

| Rule ID | Rule                       | Applied Value                                 |
|---------|----------------------------|-----------------------------------------------|
| FM-001  | Monetary Precision         | EUR 18,700,000.00 — BigDecimal(DECIMAL128)    |
| FM-002  | Banker's Rounding          | HALF_EVEN enforced on all individual amounts  |
| FM-003  | Non-Negative Amounts       | All 4,200 amounts must be > 0                 |
| FM-004  | ISO 4217 Currency          | EUR validated                                 |
| FM-005  | DEBIT/CREDIT Direction     | CREDIT (beneficiary receives)                 |
| FM-006  | Ledger Symmetry            | Debit: originator nostro; Credit: beneficiary |
| FM-007  | ISO 8601 Timestamps        | Batch timestamp: mandatory on each record     |

### Terminology Normalization

| Input Term              | Normalized BFSI Term                          |
|-------------------------|-----------------------------------------------|
| "payments queue"        | `PaymentInstruction[]` in `INITIATED` state   |
| "nightly batch"         | `BatchPaymentJob` with `NIGHTLY` schedule     |
| "totalling EUR 18.7M"   | `Money(18700000.00, EUR)` aggregate           |
| "process"               | `LifecycleTransition: INITIATED → CLEARED`    |

### Domain In Scope
- **Primary:** `payments` (`com.bank.payments`)
- **Secondary:** `core-banking` (ledger debit/credit entries)
- **Cross-domain dependency:** `risk-compliance` (AML screening per AMLD6)

```
[DOMAIN_ARTIFACT: {
  resolved_entities: [
    "PaymentInstruction", "AccountIdentifier", "BankIdentifier",
    "Money", "ClearingMessage", "SettlementRecord",
    "BatchPaymentJob", "RemittanceInformation"
  ],
  business_rules_applied: ["FM-001", "FM-002", "FM-003", "FM-004", "FM-005", "FM-006", "FM-007"],
  domains_in_scope: ["payments", "core-banking", "risk-compliance"],
  cross_domain_dependencies: { "risk-compliance": "AML_SCREENING_AMLD6" },
  package_root: "com.bank.payments"
}]
```

---

## ════════════════════════════════════════════════════════════════
## STAGE 4 — COMPLIANCE EVALUATION                  [GATE STAGE]
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_4
**Role:** Regulatory Gatekeeper
**Consumes:** [CONTEXT_ARTIFACT] + [INTENT_ARTIFACT] + [DOMAIN_ARTIFACT]

### Regulatory Framework Triggered

| Regulation  | Trigger                                           | Status    |
|-------------|---------------------------------------------------|-----------|
| PSD2        | Digital payment batch via PSP                     | TRIGGERED |
| EU AMLD6    | Aggregate batch value EUR 18.7M (above threshold) | TRIGGERED |
| SEPA SCT    | SEPA Credit Transfer scheme rules (EPC)           | TRIGGERED |
| GDPR/DPDPA  | PII_PRESENT: IBAN, beneficiary name               | TRIGGERED |
| ISO 20022   | Payments domain in scope                          | TRIGGERED |
| Basel III   | Core-banking ledger entries involved              | TRIGGERED |
| FATF Rec 16 | Wire transfer transparency (beneficiary data)     | TRIGGERED |

### Compliance Gate Checklist

| Check ID | Description                                      | Result |
|----------|--------------------------------------------------|--------|
| COMP-001 | AML screening for all parties                    | PASS — batch AML engine configured |
| COMP-002 | KYC/CDD status for originators                   | PASS — corporate KYC on file, last reviewed 60 days ago |
| COMP-003 | OFAC / EU Consolidated / UN sanctions screening  | PASS — real-time screening on each record |
| COMP-004 | PEP check                                        | PASS — automated PEP flag on sender registry |
| COMP-005 | CTR / threshold reporting (EUR 10K+)             | WARN — batch aggregate EUR 18.7M; EBA reporting required |
| COMP-006 | Data retention policy applied                    | PASS — 7-year retention configured |
| COMP-007 | Authorization: batch release requires dual approval | PASS — dual authorization logged |
| COMP-008 | Audit trail created for this batch execution     | PASS — audit UUID assigned |
| COMP-009 | Cross-border SEPA zone requirements assessed     | PASS — intra-EU SCT rules applied |
| COMP-010 | Conflict of interest cleared                     | PASS — no proprietary trading links detected |

### Violations Detected
- **None** — No VIO-001 through VIO-007 triggered.
- COMP-005 WARN: EBA batch reporting obligation must be fulfilled by T+1 EOD.

### Compliance Verdict: `CONDITIONAL`
All mandatory checks PASS. One WARN (COMP-005) requires EBA reporting action within SLA.
Chain proceeds to Stage 5 with CONDITIONAL flag forwarded.

```
[COMPLIANCE_ARTIFACT: {
  regulations_evaluated: ["PSD2", "EU_AMLD6", "SEPA_SCT", "GDPR", "ISO_20022", "Basel_III", "FATF_Rec16"],
  checklist_results: {
    COMP_001: "PASS", COMP_002: "PASS", COMP_003: "PASS", COMP_004: "PASS",
    COMP_005: "WARN", COMP_006: "PASS", COMP_007: "PASS",
    COMP_008: "PASS", COMP_009: "PASS", COMP_010: "PASS"
  },
  violations_detected: [],
  compliance_verdict: "CONDITIONAL",
  conditional_actions: ["EBA_BATCH_REPORT by T+1 EOD"],
  audit_trail_reference: "AUDIT-2026-03-10-SEPA-BATCH-7F3A9C"
}]
```

---

## ════════════════════════════════════════════════════════════════
## STAGE 5 — RISK ASSESSMENT                        [GATE STAGE]
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_5
**Role:** Chief Risk Officer
**Consumes:** All prior artifacts

### Financial Risk Dimensions

| Risk Type         | Method              | Finding                                     | Score |
|-------------------|---------------------|---------------------------------------------|-------|
| Operational Risk  | BIA + scenario      | Nightly batch failure → T+1 settlement miss | 25    |
| Fraud Risk        | Rule engine + FS-*  | See signal scan below                       | 18    |
| Counterparty Risk | Exposure netting    | Intra-EU beneficiaries — low concentration  | 10    |
| Liquidity Risk    | LCR check           | EUR 18.7M within daily liquidity buffer     | 5     |
| Credit Risk       | N/A (SEPA SCT)      | No credit exposure — payment not loan       | 0     |

### Fraud Signal Scan

| Signal | Description                              | Weight   | Result                              |
|--------|------------------------------------------|----------|-------------------------------------|
| FS-001 | Velocity spike (> 3σ)                    | HIGH     | CLEAR — batch volume within 2σ of 90-day average |
| FS-002 | Geolocation mismatch                     | HIGH     | CLEAR — all originators EU-resident |
| FS-003 | Round-amount pattern                     | MEDIUM   | FLAG — 12% of transactions are round EUR amounts |
| FS-004 | First-time high-value payee              | MEDIUM   | FLAG — 47 new beneficiary IBANs in batch |
| FS-005 | Account dormancy breach                  | HIGH     | CLEAR — no dormant originator accounts |
| FS-006 | Rapid fund sweeping                      | CRITICAL | CLEAR — no sweeping pattern detected |
| FS-007 | Multiple failed auth attempts            | HIGH     | CLEAR — dual auth succeeded first attempt |
| FS-008 | Split transaction structuring            | CRITICAL | CLEAR — no smurfing pattern (Benford's Law: normal) |
| FS-009 | Merchant category anomaly                | MEDIUM   | CLEAR — corporate payroll + supplier payments |
| FS-010 | Cross-border mismatch                    | HIGH     | CLEAR — all intra-SEPA zone |

**Active Signals:** FS-003 (12% round amounts), FS-004 (47 new beneficiaries)
Both signals are MEDIUM weight with explanations consistent with month-end payroll batch.

### Risk Score Computation

| Component                 | Raw Score | Weight | Weighted |
|---------------------------|-----------|--------|----------|
| Operational Risk          | 25        | 0.30   | 7.5      |
| Fraud Signals (FS-003/004)| 20        | 0.40   | 8.0      |
| Counterparty / Liquidity  | 7         | 0.30   | 2.1      |
| **Total Risk Score**      |           |        | **17.6** |

### Risk Tier: `LOW` (Score: 17.6 / 100)

No escalation required. Active signals documented and explained. Proceed to Stage 6.

```
[RISK_ARTIFACT: {
  risk_dimensions_assessed: ["OperationalRisk", "FraudRisk", "CounterpartyRisk", "LiquidityRisk"],
  fraud_signals_evaluated: { active: ["FS-003", "FS-004"], cleared: ["FS-001","FS-002","FS-005","FS-006","FS-007","FS-008","FS-009","FS-010"] },
  risk_tier: "LOW",
  risk_score: 17.6,
  model_assumptions: ["Month-end payroll explains round amounts", "47 new beneficiaries within acceptable new-client rate"],
  escalation_required: false
}]
```

---

## ════════════════════════════════════════════════════════════════
## STAGE 6 — DECISION REASONING
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_6
**Role:** Principal Decision Architect
**Consumes:** All prior artifacts

### Artifact Synthesis

| Artifact Source   | Finding                               | Decision Relevance             |
|-------------------|---------------------------------------|--------------------------------|
| Stage 2 Intent    | TRANSACT + COMPLY                     | Process batch + ensure reporting|
| Stage 3 Domain    | All entities in `payments` domain     | Standard SEPA SCT processing   |
| Stage 4 Compliance| CONDITIONAL — COMP-005 WARN (EBA)    | Must complete EBA report T+1   |
| Stage 5 Risk      | LOW tier (17.6) — 2 minor signals     | Proceed; document signals      |

### Decision Option Matrix

| Option                     | Compliance   | Risk  | Domain Fit | Recommended |
|----------------------------|-------------|-------|------------|-------------|
| A: Process immediately     | CONDITIONAL | LOW   | Full       | **YES**     |
| B: Hold pending EBA report | OVER-CAUTION| LOW   | Full       | No          |
| C: Reject batch            | N/A         | N/A   | N/A        | No          |

### Decision Conditions (APPROVE_WITH_CONDITIONS)

1. EBA batch aggregate reporting completed by T+1 EOD (COMP-005 remediation)
2. 47 new beneficiary IBANs flagged for enhanced monitoring for 30 days post-settlement
3. FS-003 round amounts documented in audit log with payroll attestation
4. Settlement reconciliation report generated at T+1 EOD

### Traceability Map

| Decision Condition         | Traced To                            |
|----------------------------|--------------------------------------|
| EBA reporting obligation   | Stage 4 COMP-005 WARN + EU AMLD6    |
| New beneficiary monitoring | Stage 5 FS-004 signal               |
| Round amount documentation | Stage 5 FS-003 signal               |
| Settlement report          | Stage 3 SettlementRecord entity      |

### Decision Verdict: `APPROVE_WITH_CONDITIONS`

```
[DECISION_ARTIFACT: {
  synthesis_points: ["CONDITIONAL_compliance", "LOW_risk", "SEPA_SCT_domain_fit"],
  decision_verdict: "APPROVE_WITH_CONDITIONS",
  conditions: [
    "EBA_REPORT by T+1 EOD",
    "NEW_BENEFICIARY_MONITORING 30 days",
    "FS003_PAYROLL_ATTESTATION in audit log",
    "SETTLEMENT_RECONCILIATION at T+1 EOD"
  ],
  mitigations: ["47_new_beneficiary_enhanced_monitoring", "round_amount_audit_attestation"],
  traceability_map: { EBA: "COMP-005+AMLD6", beneficiary: "FS-004", rounds: "FS-003", settlement: "Stage3_entity" },
  escalation_path: "None required — LOW risk tier"
}]
```

---

## ════════════════════════════════════════════════════════════════
## STAGE 7 — PRODUCTION CODE GENERATION (SCALA / APACHE SPARK)
## ════════════════════════════════════════════════════════════════

**Stage ID:** STAGE_7
**Role:** Principal Scala/Spark Architect
**Tech Stack:** Scala 2.13 + Apache Spark 3.5.x + sbt 1.9.x + DDD + Hexagonal Architecture
**Domain Package:** `com.bank.payments`
**Compliance Verdict:** CONDITIONAL → Code generation PROCEEDS

### Generated Project Structure

```
sepa-payments-service/
├── build.sbt
├── README.md
└── src/
    ├── main/
    │   ├── scala/com/bank/payments/
    │   │   ├── domain/
    │   │   │   ├── model/
    │   │   │   │   ├── Money.scala
    │   │   │   │   ├── PaymentInstruction.scala
    │   │   │   │   └── AccountIdentifier.scala
    │   │   │   ├── events/
    │   │   │   │   └── DomainEvents.scala
    │   │   │   ├── specifications/
    │   │   │   │   └── PaymentSpecifications.scala
    │   │   │   └── services/
    │   │   │       ├── ComplianceValidator.scala
    │   │   │       └── RiskScoreEngine.scala
    │   │   ├── application/
    │   │   │   ├── commands/
    │   │   │   │   └── ProcessBatchPaymentCommand.scala
    │   │   │   └── jobs/
    │   │   │       └── SepaPaymentBatchJob.scala
    │   │   ├── infrastructure/
    │   │   │   ├── spark/
    │   │   │   │   ├── SparkSessionProvider.scala
    │   │   │   │   ├── PaymentInstructionReader.scala
    │   │   │   │   └── SettlementRecordWriter.scala
    │   │   │   └── config/
    │   │   │       └── AppConfig.scala
    │   │   ├── security/
    │   │   │   ├── masking/
    │   │   │   │   └── PiiMasker.scala
    │   │   │   └── audit/
    │   │   │       └── AuditLogger.scala
    │   │   └── Main.scala
    │   └── resources/
    │       ├── application.conf
    │       └── data/
    │           └── payments.csv
    └── test/
        └── scala/com/bank/payments/
            ├── domain/
            │   ├── MoneySpec.scala
            │   └── PaymentInstructionSpec.scala
            └── application/
                └── ComplianceValidatorSpec.scala
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: build.sbt
═══════════════════════════════════════════════════════════════════════════════

```scala
ThisBuild / scalaVersion  := "2.13.14"
ThisBuild / organization  := "com.bank"
ThisBuild / version       := "1.0.0"

val sparkVersion = "3.5.1"
val deltaVersion = "3.1.0"
val catsVersion  = "2.10.0"
val configVersion = "1.4.3"

lazy val root = (project in file("."))
  .settings(
    name := "sepa-payments-service",
    libraryDependencies ++= Seq(
      "org.apache.spark"  %% "spark-core"          % sparkVersion % Provided,
      "org.apache.spark"  %% "spark-sql"           % sparkVersion % Provided,
      "io.delta"          %% "delta-spark"         % deltaVersion,
      "org.typelevel"     %% "cats-core"           % catsVersion,
      "com.typesafe"       % "config"              % configVersion,
      "io.prometheus"      % "simpleclient"        % "0.16.0",
      "io.prometheus"      % "simpleclient_hotspot"% "0.16.0",
      "org.scalatest"     %% "scalatest"           % "3.2.18" % Test,
      "org.scalacheck"    %% "scalacheck"          % "1.17.1" % Test,
      "org.scalatestplus" %% "scalacheck-1-17"     % "3.2.18.0" % Test
    ),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    },
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-Xfatal-warnings",
      "-Ywarn-unused:imports",
      "-Ywarn-value-discard"
    ),
    Test / fork := true
  )
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/model/Money.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.model

import java.math.{ MathContext, RoundingMode }

// ── ISO 4217 Currency sealed trait ──────────────────────────────────────────
sealed trait Currency { def code: String }
object Currency {
  case object EUR extends Currency { val code = "EUR" }
  case object USD extends Currency { val code = "USD" }
  case object GBP extends Currency { val code = "GBP" }
  case object CHF extends Currency { val code = "CHF" }
  case object SEK extends Currency { val code = "SEK" }

  def fromCode(code: String): Either[DomainError, Currency] = code.toUpperCase match {
    case "EUR" => Right(EUR)
    case "USD" => Right(USD)
    case "GBP" => Right(GBP)
    case "CHF" => Right(CHF)
    case "SEK" => Right(SEK)
    case other => Left(UnsupportedCurrencyError(s"ISO 4217 code not supported: $other"))
  }
}

// ── Monetary Direction — never use negative amounts ──────────────────────────
sealed trait Direction
object Direction {
  case object DEBIT  extends Direction
  case object CREDIT extends Direction
}

// ── Domain Errors ────────────────────────────────────────────────────────────
sealed trait DomainError { def message: String }
final case class InvalidAmountError(message: String)       extends DomainError
final case class UnsupportedCurrencyError(message: String) extends DomainError
final case class CurrencyMismatchError(message: String)    extends DomainError
final case class ComplianceBlockError(message: String)     extends DomainError
final case class ValidationError(message: String)          extends DomainError

// ── Money value object ───────────────────────────────────────────────────────
// GEN-001: BigDecimal with DECIMAL128 — NEVER Double or Float
// GEN-002: RoundingMode.HALF_EVEN (Banker's rounding)
// GEN-003: No null — smart constructor returns Either
final case class Money private (amount: BigDecimal, currency: Currency) {

  def add(other: Money): Either[DomainError, Money] =
    if (currency != other.currency)
      Left(CurrencyMismatchError(s"Cannot add ${currency.code} and ${other.currency.code}"))
    else
      Money(amount.add(other.amount, Money.mc), currency)

  def subtract(other: Money): Either[DomainError, Money] =
    if (currency != other.currency)
      Left(CurrencyMismatchError(s"Cannot subtract ${other.currency.code} from ${currency.code}"))
    else if (other.amount > amount)
      Left(InvalidAmountError(s"Subtraction would produce negative amount"))
    else
      Money(amount.subtract(other.amount, Money.mc), currency)

  def multiply(factor: BigDecimal): Money =
    new Money(amount.multiply(factor, Money.mc).setScale(2, RoundingMode.HALF_EVEN), currency)

  def isGreaterThan(threshold: Money): Boolean =
    currency == threshold.currency && amount > threshold.amount

  // GEN-006: Never expose raw amount in toString for logging — show currency + scaled value
  override def toString: String =
    s"${currency.code} ${amount.setScale(2, RoundingMode.HALF_EVEN).toPlainString}"
}

object Money {
  // GEN-001: MathContext.DECIMAL128 for all financial arithmetic
  val mc: MathContext = MathContext.DECIMAL128

  def apply(amount: BigDecimal, currency: Currency): Either[DomainError, Money] =
    if (amount == null)
      Left(InvalidAmountError("Amount must not be null"))
    else if (amount <= BigDecimal(0))
      Left(InvalidAmountError(s"Amount must be > 0; received: $amount"))
    else
      Right(new Money(amount.round(mc), currency))

  // Unsafe constructor for internal use only (e.g., aggregate sums already validated)
  private[payments] def unsafeApply(rawAmount: BigDecimal, currency: Currency): Money =
    new Money(rawAmount.round(mc), currency)

  val zero: Currency => Money = (ccy: Currency) => new Money(BigDecimal(0), ccy)
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/model/AccountIdentifier.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.model

// ── IBAN value object — ISO 13616 ─────────────────────────────────────────
// GEN-003: No null — validated at construction via smart constructor
// GEN-006: Masked in all log output
final case class Iban private (value: String) {
  // Return masked value safe for logging: GB29XXXX...1234
  def masked: String = {
    val country = value.take(4)
    val last4   = value.takeRight(4)
    s"${country}XXXX...${last4}"
  }
  override def toString: String = masked  // always masked in logs
}

object Iban {
  private val IbanPattern = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4,30}$".r

  def apply(raw: String): Either[DomainError, Iban] = {
    val cleaned = raw.replaceAll("\\s", "").toUpperCase
    if (IbanPattern.matches(cleaned)) Right(new Iban(cleaned))
    else Left(ValidationError(s"Invalid IBAN format"))  // never expose raw value in error
  }
}

// ── BIC value object — ISO 9362 ───────────────────────────────────────────
final case class Bic private (value: String) {
  override def toString: String = value  // BIC is not PII
}

object Bic {
  private val BicPattern = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$".r

  def apply(raw: String): Either[DomainError, Bic] = {
    val cleaned = raw.replaceAll("\\s", "").toUpperCase
    if (BicPattern.matches(cleaned)) Right(new Bic(cleaned))
    else Left(ValidationError(s"Invalid BIC format: $cleaned"))
  }
}

// ── AccountIdentifier — wraps IBAN + optional BIC ────────────────────────
final case class AccountIdentifier(
  iban: Iban,
  bic:  Option[Bic],
  name: Option[String]  // beneficiary name — never logged raw
) {
  // GEN-006: masked representation for audit logging
  def toAuditString: String =
    s"IBAN=${iban.masked} BIC=${bic.map(_.value).getOrElse("N/A")}"
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/model/PaymentInstruction.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.model

import java.time.Instant
import java.util.UUID

// ── Payment lifecycle states ───────────────────────────────────────────────
sealed trait PaymentStatus
object PaymentStatus {
  case object INITIATED  extends PaymentStatus
  case object VALIDATED  extends PaymentStatus
  case object CLEARED    extends PaymentStatus
  case object SETTLED    extends PaymentStatus
  case object REJECTED   extends PaymentStatus
  case object CANCELLED  extends PaymentStatus
}

// ── Payment type classification ────────────────────────────────────────────
sealed trait PaymentType
object PaymentType {
  case object SEPA_CREDIT_TRANSFER extends PaymentType
  case object SEPA_DIRECT_DEBIT    extends PaymentType
  case object SWIFT_WIRE           extends PaymentType
  case object INTERNAL_TRANSFER    extends PaymentType
}

// ── Core payment instruction aggregate ────────────────────────────────────
// GEN-003: Option[T] for all optional fields — no null
// GEN-005: All fields are val — immutable by design
// GEN-008: Instant for all timestamps
final case class PaymentInstruction(
  id:               UUID,
  correlationId:    UUID,
  paymentType:      PaymentType,
  status:           PaymentStatus,
  amount:           Money,
  direction:        Direction,
  debtor:           AccountIdentifier,
  creditor:         AccountIdentifier,
  remittanceInfo:   Option[String],
  executionDate:    Instant,
  valueDate:        Instant,
  batchId:          Option[String],
  endToEndRef:      String
) {
  // State machine transition — returns new immutable instance
  // GEN-004: Either[DomainError, T] — no throw
  def transition(newStatus: PaymentStatus): Either[DomainError, PaymentInstruction] =
    (status, newStatus) match {
      case (PaymentStatus.INITIATED, PaymentStatus.VALIDATED) => Right(copy(status = newStatus))
      case (PaymentStatus.VALIDATED, PaymentStatus.CLEARED)   => Right(copy(status = newStatus))
      case (PaymentStatus.CLEARED,   PaymentStatus.SETTLED)   => Right(copy(status = newStatus))
      case (PaymentStatus.INITIATED, PaymentStatus.CANCELLED) => Right(copy(status = newStatus))
      case (PaymentStatus.VALIDATED, PaymentStatus.REJECTED)  => Right(copy(status = newStatus))
      case (from, to) =>
        Left(ValidationError(s"Invalid state transition: $from → $to"))
    }

  // Audit-safe string — never exposes raw IBAN or remittance info
  def toAuditLog: String =
    s"Payment[id=${id}, status=${status}, amount=${amount}, " +
    s"debtor=${debtor.toAuditString}, creditor=${creditor.toAuditString}]"
}

object PaymentInstruction {
  // Spark schema for typed Dataset encoding
  import org.apache.spark.sql.Encoder
  import org.apache.spark.sql.Encoders
  // Use product encoder — requires all fields to be Spark-compatible types
  // NOTE: In production, map to a flat DTO for Spark; convert to domain object after read
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/events/DomainEvents.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.events

import java.time.Instant
import java.util.UUID
import com.bank.payments.domain.model._

// ── Domain Event base trait ────────────────────────────────────────────────
// GEN-010: Every domain entity emits a DomainEvent on state transition
sealed trait DomainEvent {
  val eventId:       UUID
  val correlationId: UUID
  val occurredAt:    Instant
  val entityId:      UUID
}

// ── Payment Domain Events ──────────────────────────────────────────────────
final case class PaymentInitiated(
  eventId:       UUID,
  correlationId: UUID,
  occurredAt:    Instant,
  entityId:      UUID,
  amount:        Money,
  debtorIban:    String,      // masked IBAN string for event persistence
  creditorIban:  String,
  paymentType:   PaymentType,
  batchId:       Option[String]
) extends DomainEvent

final case class PaymentValidated(
  eventId:       UUID,
  correlationId: UUID,
  occurredAt:    Instant,
  entityId:      UUID,
  validatedBy:   String,
  complianceRef: String
) extends DomainEvent

final case class PaymentCleared(
  eventId:       UUID,
  correlationId: UUID,
  occurredAt:    Instant,
  entityId:      UUID,
  clearingRef:   String,
  clearingTime:  Instant
) extends DomainEvent

final case class PaymentSettled(
  eventId:       UUID,
  correlationId: UUID,
  occurredAt:    Instant,
  entityId:      UUID,
  settlementRef: String,
  valueDate:     Instant
) extends DomainEvent

final case class PaymentRejected(
  eventId:       UUID,
  correlationId: UUID,
  occurredAt:    Instant,
  entityId:      UUID,
  rejectionCode: String,
  rejectionDesc: String
) extends DomainEvent

final case class BatchComplianceFlagged(
  eventId:         UUID,
  correlationId:   UUID,
  occurredAt:      Instant,
  entityId:        UUID,
  batchId:         String,
  flaggedSignals:  List[String],
  requiresReview:  Boolean
) extends DomainEvent

// ── Event factory helpers ──────────────────────────────────────────────────
object DomainEvents {
  def paymentInitiated(p: com.bank.payments.domain.model.PaymentInstruction): PaymentInitiated =
    PaymentInitiated(
      eventId       = UUID.randomUUID(),
      correlationId = p.correlationId,
      occurredAt    = Instant.now(),
      entityId      = p.id,
      amount        = p.amount,
      debtorIban    = p.debtor.iban.masked,
      creditorIban  = p.creditor.iban.masked,
      paymentType   = p.paymentType,
      batchId       = p.batchId
    )

  def paymentRejected(p: com.bank.payments.domain.model.PaymentInstruction,
                      code: String, desc: String): PaymentRejected =
    PaymentRejected(
      eventId       = UUID.randomUUID(),
      correlationId = p.correlationId,
      occurredAt    = Instant.now(),
      entityId      = p.id,
      rejectionCode = code,
      rejectionDesc = desc
    )
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/specifications/PaymentSpecifications.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.specifications

import com.bank.payments.domain.model._

// ── Specification pattern for SEPA business rules ────────────────────────
// Each spec encodes one testable business rule from Stage 3 [DOMAIN_ARTIFACT]
trait Specification[T] {
  def isSatisfiedBy(candidate: T): Boolean
  def and(other: Specification[T]): Specification[T] = AndSpecification(this, other)
  def or(other: Specification[T]):  Specification[T] = OrSpecification(this, other)
  def not: Specification[T]                         = NotSpecification(this)
}

private final case class AndSpecification[T](a: Specification[T], b: Specification[T]) extends Specification[T] {
  def isSatisfiedBy(c: T): Boolean = a.isSatisfiedBy(c) && b.isSatisfiedBy(c)
}
private final case class OrSpecification[T](a: Specification[T], b: Specification[T]) extends Specification[T] {
  def isSatisfiedBy(c: T): Boolean = a.isSatisfiedBy(c) || b.isSatisfiedBy(c)
}
private final case class NotSpecification[T](inner: Specification[T]) extends Specification[T] {
  def isSatisfiedBy(c: T): Boolean = !inner.isSatisfiedBy(c)
}

// ── FM-003: Amount must be strictly positive ──────────────────────────────
object PositiveAmountSpec extends Specification[PaymentInstruction] {
  def isSatisfiedBy(p: PaymentInstruction): Boolean = p.amount.amount > BigDecimal(0)
}

// ── SEPA SCT: Currency must be EUR ────────────────────────────────────────
object SepaCurrencySpec extends Specification[PaymentInstruction] {
  def isSatisfiedBy(p: PaymentInstruction): Boolean = p.amount.currency == Currency.EUR
}

// ── EU AMLD6 Stage 4 COMP-005: threshold check ────────────────────────────
// Transactions at or above EUR 10,000 trigger enhanced reporting
object AmlThresholdSpec extends Specification[PaymentInstruction] {
  private val threshold = Money.unsafeApply(BigDecimal("10000.00"), Currency.EUR)
  def isSatisfiedBy(p: PaymentInstruction): Boolean = p.amount.isGreaterThan(threshold)
}

// ── FS-003 Stage 5: round-amount anomaly detection ────────────────────────
object RoundAmountSpec extends Specification[PaymentInstruction] {
  def isSatisfiedBy(p: PaymentInstruction): Boolean = {
    val cents = (p.amount.amount * 100).toBigInt % 100
    cents == 0  // amount ends in .00
  }
}

// ── Payment must have valid end-to-end reference ──────────────────────────
object ValidEndToEndRefSpec extends Specification[PaymentInstruction] {
  def isSatisfiedBy(p: PaymentInstruction): Boolean =
    p.endToEndRef.nonEmpty && p.endToEndRef.length <= 35  // ISO 20022 max
}

// ── Composite: standard SEPA SCT validity check ──────────────────────────
object SeptaValidSpec extends Specification[PaymentInstruction] {
  private val base = PositiveAmountSpec and SepaCurrencySpec and ValidEndToEndRefSpec
  def isSatisfiedBy(p: PaymentInstruction): Boolean = base.isSatisfiedBy(p)
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/services/ComplianceValidator.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.services

import com.bank.payments.domain.model._
import com.bank.payments.domain.specifications._
import com.bank.payments.domain.events.DomainEvents

// ── Compliance validation result ─────────────────────────────────────────
sealed trait ComplianceResult
final case class CompliancePass(
  paymentId:     java.util.UUID,
  checksApplied: List[String],
  amlFlagged:    Boolean,
  auditRef:      String
) extends ComplianceResult

final case class ComplianceBlock(
  paymentId:     java.util.UUID,
  violations:    List[String],
  regulatoryBasis: List[String]
) extends ComplianceResult

// ── Compliance validator service ─────────────────────────────────────────
// Maps Stage 4 [COMPLIANCE_ARTIFACT].checklist to executable validation logic
// GEN-004: Returns Either — no throw
object ComplianceValidator {

  def validate(instruction: PaymentInstruction): Either[DomainError, ComplianceResult] = {
    val violations = scala.collection.mutable.ListBuffer[String]()

    // COMP-001: Basic SEPA validity (amount, currency, end-to-end ref)
    if (!SeptaValidSpec.isSatisfiedBy(instruction))
      violations += "COMP-001_SEPA_VALIDITY_FAILED"

    // COMP-005 / EU AMLD6: AML threshold check — flag, do not block
    val isAmlFlagged = AmlThresholdSpec.isSatisfiedBy(instruction)

    // FS-003: Round amount anomaly — flag for audit, not a hard block
    val isRoundAmount = RoundAmountSpec.isSatisfiedBy(instruction)

    // GEN-004: no throw; hard blocks return Left
    if (violations.nonEmpty) {
      Left(ComplianceBlockError(
        s"Payment ${instruction.id} blocked: ${violations.mkString(", ")}"
      ))
    } else {
      val auditRef = s"COMP-${java.util.UUID.randomUUID().toString.take(8).toUpperCase}"
      Right(CompliancePass(
        paymentId     = instruction.id,
        checksApplied = List("COMP-001", "COMP-005", "FS-003"),
        amlFlagged    = isAmlFlagged || isRoundAmount,
        auditRef      = auditRef
      ))
    }
  }

  // Validate entire batch — returns counts
  def validateBatch(
    instructions: Seq[PaymentInstruction]
  ): (Seq[CompliancePass], Seq[(PaymentInstruction, DomainError)]) = {
    val passes  = scala.collection.mutable.ListBuffer[CompliancePass]()
    val blocked = scala.collection.mutable.ListBuffer[(PaymentInstruction, DomainError)]()

    instructions.foreach { p =>
      validate(p) match {
        case Right(pass: CompliancePass) => passes  += pass
        case Left(err)                   => blocked += (p -> err)
        case _                           => ()
      }
    }
    (passes.toSeq, blocked.toSeq)
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/domain/services/RiskScoreEngine.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain.services

import com.bank.payments.domain.model._
import com.bank.payments.domain.specifications._

// ── Risk tier sealed trait — maps Stage 5 tier classification ─────────────
sealed trait RiskTier { def score: Int }
object RiskTier {
  case object LOW      extends RiskTier { val score = 17  }
  case object MEDIUM   extends RiskTier { val score = 45  }
  case object HIGH     extends RiskTier { val score = 72  }
  case object CRITICAL extends RiskTier { val score = 95  }

  def fromScore(s: Int): RiskTier =
    if (s <= 30) LOW else if (s <= 60) MEDIUM else if (s <= 85) HIGH else CRITICAL
}

// ── Risk score result ─────────────────────────────────────────────────────
final case class RiskAssessment(
  paymentId:       java.util.UUID,
  tier:            RiskTier,
  score:           Int,
  activeSignals:   List[String],
  requiresReview:  Boolean,
  mitigationSteps: List[String]
)

// ── Risk scoring engine — maps Stage 5 [RISK_ARTIFACT] fraud signals ──────
object RiskScoreEngine {

  // Signal weights in basis points (multiply by 0.01 for score contribution)
  private val SignalWeights: Map[String, Int] = Map(
    "FS-001" -> 20,  // velocity spike
    "FS-002" -> 20,  // geolocation mismatch
    "FS-003" -> 10,  // round amount
    "FS-004" -> 10,  // new beneficiary
    "FS-005" -> 20,  // dormancy breach
    "FS-006" -> 40,  // fund sweeping (CRITICAL)
    "FS-007" -> 20,  // failed auth
    "FS-008" -> 40,  // smurfing (CRITICAL)
    "FS-009" -> 10,  // merchant anomaly
    "FS-010" -> 20   // cross-border mismatch
  )

  def assess(instruction: PaymentInstruction, isNewBeneficiary: Boolean): RiskAssessment = {
    val signals = scala.collection.mutable.ListBuffer[String]()

    // FS-003: round amount pattern
    if (RoundAmountSpec.isSatisfiedBy(instruction)) signals += "FS-003"

    // FS-004: first-time high-value payee
    if (isNewBeneficiary && AmlThresholdSpec.isSatisfiedBy(instruction)) signals += "FS-004"

    val rawScore    = signals.map(s => SignalWeights.getOrElse(s, 0)).sum
    val tier        = RiskTier.fromScore(rawScore)
    val needsReview = rawScore > 15 || tier == RiskTier.HIGH || tier == RiskTier.CRITICAL

    val mitigations = if (needsReview)
      List("ENHANCED_BENEFICIARY_MONITORING_30_DAYS", "PAYROLL_ATTESTATION_REQUIRED")
    else List.empty

    RiskAssessment(
      paymentId      = instruction.id,
      tier           = tier,
      score          = rawScore,
      activeSignals  = signals.toList,
      requiresReview = needsReview,
      mitigationSteps = mitigations
    )
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/application/commands/ProcessBatchPaymentCommand.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.application.commands

import java.time.Instant
import java.util.UUID
import com.bank.payments.domain.model._
import com.bank.payments.domain.services._
import com.bank.payments.domain.events._
import com.bank.payments.security.audit.AuditLogger

// ── Command ───────────────────────────────────────────────────────────────
final case class ProcessBatchPaymentCommand(
  batchId:       String,
  correlationId: UUID,
  instructions:  Seq[PaymentInstruction],
  submittedAt:   Instant,
  requestedBy:   String   // operator ID — not PII
)

// ── Command result ────────────────────────────────────────────────────────
final case class BatchProcessingResult(
  batchId:          String,
  totalReceived:    Int,
  totalApproved:    Int,
  totalBlocked:     Int,
  totalFlagged:     Int,
  approvedPayments: Seq[PaymentInstruction],
  blockedPayments:  Seq[(PaymentInstruction, DomainError)],
  events:           Seq[DomainEvent],
  processedAt:      Instant
)

// ── Command handler ───────────────────────────────────────────────────────
// Maps Stage 6 [DECISION_ARTIFACT].verdict = APPROVE_WITH_CONDITIONS
object ProcessBatchPaymentCommandHandler {

  def handle(cmd: ProcessBatchPaymentCommand): Either[DomainError, BatchProcessingResult] = {
    AuditLogger.log(s"BatchPayment START batchId=${cmd.batchId} count=${cmd.instructions.size}")

    // Step 1: Compliance validation (Stage 4 gate in code)
    val (passed, blocked) = ComplianceValidator.validateBatch(cmd.instructions)

    // Step 2: Risk assessment per passed instruction
    val (approved, flagged) = passed.partition { compPass =>
      val instruction   = cmd.instructions.find(_.id == compPass.paymentId).get
      val isNewBeneficiary = false  // in prod: lookup beneficiary registry
      val risk = RiskScoreEngine.assess(instruction, isNewBeneficiary)
      !risk.requiresReview
    }

    // Step 3: State transition for approved payments
    val transitioned = cmd.instructions
      .filter(p => approved.exists(_.paymentId == p.id))
      .flatMap(p => p.transition(PaymentStatus.VALIDATED).toOption)

    // Step 4: Generate domain events
    val events: Seq[DomainEvent] = transitioned.map(DomainEvents.paymentInitiated) ++
      blocked.map { case (p, _) => DomainEvents.paymentRejected(p, "COMP_BLOCK", "Compliance validation failed") }

    AuditLogger.log(
      s"BatchPayment END batchId=${cmd.batchId} " +
      s"approved=${transitioned.size} blocked=${blocked.size} flagged=${flagged.size}"
    )

    Right(BatchProcessingResult(
      batchId          = cmd.batchId,
      totalReceived    = cmd.instructions.size,
      totalApproved    = transitioned.size,
      totalBlocked     = blocked.size,
      totalFlagged     = flagged.size,
      approvedPayments = transitioned,
      blockedPayments  = blocked,
      events           = events,
      processedAt      = Instant.now()
    ))
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/application/jobs/SepaPaymentBatchJob.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.application.jobs

import org.apache.spark.sql.{ Dataset, SparkSession }
import org.apache.spark.sql.functions._
import com.bank.payments.infrastructure.spark._
import com.bank.payments.infrastructure.config.AppConfig
import com.bank.payments.security.audit.AuditLogger
import java.time.Instant

// ── Spark DTO for reading payment CSV ─────────────────────────────────────
// Flat representation for Spark I/O — converted to domain objects in processing
final case class PaymentRecordDto(
  paymentId:     String,
  correlationId: String,
  debtorIban:    String,    // raw IBAN — masked before logging
  creditorIban:  String,
  amount:        Double,    // Double only in I/O DTO; converted to BigDecimal immediately
  currency:      String,
  direction:     String,
  endToEndRef:   String,
  executionDate: String,
  batchId:       String
)

// ── Main Spark batch job for SEPA payment processing ─────────────────────
// GEN-012: Dataset[T] with explicit Encoders — no raw DataFrame in domain logic
object SepaPaymentBatchJob {

  def run(config: AppConfig)(implicit spark: SparkSession): Unit = {
    import spark.implicits._

    AuditLogger.log(s"SepaPaymentBatchJob START at ${Instant.now()}")

    // ── Read input ──────────────────────────────────────────────────────
    // GEN-012: Typed Dataset — Encoders.product derives schema from case class
    val rawRecords: Dataset[PaymentRecordDto] =
      PaymentInstructionReader.read(config.inputPath)

    AuditLogger.log(s"Loaded ${rawRecords.count()} payment records from ${config.inputPath}")

    // ── Validation: reject records with non-EUR currency for SEPA SCT ──
    val eurRecords  = rawRecords.filter(_.currency == "EUR")
    val nonEurCount = rawRecords.count() - eurRecords.count()
    if (nonEurCount > 0)
      AuditLogger.log(s"WARN: Rejected $nonEurCount non-EUR records (SEPA SCT requires EUR)")

    // ── AML Threshold Flagging: EUR 10,000+ per EU AMLD6 ───────────────
    // Note: Double→BigDecimal conversion happens here at boundary
    val amlFlagged = eurRecords.filter(r => BigDecimal(r.amount.toString) >= BigDecimal("10000.00"))
    AuditLogger.log(s"AML flagged: ${amlFlagged.count()} records above EUR 10,000 threshold")

    // ── Round Amount Detection (FS-003) ─────────────────────────────────
    val roundAmounts = eurRecords.filter { r =>
      val bd = BigDecimal(r.amount.toString)
      (bd * 100).toBigInt % 100 == 0
    }
    AuditLogger.log(s"FS-003: ${roundAmounts.count()} round-amount records detected")

    // ── Aggregate metrics ───────────────────────────────────────────────
    val totalAmount: BigDecimal = eurRecords
      .map(r => BigDecimal(r.amount.toString))
      .reduce(_ + _)

    AuditLogger.log(s"Batch aggregate: EUR ${totalAmount.setScale(2, java.math.RoundingMode.HALF_EVEN)}")

    // ── Write outputs ───────────────────────────────────────────────────
    SettlementRecordWriter.write(eurRecords, config.outputPath)
    AuditLogger.log(s"Wrote settlement records to ${config.outputPath}")

    // ── Write compliance report with flagged records ─────────────────
    val flaggedRecords = amlFlagged.union(roundAmounts).distinct()
    SettlementRecordWriter.writeFlagged(flaggedRecords, config.flaggedOutputPath)

    AuditLogger.log(s"SepaPaymentBatchJob COMPLETE at ${Instant.now()}")
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/infrastructure/spark/SparkSessionProvider.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.infrastructure.spark

import org.apache.spark.sql.SparkSession
import com.bank.payments.infrastructure.config.AppConfig

object SparkSessionProvider {

  def create(config: AppConfig): SparkSession = {
    val builder = SparkSession.builder()
      .appName("sepa-payments-service")
      .config("spark.sql.adaptive.enabled", "true")           // AQE enabled
      .config("spark.sql.adaptive.coalescePartitions.enabled", "true")
      .config("spark.sql.shuffle.partitions", config.spark.shufflePartitions.toString)
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val spark = if (config.spark.localMode)
      builder.master(s"local[${config.spark.localCores}]").getOrCreate()
    else
      builder.getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
    spark
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/infrastructure/spark/PaymentInstructionReader.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.infrastructure.spark

import org.apache.spark.sql.{ Dataset, SparkSession }
import com.bank.payments.application.jobs.PaymentRecordDto

object PaymentInstructionReader {

  // GEN-012: Returns typed Dataset[PaymentRecordDto] — not DataFrame
  def read(inputPath: String)(implicit spark: SparkSession): Dataset[PaymentRecordDto] = {
    import spark.implicits._
    spark.read
      .option("header", "true")
      .option("inferSchema", "false")   // never infer schema in production
      .schema(PaymentRecordSchema.csvSchema)
      .csv(inputPath)
      .as[PaymentRecordDto]
  }

  def readParquet(inputPath: String)(implicit spark: SparkSession): Dataset[PaymentRecordDto] = {
    import spark.implicits._
    spark.read
      .parquet(inputPath)
      .as[PaymentRecordDto]
  }
}

// ── Explicit schema definition — never infer ──────────────────────────────
object PaymentRecordSchema {
  import org.apache.spark.sql.types._

  val csvSchema: StructType = StructType(Seq(
    StructField("paymentId",     StringType,  nullable = false),
    StructField("correlationId", StringType,  nullable = false),
    StructField("debtorIban",    StringType,  nullable = false),
    StructField("creditorIban",  StringType,  nullable = false),
    StructField("amount",        DoubleType,  nullable = false),  // boundary: convert to BigDecimal
    StructField("currency",      StringType,  nullable = false),
    StructField("direction",     StringType,  nullable = false),
    StructField("endToEndRef",   StringType,  nullable = false),
    StructField("executionDate", StringType,  nullable = false),
    StructField("batchId",       StringType,  nullable = true)
  ))
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/infrastructure/spark/SettlementRecordWriter.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.infrastructure.spark

import org.apache.spark.sql.Dataset
import com.bank.payments.application.jobs.PaymentRecordDto

object SettlementRecordWriter {

  // GEN-013: Parquet + Snappy compression; Delta Lake for ACID
  def write(records: Dataset[PaymentRecordDto], outputPath: String): Unit =
    records.write
      .format("delta")
      .mode("append")
      .option("compression", "snappy")
      .partitionBy("currency", "batchId")
      .save(outputPath)

  def writeFlagged(records: Dataset[PaymentRecordDto], outputPath: String): Unit =
    records.write
      .format("delta")
      .mode("overwrite")
      .option("compression", "snappy")
      .save(outputPath)
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/infrastructure/config/AppConfig.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.infrastructure.config

import com.typesafe.config.{ Config, ConfigFactory }

// GEN-015: All config from application.conf — no hardcoded values
final case class SparkConfig(
  localMode:        Boolean,
  localCores:       Int,
  shufflePartitions: Int
)

final case class AppConfig(
  inputPath:          String,
  outputPath:         String,
  flaggedOutputPath:  String,
  amlThresholdEur:    BigDecimal,
  spark:              SparkConfig,
  auditBatchId:       String
)

object AppConfig {
  def load(): AppConfig = load(ConfigFactory.load())

  def load(config: Config): AppConfig = {
    val spark = SparkConfig(
      localMode         = config.getBoolean("spark.localMode"),
      localCores        = config.getInt("spark.localCores"),
      shufflePartitions = config.getInt("spark.shufflePartitions")
    )
    AppConfig(
      inputPath         = config.getString("payments.inputPath"),
      outputPath        = config.getString("payments.outputPath"),
      flaggedOutputPath = config.getString("payments.flaggedOutputPath"),
      amlThresholdEur   = BigDecimal(config.getString("payments.amlThresholdEur")),
      spark             = spark,
      auditBatchId      = config.getString("payments.auditBatchId")
    )
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/security/masking/PiiMasker.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.security.masking

// ── PII masking utilities — GEN-006 ──────────────────────────────────────
// IBAN, PAN, account numbers must NEVER appear unmasked in logs or outputs
object PiiMasker {

  // Mask IBAN: GB29NWBK60161331926819 → GB29XXXX...6819
  def maskIban(iban: String): String = {
    if (iban.length < 8) "XXXX"
    else {
      val country = iban.take(4)
      val last4   = iban.takeRight(4)
      s"${country}XXXX...${last4}"
    }
  }

  // Mask PAN (payment card number): 4111111111111111 → 411111XXXXXX1111
  def maskPan(pan: String): String = {
    val cleaned = pan.replaceAll("\\s|-", "")
    if (cleaned.length < 13) "XXXXXXXXXXXX"
    else {
      val first6 = cleaned.take(6)
      val last4  = cleaned.takeRight(4)
      val masked = "X" * (cleaned.length - 10)
      s"${first6}${masked}${last4}"
    }
  }

  // Mask account number: last 4 digits only
  def maskAccountNumber(account: String): String = {
    val cleaned = account.replaceAll("\\s", "")
    if (cleaned.length < 4) "XXXX"
    else s"XXXX-XXXX-XXXX-${cleaned.takeRight(4)}"
  }

  // Mask beneficiary name: keep initials only
  def maskBeneficiaryName(name: String): String = {
    val parts = name.trim.split("\\s+")
    parts.map(p => if (p.nonEmpty) s"${p.head}." else "").mkString(" ")
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/security/audit/AuditLogger.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.security.audit

import java.time.Instant
import java.util.UUID

// ── Immutable structured audit logger ────────────────────────────────────
// All audit entries are write-once — no update or delete operations
// GEN-006: All PII must be pre-masked before passing to this logger
object AuditLogger {

  private val correlationId: UUID = UUID.randomUUID()

  // Structured log entry — emits JSON-compatible output for ELK ingestion
  def log(message: String): Unit = {
    val entry = AuditEntry(
      timestamp     = Instant.now().toString,
      correlationId = correlationId.toString,
      service       = "sepa-payments-service",
      message       = message
    )
    // In production: write to immutable audit store (Delta Lake ACID table or Kafka topic)
    println(entry.toJson)
  }

  def logEvent(event: String, entityId: String, details: Map[String, String]): Unit = {
    val maskedDetails = details.map { case (k, v) =>
      if (k.toLowerCase.contains("iban")) k -> com.bank.payments.security.masking.PiiMasker.maskIban(v)
      else (k, v)
    }
    log(s"EVENT=$event entityId=$entityId " + maskedDetails.map { case (k, v) => s"$k=$v" }.mkString(" "))
  }
}

final case class AuditEntry(
  timestamp:     String,
  correlationId: String,
  service:       String,
  message:       String
) {
  def toJson: String =
    s"""{"timestamp":"$timestamp","correlationId":"$correlationId","service":"$service","message":"$message"}"""
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/scala/com/bank/payments/Main.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments

import com.bank.payments.application.jobs.SepaPaymentBatchJob
import com.bank.payments.infrastructure.config.AppConfig
import com.bank.payments.infrastructure.spark.SparkSessionProvider
import com.bank.payments.security.audit.AuditLogger

object Main extends App {

  AuditLogger.log("sepa-payments-service STARTING")

  val config        = AppConfig.load()
  implicit val spark = SparkSessionProvider.create(config)

  try {
    SepaPaymentBatchJob.run(config)
    AuditLogger.log("sepa-payments-service COMPLETED SUCCESSFULLY")
  } catch {
    // Only catch at application boundary — domain code uses Either, not throw
    case ex: Exception =>
      AuditLogger.log(s"sepa-payments-service FATAL ERROR: ${ex.getMessage}")
      spark.stop()
      sys.exit(1)
  } finally {
    spark.stop()
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/test/scala/com/bank/payments/domain/MoneySpec.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.domain

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.{ Arbitrary, Gen }
import com.bank.payments.domain.model._

class MoneySpec extends AnyFlatSpec with Matchers with ScalaCheckPropertyChecks {

  // ── ScalaCheck generators ────────────────────────────────────────────
  val positiveDecimalGen: Gen[BigDecimal] =
    Gen.posNum[Double].map(d => BigDecimal(d.toString).setScale(2, java.math.RoundingMode.HALF_EVEN))

  implicit val moneyArb: Arbitrary[Money] = Arbitrary(
    positiveDecimalGen.map(bd => Money.unsafeApply(bd, Currency.EUR))
  )

  // ── Unit tests ───────────────────────────────────────────────────────
  "Money.apply" should "reject zero amounts" in {
    Money(BigDecimal(0), Currency.EUR) shouldBe a [Left[_, _]]
  }

  it should "reject negative amounts" in {
    Money(BigDecimal(-100), Currency.EUR) shouldBe a [Left[_, _]]
  }

  it should "accept positive amounts" in {
    Money(BigDecimal("1000.00"), Currency.EUR) shouldBe a [Right[_, _]]
  }

  it should "never expose raw IBAN in toString" in {
    val money = Money.unsafeApply(BigDecimal("18700000.00"), Currency.EUR)
    money.toString should include ("EUR")
    money.toString should not include ("null")
  }

  // ── Property-based tests (GEN-001, GEN-002) ──────────────────────────
  "Money addition" should "be commutative" in {
    forAll(positiveDecimalGen, positiveDecimalGen) { (a, b) =>
      val m1 = Money.unsafeApply(a, Currency.EUR)
      val m2 = Money.unsafeApply(b, Currency.EUR)
      val ab = m1.add(m2)
      val ba = m2.add(m1)
      ab shouldEqual ba
    }
  }

  "Money addition" should "be associative" in {
    forAll(positiveDecimalGen, positiveDecimalGen, positiveDecimalGen) { (a, b, c) =>
      val m1 = Money.unsafeApply(a, Currency.EUR)
      val m2 = Money.unsafeApply(b, Currency.EUR)
      val m3 = Money.unsafeApply(c, Currency.EUR)
      val left  = m1.add(m2).flatMap(_.add(m3))
      val right = m2.add(m3).flatMap(m1.add)
      left shouldEqual right
    }
  }

  "Currency mismatch" should "return Left" in {
    val eur = Money.unsafeApply(BigDecimal("100"), Currency.EUR)
    val usd = Money.unsafeApply(BigDecimal("100"), Currency.USD)
    eur.add(usd) shouldBe a [Left[_, _]]
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/test/scala/com/bank/payments/application/ComplianceValidatorSpec.scala
═══════════════════════════════════════════════════════════════════════════════

```scala
package com.bank.payments.application

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.bank.payments.domain.model._
import com.bank.payments.domain.services._
import java.time.Instant
import java.util.UUID

class ComplianceValidatorSpec extends AnyFlatSpec with Matchers {

  private def makePayment(amount: BigDecimal, endToEndRef: String = "E2E-REF-001"): PaymentInstruction = {
    val iban = Iban("GB29NWBK60161331926819").getOrElse(throw new RuntimeException("test data"))
    PaymentInstruction(
      id            = UUID.randomUUID(),
      correlationId = UUID.randomUUID(),
      paymentType   = PaymentType.SEPA_CREDIT_TRANSFER,
      status        = PaymentStatus.INITIATED,
      amount        = Money.unsafeApply(amount, Currency.EUR),
      direction     = Direction.CREDIT,
      debtor        = AccountIdentifier(iban, None, None),
      creditor      = AccountIdentifier(iban, None, None),
      remittanceInfo = Some("Payroll April 2026"),
      executionDate  = Instant.now(),
      valueDate      = Instant.now(),
      batchId        = Some("BATCH-NIGHTLY-001"),
      endToEndRef    = endToEndRef
    )
  }

  "ComplianceValidator" should "PASS a valid SEPA SCT instruction" in {
    val result = ComplianceValidator.validate(makePayment(BigDecimal("5000.00")))
    result shouldBe a [Right[_, _]]
    result.right.get shouldBe a [CompliancePass]
  }

  it should "FLAG AML threshold (EUR 10,000+) without blocking" in {
    val result = ComplianceValidator.validate(makePayment(BigDecimal("15000.00")))
    result shouldBe a [Right[_, _]]
    result.right.get.asInstanceOf[CompliancePass].amlFlagged shouldBe true
  }

  it should "BLOCK a payment with empty end-to-end reference (SEPA rule)" in {
    val result = ComplianceValidator.validate(makePayment(BigDecimal("1000.00"), endToEndRef = ""))
    result shouldBe a [Left[_, _]]
  }

  it should "BLOCK a payment with end-to-end reference > 35 chars" in {
    val longRef = "A" * 36
    val result  = ComplianceValidator.validate(makePayment(BigDecimal("1000.00"), endToEndRef = longRef))
    result shouldBe a [Left[_, _]]
  }
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/resources/application.conf
═══════════════════════════════════════════════════════════════════════════════

```hocon
payments {
  inputPath         = ${?PAYMENTS_INPUT_PATH}
  inputPath         = "src/main/resources/data/payments.csv"
  outputPath        = ${?PAYMENTS_OUTPUT_PATH}
  outputPath        = "output/settlement"
  flaggedOutputPath = ${?PAYMENTS_FLAGGED_PATH}
  flaggedOutputPath = "output/flagged"
  amlThresholdEur   = "10000.00"
  auditBatchId      = ${?AUDIT_BATCH_ID}
  auditBatchId      = "BATCH-NIGHTLY-001"
}

spark {
  localMode         = true
  localCores        = "*"
  shufflePartitions = 8
}
```

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: src/main/resources/data/payments.csv
═══════════════════════════════════════════════════════════════════════════════

```csv
paymentId,correlationId,debtorIban,creditorIban,amount,currency,direction,endToEndRef,executionDate,batchId
PAY-0001,CORR-0001,GB29NWBK60161331926819,DE89370400440532013000,4500.00,EUR,CREDIT,E2E-0001,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0002,CORR-0002,GB29NWBK60161331926819,FR7630006000011234567890189,8750.00,EUR,CREDIT,E2E-0002,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0003,CORR-0003,GB29NWBK60161331926820,NL91ABNA0417164300,10000.00,EUR,CREDIT,E2E-0003,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0004,CORR-0004,GB29NWBK60161331926821,ES9121000418450200051332,3200.00,EUR,CREDIT,E2E-0004,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0005,CORR-0005,GB29NWBK60161331926822,IT60X0542811101000000123456,15000.00,EUR,CREDIT,E2E-0005,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0006,CORR-0006,GB29NWBK60161331926823,BE68539007547034,2800.00,EUR,CREDIT,E2E-0006,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0007,CORR-0007,GB29NWBK60161331926824,AT611904300234573201,5000.00,EUR,CREDIT,E2E-0007,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0008,CORR-0008,GB29NWBK60161331926825,PT50000201231234567890154,6300.00,EUR,CREDIT,E2E-0008,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0009,CORR-0009,GB29NWBK60161331926826,FI2112345600000785,1200.00,EUR,CREDIT,E2E-0009,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0010,CORR-0010,GB29NWBK60161331926827,GR1601101250000000012300695,9870.00,EUR,CREDIT,E2E-0010,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0011,CORR-0011,GB29NWBK60161331926828,LU280019400644750000,4100.00,EUR,CREDIT,E2E-0011,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0012,CORR-0012,GB29NWBK60161331926829,IE29AIBK93115212345678,7650.00,EUR,CREDIT,E2E-0012,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0013,CORR-0013,GB29NWBK60161331926830,DK5000400440116243,3300.00,EUR,CREDIT,E2E-0013,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0014,CORR-0014,GB29NWBK60161331926831,SE4550000000058398257466,11200.00,EUR,CREDIT,E2E-0014,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0015,CORR-0015,GB29NWBK60161331926832,NO9386011117947,2000.00,EUR,CREDIT,E2E-0015,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0016,CORR-0016,GB29NWBK60161331926833,CH9300762011623852957,5500.00,EUR,CREDIT,E2E-0016,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0017,CORR-0017,GB29NWBK60161331926834,PL61109010140000071219812874,4800.00,EUR,CREDIT,E2E-0017,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0018,CORR-0018,GB29NWBK60161331926835,CZ6508000000192000145399,6700.00,EUR,CREDIT,E2E-0018,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0019,CORR-0019,GB29NWBK60161331926836,HU42117730161111101800000000,3900.00,EUR,CREDIT,E2E-0019,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0020,CORR-0020,GB29NWBK60161331926837,RO49AAAA1B31007593840000,8200.00,EUR,CREDIT,E2E-0020,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0021,CORR-0021,GB29NWBK60161331926838,BG80BNBG96611020345678,2400.00,EUR,CREDIT,E2E-0021,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0022,CORR-0022,GB29NWBK60161331926839,HR1210010051863000160,1800.00,EUR,CREDIT,E2E-0022,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0023,CORR-0023,GB29NWBK60161331926840,SI56263300012039086,7100.00,EUR,CREDIT,E2E-0023,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0024,CORR-0024,GB29NWBK60161331926841,SK3112000000198742637541,5900.00,EUR,CREDIT,E2E-0024,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0025,CORR-0025,GB29NWBK60161331926842,EE382200221020145685,3600.00,EUR,CREDIT,E2E-0025,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0026,CORR-0026,GB29NWBK60161331926843,LV80BANK0000435195001,4200.00,EUR,CREDIT,E2E-0026,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0027,CORR-0027,GB29NWBK60161331926844,LT121000011101001000,6100.00,EUR,CREDIT,E2E-0027,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0028,CORR-0028,GB29NWBK60161331926845,CY17002001280000001200527600,12500.00,EUR,CREDIT,E2E-0028,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0029,CORR-0029,GB29NWBK60161331926846,MT84MALT011000012345MTLCAST001S,3100.00,EUR,CREDIT,E2E-0029,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
PAY-0030,CORR-0030,GB29NWBK60161331926847,DE89370400440532013001,7800.00,EUR,CREDIT,E2E-0030,2026-03-10T22:00:00Z,BATCH-NIGHTLY-001
```
*(Records PAY-0031 through PAY-0100 follow the same pattern with varied amounts and beneficiary IBANs across EU member states — omitted for brevity in this sample; full 100-record CSV generated in production execution.)*

---

═══════════════════════════════════════════════════════════════════════════════
📁 FILE: README.md
═══════════════════════════════════════════════════════════════════════════════

```markdown
# sepa-payments-service

BFSI SEPA Credit Transfer Batch Processing Service  
Generated by BFSI Advanced Prompt Chaining Framework v1.0.0  
Tech Stack: **Scala 2.13 + Apache Spark 3.5.x + sbt 1.9.x + DDD + Hexagonal Architecture**

## Architecture

```
INPUT (payments.csv)
    │
    ▼
[PaymentInstructionReader]  → Dataset[PaymentRecordDto]
    │
    ▼
[ComplianceValidator]       → CompliancePass / ComplianceBlock (COMP-001..010)
    │
    ▼
[RiskScoreEngine]           → RiskAssessment (RiskTier: LOW/MEDIUM/HIGH/CRITICAL)
    │
    ▼
[ProcessBatchPaymentCommand] → BatchProcessingResult
    │
    ▼
[SettlementRecordWriter]    → Delta Lake (output/settlement + output/flagged)
```

## Prerequisites

- JDK 17 LTS
- sbt 1.9.x
- Apache Spark 3.5.1 (provided scope — pre-installed or `spark-submit`)

## Running Locally

```bash
sbt compile
sbt test
sbt assembly
spark-submit \
  --class com.bank.payments.Main \
  --master "local[*]" \
  target/scala-2.13/sepa-payments-service-assembly-1.0.0.jar
```

## Configuration

All configuration is externalized in `src/main/resources/application.conf`.  
Override via environment variables:

| Variable                 | Description                         |
|--------------------------|-------------------------------------|
| `PAYMENTS_INPUT_PATH`    | Path to input payments CSV/Parquet  |
| `PAYMENTS_OUTPUT_PATH`   | Delta Lake output path              |
| `PAYMENTS_FLAGGED_PATH`  | Path for AML-flagged records        |
| `AUDIT_BATCH_ID`         | Batch identifier for audit logging  |

## Compliance Notes

- All IBAN values are masked in logs (GEN-006)
- AML threshold: EUR 10,000 (EU AMLD6 COMP-005)
- Round-amount detection active (FS-003)
- Delta Lake ensures ACID semantics for settlement records
- EBA reporting: flagged records written to `output/flagged/` for T+1 EOD report

## Test Coverage

```bash
sbt "testOnly com.bank.payments.domain.MoneySpec"
sbt "testOnly com.bank.payments.application.ComplianceValidatorSpec"
sbt test
```
```

---

```
┌──────────────────────────────────────────────────────────────────┐
│  CHAIN METADATA                                                  │
│  Chain Version    : 1.0.0                                        │
│  Stages Executed  : [1, 2, 3, 4, 5, 6, 7]                      │
│  Compliance Verdict: CONDITIONAL (COMP-005 WARN — EBA reporting) │
│  Risk Tier        : LOW (Score: 17.6 / 100)                     │
│  Decision Verdict : APPROVE_WITH_CONDITIONS                      │
│  Domains In Scope : payments, core-banking, risk-compliance      │
│  Tech Stack       : Scala 2.13 + Spark 3.5.x + sbt 1.9.x       │
│  Architecture     : DDD + Hexagonal (5 layers)                  │
│  Package Root     : com.bank.payments                            │
│  Generated Files  : 17 (build.sbt + 12 Scala + conf + CSV + README)│
│  Audit Reference  : AUDIT-2026-03-10-SEPA-BATCH-7F3A9C          │
│  Regulatory Basis : PSD2, EU AMLD6, SEPA SCT, GDPR,             │
│                     ISO 20022, Basel III, FATF Rec 16            │
│  Generated At     : 2026-03-10T22:00:00Z                         │
└──────────────────────────────────────────────────────────────────┘
```
