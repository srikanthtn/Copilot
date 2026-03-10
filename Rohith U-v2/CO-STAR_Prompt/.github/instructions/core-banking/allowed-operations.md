<!-- version: 2.0.0 | domain: core-banking | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Core Banking — Allowed Operations

## Permitted Account Operations

| Operation                      | Conditions                                        |
|-------------------------------|---------------------------------------------------|
| Open new account               | KYC passed; product minimum met                  |
| Post debit entry               | Balance sufficient; account ACTIVE                |
| Post credit entry              | Account ACTIVE or BLOCKED (regulatory receipts)  |
| Query account balance          | Account owner or authorised delegate              |
| Retrieve ledger history        | Owner; PII masked in read model                  |
| Apply hold / earmark           | Pending payment or court order                   |
| Release hold                   | Payment settled or order cancelled               |
| Change overdraft limit         | Credit assessment passed; 4-eyes approval        |
| Set account DORMANT            | Regulatory dormancy trigger                      |
| Close account                  | Zero balance; no pending items; customer request |

## Permitted Reporting Operations

| Operation                      | Conditions                                        |
|-------------------------------|---------------------------------------------------|
| Daily reconciliation report    | Authorised ops team; T+1                         |
| Month-end statement            | Customer-scoped; PII included (encrypted channel)|
| Regulatory reporting (FINREP)  | Aggregated; no customer PII in report            |
| Interest statement             | Customer-scoped                                  |


# Core Banking — Forbidden Operations

## Absolutely Forbidden

| Forbidden Operation                              | Reason                      |
|--------------------------------------------------|-----------------------------|
| Modify or delete a posted ledger entry           | Accounting integrity / IFRS |
| Debit a CLOSED account                           | Terminal state               |
| Open account without KYC completion              | 5AMLD / 6AMLD               |
| Grant overdraft without credit assessment        | Prudential / CRR             |
| Allow balance to go below approved overdraft     | Risk management              |
| Cross-customer ledger view without delegation    | GDPR + access control        |
| Skip dormancy process and close directly         | Unclaimed assets legislation |
| Override block without audit log + 4-eyes        | Fraud / compliance gate      |
| Reverse interest accrual after month-end close   | IFRS 9 / accounting policy   |
