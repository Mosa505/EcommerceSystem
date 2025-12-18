# E-commerce System - Design Patterns Demonstration

A desktop GUI Java application demonstrating 5 essential design patterns in an e-commerce context.

## Project Overview

This application implements a complete e-commerce system with product management, shopping cart, order processing, and payment handling. It showcases how design patterns can be effectively used to create maintainable, scalable, and well-structured software.

## Design Patterns Implemented

### 1. Singleton Pattern
- **CartManager**: Ensures only one shopping cart instance exists per user session, maintaining consistency across the application
- **PaymentGateway**: Provides a single point of payment processing, ensuring consistent transaction handling

### 2. Factory Pattern
- **ProductFactory**: Creates different product types (Electronics, Clothing, Home Appliances) based on category
- **OrderFactory**: Creates different order types (Standard, Express) with appropriate shipping costs and delivery times

### 3. Prototype Pattern
- **ProductPrototype**: Allows cloning of product objects for creating variants without reinitializing all properties
- Useful for creating similar products with minor variations (e.g., different colors, sizes)

### 4. Builder Pattern
- **OrderBuilder**: Constructs complex Order objects step-by-step with a fluent API
- Simplifies order creation with many optional parameters

### 5. Proxy Pattern
- **PaymentProxy**: Adds validation, logging, and security layer before payment processing
- Validates payment information, implements rate limiting, and logs all transactions

## Features

1. **Product Management**
   - Browse products by category (Electronics, Clothing, Home Appliances)
   - View detailed product information
   - Sample products pre-loaded for demonstration

2. **Shopping Cart**
   - Add products to cart with desired quantities
   - View cart contents with subtotals
   - Remove items or clear entire cart
   - Real-time total calculation

3. **Order Processing**
   - Customer information form
   - Shipping address input
   - Payment method selection
   - Order type selection (Standard/Express shipping)
   - Order summary and confirmation

4. **Payment Processing**
   - Secure payment validation through Proxy
   - Multiple payment methods (Credit Card, Debit Card, PayPal)
   - Transaction ID generation
   - Payment logging and auditing

## Package Structure

```
src/
├── main/
│   ├── Main.java                    # Application entry point
│   └── gui/
│       └── MainFrame.java           # Main GUI window (Swing)
├── models/
│   ├── Product.java                 # Abstract product base class
│   ├── Electronics.java             # Electronics product type
│   ├── Clothing.java                # Clothing product type
│   ├── HomeAppliance.java           # Home appliance product type
│   ├── Customer.java                # Customer model
│   └── Order.java                   # Order model
├── patterns/
│   ├── singleton/
│   │   ├── CartManager.java         # Singleton cart management
│   │   └── PaymentGateway.java      # Singleton payment processing
│   ├── factory/
│   │   ├── ProductFactory.java      # Factory for products
│   │   └── OrderFactory.java        # Factory for orders
│   ├── prototype/
│   │   └── ProductPrototype.java    # Prototype interface
│   ├── builder/
│   │   └── OrderBuilder.java        # Builder for orders
│   └── proxy/
│       └── PaymentProxy.java        # Proxy for payment gateway
└── services/
    └── OrderProcessor.java          # Order processing service
```

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (NetBeans, Eclipse, IntelliJ IDEA) or command line

### Using Command Line

1. **Compile the project**:
```bash
javac -d bin src/main/*.java src/main/gui/*.java src/models/*.java src/patterns/*/*.java src/services/*.java
```

2. **Run the application**:
```bash
java -cp bin main.Main
```

### Using NetBeans IDE

1. Open NetBeans IDE
2. Click "File" > "Open Project"
3. Navigate to the project directory
4. Right-click on the project and select "Run"

### Using Eclipse IDE

1. Open Eclipse IDE
2. Click "File" > "Import" > "Existing Projects into Workspace"
3. Select the project directory
4. Right-click on `Main.java` and select "Run As" > "Java Application"

## Usage Guide

1. **Welcome Screen**: Upon launching, you'll see a welcome dialog explaining the design patterns used

2. **Browse Products Tab**:
   - Select a category from the dropdown menu
   - Click on a product to view its details
   - Select quantity and click "Add to Cart"

3. **Shopping Cart Tab**:
   - View all items in your cart
   - See individual prices and subtotals
   - Remove items or clear the entire cart
   - Click "Proceed to Checkout" when ready

4. **Checkout Tab**:
   - Fill in customer information (pre-filled with sample data)
   - Enter shipping address
   - Select payment method
   - Choose shipping type (Standard or Express)
   - Enter card number (use any 16-digit number for demo)
   - Click "Place Order" to complete the purchase

5. **Order Confirmation**:
   - Review order summary
   - Confirm payment
   - Receive transaction ID and order confirmation

## Design Pattern Justifications

### Why Singleton for CartManager?
- A user should have only ONE shopping cart during their session
- Multiple cart instances would lead to data inconsistency
- Provides global access point from any part of the application

### Why Factory for Products and Orders?
- Centralizes object creation logic
- Easy to add new product categories or order types
- Client code doesn't need to know specific implementation classes
- Promotes loose coupling and follows Open/Closed Principle

### Why Prototype for Products?
- Efficient creation of similar products with minor variations
- Avoids expensive initialization when creating product variants
- Useful in e-commerce where products often have similar characteristics

### Why Builder for Orders?
- Orders have many parameters, some optional
- Traditional constructors would be difficult to use and error-prone
- Provides a fluent, readable API
- Validates order before creation

### Why Proxy for Payment Processing?
- Payment processing requires validation before execution
- Adds security layer without modifying the payment gateway
- Enables logging and auditing of all payment attempts
- Implements rate limiting to prevent abuse
- Separates validation concerns from processing logic

## Code Documentation

All classes are thoroughly documented with:
- Class-level comments explaining purpose and design patterns used
- Justifications for why each pattern is appropriate
- Method-level documentation with parameters and return values
- Implementation notes and usage examples

## Technical Details

- **Language**: Java 8+
- **GUI Framework**: Java Swing
- **Design**: MVC-inspired architecture
- **Thread Safety**: Singleton implementations use thread-safe initialization
- **Error Handling**: Comprehensive exception handling and validation

## Sample Credentials (for testing)

The application comes pre-loaded with:
- 12 sample products across 3 categories
- Default customer: John Doe (john@example.com)
- Card number: Any 16-digit number (e.g., 1234567890123456)
- Payment success rate: 90% (simulated)

## Learning Outcomes

This project demonstrates:
1. How to implement classic design patterns in a real-world application
2. When and why to use each design pattern
3. How patterns work together to create maintainable code
4. Proper Java package organization and code structure
5. GUI development with Java Swing
6. Object-oriented design principles

## Future Enhancements

Potential improvements:
- Add database persistence
- Implement user authentication
- Add product search functionality
- Include order history view
- Implement inventory management
- Add more product categories
- Support for multiple currencies
- Order tracking functionality

## Authors

E-commerce System Team - Software Engineering Project

## License

This project is created for educational purposes as part of a Software Engineering course.
