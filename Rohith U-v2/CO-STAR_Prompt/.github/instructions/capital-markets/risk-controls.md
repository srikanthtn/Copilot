<!-- version: 2.0.0 | domain: capital-markets | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Capital Markets — Risk Controls

## Pre-Trade Hard Limits (enforced by RiskControlEngine.preTradeCheck)

| Limit                   | Hard Limit     | Action on Breach               |
|------------------------|----------------|--------------------------------|
| Single trade notional  | USD 50,000,000 | Reject order; raise event      |
| Daily P&L drawdown     | -USD 5,000,000 | Halt trading; escalate to desk |
| Net delta equivalent   | USD 100,000,000| Reject order; trader notified  |
| Gross exposure/sector  | USD 200,000,000| Soft halt; risk manager review |
| Counterparty credit    | 25% Elig. Cap  | Reject; credit team escalation |

All monetary limits: `BigDecimal(MathContext.DECIMAL128)` + HALF_EVEN rounding.
All domain events on breach: `TradeLimitBreached`, `DailyPnlBreached`, etc.

## Market Risk Stress Scenarios

| Scenario           | Shock                                  | Max Permitted P&L Loss |
|-------------------|----------------------------------------|------------------------|
| Equity crash       | -30% all equity positions              | EVE < 15% Tier 1 Cap  |
| Rate parallel up   | +200bp all rates                       | NII < -10% projected  |
| FX shock           | ±15% all currency pairs                | P&L < -USD 5M         |
| Credit spread      | +300bp corporate bond portfolio        | P&L < -EUR 3M         |

## Mandatory Risk Events

| Event                    | Trigger                               |
|--------------------------|---------------------------------------|
| `TradeLimitBreached`     | Any hard limit exceeded               |
| `RiskScenarioBreached`   | Stress scenario result > limit        |
| `MarginCallIssued`       | Daily MtM change > 2% of notional    |
| `CounterpartyLimitAlert` | Exposure > soft limit                 |
