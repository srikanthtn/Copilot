<!-- version: 2.0.0 | domain: capital-markets | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Capital Markets — Settlement Rules

## Settlement Cycles

| Instrument Type  | Standard Cycle | Regulatory Basis   |
|------------------|---------------|--------------------|
| Equities (EU)    | T+2           | CSDR Art. 5        |
| Equities (US)    | T+1 (2024+)   | SEC Rule 15c6-1    |
| Government bonds | T+2           | CSDR Art. 5        |
| Corporate bonds  | T+2 to T+3    | CSDR Art. 5        |
| FX Spot          | T+2           | Market convention  |
| Money market     | T+0 or T+1    | Market convention  |

## CSDR Settlement Discipline

- Cash penalties accrue from T+4 for failing settlement instructions
- Buy-in process triggered at T+7 for all EU settlement fails
- `SettlementFailed` domain event must be raised at T+0 failure detection
- Cash compensation calculated at prevailing market price

## DvP Instruction Requirements

All settlement instructions include: ISIN, PLACE (LEI), QUANTITY, PRICE,
counterparty BIC/LEI, SSI reference, ISO 8601 settlement date, and matching
reference. These are mandatory fields in `Settlement` entity.


# Capital Markets — Regulatory Constraints

| Regulation           | Key Requirement                                  |
|----------------------|--------------------------------------------------|
| MiFID II / MiFIR     | Transparency, best execution, trade reporting    |
| EMIR (EU 648/2012)   | OTC derivative clearing, reporting, margin       |
| MAR (EU 596/2014)    | Market abuse prevention; insider dealing rules   |
| CSDR (EU 909/2014)   | Settlement discipline; buy-in and cash penalties |
| Dodd-Frank (US)      | Swap dealer registration; SDR trade reporting    |
| SFTR (EU 2365/2015)  | Securities Financing Transaction reporting       |
| FRTB (CRR II)        | Trading Book capital requirements                |
| SFDR (EU 2019/2088)  | ESG sustainability disclosure obligations        |


# Capital Markets — Risk Controls

## Real-Time Limits

| Limit                   | Hard Limit     | Soft Limit (Alert) |
|------------------------|----------------|--------------------|
| Single trade notional  | USD 50M        | USD 30M            |
| Daily P&L              | -USD 5M        | -USD 3M            |
| Net delta              | USD 100M       | USD 70M            |
| Counterparty exposure  | 25% Elig. Cap  | 20% Elig. Cap      |

All limit breaches raise domain events: `TradeLimitBreached`, `DailyPnLLimitBreached`.
Calculations use BigDecimal(MathContext.DECIMAL128) with HALF_EVEN rounding only.
