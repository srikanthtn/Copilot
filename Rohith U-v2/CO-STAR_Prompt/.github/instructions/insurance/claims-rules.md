<!-- version: 2.0.0 | domain: insurance | framework: CO-STAR+PromptML | updated: 2024-01-15 -->

# Insurance — Claims Rules

## Claims Processing Rules

| Rule ID     | Rule                                                      | Error                        |
|-------------|-----------------------------------------------------------|------------------------------|
| INS-CLM-001 | Claim must reference an ACTIVE policy at incident date    | `NoPolicyInForceError`       |
| INS-CLM-002 | Incident date must be within policy coverage period       | `IncidentOutsideCoverageError`|
| INS-CLM-003 | Claim amount must not exceed policy coverage limit        | `CoverageExceededError`      |
| INS-CLM-004 | Deductible must be subtracted before payment calculation  | Business rule                |
| INS-CLM-005 | Exclusion check must be performed before approval         | `ExclusionAppliedError`      |
| INS-CLM-006 | Claims handler must acknowledge within 24 hours           | `ClaimAcknowledgementSLABreach`|
| INS-CLM-007 | Payment decision within 30 days of all evidence received  | `ClaimDecisionSLABreach`     |
| INS-CLM-008 | Rejected claims must include written reason code          | `MissingRejectionReasonError`|

## Anti-Fraud Checks

| Check                                  | Trigger                                |
|----------------------------------------|----------------------------------------|
| Multiple claims on same policy in 12M  | Flag: `SuspectedInsuranceFraud`        |
| Claim filed days before policy lapse  | Flag: `SuspectedFraud_TimingAnomaly`   |
| Claim amount equals policy limit exactly| Flag: `SuspectedFraud_MaxClaim`       |
| Same claimant across multiple policies | Cross-policy fraud detection check     |

## Payment Calculation

```
NetClaimPayment = ClaimAmount - Deductible
if ClaimAmount > CoverageLimit: NetClaimPayment = CoverageLimit - Deductible

// All BigDecimal with DECIMAL128 + HALF_EVEN rounding
// Negative result: Left(ClaimCalculationError) — never process
```


# Insurance — Policy Lifecycle

## State Machine

```
QUOTED    → [bindPolicy()        ] → ACTIVE
ACTIVE    → [lapsePolicy()       ] → LAPSED       (missed premium + grace)
ACTIVE    → [cancelPolicy()      ] → CANCELLED    (customer or insurer)
ACTIVE    → [expirePolicy()      ] → EXPIRED      (end of term)
LAPSED    → [reinstatePolicy()   ] → ACTIVE       (arrears paid; health recheck)
LAPSED    → [closePolicy()       ] → EXPIRED
CANCELLED → [final]                              (terminal state)
EXPIRED   → [renewPolicy()       ] → ACTIVE       (new policy ID issued)
```

## Premium Rules

| Rule ID     | Rule                                                    |
|-------------|---------------------------------------------------------|
| INS-PREM-001| Premium must be paid by due date or within grace period |
| INS-PREM-002| Grace period: 30 days for life; 15 days for non-life   |
| INS-PREM-003| Mid-term adjustments: pro-rata recalculation required  |
| INS-PREM-004| Renewal: re-underwriting required for high-risk changes|

## Cancellation Rules

| Rule ID     | Rule                                                     |
|-------------|----------------------------------------------------------|
| INS-CAN-001 | Insurer-initiated: 30 days written notice required      |
| INS-CAN-002 | Return premium calculated pro-rata from cancellation date|
| INS-CAN-003 | No cancellation while claim is in progress              |
