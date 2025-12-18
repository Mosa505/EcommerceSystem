# Design Patterns Implementation Guide

This document provides detailed information about each design pattern implemented in the E-commerce System.

## Table of Contents
1. [Singleton Pattern](#singleton-pattern)
2. [Factory Pattern](#factory-pattern)
3. [Prototype Pattern](#prototype-pattern)
4. [Builder Pattern](#builder-pattern)
5. [Proxy Pattern](#proxy-pattern)

---

## Singleton Pattern

### Implementation Files
- `src/patterns/singleton/CartManager.java`
- `src/patterns/singleton/PaymentGateway.java`

### Purpose
Ensures that a class has only one instance and provides a global point of access to it.

### Why Used in E-commerce System

#### CartManager
**Justification:**
- A user should have only ONE shopping cart during their session
- Multiple cart instances would lead to data inconsistency (items in different carts)
- Centralized cart management makes it easier to track cart state across different GUI panels
- Global access point allows any part of the application to access the cart
- Thread-safe implementation ensures proper behavior in multi-threaded GUI environment

**Implementation Details:**
```java
// Thread-safe lazy initialization with double-checked locking
private static volatile CartManager instance;

private CartManager() {
    cartItems = new HashMap<>();
}

public static CartManager getInstance() {
    if (instance == null) {
        synchronized (CartManager.class) {
            if (instance == null) {
                instance = new CartManager();
            }
        }
    }
    return instance;
}
```

**Key Features:**
- Thread-safe using volatile and synchronized
- Lazy initialization (created only when needed)
- Double-checked locking for performance
- Private constructor prevents external instantiation

#### PaymentGateway
**Justification:**
- Payment processing should be centralized for consistency and security
- Multiple payment gateway instances could lead to:
  - Duplicate payment processing
  - Inconsistent transaction logging
  - Security vulnerabilities
- Single instance allows centralized monitoring and auditing
- Easier to manage payment gateway configuration and credentials

**Implementation Details:**
```java
// Eager initialization - thread-safe by default
private static final PaymentGateway instance = new PaymentGateway();

private PaymentGateway() {
    transactionHistory = new HashMap<>();
    transactionCounter = 1000;
}

public static PaymentGateway getInstance() {
    return instance;
}
```

**Key Features:**
- Eager initialization (created at class loading)
- Thread-safe by design
- Transaction history tracking
- Unique transaction ID generation

### Usage Example
```java
// Get the singleton instance
CartManager cart = CartManager.getInstance();

// Add product to cart
cart.addProduct(product, quantity);

// Get total
double total = cart.getTotalPrice();

// Clear cart
cart.clear();
```

---

## Factory Pattern

### Implementation Files
- `src/patterns/factory/ProductFactory.java`
- `src/patterns/factory/OrderFactory.java`

### Purpose
Provides an interface for creating objects without specifying their exact classes.

### Why Used in E-commerce System

#### ProductFactory
**Justification:**
- E-commerce systems have multiple product types with different attributes
- Direct instantiation would scatter object creation logic throughout the code
- Factory pattern centralizes product creation logic
- Easy to add new product types without modifying existing code
- Encapsulates the complexity of deciding which product class to instantiate
- Promotes loose coupling - client code doesn't need to know specific product classes

**Implementation Details:**
```java
public static Product createProduct(String category, String productId, 
                                   String name, double price, 
                                   String description, int stockQuantity,
                                   String attribute1, String attribute2) {
    switch (category.toLowerCase()) {
        case "electronics":
            return new Electronics(productId, name, price, description, 
                                 stockQuantity, attribute1, attribute2);
        case "clothing":
            return new Clothing(productId, name, price, description, 
                              stockQuantity, attribute1, attribute2);
        case "home appliance":
            return new HomeAppliance(productId, name, price, description, 
                                   stockQuantity, attribute1, attribute2);
        default:
            throw new IllegalArgumentException("Unknown category: " + category);
    }
}
```

**Product Types:**
- **Electronics**: warranty, brand
- **Clothing**: size, material
- **Home Appliance**: powerConsumption, energyRating

#### OrderFactory
**Justification:**
- Different order types have different characteristics (shipping cost, delivery time)
- Each order type requires different processing
- Factory simplifies order creation by encapsulating order type logic
- Easy to add new order types (e.g., International, Same-Day) in the future
- Client code doesn't need to know the specifics of each order type

**Implementation Details:**
```java
public static Order createOrder(String orderType, Customer customer, 
                               List<Product> products, String shippingAddress, 
                               String paymentMethod) {
    String orderId = "ORD" + (orderCounter++);
    double productTotal = products.stream()
                                .mapToDouble(Product::getPrice)
                                .sum();
    
    switch (orderType.toLowerCase()) {
        case "standard":
            return new Order(orderId, customer, products, shippingAddress, 
                           paymentMethod, "Standard", productTotal + 10.0);
        case "express":
            return new Order(orderId, customer, products, shippingAddress, 
                           paymentMethod, "Express", productTotal + 25.0);
        default:
            throw new IllegalArgumentException("Unknown order type");
    }
}
```

**Order Types:**
- **Standard**: $10 shipping, 5-7 business days
- **Express**: $25 shipping, 1-2 business days

### Usage Example
```java
// Create products using Factory
Product laptop = ProductFactory.createProduct("Electronics", "E001", 
    "Laptop", 999.99, "High-end laptop", 10, "2 Years", "Dell");

Product shirt = ProductFactory.createProduct("Clothing", "C001", 
    "T-Shirt", 29.99, "Cotton shirt", 50, "M", "Cotton");

// Create orders using Factory
Order standardOrder = OrderFactory.createStandardOrder(customer, 
    products, address, "Credit Card");

Order expressOrder = OrderFactory.createExpressOrder(customer, 
    products, address, "PayPal");
```

---

## Prototype Pattern

### Implementation Files
- `src/patterns/prototype/ProductPrototype.java`
- `src/models/Product.java` (implements Prototype)

### Purpose
Creates new objects by copying existing objects (prototypes) rather than creating from scratch.

### Why Used in E-commerce System

**Justification:**
- Products often have similar characteristics (same brand, similar specs)
- Creating products from scratch each time is inefficient
- Useful for creating product variants (different colors, sizes) without reinitializing all fields
- In e-commerce, product templates can be cloned and modified
- Reduces complexity when creating similar products

**Implementation Details:**
```java
// Interface
public interface ProductPrototype extends Cloneable {
    ProductPrototype clone() throws CloneNotSupportedException;
}

// Product base class
public abstract class Product implements ProductPrototype {
    @Override
    public Product clone() throws CloneNotSupportedException {
        return (Product) super.clone();
    }
}

// Concrete product
public class Electronics extends Product {
    @Override
    public Electronics clone() throws CloneNotSupportedException {
        return (Electronics) super.clone();
    }
}
```

**Key Features:**
- Shallow copy for efficiency (products don't have nested objects)
- Each product type overrides clone() for type safety
- Implements Java's Cloneable interface
- Throws CloneNotSupportedException if needed

### Usage Example
```java
// Create original product
Electronics originalLaptop = new Electronics("E001", "Laptop", 999.99, 
    "Dell XPS", 10, "2 Years", "Dell");

// Clone for a variant
Electronics clonedLaptop = originalLaptop.clone();

// Modify the clone
clonedLaptop.setProductId("E002");
clonedLaptop.setName("Laptop - Silver Edition");
clonedLaptop.setPrice(1099.99);

// Original remains unchanged
// This is much faster than creating from scratch
```

**Use Cases:**
- Creating product variants (different colors, sizes)
- Duplicating product templates
- Copying products with minor modifications
- Efficient object creation when initialization is expensive

---

## Builder Pattern

### Implementation Files
- `src/patterns/builder/OrderBuilder.java`

### Purpose
Constructs complex objects step by step, separating construction from representation.

### Why Used in E-commerce System

**Justification:**
- Order objects have many parameters (customer, products, address, payment, type, etc.)
- Traditional constructors with many parameters are difficult to use and error-prone
- The order of parameters can be confusing
- Some parameters are optional
- Builder pattern provides a fluent, readable API for constructing orders
- Allows constructing orders in any order with clear, named methods
- Validates the order before creating it

**Implementation Details:**
```java
public class OrderBuilder {
    private String orderId;
    private Customer customer;
    private List<Product> products;
    private String shippingAddress;
    private String paymentMethod;
    private String orderType = "Standard"; // Default value
    
    public OrderBuilder setCustomer(Customer customer) {
        this.customer = customer;
        return this; // Method chaining
    }
    
    public OrderBuilder addProduct(Product product) {
        this.products.add(product);
        return this;
    }
    
    public Order build() {
        validate(); // Ensure all required fields are set
        totalAmount = calculateTotalAmount();
        return new Order(orderId, customer, products, shippingAddress, 
                        paymentMethod, orderType, totalAmount);
    }
}
```

**Key Features:**
- Fluent interface (method chaining)
- Step-by-step construction
- Default values for optional parameters
- Validation before object creation
- Clear, readable code
- Auto-generated order IDs

### Usage Example
```java
// Fluent API - very readable
Order order = new OrderBuilder()
    .setCustomer(customer)
    .addProduct(laptop)
    .addProduct(mouse)
    .addProduct(keyboard)
    .setShippingAddress("123 Main St")
    .setPaymentMethod("Credit Card")
    .setOrderType("Express")
    .build();

// Compare with constructor (hard to read):
// Order order = new Order("ORD123", customer, products, "123 Main St", 
//                        "Credit Card", "Express", 1234.56);
```

**Benefits:**
- No telescoping constructors
- Clear parameter names
- Optional parameters with defaults
- Validation logic in one place
- Immutable objects after building
- Easy to add new optional parameters

---

## Proxy Pattern

### Implementation Files
- `src/patterns/proxy/PaymentProxy.java`

### Purpose
Provides a surrogate or placeholder to control access to another object.

### Why Used in E-commerce System

**Justification:**
- Payment processing is sensitive and requires validation before execution
- Direct access to PaymentGateway could lead to:
  - Invalid payment data being processed
  - Security vulnerabilities
  - Lack of logging and auditing
- Proxy pattern adds a security and validation layer
- Allows for additional features without modifying the gateway
- Separates concerns: validation in proxy, processing in gateway

**Implementation Details:**
```java
public class PaymentProxy {
    private final PaymentGateway paymentGateway;
    private Map<String, PaymentAttempt> paymentLogs;
    private Map<String, Integer> attemptCounter;
    
    public PaymentProxy() {
        this.paymentGateway = PaymentGateway.getInstance();
        this.paymentLogs = new HashMap<>();
        this.attemptCounter = new HashMap<>();
    }
    
    public String processPayment(String paymentMethod, double amount, 
                                String cardNumber, String customerName) 
                                throws PaymentException {
        // Step 1: Log attempt
        logPaymentAttempt(customerName, amount, paymentMethod);
        
        // Step 2: Check rate limiting
        checkRateLimit(customerName);
        
        // Step 3: Validate payment info
        validatePaymentInfo(paymentMethod, amount, cardNumber, customerName);
        
        // Step 4: Forward to real gateway
        return paymentGateway.processPayment(paymentMethod, amount, 
                                            cardNumber, customerName);
    }
}
```

**Features Provided by Proxy:**
1. **Validation**: 
   - Card number format validation
   - Payment amount validation (> 0, < $10,000)
   - Payment method validation
   - Customer name validation

2. **Logging**:
   - All payment attempts logged
   - Success/failure tracking
   - Audit trail for compliance

3. **Rate Limiting**:
   - Maximum 5 attempts per customer per session
   - Prevents payment spam
   - Security against brute force

4. **Access Control**:
   - Controlled access to PaymentGateway
   - Can add authentication checks
   - Easy to add authorization rules

### Usage Example
```java
// Client code uses Proxy instead of Gateway directly
PaymentProxy proxy = new PaymentProxy();

try {
    // Proxy validates before forwarding to gateway
    String txnId = proxy.processPayment("Credit Card", 100.0, 
                                       "1234567890123456", "John Doe");
    System.out.println("Payment successful: " + txnId);
    
} catch (PaymentException e) {
    // Proxy provides detailed error messages
    System.out.println("Payment failed: " + e.getMessage());
}

// Invalid payment - caught by proxy
try {
    proxy.processPayment("Credit Card", -50.0, "invalid", "John");
} catch (PaymentException e) {
    System.out.println("Validation failed: " + e.getMessage());
}
```

**Validation Rules:**
- Card number: 13-19 digits
- Amount: > 0 and <= $10,000
- Payment method: Credit Card, Debit Card, or PayPal
- Customer name: Required, non-empty

---

## How Patterns Work Together

### Complete Order Flow

1. **Browse Products** (Factory Pattern)
   ```
   ProductFactory creates products based on category
   → Electronics, Clothing, or Home Appliance
   ```

2. **Add to Cart** (Singleton Pattern)
   ```
   CartManager.getInstance().addProduct(product, quantity)
   → Single cart instance across entire application
   ```

3. **Clone Product** (Prototype Pattern - Optional)
   ```
   Product variant = original.clone()
   variant.setName("Modified Name")
   → Efficient creation of similar products
   ```

4. **Checkout - Build Order** (Builder Pattern)
   ```
   Order order = new OrderBuilder()
       .setCustomer(customer)
       .addProducts(cartProducts)
       .setShippingAddress(address)
       .setPaymentMethod("Credit Card")
       .setOrderType("Express")
       .build()
   → Complex object constructed step-by-step
   ```

5. **Process Payment** (Proxy + Singleton Patterns)
   ```
   PaymentProxy validates → PaymentGateway.getInstance() processes
   → Secure, validated payment processing
   ```

6. **Create Order** (Factory Pattern)
   ```
   OrderFactory determines Standard vs Express
   → Appropriate shipping cost and delivery time
   ```

### Pattern Dependencies

```
CartManager (Singleton)
    ↓
OrderBuilder (Builder) ← uses products from → ProductFactory (Factory)
    ↓
PaymentProxy (Proxy)
    ↓
PaymentGateway (Singleton)
```

### Benefits of This Architecture

1. **Maintainability**: Each pattern has a clear responsibility
2. **Extensibility**: Easy to add new product types, order types, payment methods
3. **Testability**: Patterns can be tested independently
4. **Scalability**: Singleton ensures efficient resource usage
5. **Security**: Proxy adds validation layer
6. **Usability**: Builder provides clean API

---

## Design Principles Applied

### SOLID Principles

1. **Single Responsibility Principle**
   - CartManager: Only manages cart
   - PaymentProxy: Only validates payments
   - OrderBuilder: Only builds orders

2. **Open/Closed Principle**
   - Factory: Open for extension (new product types), closed for modification
   - Builder: Easy to add new optional fields

3. **Liskov Substitution Principle**
   - All products can be used wherever Product is expected
   - Polymorphism works correctly

4. **Interface Segregation Principle**
   - ProductPrototype: Small, focused interface
   - No fat interfaces

5. **Dependency Inversion Principle**
   - Depend on Product abstraction, not concrete classes
   - PaymentProxy depends on PaymentGateway interface (conceptually)

### Additional Benefits

- **DRY (Don't Repeat Yourself)**: Factory centralizes creation logic
- **Separation of Concerns**: Each pattern handles one aspect
- **Encapsulation**: Implementation details hidden
- **Loose Coupling**: Patterns reduce dependencies

---

## Testing

Run the comprehensive test suite:

```bash
javac -d bin -sourcepath src src/test/TestDesignPatterns.java
java -cp bin test.TestDesignPatterns
```

Tests verify:
- ✓ Singleton instances are identical
- ✓ Factory creates correct product types
- ✓ Prototype cloning works correctly
- ✓ Builder constructs valid orders
- ✓ Proxy validates and processes payments

---

## Summary

This E-commerce System demonstrates how design patterns solve real-world problems:

- **Singleton**: Ensures single cart and payment gateway
- **Factory**: Simplifies product and order creation
- **Prototype**: Efficient cloning of similar products
- **Builder**: Clean API for complex order construction
- **Proxy**: Secure, validated payment processing

Each pattern has a clear justification and solves a specific problem in the e-commerce domain.
