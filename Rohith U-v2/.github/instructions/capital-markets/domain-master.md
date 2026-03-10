# Domain Instruction: Capital Markets — Trading, Securities & Derivatives
**Version:** 2.0.0 | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY
- **Domain:** Capital Markets (Front Office, Middle Office, Back Office)
- **Products:** Equities, Fixed Income, FX Spot/Forward/Options, Interest Rate Swaps, CDS, Repos
- **Regulatory:** MiFID II, EMIR, SFTR, MAR, CSDR, Dodd-Frank (US counterparties)

---

## 2. CORE TRADING INVARIANTS

### 2.1 Trade Lifecycle
```
ORDER → EXECUTED → CONFIRMED → CLEARED → SETTLED [TERMINAL]
          │              │
          └─ CANCELLED   └─ FAILED (→ FAILED SETTLEMENT process)
```
- Orders may be CANCELLED before execution only
- SETTLED is a TERMINAL state — reversals via separate `TradeReversal` entity

### 2.2 Position Management
- Net position = SUM(BUY legs) - SUM(SELL legs) per instrument per book
- Positions must be recalculated in real-time for risk and margin calls
- Intraday position limits checked before every NEW order

### 2.3 Mark-to-Market (P&L)
- All positions marked to market at official close prices (independent source)
- P&L = (Current Market Value) - (Book Value / Cost Basis)
- Unrealized P&L reported separately from realized P&L
- P&L calculations MUST use `Decimal` for all monetary values  [FM-001]

### 2.4 Derivatives Valuation
- OTC derivatives: Fair value using market-accepted models (Black-Scholes, Hull-White)
- Model parameters MUST be sourced from market data service (not hardcoded)
- CVA (Credit Valuation Adjustment) calculated for all bilateral OTC positions

---

## 3. REGULATORY CONSTRAINTS
[SOURCE: code-generation-master.md §5.5]

- **MiFID II:** Pre-trade and post-trade transparency; best execution obligation
- **EMIR:** All OTC derivatives reported to Trade Repository; CCP clearing for standard products
- **SFTR:** Securities financing transactions reported (repos, stock lending)
- **MAR (Market Abuse Regulation):** Unusual trading patterns must generate `MarketAbuseAlert`
- **FRTB:** Sensitivities-Based Approach (SBA) for market risk capital; IMA for approved desks
- **CSDR:** Mandatory buy-in for settlement failures > T+2 (equities) / T+3 (bonds)

---

## 4. FORBIDDEN OPERATIONS

- ⛔ Order above position limit without approval — BLOCKER
- ⛔ Model parameter hardcoding — CRITICAL
- ⛔ OTC derivative without Trade Repository reporting — BLOCKER (EMIR)
- ⛔ Market abuse signals suppressed or not generated — BLOCKER (MAR)
- ⛔ Realized and unrealized P&L co-mingled — MAJOR
- ⛔ Settlement T+2/T+3 breach without CSDR buy-in initiation — BLOCKER

---

## 5. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Type | Purpose |
|--------|------|---------|
| `TradeOrder` | Aggregate | Trade initiation |
| `ExecutedTrade` | Entity | Post-execution record |
| `TradePosition` | Aggregate | Net exposure per instrument |
| `MarkToMarketValuation` | Value Object | MTM P&L |
| `DerivativeContract` | Aggregate | OTC/ETD derivative |
| `CreditValuationAdjustment` | Value Object | CVA |
| `MarketAbuseAlert` | Domain Event | MAR trigger |
| `TradeRepositoryReport` | Entity | EMIR/SFTR submission |
| `SettlementInstruction` | Entity | Post-trade settlement |
| `BestExecutionRecord` | Entity | MiFID II evidence |
| `FrtbSensitivityRecord` | Entity | FRTB risk sensitivity |
| `MarketDataSnapshot` | Value Object | EOD market data |
