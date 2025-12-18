package patterns.factory;

import models.Order;
import models.Customer;
import models.Product;
import java.util.List;

/**
 * OrderFactory - Factory pattern implementation for order creation
 * 
 * DESIGN PATTERN: Factory Pattern
 * PURPOSE: Creates different types of orders (Standard and Express)
 * 
 * JUSTIFICATION:
 * - E-commerce systems need to handle different order types with different characteristics
 * - Each order type has different shipping costs, delivery times, and processing
 * - Factory pattern simplifies order creation by encapsulating order type logic
 * - Makes it easy to add new order types (e.g., International, Same-Day) in the future
 * - Client code doesn't need to know the specifics of each order type
 * 
 * ORDER TYPES:
 * - Standard Order: Regular shipping ($10), 5-7 business days
 * - Express Order: Fast shipping ($25), 1-2 business days
 * 
 * BENEFITS:
 * - Centralizes order type logic
 * - Easy to modify shipping costs and delivery times
 * - Supports future expansion with new order types
 * 
 * @author E-commerce System Team
 */
public class OrderFactory {
    
    private static int orderCounter = 1000; // Start order IDs from 1000

    /**
     * Creates an order based on the specified type
     * 
     * @param orderType Type of order ("Standard" or "Express")
     * @param customer Customer placing the order
     * @param products List of products in the order
     * @param shippingAddress Shipping address
     * @param paymentMethod Payment method
     * @return Created order instance
     * @throws IllegalArgumentException if order type is unknown
     */
    public static Order createOrder(String orderType, Customer customer, List<Product> products,
                                   String shippingAddress, String paymentMethod) {
        
        if (orderType == null) {
            throw new IllegalArgumentException("Order type cannot be null");
        }
        
        // Generate unique order ID
        String orderId = "ORD" + (orderCounter++);
        
        // Calculate total amount (product prices + shipping)
        double productTotal = products.stream()
                                    .mapToDouble(Product::getPrice)
                                    .sum();
        
        double shippingCost;
        
        // Factory decides how to create the order based on type
        switch (orderType.toLowerCase()) {
            case "standard":
                shippingCost = 10.0; // Standard shipping cost
                return new Order(orderId, customer, products, shippingAddress, 
                               paymentMethod, "Standard", productTotal + shippingCost);
            
            case "express":
                shippingCost = 25.0; // Express shipping cost
                return new Order(orderId, customer, products, shippingAddress, 
                               paymentMethod, "Express", productTotal + shippingCost);
            
            default:
                throw new IllegalArgumentException("Unknown order type: " + orderType);
        }
    }

    /**
     * Creates a Standard order (convenience method)
     * 
     * @param customer Customer placing the order
     * @param products List of products
     * @param shippingAddress Shipping address
     * @param paymentMethod Payment method
     * @return Standard order instance
     */
    public static Order createStandardOrder(Customer customer, List<Product> products,
                                           String shippingAddress, String paymentMethod) {
        return createOrder("Standard", customer, products, shippingAddress, paymentMethod);
    }

    /**
     * Creates an Express order (convenience method)
     * 
     * @param customer Customer placing the order
     * @param products List of products
     * @param shippingAddress Shipping address
     * @param paymentMethod Payment method
     * @return Express order instance
     */
    public static Order createExpressOrder(Customer customer, List<Product> products,
                                          String shippingAddress, String paymentMethod) {
        return createOrder("Express", customer, products, shippingAddress, paymentMethod);
    }

    /**
     * Gets the shipping cost for a specific order type
     * 
     * @param orderType Order type
     * @return Shipping cost
     */
    public static double getShippingCost(String orderType) {
        switch (orderType.toLowerCase()) {
            case "standard":
                return 10.0;
            case "express":
                return 25.0;
            default:
                return 10.0;
        }
    }

    /**
     * Gets the estimated delivery time for a specific order type
     * 
     * @param orderType Order type
     * @return Estimated delivery time
     */
    public static String getEstimatedDelivery(String orderType) {
        switch (orderType.toLowerCase()) {
            case "standard":
                return "5-7 business days";
            case "express":
                return "1-2 business days";
            default:
                return "5-7 business days";
        }
    }
}
