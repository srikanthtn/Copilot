# Domain Instruction: Treasury — Liquidity, FX, ALM & Cash Management
**Version:** 2.0.0 | **CE-Enhanced:** Yes

---

## 1. DOMAIN IDENTITY
- **Scope:** Asset-Liability Management (ALM), FX, Interbank lending, Cash management, LCR/NSFR
- **Authority:** Manages the bank's own balance sheet risk and funding

---

## 2. CORE INVARIANTS

### 2.1 Liquidity Coverage Ratio (LCR)
- LCR = (High Quality Liquid Assets) / (Net Cash Outflows over 30 days) × 100
- Minimum LCR: 100% at all times (EBA ITS)
- LCR must be calculated daily by 08:00 CET
- HQLA classification: Level 1 (0% haircut), Level 2A (15%), Level 2B (25–50%)
- Breach of LCR 100% → immediate `LiquidityBreachAlert` to Treasury Risk committee

### 2.2 Net Stable Funding Ratio (NSFR)
- NSFR = Available Stable Funding (ASF) / Required Stable Funding (RSF) × 100
- Minimum NSFR: 100% (Basel III)
- Calculated monthly, reported quarterly to supervisor

### 2.3 FX Position Management
- Open FX position limited per currency per book (defined in `FxPositionLimit` registry)
- FX rates from ECB reference rate or approved inter-bank feed (timestamped, max 1min stale)
- FX rate staleness check: if rate > 60 seconds old → reject usage; re-fetch
- Cross-rates derived from benchmark pairs only (not triangulated ad hoc)

### 2.4 Intraday Liquidity Monitoring
- Intraday positions tracked in real-time against `IntradayLiquidityLimit`
- TARGET2 queue positions monitored for gridlock detection
- End-of-day nostro reconciliation mandatory before system close

---

## 3. REGULATORY CONSTRAINTS

- **LCR (CRR II Art. 411–428):** Daily monitoring; breach reporting to competent authority
- **NSFR (CRR II Art. 428a–428ax):** Monthly calculation
- **SA-CCR (Basel III):** Counterparty credit risk for derivatives
- **G-SIB Surcharge:** Additional capital buffer if systemic importance threshold exceeded
- **FRTB (Min. Haircut):** Securities financing transaction minimum haircuts

---

## 4. FORBIDDEN OPERATIONS

- ⛔ FX rate usage without staleness timestamp check — BLOCKER [FM-009]
- ⛔ LCR reported below actual (suppress breach) — BLOCKER
- ⛔ Open FX position exceeding `FxPositionLimit` without approval — BLOCKER
- ⛔ HQLA classification without applying agency-prescribed haircuts — BLOCKER
- ⛔ Intraday credit extension beyond authorized limit — BLOCKER

---

## 5. DOMAIN ENTITIES (CANONICAL NAMES)

| Entity | Purpose |
|--------|---------|
| `LiquidityCoverageRatio` | Daily LCR snapshot |
| `NetStableFundingRatio` | Monthly NSFR snapshot |
| `HighQualityLiquidAsset` | HQLA classification |
| `FxExposurePosition` | FX position per currency |
| `FxRateSnapshot` | Timestamped market rate |
| `IntradayLiquidityLimit` | Operational limit |
| `LiquidityBreachAlert` | LCR/NSFR breach event |
| `CashFlowProjection` | Forward-looking cash flows |
| `NostroReconciliationResult` | Nostro account reconciliation |
| `FundingPlanRecord` | Medium-term funding plan |
