<!-- version: 2.0.0 | domain: risk-compliance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Risk & Compliance — Audit Requirements

## Audit Trail Specification

Every compliance evaluation MUST produce an immutable `ComplianceAuditRecord`.
See `risk-frameworks.md` for the case class definition.

## Mandatory Audit Events

| Event                          | Trigger                              | Retention |
|-------------------------------|--------------------------------------|-----------|
| Sanctions screen completed     | Every payment / onboarding           | 5 years   |
| Sanctions hit manually cleared | Human override with 4-eyes           | 5 years   |
| AML alert raised               | Rule evaluation match                | 5 years   |
| AML alert dismissed            | Analyst decision + rationale         | 5 years   |
| SAR submitted to FIU           | Suspicious activity confirmed        | 5 years   |
| Risk profile regrade           | Upward or downward risk category     | 7 years   |
| PEP designation applied        | Screening match against PEP list     | 7 years   |
| EDD triggered                  | High-risk / PEP / non-standard       | 7 years   |
| Credit limit approved          | Credit committee or automated system | 7 years   |

## 4-Eyes Control Requirement

The following actions require two-person authorisation. Generated code must
enforce this via the `FourEyesValidator` service:

- Manual override of sanctions HOLD
- Closure of an AML alert without action
- Upgrade of a customer's risk category downwards (LOW)
- Approval of credit above EUR 1,000,000

## Audit Log Immutability

- Posts to append-only Delta Lake table (no UPDATE/DELETE)
- `allowOverwrite = false` on Delta write operation
- Signed with HMAC-SHA256 using audit key from HSM
- Separate read-only replica for compliance team access
