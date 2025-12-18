package patterns.proxy;

import patterns.singleton.PaymentGateway;
import patterns.singleton.PaymentGateway.PaymentException;
import patterns.singleton.PaymentGateway.TransactionRecord;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * PaymentProxy - Proxy pattern implementation for PaymentGateway
 * 
 * DESIGN PATTERN: Proxy Pattern
 * PURPOSE: Acts as a protective layer in front of the PaymentGateway
 * 
 * JUSTIFICATION:
 * - Payment processing is sensitive and requires validation before execution
 * - Direct access to PaymentGateway could lead to:
 *   * Invalid payment data being processed
 *   * Security vulnerabilities
 *   * Lack of logging and auditing
 * - Proxy pattern adds a security and validation layer without modifying the gateway
 * - Allows for additional features like logging, caching, and access control
 * - Separates concerns: validation logic in proxy, processing logic in gateway
 * 
 * FEATURES PROVIDED BY PROXY:
 * - Payment information validation (card number format, amount validation)
 * - Logging of all payment attempts (successful and failed)
 * - Authentication checks (can be extended)
 * - Rate limiting protection (prevents payment spam)
 * - Detailed error messages for validation failures
 * 
 * BENEFITS:
 * - Enhanced security through validation
 * - Comprehensive logging and auditing
 * - Client code uses proxy instead of direct gateway access
 * - Easy to add new validations without touching gateway code
 * - Follows Open/Closed Principle
 * 
 * @author E-commerce System Team
 */
public class PaymentProxy {
    
    // Reference to the real payment gateway (Singleton)
    private final PaymentGateway paymentGateway;
    
    // Logging system
    private Map<String, PaymentAttempt> paymentLogs;
    
    // Rate limiting: track attempts per customer
    private Map<String, Integer> attemptCounter;
    private static final int MAX_ATTEMPTS_PER_SESSION = 5;
    
    /**
     * Constructor initializes the proxy with the real payment gateway
     */
    public PaymentProxy() {
        this.paymentGateway = PaymentGateway.getInstance();
        this.paymentLogs = new HashMap<>();
        this.attemptCounter = new HashMap<>();
        System.out.println("PaymentProxy initialized - providing secure access to PaymentGateway");
    }

    /**
     * Processes payment with validation and logging
     * This is the main method that clients should use instead of accessing the gateway directly
     * 
     * @param paymentMethod Payment method
     * @param amount Amount to charge
     * @param cardNumber Card number
     * @param customerName Customer name
     * @return Transaction ID if successful
     * @throws PaymentException if payment fails or validation fails
     */
    public String processPayment(String paymentMethod, double amount, 
                                String cardNumber, String customerName) throws PaymentException {
        
        System.out.println("=== PaymentProxy: Processing payment request ===");
        
        // Step 1: Log the payment attempt
        logPaymentAttempt(customerName, amount, paymentMethod);
        
        // Step 2: Check rate limiting
        checkRateLimit(customerName);
        
        // Step 3: Validate payment information
        validatePaymentInfo(paymentMethod, amount, cardNumber, customerName);
        
        // Step 4: If all validations pass, forward to real payment gateway
        String transactionId;
        try {
            System.out.println("PaymentProxy: Validation passed, forwarding to PaymentGateway");
            transactionId = paymentGateway.processPayment(paymentMethod, amount, cardNumber, customerName);
            
            // Log successful payment
            logPaymentSuccess(customerName, transactionId, amount);
            
            System.out.println("PaymentProxy: Payment processed successfully");
            return transactionId;
            
        } catch (PaymentException e) {
            // Log failed payment
            logPaymentFailure(customerName, e.getMessage(), amount);
            
            System.out.println("PaymentProxy: Payment failed - " + e.getMessage());
            throw e;
        }
    }

    /**
     * Validates payment information before processing
     * 
     * @param paymentMethod Payment method
     * @param amount Amount to charge
     * @param cardNumber Card number
     * @param customerName Customer name
     * @throws PaymentException if validation fails
     */
    private void validatePaymentInfo(String paymentMethod, double amount, 
                                     String cardNumber, String customerName) throws PaymentException {
        
        System.out.println("PaymentProxy: Validating payment information...");
        
        // Validate payment method
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new PaymentException("Validation failed: Payment method is required");
        }
        
