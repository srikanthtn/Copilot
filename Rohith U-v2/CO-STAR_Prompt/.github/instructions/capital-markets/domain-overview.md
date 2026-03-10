<!-- version: 2.0.0 | domain: capital-markets | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Capital Markets — Domain Overview

| Field         | Value                               |
|---------------|-------------------------------------|
| Domain Name   | Capital Markets                     |
| Package       | `com.bank.markets`                  |
| Bounded Context | Securities Trading, Settlement   |

## Core Entities

| Entity              | Type           | Key Fields                                       |
|---------------------|----------------|--------------------------------------------------|
| `Trade`             | Aggregate Root | tradeId, instrument, quantity, price, direction  |
| `Instrument`        | Value Object   | isin, type, currency, exchange, underlier        |
| `Position`          | Entity         | accountId, instrument, netQuantity, mtmValue     |
| `Settlement`        | Entity         | tradeId, settlementDate, dvpStatus, counterparty |
| `PriceQuote`        | Value Object   | bid, ask, mid (BigDecimal DECIMAL128)            |
| `TradeDirection`    | Sealed Trait   | BUY, SELL, SHORT_SELL                            |
| `InstrumentType`    | Sealed Trait   | EQUITY, BOND, FX_FORWARD, IRS, CDS              |

## Domain Events

| Event                      | Trigger                                 |
|----------------------------|-----------------------------------------|
| `TradeExecuted`            | Order matched and confirmed             |
| `TradeConfirmed`           | Both counterparties confirmed terms     |
| `SettlementInstructed`     | Settlement instruction sent to CSD      |
| `SettlementFailed`         | CSD returned failure notification       |
| `PositionUpdated`          | Account book position changed           |
| `MarginCallIssued`         | Variation margin call on derivative     |
| `TradeAllocated`           | Block trade split across accounts       |

## Ubiquitous Language

| Term        | Definition                                               |
|-------------|----------------------------------------------------------|
| ISIN        | International Securities Identification Number           |
| DvP         | Delivery versus Payment (simultaneous exchange)          |
| CSD         | Central Securities Depository (Euroclear, Clearstream)   |
| CCP         | Central Counterparty Clearing (LCH, Eurex Clearing)      |
| MTM / Mark  | Mark-to-Market: current fair value vs. trade price       |
| NBBO        | National Best Bid and Offer (US markets)                 |
| T+2         | Standard equity settlement: trade date + 2 business days |
