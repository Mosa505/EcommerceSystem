# E-commerce System - GUI Documentation

## Application Screenshots and UI Structure

### Main Window Layout

The application features a tabbed interface with three main sections:

```
┌─────────────────────────────────────────────────────────────────┐
│  E-commerce System - Design Patterns Demo                   [_][□][X]
├─────────────────────────────────────────────────────────────────┤
│  [Browse Products] [Shopping Cart] [Checkout]                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [Current Tab Content Area]                                     │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Tab 1: Browse Products

```
┌─────────────────────────────────────────────────────────────────┐
│  Category: [All ▼]                                              │
├─────────────────────────┬───────────────────────────────────────┤
│  Products               │  Product Details                      │
│  ┌───────────────────┐  │  ┌─────────────────────────────────┐ │
│  │ Laptop - $999.99  │  │  │ Electronics: Laptop             │ │
│  │ Smartphone -$699.9│  │  │ Brand: Dell                     │ │
│  │ Headphones -$149.9│  │  │ Price: $999.99                  │ │
│  │ Tablet - $499.99  │  │  │ Warranty: 2 Years               │ │
│  │ T-Shirt - $29.99  │  │  │ Description: High-performance   │ │
│  │ Jeans - $59.99    │  │  │   laptop                        │ │
│  │ ...               │  │  │ Stock: 10                       │ │
│  │                   │  │  └─────────────────────────────────┘ │
│  │                   │  │                                       │
│  └───────────────────┘  │  Quantity: [1 ▲▼]                   │
│                         │  [  Add to Cart  ]                    │
└─────────────────────────┴───────────────────────────────────────┘
```

**Design Patterns Used:**
- **Singleton**: CartManager.getInstance() is called when adding to cart
- **Factory**: All products are created using ProductFactory.createProduct()

### Tab 2: Shopping Cart

```
┌─────────────────────────────────────────────────────────────────┐
│  Shopping Cart                                                   │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────┬──────────┬────────┬─────────┬─────────────────┐  │
│  │ Product  │ Category │ Price  │Quantity │   Subtotal      │  │
│  ├──────────┼──────────┼────────┼─────────┼─────────────────┤  │
│  │ Laptop   │Electronic│$999.99 │    1    │   $999.99       │  │
│  │ T-Shirt  │ Clothing │ $29.99 │    2    │    $59.98       │  │
│  │ Jeans    │ Clothing │ $59.99 │    1    │    $59.99       │  │
│  └──────────┴──────────┴────────┴─────────┴─────────────────┘  │
│                                                                  │
│                                         Total: $1,119.96         │
│                                                                  │
│  [Remove Selected] [Clear Cart]        [Proceed to Checkout]    │
└─────────────────────────────────────────────────────────────────┘
```

**Design Patterns Used:**
- **Singleton**: CartManager maintains the cart state
- All cart operations use the same CartManager instance

### Tab 3: Checkout

```
┌─────────────────────────────────────────────────────────────────┐
│  Checkout - Complete Your Order                                 │
├─────────────────────────────────────────────────────────────────┤
│  Customer Information                                           │
│  Name:    [John Doe                    ]                        │
│  Email:   [john@example.com            ]                        │
│  Phone:   [555-0123                    ]                        │
│                                                                  │
│  Shipping Address                                               │
│  ┌────────────────────────────────────────────────────────────┐│
│  │ 123 Main St, City, State 12345                             ││
│  │                                                            ││
│  └────────────────────────────────────────────────────────────┘│
│                                                                  │
│  Payment Information                                            │
│  Payment Method: [Credit Card ▼]                                │
│  Card Number:    [                    ]                         │
│                                                                  │
│  Shipping Options                                               │
│  Shipping Type: [Standard ($10 - 5-7 days) ▼]                  │
│                                                                  │
│                                    [Cancel]  [  Place Order  ]  │
└─────────────────────────────────────────────────────────────────┘
```

**Design Patterns Used:**
- **Builder**: OrderBuilder constructs the order step-by-step
- **Factory**: OrderFactory determines Standard vs Express order type
- **Proxy**: PaymentProxy validates and processes payment
- **Singleton**: PaymentGateway (accessed via Proxy) processes the payment

## Workflow Example

### Complete Purchase Workflow

1. **Start Application** → Welcome dialog explains design patterns
2. **Browse Products** → Select category → View product details
3. **Add to Cart** → CartManager.addProduct() (Singleton)
4. **View Cart** → CartManager displays items
5. **Checkout** → Fill customer info and payment details
6. **Place Order** → 
   - OrderBuilder constructs order (Builder Pattern)
   - PaymentProxy validates payment (Proxy Pattern)
   - PaymentGateway processes payment (Singleton Pattern)
   - OrderProcessor confirms order
   - CartManager.clear() empties cart
7. **Confirmation** → Display order ID and transaction ID

## Order Confirmation Dialog

```
┌────────────────────────────────────────┐
│  Order Successful              [X]     │
├────────────────────────────────────────┤
│                                        │
│  Order placed successfully!            │
│                                        │
│  Order ID: ORD1234                     │
│  Transaction ID: TXN5678               │
│  Total Amount: $1,134.96               │
│  Estimated Delivery: 5-7 business days │
│                                        │
│  Thank you for your order!             │
│                                        │
│              [   OK   ]                │
└────────────────────────────────────────┘
```

## Welcome Dialog

When the application starts, users see:

```
┌────────────────────────────────────────────────────────────┐
│  Welcome                                            [X]     │
├────────────────────────────────────────────────────────────┤
│  Welcome to the E-commerce System!                         │
│                                                             │
│  This application demonstrates 5 Design Patterns:          │
│                                                             │
│  1. Singleton Pattern:                                     │
│     - CartManager: Single cart instance for the session    │
│     - PaymentGateway: Single payment processor             │
│                                                             │
│  2. Factory Pattern:                                       │
│     - ProductFactory: Creates products by category         │
│     - OrderFactory: Creates Standard/Express orders        │
│                                                             │
│  3. Prototype Pattern:                                     │
│     - Products can be cloned for variants                  │
│                                                             │
│  4. Builder Pattern:                                       │
│     - OrderBuilder: Constructs complex orders step-by-step │
│                                                             │
│  5. Proxy Pattern:                                         │
│     - PaymentProxy: Validates and logs payment             │
│       transactions                                         │
│                                                             │
│  Browse products, add to cart, and place orders!           │
│                                                             │
│                        [   OK   ]                          │
└────────────────────────────────────────────────────────────┘
```

## Key Features Demonstrated

### 1. Product Browsing
- Category filtering (All, Electronics, Clothing, Home Appliance)
- Product list with live filtering
- Detailed product information display
- Add to cart with quantity selection

### 2. Shopping Cart Management
- View all cart items in table format
- Calculate subtotals and total
- Remove individual items
- Clear entire cart
- Proceed to checkout

### 3. Order Processing
- Customer information form
- Shipping address input
- Payment method selection (Credit Card, Debit Card, PayPal)
- Order type selection (Standard/Express)
- Order summary before confirmation

### 4. Payment Processing
- Card number validation
- Payment amount validation
- Payment method validation
- Rate limiting protection
- Transaction ID generation
- Success/failure feedback

## Design Pattern Integration

### How Patterns Work Together

1. **Product Creation Flow**:
   ```
   User selects category → ProductFactory creates product → 
   Display in GUI → User adds to cart → 
   CartManager (Singleton) stores product
   ```

2. **Order Placement Flow**:
   ```
   User fills form → OrderBuilder constructs order → 
   PaymentProxy validates → PaymentGateway (Singleton) processes → 
   OrderProcessor confirms → CartManager clears
   ```

3. **Product Cloning** (Prototype):
   ```
   Select product → Clone for variant → 
   Modify properties → New product created efficiently
   ```

## Console Output Example

When running the application, you'll see:

```
============================================================
E-COMMERCE SYSTEM - DESIGN PATTERNS DEMONSTRATION
============================================================

