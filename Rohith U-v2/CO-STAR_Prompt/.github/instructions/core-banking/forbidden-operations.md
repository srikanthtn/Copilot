<!-- version: 2.0.0 | domain: core-banking | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Core Banking — Forbidden Operations

> **IMPORTANT**: These operations must NEVER appear in generated code.
> The code reviewer will flag any violation as BLOCKER.

## Absolutely Forbidden

| Forbidden Operation                              | Reason                         |
|--------------------------------------------------|--------------------------------|
| Modify or delete a posted ledger entry           | Accounting integrity / IFRS    |
| Debit a CLOSED or BLOCKED account for transfers  | Terminal/frozen state           |
| Open account without KYC completion              | 5AMLD / 6AMLD compliance       |
| Grant overdraft without credit assessment        | Prudential / CRR regulation    |
| Allow balance below approved overdraft limit     | Risk management control        |
| Cross-customer ledger view without delegation    | GDPR + access control policy   |
| Skip dormancy process and close account directly | Unclaimed assets legislation   |
| Override freeze/block without audit + 4-eyes     | Fraud / compliance gate        |
| Reverse interest accrual after month-end close   | IFRS 9 / accounting policy     |
| Store account holder SSN/NIN in plain text       | GDPR Art.9 special category    |
| Generate IBAN outside bank's certified range     | Scheme integrity               |
| Apply interest rate above BOE/ECB maximum cap    | Consumer protection regulation |
