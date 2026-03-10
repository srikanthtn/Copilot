<!-- version: 2.0.0 | domain: treasury | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Treasury — Risk Boundaries

## FX Position Limits

| Currency Pair  | Intraday Limit  | Overnight Limit | Stop-Loss Trigger   |
|----------------|-----------------|-----------------|---------------------|
| EUR/USD        | USD 50M         | USD 20M         | P&L loss > USD 2M   |
| EUR/GBP        | GBP 30M         | GBP 10M         | P&L loss > GBP 1M   |
| EUR/JPY        | JPY 6,000M      | JPY 2,000M      | P&L loss > JPY 200M |
| All others     | USD 10M equiv   | USD 5M equiv    | P&L loss > USD 500K |

All limits stored as BigDecimal with DECIMAL128. Breach events must emit
`FxLimitBreached` domain event with full position snapshot.

## Interest Rate Risk Limits

| Metric             | Limit                                         |
|--------------------|-----------------------------------------------|
| DV01 / PV01        | Max EUR 500K per 1bp parallel shift            |
| Basis risk         | Max EUR 200K per basis point on any curve      |
| EVE sensitivity    | < 15% of Tier 1 Capital under stress scenario  |
| NII sensitivity    | < 10% of projected NII under +/- 200bp shock  |

## Counterparty Credit Limits

| Counterparty Type    | Limit                              |
|----------------------|------------------------------------|
| G-SIB               | Up to 25% of eligible capital      |
| Other rated bank     | Up to 10% of eligible capital      |
| Corporate            | Up to 5% of eligible capital       |
| CCP (cleared trades) | No limit on posted initial margin  |

## Liquidity Risk Boundaries

| Metric               | Minimum Threshold    |
|----------------------|----------------------|
| LCR                  | 100% (hard floor)    |
| NSFR                 | 100% (hard floor)    |
| Intraday buffer      | 110% of peak outflow |
| Unencumbered HQLA    | > EUR 500M at all times |

Breach of any boundary triggers `LiquidityAlertRaised` domain event and
automated escalation to ALCO.
