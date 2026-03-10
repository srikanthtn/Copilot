<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Allowed Operations

> Operations permitted in the payments bounded context. Generated code must
> only implement operations from this list.

## Permitted Payment Operations

| Operation                    | Conditions                                   |
|------------------------------|----------------------------------------------|
| Initiate SEPA Credit Transfer| Valid IBAN, BIC, amount, SCA passed          |
| Initiate Instant Payment     | Amount ≤ EUR 100,000; scheme reachable       |
| Submit Payment to Scheme     | Status = VALIDATED; compliance cleared       |
| Cancel Payment               | Status = CREATED only; before submission     |
| Query Payment Status         | Owner or authorised delegate only            |
| Retrieve Payment History     | Read-only; GDPR data minimisation applied    |
| Retry Failed Payment         | Status = FAILED; max 3 retries; new ref ID  |
| Generate Payment Report      | Authorised role; PII masked in output        |
| Process Bulk Payment File    | PAIN.001 ISO 20022 format; batch validated   |
| Reverse Settled Payment      | R-transaction scheme message only; T+1 max  |

## Permitted Data Operations

| Operation                         | Notes                                        |
|-----------------------------------|----------------------------------------------|
| Store payment record (encrypted)  | Delta Lake; ACID; AES-256-GCM PII            |
| Index by reference + date         | No full-text on PII fields                   |
| Read own payment records          | Customer-scoped only; no cross-customer reads|
| Archive payments > 5 years        | Anonymise PII before archive                 |
| Stream payment events to Kafka    | Masked data only; schema registry enforced   |
