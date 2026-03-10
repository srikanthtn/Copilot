<!-- version: 2.0.0 | domain: public-finance-regulation | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Public Finance — Restricted Operations

## Absolutely Forbidden Operations

| Rule ID    | Forbidden Operation                                                         | Risk                                   |
|------------|-----------------------------------------------------------------------------|----------------------------------------|
| PFR-RO-001 | Issue bonds beyond the statutory debt ceiling                               | Constitutional / treaty violation       |
| PFR-RO-002 | Record expenditure without a valid appropriation code                       | Unlawful expenditure                   |
| PFR-RO-003 | Transfer funds between budget lines without legislative authority           | Virement without authorisation          |
| PFR-RO-004 | Disburse grants without eligibility validation and audit trail               | EU structural fund fraud               |
| PFR-RO-005 | Suppress or alter deficit figures before Eurostat transmission              | Statistical manipulation / criminal    |
| PFR-RO-006 | Use Double or Float for any fiscal metric or monetary computation           | Precision loss in statutory reporting   |
| PFR-RO-007 | Back-date budget entries to prior fiscal years without restatement          | Fraudulent financial reporting          |
| PFR-RO-008 | Hide contingent liabilities by omitting IPSAS 19 provisions                 | Mis-stated public accounts             |
| PFR-RO-009 | Award public contracts without following threshold-mandated procedures      | Procurement fraud; EU infringement     |
| PFR-RO-010 | Transfer public pension funds to non-segregated operational accounts        | Pension misappropriation               |
| PFR-RO-011 | Produce Maastricht Debt calculation excluding any component of S.13 sector  | EDP reporting fraud                    |
| PFR-RO-012 | Allow unauthorised access to national budget system without audit logging   | Security and accountability failure    |

## Restricted Operations (Require Explicit Authorisation)

| Rule ID    | Operation                                                    | Gate Required                       |
|------------|--------------------------------------------------------------|-------------------------------------|
| PFR-RO-013 | Supplementary estimate > 5% of original appropriation line   | Finance Committee approval          |
| PFR-RO-014 | Emergency spending without parliamentary session             | Finance Minister Order + 48hr review|
| PFR-RO-015 | Debt restructuring or liability swapping                     | Treasury Board + Central Bank sign-off|
| PFR-RO-016 | Cross-border currency swap on sovereign debt                 | Central Bank Governor approval      |
| PFR-RO-017 | PPP contract commitment > €500M                              | Cabinet-level authorisation          |
| PFR-RO-018 | Grant re-allocation between programme areas                  | Programme Authority approval + audit|

## Code Anti-Patterns

| Anti-Pattern                                              | Why Forbidden                                |
|-----------------------------------------------------------|----------------------------------------------|
| `val deficit = expenditure - revenue` using `Double`      | Floating-point rounding in national accounts |
| Missing `AppropriationCode` on expenditure aggregate      | Unlawful public spending                     |
| `BondIssued` event without debt-ceiling pre-check         | Treaty violation risk                        |
| Granting budget transfers in a single DB transaction without saga | Distributed state inconsistency      |
| Hardcoded 3% or 60% thresholds without configuration key  | Cannot update for new treaty obligations     |
| No idempotency key on grant disbursement payment call     | Double-disbursement risk, audit failure      |
| Tax revenue without `CoFOG` classification attribute      | GFS / ESA reporting failure                  |

## Prohibited Data Flows

| Flow                                                    | Reason                              |
|---------------------------------------------------------|-------------------------------------|
| Raw taxpayer identity → external systems                | GDPR + tax secrecy laws             |
| Unsanitised budget data → public APIs before approval   | Pre-decision leakage; market risk   |
| Grant recipient bank details → non-finance systems      | PII exposure                        |
| IPSAS draft accounts → investors before audit sign-off  | Market abuse risk                   |
