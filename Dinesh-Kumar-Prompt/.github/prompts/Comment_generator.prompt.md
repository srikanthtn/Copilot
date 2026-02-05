---
agent: agent
# Java Comment Generator Prompt

## Role
Automated Javadoc and inline comment generator for Java/Spring Boot applications in regulated environments.

## Context
Generate clear, concise, and compliant documentation for Java code following industry best practices and regulatory requirements.

---

## Comment Types

### 1. Class-Level Javadoc

```java
/**
 * Service for managing SEPA credit transfer payments.
 * <p>
 * This service handles the complete lifecycle of SEPA payments including:
 * <ul>
 *   <li>Payment validation and compliance checks</li>
 *   <li>Idempotency and duplicate detection</li>
 *   <li>Transaction state management</li>
 *   <li>Audit trail generation</li>
 * </ul>
 * </p>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe.</p>
 * <p><b>Compliance:</b> SEPA, PSD2, GDPR</p>
 *
 * @author [Team Name]
 * @version 1.0
 * @since 2024-01-01
 * @see PaymentRepository
 * @see PaymentValidator
 */
@Service
public class SepaPaymentService {
    // ...
}
```

### 2. Method-Level Javadoc

```java
/**
 * Creates a new SEPA credit transfer payment.
 * <p>
 * This method performs the following operations:
 * <ol>
 *   <li>Validates payment request (IBAN, BIC, amount)</li>
 *   <li>Checks for duplicates using idempotency key</li>
 *   <li>Performs compliance checks (sanctions, AML)</li>
 *   <li>Creates payment record with PENDING status</li>
 *   <li>Generates audit trail event</li>
 * </ol>
 * </p>
 *
 * @param request the payment request containing IBAN, BIC, amount, and reference
 * @param idempotencyKey unique key to prevent duplicate processing
 * @return created payment with assigned ID and PENDING status
 * @throws InvalidIbanException if IBAN format or checksum is invalid
 * @throws DuplicatePaymentException if idempotency key already exists
 * @throws InsufficientFundsException if account balance is insufficient
 * @throws ComplianceException if payment fails sanctions or AML checks
 * @see #validatePayment(PaymentRequest)
 * @see #checkDuplicate(String)
 */
@Transactional
public Payment createPayment(PaymentRequest request, String idempotencyKey) {
    // ...
}
```

### 3. Field-Level Comments

```java
/**
 * Payment repository for database operations.
 * <p>
 * Provides CRUD operations and custom queries for payment entities.
 * All operations are transactional and audit-logged.
 * </p>
 */
private final PaymentRepository paymentRepository;

/**
 * Maximum payment amount in EUR for instant SEPA transfers.
 * <p>
 * As per SEPA Instant Credit Transfer scheme rules (SCT Inst).
 * </p>
 */
private static final BigDecimal MAX_INSTANT_AMOUNT = new BigDecimal("100000.00");

/**
 * Idempotency key expiration time in hours.
 * <p>
 * After this period, the same idempotency key can be reused.
 * Default: 24 hours as per PSD2 recommendations.
 * </p>
 */
@Value("${payment.idempotency.expiration:24}")
private int idempotencyExpirationHours;
```

### 4. Inline Comments for Complex Logic

```java
// Calculate IBAN checksum using modulo 97 algorithm (ISO 13616)
// 1. Move first 4 characters to end
// 2. Replace letters with numbers (A=10, B=11, ..., Z=35)
// 3. Calculate mod 97
// 4. Checksum should be 98 - (result)
String rearranged = iban.substring(4) + iban.substring(0, 4);
String numericIban = convertToNumeric(rearranged);
BigInteger ibanNumber = new BigInteger(numericIban);
int checksum = 98 - ibanNumber.mod(BigInteger.valueOf(97)).intValue();
```

### 5. TODO and FIXME Comments

```java
// TODO: Implement real-time sanctions screening integration
// Ticket: SEPA-1234
// Priority: High
// Deadline: 2024-Q2
private void performSanctionsCheck(Payment payment) {
    // Temporary: Always pass
    log.warn("Sanctions screening not yet implemented");
}

// FIXME: Race condition in concurrent payment processing
// Issue: Multiple threads can process same payment
// Impact: Duplicate payments possible
// Solution: Add distributed lock using Redis
// Ticket: SEPA-5678
```

---

## Comment Rules

### DO:
✅ Explain **WHY**, not **WHAT** (code shows what)
✅ Document business rules and regulatory requirements
✅ Include examples for complex APIs
✅ Document thread safety and concurrency
✅ Document performance characteristics
✅ Document security considerations
✅ Use proper grammar and spelling
✅ Keep comments up-to-date with code
✅ Document exceptions and error conditions
✅ Include references to external specs (ISO, SEPA, PSD2)

### DON'T:
❌ State the obvious (`i++; // increment i`)
❌ Comment bad code (refactor instead)
❌ Leave commented-out code
❌ Use vague comments ("fix later", "hack")
❌ Include personal opinions
❌ Duplicate information from method signature
❌ Use abbreviations (write "transaction" not "txn")
❌ Include sensitive information (passwords, keys)

---

## Financial Domain Comments

### Money Handling
```java
/**
 * Calculates payment amount with proper rounding.
 * <p>
 * Uses HALF_UP rounding mode as per banking standards.
 * All monetary calculations use BigDecimal to avoid
 * floating-point precision issues.
 * </p>
 *
 * @param grossAmount the gross amount before fees
 * @param feePercentage the fee percentage (e.g., 0.5 for 0.5%)
 * @return net amount after fees, rounded to 2 decimal places
 */
public BigDecimal calculateNetAmount(BigDecimal grossAmount, BigDecimal feePercentage) {
    BigDecimal fee = grossAmount.multiply(feePercentage)
        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    return grossAmount.subtract(fee);
}
```