        // Validate supported payment methods
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new PaymentException("Validation failed: Unsupported payment method: " + paymentMethod);
        }
        
        // Validate amount
        if (amount <= 0) {
            throw new PaymentException("Validation failed: Payment amount must be greater than zero");
        }
        
        if (amount > 10000) {
            throw new PaymentException("Validation failed: Payment amount exceeds maximum limit ($10,000)");
        }
        
        // Validate card number format
        if (!isValidCardNumber(cardNumber)) {
            throw new PaymentException("Validation failed: Invalid card number format");
        }
        
        // Validate customer name
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new PaymentException("Validation failed: Customer name is required");
        }
        
        System.out.println("PaymentProxy: Validation successful");
    }

    /**
     * Checks if payment method is supported
     * 
     * @param paymentMethod Payment method
     * @return true if supported
     */
    private boolean isValidPaymentMethod(String paymentMethod) {
        String method = paymentMethod.toLowerCase();
        return method.equals("credit card") || 
               method.equals("debit card") || 
               method.equals("paypal");
    }

    /**
     * Validates card number format
     * Simple validation: checks if it's numeric and has 13-19 digits
     * 
     * @param cardNumber Card number
     * @return true if valid format
     */
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        
        // Remove spaces and dashes
        String cleaned = cardNumber.replaceAll("[\\s-]", "");
        
        // Check if it's numeric and has valid length (13-19 digits)
        return Pattern.matches("\\d{13,19}", cleaned);
    }

    /**
     * Checks rate limiting to prevent payment spam
     * 
     * @param customerName Customer name
     * @throws PaymentException if rate limit exceeded
     */
    private void checkRateLimit(String customerName) throws PaymentException {
        int attempts = attemptCounter.getOrDefault(customerName, 0);
        
        if (attempts >= MAX_ATTEMPTS_PER_SESSION) {
            throw new PaymentException("Rate limit exceeded: Too many payment attempts. Please try again later.");
        }
        
        attemptCounter.put(customerName, attempts + 1);
    }

    /**
     * Logs a payment attempt
     */
    private void logPaymentAttempt(String customerName, double amount, String paymentMethod) {
        String logKey = customerName + "_" + System.currentTimeMillis();
        PaymentAttempt attempt = new PaymentAttempt(customerName, amount, paymentMethod, new Date());
        paymentLogs.put(logKey, attempt);
        System.out.println("PaymentProxy: Logged payment attempt for " + customerName);
    }

    /**
     * Logs a successful payment
     */
    private void logPaymentSuccess(String customerName, String transactionId, double amount) {
        System.out.println(String.format("PaymentProxy: SUCCESS - Customer: %s, Transaction: %s, Amount: $%.2f", 
                customerName, transactionId, amount));
    }

    /**
     * Logs a failed payment
     */
    private void logPaymentFailure(String customerName, String reason, double amount) {
        System.out.println(String.format("PaymentProxy: FAILURE - Customer: %s, Reason: %s, Amount: $%.2f", 
                customerName, reason, amount));
    }

    /**
     * Gets transaction details from the gateway
     * 
     * @param transactionId Transaction ID
     * @return Transaction record
     */
    public TransactionRecord getTransaction(String transactionId) {
        return paymentGateway.getTransaction(transactionId);
    }

    /**
     * Refunds a payment through the proxy
     * 
     * @param transactionId Transaction ID to refund
     * @return true if successful
     * @throws PaymentException if refund fails
     */
    public boolean refundPayment(String transactionId) throws PaymentException {
        System.out.println("PaymentProxy: Processing refund request");
        
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new PaymentException("Transaction ID is required for refund");
        }
        
        return paymentGateway.refundPayment(transactionId);
    }

    /**
     * Gets all payment logs
     * 
     * @return Map of payment logs
     */
    public Map<String, PaymentAttempt> getPaymentLogs() {
        return new HashMap<>(paymentLogs);
    }

    /**
     * Inner class to represent a payment attempt log
     */
    public static class PaymentAttempt {
        private String customerName;
        private double amount;
        private String paymentMethod;
        private Date attemptDate;

        public PaymentAttempt(String customerName, double amount, String paymentMethod, Date attemptDate) {
            this.customerName = customerName;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.attemptDate = attemptDate;
        }

        // Getters
        public String getCustomerName() { return customerName; }
        public double getAmount() { return amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public Date getAttemptDate() { return attemptDate; }
    }
}
