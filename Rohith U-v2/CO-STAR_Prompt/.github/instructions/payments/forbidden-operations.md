<!-- version: 2.0.0 | domain: payments | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Payments Domain — Forbidden Operations

> Code generators MUST NOT generate code that performs any operation on this list.
> The code reviewer will flag any violation as BLOCKER.

## Absolutely Forbidden

| Forbidden Operation                                    | Reason                      |
|--------------------------------------------------------|-----------------------------|
| Store CVV/CVV2 in any form                             | PCI-DSS Req 3.2 — absolute  |
| Store full PAN in plain text                           | PCI-DSS Req 3.4             |
| Log full IBAN without masking                          | GDPR Art. 32 / internal policy|
| Process payment without sanctions screening            | 6AMLD / FATF Rec 16          |
| Accept payment with structuring pattern (smurfing)    | AML obligation               |
| Transfer funds to OFAC / HMT / EU sanctioned entity   | Sanctions law               |
| Bypass SCA for payment > EUR 30 without exemption     | PSD2 Art. 97                |
| Accept back-dated value date > D-1                    | ECB regulatory reporting    |
| Process without originator name + account number      | FATF wire transfer rules    |
| Settle to non-SWIFT / non-SEPA unrecognised BIC       | Fraud risk                  |
| Allow retry count > 3 for a single payment            | Scheme rules                |
| Modify a settled payment record                       | Accounting integrity        |
| Cross-customer data access without explicit delegation| GDPR + access control       |
| Allow admin override of sanctions flag without audit  | Compliance gate             |
| Store encryption keys in application.conf             | PCI-DSS Req 3.6             |