### IBAN Validation
```java
/**
 * Validates IBAN format and checksum.
 * <p>
 * Implements ISO 13616 IBAN validation:
 * <ol>
 *   <li>Length check (15-34 characters)</li>
 *   <li>Format check (2 letters + 2 digits + alphanumeric)</li>
 *   <li>Checksum validation using modulo 97</li>
 *   <li>Country-specific length validation</li>
 * </ol>
 * </p>
 *
 * @param iban the IBAN to validate (with or without spaces)
 * @return true if IBAN is valid, false otherwise
 * @see <a href="https://www.swift.com/standards/data-standards/iban">SWIFT IBAN Registry</a>
 */
public boolean isValidIban(String iban) {
    // ...
}
```

### Transaction States
```java
/**
 * Payment lifecycle states as per SEPA SCT scheme.
 * <p>
 * State transitions:
 * <pre>
 * PENDING → VALIDATED → SUBMITTED → ACCEPTED → SETTLED
 *    ↓         ↓           ↓           ↓
 * REJECTED  REJECTED   REJECTED    REJECTED
 * </pre>
 * </p>
 *
 * <p><b>Important:</b> State transitions are irreversible except for
 * PENDING → REJECTED. Once SUBMITTED, payment cannot be cancelled.</p>
 */
public enum PaymentState {
    /** Initial state after creation */
    PENDING,
    
    /** Validation passed, ready for submission */
    VALIDATED,
    
    /** Submitted to clearing house */
    SUBMITTED,
    
    /** Accepted by beneficiary bank */
    ACCEPTED,
    
    /** Funds settled */
    SETTLED,
    
    /** Rejected at any stage */
    REJECTED
}
```

---

## Security & Compliance Comments

### PII Handling
```java
/**
 * Masks IBAN for logging and display.
 * <p>
 * GDPR Compliance: Only shows first 4 and last 4 characters.
 * Example: DE89370400440532013000 → DE89************3000
 * </p>
 *
 * @param iban the IBAN to mask
 * @return masked IBAN safe for logging
 */
public String maskIban(String iban) {
    // ...
}
```

### Audit Trail
```java
/**
 * Records payment event for audit trail.
 * <p>
 * Compliance: Required by PSD2 Article 72 (Record Keeping).
 * Retention: 5 years as per regulatory requirements.
 * </p>
 *
 * <p><b>Audit Fields:</b></p>
 * <ul>
 *   <li>Event type and timestamp</li>
 *   <li>User ID and IP address</li>
 *   <li>Payment ID and state transition</li>
 *   <li>Masked sensitive data (IBAN, amount)</li>
 * </ul>
 *
 * @param event the audit event to record
 */
private void recordAuditEvent(AuditEvent event) {
    // ...
}
```

---

## Spring Boot Specific Comments

### Configuration Properties
```java
/**
 * SEPA payment service configuration properties.
 * <p>
 * Configure via application.yml:
 * <pre>
 * sepa:
 *   payment:
 *     max-amount: 999999.99
 *     idempotency-expiration: 24
 *     batch-size: 1000
 * </pre>
 * </p>
 */
@ConfigurationProperties(prefix = "sepa.payment")
public class SepaPaymentProperties {
    // ...
}
```

### REST Endpoints
```java
/**
 * Creates a new SEPA payment.
 * <p>
 * <b>Endpoint:</b> POST /api/v1/payments
 * </p>
 * <p>
 * <b>Request Body:</b>
 * <pre>
 * {
 *   "debtorIban": "DE89370400440532013000",
 *   "creditorIban": "FR1420041010050500013M02606",
 *   "amount": "100.50",
 *   "currency": "EUR",
 *   "reference": "Invoice 12345"
 * }
 * </pre>
 * </p>
 * <p>
 * <b>Response:</b> 201 Created with payment ID
 * </p>
 * <p>
 * <b>Idempotency:</b> Use X-Idempotency-Key header to prevent duplicates
 * </p>
 *
 * @param request the payment request
 * @param idempotencyKey unique key for idempotency
 * @return created payment
 */
@PostMapping("/api/v1/payments")
public ResponseEntity<Payment> createPayment(
    @Valid @RequestBody PaymentRequest request,
    @RequestHeader("X-Idempotency-Key") String idempotencyKey) {
    // ...
}
```

---

## Comment Templates

### Service Class
```java
/**
 * [Brief description of service responsibility].
 * <p>
 * [Detailed description of what this service does]
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>[Feature 1]</li>
 *   <li>[Feature 2]</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b> [Thread-safe/Not thread-safe]</p>
 * <p><b>Compliance:</b> [Relevant regulations]</p>
 *
 * @author [Team/Author]
 * @version [Version]
 * @since [Date]
 */
```

### Repository Interface
```java
/**
 * Repository for [Entity] persistence operations.
 * <p>
 * Provides CRUD operations and custom queries for [entity description].
 * All operations are transactional and audit-logged.
 * </p>
 *
 * @see [Entity]
 */
```

### Validator Class
```java
/**
 * Validates [what it validates] according to [standard/regulation].
 * <p>
 * Validation Rules:
 * <ul>
 *   <li>[Rule 1]</li>
 *   <li>[Rule 2]</li>
 * </ul>
 * </p>
 *
 * @see [Related standard/spec]
 */
```

---

## Conclusion

Generate comments that:
- Add value (explain WHY, not WHAT)
- Follow Javadoc standards
- Include regulatory references
- Document business rules
- Are maintainable and accurate

---
Define the task to achieve, including specific requirements, constraints, and success criteria.