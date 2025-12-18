package patterns.singleton;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * PaymentGateway - Singleton pattern implementation for payment processing
 * 
 * DESIGN PATTERN: Singleton Pattern
 * PURPOSE: Ensures a single, consistent payment processing instance across the application
 * 
 * JUSTIFICATION:
 * - Payment processing should be centralized to maintain consistency and security
 * - Multiple payment gateway instances could lead to:
 *   * Duplicate payment processing
 *   * Inconsistent transaction logging
 *   * Security vulnerabilities
 * - Single instance allows for centralized monitoring and auditing of all transactions
 * - Easier to manage payment gateway configuration and credentials
 * - Provides a single point of control for payment operations
 * 
 * IMPLEMENTATION: Thread-safe eager initialization
 * 
 * NOTE: This is accessed through PaymentProxy in production (Proxy pattern)
 * 
 * @author E-commerce System Team
 */
public class PaymentGateway {
    // Eager initialization - instance created at class loading time
    private static final PaymentGateway instance = new PaymentGateway();
    
    // Store transaction history
    private Map<String, TransactionRecord> transactionHistory;
    private int transactionCounter;

    /**
     * Private constructor to prevent instantiation from outside
     * This is a key element of the Singleton pattern
     */
    private PaymentGateway() {
        transactionHistory = new HashMap<>();
        transactionCounter = 1000; // Start transaction IDs from 1000
        System.out.println("PaymentGateway initialized");
    }

    /**
     * Gets the single instance of PaymentGateway
     * Thread-safe due to eager initialization
     * 
     * @return The single instance of PaymentGateway
     */
    public static PaymentGateway getInstance() {
        return instance;
    }

    /**
     * Processes a payment transaction
     * 
     * @param paymentMethod Payment method (e.g., "Credit Card", "PayPal", "Debit Card")
     * @param amount Amount to charge
     * @param cardNumber Card number or payment account identifier
     * @param customerName Name of the customer
     * @return Transaction ID if successful
     * @throws PaymentException if payment processing fails
     */
    public synchronized String processPayment(String paymentMethod, double amount, 
                                             String cardNumber, String customerName) throws PaymentException {
        System.out.println("Processing payment...");
        System.out.println("Method: " + paymentMethod + ", Amount: $" + amount);
        
        // Generate unique transaction ID
        String transactionId = "TXN" + (transactionCounter++);
        
        // Simulate payment processing
        try {
            Thread.sleep(1000); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate success (90% success rate)
        boolean success = Math.random() > 0.1;
        
        if (!success) {
            throw new PaymentException("Payment declined. Please check your payment details.");
        }
        
        // Record transaction
        TransactionRecord record = new TransactionRecord(
            transactionId, paymentMethod, amount, cardNumber, customerName, new Date(), true
        );
        transactionHistory.put(transactionId, record);
        
        System.out.println("Payment successful. Transaction ID: " + transactionId);
        return transactionId;
    }

    /**
     * Refunds a payment transaction
     * 
     * @param transactionId Transaction ID to refund
     * @return true if refund successful
     * @throws PaymentException if refund fails
     */
    public synchronized boolean refundPayment(String transactionId) throws PaymentException {
        TransactionRecord record = transactionHistory.get(transactionId);
        
        if (record == null) {
            throw new PaymentException("Transaction not found: " + transactionId);
        }
        
        if (!record.isSuccess()) {
            throw new PaymentException("Cannot refund a failed transaction");
        }
        
        System.out.println("Refunding transaction: " + transactionId);
        record.setRefunded(true);
        
        return true;
    }

    /**
     * Gets transaction details
     * 
     * @param transactionId Transaction ID
     * @return Transaction record
     */
    public synchronized TransactionRecord getTransaction(String transactionId) {
        return transactionHistory.get(transactionId);
    }

    /**
     * Gets all transaction history
     * 
     * @return Map of all transactions
     */
    public synchronized Map<String, TransactionRecord> getTransactionHistory() {
        return new HashMap<>(transactionHistory);
    }

    /**
     * Inner class to represent a transaction record
     */
    public static class TransactionRecord {
        private String transactionId;
        private String paymentMethod;
        private double amount;
        private String cardNumber;
        private String customerName;
        private Date transactionDate;
        private boolean success;
        private boolean refunded;

        public TransactionRecord(String transactionId, String paymentMethod, double amount,
                               String cardNumber, String customerName, Date transactionDate, boolean success) {
            this.transactionId = transactionId;
            this.paymentMethod = paymentMethod;
            this.amount = amount;
            this.cardNumber = maskCardNumber(cardNumber);
            this.customerName = customerName;
            this.transactionDate = transactionDate;
            this.success = success;
            this.refunded = false;
        }

        private String maskCardNumber(String cardNumber) {
            if (cardNumber == null || cardNumber.length() < 4) {
                return "****";
            }
            return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        }

        // Getters and Setters
        public String getTransactionId() { return transactionId; }
        public String getPaymentMethod() { return paymentMethod; }
        public double getAmount() { return amount; }
        public String getCardNumber() { return cardNumber; }
        public String getCustomerName() { return customerName; }
        public Date getTransactionDate() { return transactionDate; }
        public boolean isSuccess() { return success; }
        public boolean isRefunded() { return refunded; }
        public void setRefunded(boolean refunded) { this.refunded = refunded; }

        @Override
        public String toString() {
            return String.format("Transaction[%s] %s: $%.2f (%s)", 
                    transactionId, paymentMethod, amount, success ? "Success" : "Failed");
        }
    }

    /**
     * Custom exception for payment processing errors
     */
    public static class PaymentException extends Exception {
        public PaymentException(String message) {
            super(message);
        }
    }
}
