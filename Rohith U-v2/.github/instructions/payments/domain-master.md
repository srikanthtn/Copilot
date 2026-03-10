# Domain Instruction: SEPA, Instant, Cross-Border & ISO 20022 Payments
**Version:** 2.0.0 | **Scope:** All Payment Schemes | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY & BOUNDED CONTEXT
- **Domain:** Payments Processing
- **Authority:** This domain is authoritative for all payment initiation, routing, clearing, and settlement
- **Consumers:** Core Banking (for ledger posting), Risk (for screening), Reporting (for regulatory)

---

## 2. CORE BUSINESS INVARIANTS (ABSOLUTE)

### 2.1 Financial Invariants
- Transaction amounts MUST be strictly positive (`> 0`)  [FM-003]
- Zero-value transactions ONLY for `Ping` or `Heartbeat` messages with explicit `MessageType.HEARTBEAT` enum
- Negative values are FORBIDDEN — use `Direction: DEBIT | CREDIT` instead  [FM-003]
- All SEPA transactions MUST be denominated in `EUR` — reject `USD`, `GBP` with `CurrencyMismatchError`

### 2.2 IBAN Validation (Mandatory)
```python
# IBAN validation: Mod-97 checksum (ISO 13616)
# Steps: Move first 4 chars to end, replace letters with numbers, compute mod 97
# Valid if result == 1
# [SOURCE: payments/business-rules.md §1.2]
import re

IBAN_PATTERN = re.compile(r'^[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}$')

def validate_iban(iban: str) -> bool:
    iban_clean = iban.replace(" ", "").upper()
    if not IBAN_PATTERN.match(iban_clean):
        return False
    rearranged = iban_clean[4:] + iban_clean[:4]
    numeric = ''.join(str(ord(c) - 55) if c.isalpha() else c for c in rearranged)
    return int(numeric) % 97 == 1
```

### 2.3 BIC Validation
- BIC must be 8 or 11 characters: `[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?`
- BIC must exist in ECB/SWIFT BIC Directory (validate against registry)

### 2.4 State Machine (Forward-Only)
```
INITIATED → VALIDATED → CLEARED → SETTLED [TERMINAL]
                │
                └──► REJECTED [TERMINAL on any stage]
SETTLED ──► ARCHIVED (after retention period)
```
- Transitions are FORWARD-ONLY (no rollback — use reversal transaction)
- Every transition MUST emit a `DomainEvent` with `occurred_at`, `transaction_id`, `correlation_id`

---

## 3. PAYMENT SCHEMES & SLA REQUIREMENTS

| Scheme | Currency | Max End-to-End | Cut-off Time | Standard |
|--------|----------|---------------|-------------|---------|
| SEPA Credit Transfer (SCT) | EUR | D+1 | 16:00 CET | EPC 125-05 |
| SEPA Instant Payment (SCT Inst) | EUR | 10 seconds | 24/7/365 | EPC 004-16 |
| SEPA Direct Debit Core (SDD Core) | EUR | D+5 | D-1 | EPC 008-08 |
| SEPA Direct Debit B2B (SDD B2B) | EUR | D+1 | D | EPC 222-07 |
| SWIFT MT103 (Cross-Border) | Multi | Variable | Market-dependent | ISO 15022 |
| ISO 20022 pacs.008 (Cross-Border) | Multi | Variable | Market-dependent | ISO 20022 |

---

## 4. REGULATORY CONSTRAINTS
[SOURCE: code-generation-master.md §5.2, §5.3, §5.4]

- **PSD2 SCA:** All customer-initiated payments MUST carry a validated `AuthenticationToken`
- **AML Large Transaction:** If `amount > 10000 EUR`, immediately trigger `AmlScreeningEvent`
- **AML Structuring:** Detect multiple transactions < 10000 EUR from same debtor within 24h
- **Sanctions Screening:** ALL cross-border payments — real-time check against OFAC, EU, UN lists
- **GDPR:** IBAN, BIC, CustomerName are PII — encrypt at rest (AES-256), mask in logs (last 4)
- **PCI-DSS:** If card-based, never store CVV/CVC post-authorization; tokenize PAN

---

## 5. FORBIDDEN OPERATIONS
[SOURCE: code-generation-master.md §3.1, §3.2, §3.3]

- ⛔ `float`/`double` for monetary amounts — BLOCKER
- ⛔ Silent ledger mutation (append-only ledger) — BLOCKER
- ⛔ `System.currentTimeMillis()` / `datetime.now()` without UTC — BLOCKER
- ⛔ Logging IBAN, BIC, or CustomerName in INFO/DEBUG — BLOCKER
- ⛔ Skipping AML screening for transactions > 10000 EUR — BLOCKER
- ⛔ Processing SEPA in non-EUR currency — BLOCKER
- ⛔ Missing idempotency key on payment initiation — BLOCKER

---

## 6. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Type | Purpose |
|--------|------|---------|
| `SepaCreditTransfer` | Aggregate | SEPA Credit Transfer |
| `SepaInstantPayment` | Aggregate | SCT Inst < 10 sec |
| `SepaDirectDebit` | Aggregate | SEPA Direct Debit |
| `CrossBorderPayment` | Aggregate | Non-SEPA international |
| `PaymentInstruction` | Command | Initiate payment |
| `LedgerEntry` | Value Object | Double-entry posting |
| `SettlementRecord` | Entity | Final settlement |
| `AmlScreeningEvent` | Domain Event | AML trigger |
| `SuspiciousActivityReport` | Entity | SAR |
| `IdempotencyKey` | Value Object | Dedup key |
| `Pacs008Message` | Value Object | ISO 20022 credit transfer |
| `SepaBatchProcessor` | Service | Batch processing |
| `ClearingGatewayAdapter` | Port | Clearing system |
| `PaymentSettlementEngine` | Service | Settlement |

---

## 7. DATA BOUNDARIES
- **Read-only:** BIC Directory, Currency Exchange Rates, Holiday Calendar, IBAN Country Formats
- **Append-only:** Ledger Entries, Audit Logs, AML Screening Results, SAR Records
- **Read-write (ephemeral):** Payment processing queues, idempotency store
- **NEVER expose:** Full IBAN, BIC owner details, customer PII outside encryption boundary
