<!-- version: 2.0.0 | domain: capital-markets | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Capital Markets — Trading Rules

## Pre-Trade Checks (must pass before order submission)

| Rule ID   | Check                                    | Error                        |
|-----------|------------------------------------------|------------------------------|
| TRADE-001 | Instrument must be on approved list      | `InstrumentNotApprovedError` |
| TRADE-002 | Counterparty credit limit not breached   | `CounterpartyLimitError`     |
| TRADE-003 | Position limit not exceeded post-trade   | `PositionLimitError`         |
| TRADE-004 | Short sell requires locate/borrow        | `ShortSellWithoutBorrowError`|
| TRADE-005 | Trader has mandate for instrument type   | `UnauthorisedInstrumentError`|
| TRADE-006 | Market hours check (exchange open)       | `MarketClosedError`          |

## Post-Trade Requirements

| Requirement                 | Regulation     | Deadline          |
|-----------------------------|---------------|-------------------|
| Trade reporting to ARM/APA  | MiFID II TR   | T+1 end of day    |
| Transaction reporting       | EMIR / MiFIR  | T+1 09:00 UTC     |
| Large trader report         | CFTC (US)     | If > threshold    |
| Best execution record       | MiFID II Art.27 | Per transaction  |

## Best Execution (MiFID II Art. 27)

Generated order management code must:
1. Log all considered execution venues per order
2. Record: price, cost, speed, likelihood of execution, size
3. Document rationale for chosen venue
4. Publish best execution policy annually
5. Trigger `BestExecutionRecord` domain event on every execution


# Capital Markets — Settlement Rules

## Settlement Cycles

| Instrument Type    | Standard Cycle | Fails Penalty     |
|--------------------|---------------|-------------------|
| Equities (EU)      | T+2           | CSDR cash penalty |
| Equities (US)      | T+1 (2024+)   | FINRA enforcement |
| Government bonds   | T+2           | CSDR cash penalty |
| Corporate bonds    | T+2 to T+3    | CSDR cash penalty |
| FX Spot            | T+2           | Counterparty risk |
| Money market       | T+0 or T+1    | n/a               |

## CSDR Settlement Discipline (EU 909/2014 Art. 6-7)

| Phase        | Action                                    |
|--------------|-------------------------------------------|
| T+4 to T+7   | Cash penalties accrue daily for fails     |
| T+7          | Buy-in process initiated on failing trades|
| Buy-in       | Automatic purchase at prevailing market   |
| If buy-in fails | Cash compensation to receiving party    |

## DvP Instruction Requirements

All settlement instructions must include:
- ISIN, PLACE OF SETTLEMENT (LEI), QUANTITY, PRICE
- Counterparty BIC / LEI
- SSI (Standard Settlement Instruction) from pre-agreed SSI database
- Settlement date in ISO 8601 (no time zone ambiguity)
- Matching reference (bilateral or CCP-generated)


# Capital Markets — Regulatory Constraints

| Regulation              | Requirement                                       |
|-------------------------|---------------------------------------------------|
| MiFID II / MiFIR        | Transparency, best execution, trade reporting      |
| EMIR (EU 648/2012)      | OTC derivative clearing, reporting, margin        |
| MAR (EU 596/2014)       | Market abuse prevention; insider dealing          |
| CSDR (EU 909/2014)      | Settlement discipline; cash penalties             |
| Dodd-Frank (US)         | Swap reporting to SDR; margin rules               |
| SFTR (EU 2365/2015)     | Securities Financing Transaction reporting        |
| Basel III SA-CCR        | Standardised approach for counterparty credit     |
| FRTB (CRR II)           | Fundamental Review of Trading Book capital         |
| SFDR (EU 2019/2088)     | ESG sustainability disclosure on AIF/UCITS        |


# Capital Markets — Risk Controls

## Real-Time Risk Limits (enforced by RiskControlEngine)

| Limit                    | Hard Limit              | Soft Limit (Alert)     |
|--------------------------|------------------------|------------------------|
| Single trade notional    | USD 50M                | USD 30M                |
| Daily trading P&L        | -USD 5M                | -USD 3M                |
| Net delta equivalent     | USD 100M               | USD 70M                |
| Gross exposure per sector| USD 200M               | USD 150M               |
| Counterparty exposure    | 25% of eligible capital| 20% of eligible capital|

## Pre-Settlement Risk

- MTM exposure calculated T+0 to settlement date
- Daily MtM changes > 2% of notional trigger margin call
- All derivative MTM: `BigDecimal(MathContext.DECIMAL128)` + HALF_EVEN

## Market Risk Scenarios

| Scenario             | Shock Applied                       | Limit         |
|---------------------|-------------------------------------|---------------|
| Equity crash        | -30% on all equity positions        | EVE < 15% T1C |
| Rate shock          | +/-200bp parallel shift             | NII < 10%     |
| FX shock            | ±15% on all currency pairs          | P&L < -USD 5M |
| Credit spread       | +300bp on corporate bond portfolio  | PnL < -EUR 3M |