Design Patterns Implemented:
1. Singleton Pattern - CartManager, PaymentGateway
2. Factory Pattern - ProductFactory, OrderFactory
3. Prototype Pattern - Product cloning
4. Builder Pattern - OrderBuilder
5. Proxy Pattern - PaymentProxy

Starting application...

Application launched successfully!
GUI is now running...

Added 1x Laptop to cart
PaymentProxy initialized - providing secure access to PaymentGateway
=== PaymentProxy: Processing payment request ===
PaymentProxy: Logged payment attempt for John Doe
PaymentProxy: Validating payment information...
PaymentProxy: Validation successful
PaymentProxy: Validation passed, forwarding to PaymentGateway
Processing payment...
Method: Credit Card, Amount: $1009.99
Payment successful. Transaction ID: TXN1000
PaymentProxy: SUCCESS - Customer: John Doe, Transaction: TXN1000, Amount: $1009.99
PaymentProxy: Payment processed successfully
OrderProcessor: Order processed successfully. Transaction ID: TXN1000
Order confirmed: ORD2000 (Transaction: TXN1000)
Cart cleared
```

## Running the Application

### On Windows:
```batch
compile.bat
run.bat
```

### On Linux/Mac:
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

### Manual Compilation:
```bash
javac -d bin -sourcepath src src/main/Main.java src/main/gui/*.java src/models/*.java src/patterns/*/*.java src/services/*.java
java -cp bin main.Main
```

## Testing

Run the comprehensive test suite:
```bash
javac -d bin -sourcepath src src/test/TestDesignPatterns.java
java -cp bin test.TestDesignPatterns
```

This will test all 5 design patterns and verify they work correctly.
