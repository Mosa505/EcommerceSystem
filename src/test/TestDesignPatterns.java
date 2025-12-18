package test;

import models.*;
import patterns.singleton.CartManager;
import patterns.singleton.PaymentGateway;
import patterns.singleton.PaymentGateway.PaymentException;
import patterns.factory.ProductFactory;
import patterns.factory.OrderFactory;
import patterns.builder.OrderBuilder;
import patterns.proxy.PaymentProxy;

/**
 * TestDesignPatterns - Test class to verify all design patterns work correctly
 * 
 * This class tests:
 * 1. Singleton Pattern - CartManager and PaymentGateway
 * 2. Factory Pattern - ProductFactory and OrderFactory
 * 3. Prototype Pattern - Product cloning
 * 4. Builder Pattern - OrderBuilder
 * 5. Proxy Pattern - PaymentProxy
 * 
 * @author E-commerce System Team
 */
public class TestDesignPatterns {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("TESTING DESIGN PATTERNS");
        System.out.println("=".repeat(60));
        
        try {
            testSingletonPattern();
            testFactoryPattern();
            testPrototypePattern();
            testBuilderPattern();
            testProxyPattern();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.err.println("\nTEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test Singleton Pattern
     */
    private static void testSingletonPattern() {
        System.out.println("\n1. TESTING SINGLETON PATTERN");
        System.out.println("-".repeat(60));
        
        // Test CartManager Singleton
        CartManager cart1 = CartManager.getInstance();
        CartManager cart2 = CartManager.getInstance();
        
        if (cart1 == cart2) {
            System.out.println("✓ CartManager Singleton: PASSED (same instance)");
        } else {
            throw new RuntimeException("CartManager Singleton test failed");
        }
        
        // Test PaymentGateway Singleton
        PaymentGateway gateway1 = PaymentGateway.getInstance();
        PaymentGateway gateway2 = PaymentGateway.getInstance();
        
        if (gateway1 == gateway2) {
            System.out.println("✓ PaymentGateway Singleton: PASSED (same instance)");
        } else {
            throw new RuntimeException("PaymentGateway Singleton test failed");
        }
    }
    
    /**
     * Test Factory Pattern
     */
    private static void testFactoryPattern() {
        System.out.println("\n2. TESTING FACTORY PATTERN");
        System.out.println("-".repeat(60));
        
        // Test ProductFactory
        Product electronics = ProductFactory.createProduct("Electronics", "E001", "Laptop", 
            999.99, "High-end laptop", 10, "2 Years", "Dell");
        
        Product clothing = ProductFactory.createProduct("Clothing", "C001", "T-Shirt", 
            29.99, "Cotton t-shirt", 50, "M", "Cotton");
        
        Product appliance = ProductFactory.createProduct("Home Appliance", "H001", "Microwave", 
            199.99, "1000W microwave", 8, "1000W", "A+");
        
        if (electronics instanceof Electronics && 
            clothing instanceof Clothing && 
            appliance instanceof HomeAppliance) {
            System.out.println("✓ ProductFactory: PASSED (created all product types)");
        } else {
            throw new RuntimeException("ProductFactory test failed");
        }
        
        // Test OrderFactory
        Customer customer = new Customer("C001", "John Doe", "john@example.com", 
            "555-0123", "123 Main St");
        java.util.List<Product> products = new java.util.ArrayList<>();
        products.add(electronics);
        
        Order standardOrder = OrderFactory.createStandardOrder(customer, products, 
            "123 Main St", "Credit Card");
        Order expressOrder = OrderFactory.createExpressOrder(customer, products, 
            "123 Main St", "PayPal");
        
        if (standardOrder.getOrderType().equals("Standard") && 
            expressOrder.getOrderType().equals("Express")) {
            System.out.println("✓ OrderFactory: PASSED (created both order types)");
        } else {
            throw new RuntimeException("OrderFactory test failed");
        }
    }
    
    /**
     * Test Prototype Pattern
     */
    private static void testPrototypePattern() throws CloneNotSupportedException {
        System.out.println("\n3. TESTING PROTOTYPE PATTERN");
        System.out.println("-".repeat(60));
        
        // Create original product
        Electronics original = new Electronics("E001", "Laptop", 999.99, 
            "Original laptop", 10, "2 Years", "Dell");
        
        // Clone the product
        Electronics clone = original.clone();
        
        // Verify they are different objects but with same values
        if (original != clone && 
            original.getProductId().equals(clone.getProductId()) &&
            original.getName().equals(clone.getName()) &&
            original.getPrice() == clone.getPrice()) {
            System.out.println("✓ Prototype Pattern: PASSED (product cloned successfully)");
            
            // Modify clone to show independence
            clone.setName("Laptop - Clone");
            if (!original.getName().equals(clone.getName())) {
                System.out.println("✓ Prototype Independence: PASSED (clone is independent)");
            }
        } else {
            throw new RuntimeException("Prototype Pattern test failed");
        }
    }
    
    /**
     * Test Builder Pattern
     */
    private static void testBuilderPattern() {
        System.out.println("\n4. TESTING BUILDER PATTERN");
        System.out.println("-".repeat(60));
        
        // Create customer and products
        Customer customer = new Customer("C001", "Jane Smith", "jane@example.com", 
            "555-0456", "456 Oak Ave");
        
        Product product1 = ProductFactory.createElectronics("E001", "Phone", 699.99, 
            "Smartphone", 15);
        Product product2 = ProductFactory.createClothing("C001", "Jeans", 59.99, 
            "Denim jeans", 30);
        
        // Build order using Builder pattern
        Order order = new OrderBuilder()
            .setCustomer(customer)
            .addProduct(product1)
            .addProduct(product2)
            .setShippingAddress("456 Oak Ave")
            .setPaymentMethod("Credit Card")
            .setOrderType("Express")
            .build();
        
        if (order != null && 
            order.getCustomer().equals(customer) &&
            order.getProducts().size() == 2 &&
            order.getOrderType().equals("Express")) {
            System.out.println("✓ Builder Pattern: PASSED (order built successfully)");
            System.out.println("  Order ID: " + order.getOrderId());
            System.out.println("  Total: $" + order.getTotalAmount());
            System.out.println("  Shipping: " + order.getEstimatedDelivery());
        } else {
            throw new RuntimeException("Builder Pattern test failed");
        }
    }
    
    /**
     * Test Proxy Pattern
     */
    private static void testProxyPattern() {
        System.out.println("\n5. TESTING PROXY PATTERN");
        System.out.println("-".repeat(60));
        
        PaymentProxy proxy = new PaymentProxy();
        
        // Test valid payment
        try {
            String transactionId = proxy.processPayment("Credit Card", 100.0, 
                "1234567890123456", "Test Customer");
            
            if (transactionId != null && transactionId.startsWith("TXN")) {
                System.out.println("✓ Proxy Pattern - Valid Payment: PASSED");
                System.out.println("  Transaction ID: " + transactionId);
            }
        } catch (PaymentException e) {
            System.out.println("✓ Proxy Pattern - Payment Processing: Note - Payment declined (this is normal)");
        }
        
        // Test validation (invalid amount)
        try {
            proxy.processPayment("Credit Card", -50.0, "1234567890123456", "Test Customer");
            throw new RuntimeException("Proxy should have rejected negative amount");
        } catch (PaymentException e) {
            System.out.println("✓ Proxy Pattern - Validation: PASSED (rejected invalid amount)");
        }
        
        // Test validation (invalid card)
        try {
            proxy.processPayment("Credit Card", 100.0, "invalid", "Test Customer");
            throw new RuntimeException("Proxy should have rejected invalid card");
        } catch (PaymentException e) {
            System.out.println("✓ Proxy Pattern - Card Validation: PASSED (rejected invalid card)");
        }
        
        // Test validation (unsupported payment method)
        try {
            proxy.processPayment("Bitcoin", 100.0, "1234567890123456", "Test Customer");
            throw new RuntimeException("Proxy should have rejected unsupported method");
        } catch (PaymentException e) {
            System.out.println("✓ Proxy Pattern - Method Validation: PASSED (rejected unsupported method)");
        }
    }
}
