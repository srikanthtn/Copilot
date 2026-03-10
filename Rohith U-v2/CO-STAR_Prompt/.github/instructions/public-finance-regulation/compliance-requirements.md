<!-- version: 2.0.0 | domain: public-finance-regulation | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Public Finance — Compliance Requirements

## Statutory Compliance Obligations

| Obligation                            | Source                          | Scope                                         |
|---------------------------------------|---------------------------------|------------------------------------------------|
| EU Stability and Growth Pact (SGP)    | ECG Treaty + EU Reg 1466/97     | Deficit ≤ 3% GDP; Debt ≤ 60% GDP              |
| European Fiscal Compact               | Treaty on Stability, 2012       | Structural deficit ≤ 0.5% GDP; automatic correction |
| UK Fiscal Responsibility Act 2010     | UK legislation                  | Debt falling as % GDP; deficit below 2% by midterm |
| Public Finance Management Act (PFMA)  | National legislation variants   | Parliamentary appropriation; treasurer controls|
| EU Court of Auditors Standards        | ECA regulations                 | EU-funded projects; public procurement audit   |
| IPSAS (IFAC)                          | International standards         | Accrual accounting for public sector           |
| INTOSAI Government Auditing Standards | ISSAI framework                 | Supreme audit institution standards            |

## Code Compliance Rules

| Rule ID    | Rule                                                                      |
|------------|---------------------------------------------------------------------------|
| PFR-CC-001 | All deficits must be calculated as BigDecimal; Float/Double forbidden     |
| PFR-CC-002 | Deficit-to-GDP ratio check must execute before any new bond issuance      |
| PFR-CC-003 | Debt ceiling validation required as a pre-condition for `BondIssued` event|
| PFR-CC-004 | Budget revisions require a `FiscalBudgetRevised` domain event             |
| PFR-CC-005 | Grant disbursements must link to `BudgetAllocationId` reference           |
| PFR-CC-006 | All public expenditure entries must carry an appropriation code           |
| PFR-CC-007 | Year-end financial statements must reconcile to zero before close         |
| PFR-CC-008 | Every GrantDisbursed event must have an audit evidence reference attached |

## Audit Requirements

| Requirement                             | Standard                |
|-----------------------------------------|-------------------------|
| Annual Accounts Certification           | IPSAS 1                 |
| Comparative Prior-Year Figures          | IPSAS 1 para 38         |
| Contingent Liabilities Disclosure       | IPSAS 19                |
| Related-Party Transactions Disclosure   | IPSAS 20                |
| Public Sector Borrowing Requirement     | National Accounts (ESA) |
| Freedom of Information Compliance       | National FOI legislation|
| Public Procurement Transparency         | EU Directive 2014/24    |

## Reporting Deadlines

| Report                            | Frequency    | Deadline            | Recipient       |
|-----------------------------------|--------------|---------------------|-----------------|
| Consolidated Financial Statements | Annual       | Within 6 months EoY | Parliament / ECA|
| Deficit Notification              | Annual       | March of following year | Eurostat     |
| Quarterly Fiscal Balance Report   | Quarterly    | 30 days after quarter| Finance Ministry|
| Grant Utilisation Report          | Per-project  | 60 days post-close  | Audit Authority |
| Debt Portfolio Report             | Annual       | January 31          | Central Bank    |
