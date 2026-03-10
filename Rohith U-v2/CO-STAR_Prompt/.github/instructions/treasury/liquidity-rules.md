<!-- version: 2.0.0 | domain: treasury | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Treasury — Liquidity Rules

## LCR Calculation Rules

| Component         | Rule                                                       |
|-------------------|------------------------------------------------------------|
| HQLA Level 1      | Cash + CB reserves: 0% haircut                            |
| HQLA Level 2A     | Covered bonds AA+: 15% haircut; max 40% of HQLA buffer    |
| HQLA Level 2B     | Other assets: 25-50% haircut; max 15% of HQLA buffer      |
| Net Cash Outflows | Modelled over 30-day stress scenario                       |
| LCR Formula       | `LCR = (HQLA / Net30DayOutflows) × 100 ≥ 100%`           |
| Reporting         | Daily to ALCO; monthly to ECB/PRA                         |

## NSFR Calculation Rules

| Component         | Rule                                                       |
|-------------------|------------------------------------------------------------|
| ASF               | Available Stable Funding = liabilities weighted by factor  |
| RSF               | Required Stable Funding = assets weighted by factor        |
| NSFR Formula      | `NSFR = (ASF / RSF) × 100 ≥ 100%`                        |
| Reporting         | Monthly to supervisor                                      |

## Intraday Liquidity Rules

| Rule ID       | Rule                                                     |
|---------------|----------------------------------------------------------|
| LIQ-001       | Intraday liquidity buffer ≥ peak daily payment outflows  |
| LIQ-002       | Time-critical payments (RTGS) prioritised over ACH       |
| LIQ-003       | Alert raised if buffer < 110% of historical peak         |
| LIQ-004       | Contingency funding plan activated if buffer < 100%      |
| LIQ-005       | All calculations in BigDecimal (DECIMAL128, HALF_EVEN)   |

## Collateral Rules

| Rule          | Requirement                                              |
|---------------|----------------------------------------------------------|
| Eligible HQLA | Must be unencumbered; centralised in liquidity pool      |
| Haircut calc  | Applied per Basel III Table 1 (CRR Art. 416)            |
| Concentration | Level 2A ≤ 40%; Level 2B ≤ 15% of total HQLA            |
| Repo eligible | HQLA must be repo/central-bank eligible                  |


# Treasury — Regulatory Constraints

| Regulation           | Requirement                                             |
|----------------------|---------------------------------------------------------|
| CRR Art. 412-428     | LCR calculation, reporting, and composition             |
| CRR Art. 428a-428au  | NSFR calculation, reporting, and composition            |
| Basel III LCR (BCBS Jan 2013) | 100% minimum from 1 Jan 2019              |
| ECB ILAAP Guide 2018 | Internal liquidity adequacy assessment                  |
| PRA SS24/15          | ILAAP and liquidity risk management for UK banks        |
| EMIR Art. 11         | Derivatives margining; VM + IM on cleared/uncleared     |
| MiFID II Art. 49     | Best execution for FX transactions                      |
| MAR (EU 596/2014)    | No information asymmetry in FX dealing; market abuse    |


# Treasury — Risk Boundaries

## FX Position Limits

| Currency Pair | Intraday Limit | Overnight Limit | Stop-Loss Trigger  |
|---------------|---------------|-----------------|--------------------|
| EUR/USD       | USD 50M       | USD 20M         | P&L loss > USD 2M  |
| EUR/GBP       | GBP 30M       | GBP 10M         | P&L loss > GBP 1M  |
| EUR/JPY       | JPY 6B        | JPY 2B          | P&L loss > JPY 200M|
| All others    | USD 10M equiv | USD 5M equiv    | P&L loss > USD 500K|

## Interest Rate Risk Boundaries

| Metric          | Limit                                    |
|-----------------|------------------------------------------|
| Duration (DV01) | Max EUR 500K PV01 per 1bp shift          |
| Basis Risk      | Max EUR 200K per basis point             |
| EVE sensitivity | < 15% of Tier 1 Capital per stress       |
| NII sensitivity | < 10% of projected NII per stress        |

## Counterparty Risk Limits

| Counterparty Type     | Limit                           |
|-----------------------|---------------------------------|
| G-SIB counterparty    | Up to 25% of eligible capital   |
| Other bank            | Up to 10% of eligible capital   |
| Corporate (non-bank)  | Up to 5% of eligible capital    |
| CCPs (cleared trades) | No limit on margin-posted        |
