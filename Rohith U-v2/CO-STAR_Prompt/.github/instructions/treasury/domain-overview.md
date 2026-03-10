<!-- version: 2.0.0 | domain: treasury | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Treasury — Domain Overview

| Field         | Value                               |
|---------------|-------------------------------------|
| Domain Name   | Treasury                            |
| Package       | `com.bank.treasury`                 |
| Bounded Context | Liquidity, FX, Cash, Funding     |

## Core Entities

| Entity              | Type           | Key Fields                                  |
|---------------------|----------------|---------------------------------------------|
| `LiquidityPosition` | Aggregate Root | date, currency, hqlaAmount, lcr, nsfr       |
| `FxPosition`        | Entity         | currencyPair, spot, forward, mtmValue       |
| `CashFlowForecast`  | Entity         | date, currency, inflows, outflows, netFlow  |
| `FundingGap`        | Value Object   | term, currency, amount, urgency             |
| `NostroPosition`    | Entity         | correspondent, currency, balance            |
| `SpotRate`          | Value Object   | base, quote, rate (BigDecimal), timestamp   |

## Domain Events

| Event                        | Trigger                               |
|------------------------------|---------------------------------------|
| `LiquidityAlertRaised`       | LCR drops below regulatory minimum   |
| `FxPositionOpened`           | New FX trade executed                 |
| `CashForecastUpdated`        | End-of-day cash position refresh      |
| `FundingGapIdentified`       | Projected shortfall detected          |
| `NostroReconciled`           | Intraday nostro balance matched       |
| `HedgeActivated`             | Automatic FX hedge triggered          |

## Key Financial Metrics

| Metric    | Description                                     | Regulatory Minimum |
|-----------|-------------------------------------------------|--------------------|
| LCR       | Liquidity Coverage Ratio (30-day stress)        | ≥ 100%             |
| NSFR      | Net Stable Funding Ratio (1-year horizon)       | ≥ 100%             |
| HQLA      | High Quality Liquid Assets (Level 1 + Level 2)  | Defined by CRR     |
| ILAAP     | Internal Liquidity Adequacy Assessment Process  | Annual submission  |
