package services;

import models.Order;
import models.Customer;
import models.Product;
import patterns.singleton.CartManager;
import patterns.proxy.PaymentProxy;
import patterns.singleton.PaymentGateway.PaymentException;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderProcessor - Service class for processing orders
 * 
 * This class coordinates the order processing workflow including:
 * - Cart management
 * - Order creation
 * - Payment processing through PaymentProxy
 * - Order confirmation
 * 
 * @author E-commerce System Team
 */
public class OrderProcessor {
    
    private CartManager cartManager;
    private PaymentProxy paymentProxy;
    private List<Order> orderHistory;

    /**
     * Constructor initializes the order processor
     */
    public OrderProcessor() {
        this.cartManager = CartManager.getInstance();
        this.paymentProxy = new PaymentProxy();
        this.orderHistory = new ArrayList<>();
    }

    /**
     * Processes an order: validates cart, processes payment, creates order
     * 
     * @param order Order to process
     * @param cardNumber Card number for payment
     * @return Transaction ID if successful
     * @throws PaymentException if payment fails
     * @throws IllegalStateException if cart is empty or order is invalid
     */
    public String processOrder(Order order, String cardNumber) throws PaymentException {
        
        System.out.println("OrderProcessor: Processing order...");
        
        // Validate order
        if (order == null) {
            throw new IllegalStateException("Order cannot be null");
        }
        
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalStateException("Order must contain at least one product");
        }
        
        // Process payment through proxy
        String transactionId = paymentProxy.processPayment(
            order.getPaymentMethod(),
            order.getTotalAmount(),
            cardNumber,
            order.getCustomer().getName()
        );
        
        // Update order status
        order.setStatus("Paid");
        
        // Add to order history
        orderHistory.add(order);
        
        System.out.println("OrderProcessor: Order processed successfully. Transaction ID: " + transactionId);
        
        return transactionId;
    }

    /**
     * Confirms an order after successful payment
     * 
     * @param order Order to confirm
     * @param transactionId Transaction ID from payment
     */
    public void confirmOrder(Order order, String transactionId) {
        order.setStatus("Confirmed");
        System.out.println("Order confirmed: " + order.getOrderId() + " (Transaction: " + transactionId + ")");
    }

    /**
     * Cancels an order
     * 
     * @param order Order to cancel
     */
    public void cancelOrder(Order order) {
        order.setStatus("Cancelled");
        System.out.println("Order cancelled: " + order.getOrderId());
    }

    /**
     * Gets all orders in history
     * 
     * @return List of all orders
     */
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    /**
     * Gets orders for a specific customer
     * 
     * @param customer Customer to get orders for
     * @return List of customer's orders
     */
    public List<Order> getCustomerOrders(Customer customer) {
        List<Order> customerOrders = new ArrayList<>();
        
        for (Order order : orderHistory) {
            if (order.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                customerOrders.add(order);
            }
        }
        
        return customerOrders;
    }

    /**
     * Clears the shopping cart (typically after successful order)
     */
    public void clearCart() {
        cartManager.clear();
    }
}
