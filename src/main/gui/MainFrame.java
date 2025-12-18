package main.gui;

import models.*;
import patterns.singleton.CartManager;
import patterns.factory.ProductFactory;
import patterns.factory.OrderFactory;
import patterns.builder.OrderBuilder;
import patterns.proxy.PaymentProxy;
import patterns.singleton.PaymentGateway.PaymentException;
import services.OrderProcessor;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MainFrame - Main GUI window for the E-commerce System
 * 
 * This Swing-based GUI provides a complete interface for:
 * - Product browsing by category
 * - Shopping cart management
 * - Order creation and processing
 * - Payment processing
 * 
 * DESIGN PATTERNS USED:
 * - Singleton: CartManager for cart management
 * - Factory: ProductFactory for product creation, OrderFactory for order types
 * - Builder: OrderBuilder for complex order construction
 * - Proxy: PaymentProxy for secure payment processing
 * - Prototype: Products can be cloned for variants
 * 
 * @author E-commerce System Team
 */
public class MainFrame extends JFrame {
    
    // Singleton instances
    private CartManager cartManager;
    private OrderProcessor orderProcessor;
    
    // Sample data
    private List<Product> allProducts;
    private Customer currentCustomer;
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private JPanel productsPanel;
    private JPanel cartPanel;
    private JPanel orderPanel;
    
    // Product browsing components
    private JComboBox<String> categoryComboBox;
    private JList<Product> productList;
    private DefaultListModel<Product> productListModel;
    private JTextArea productDetailsArea;
    private JSpinner quantitySpinner;
    
    // Cart components
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel cartTotalLabel;
    
    // Order components
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField customerPhoneField;
    private JTextArea shippingAddressArea;
    private JComboBox<String> paymentMethodComboBox;
    private JComboBox<String> orderTypeComboBox;
    private JTextField cardNumberField;

    /**
     * Constructor initializes the main frame
     */
    public MainFrame() {
        // Initialize singletons and services
        cartManager = CartManager.getInstance();
        orderProcessor = new OrderProcessor();
        
        // Create sample data
        initializeSampleData();
        
        // Set up the frame
        setTitle("E-commerce System - Design Patterns Demo");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create the UI
        createUI();
        
        // Show welcome message
        showWelcomeDialog();
    }

    /**
     * Creates the user interface
     */
    private void createUI() {
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels
        productsPanel = createProductsPanel();
        cartPanel = createCartPanel();
        orderPanel = createOrderPanel();
        
        // Add tabs
        tabbedPane.addTab("Browse Products", new ImageIcon(), productsPanel, "Browse and add products to cart");
        tabbedPane.addTab("Shopping Cart", new ImageIcon(), cartPanel, "View and manage your cart");
        tabbedPane.addTab("Checkout", new ImageIcon(), orderPanel, "Complete your order");
        
        // Add to frame
        add(tabbedPane);
    }

    /**
     * Creates the products browsing panel
     */
    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top panel - Category filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Category:"));
        
        String[] categories = {"All", "Electronics", "Clothing", "Home Appliance"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.addActionListener(e -> filterProductsByCategory());
        topPanel.add(categoryComboBox);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Product list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Left - Product list
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displayProductDetails();
            }
        });
        
        JScrollPane listScroll = new JScrollPane(productList);
        listScroll.setBorder(new TitledBorder("Products"));
        listScroll.setPreferredSize(new Dimension(300, 400));
        
        // Right - Product details
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        
        productDetailsArea = new JTextArea();
        productDetailsArea.setEditable(false);
        productDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        productDetailsArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane detailsScroll = new JScrollPane(productDetailsArea);
        detailsScroll.setBorder(new TitledBorder("Product Details"));
        
        rightPanel.add(detailsScroll, BorderLayout.CENTER);
        
        // Add to cart section
        JPanel addToCartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addToCartPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        addToCartPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        addToCartPanel.add(quantitySpinner);
        
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(new Color(46, 125, 50));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        addToCartButton.addActionListener(e -> addToCart());
        addToCartPanel.add(addToCartButton);
        
        rightPanel.add(addToCartPanel, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(listScroll);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(300);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        // Load all products initially
        filterProductsByCategory();
        
        return panel;
    }

    /**
     * Creates the shopping cart panel
     */
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Cart table
        String[] columnNames = {"Product", "Category", "Price", "Quantity", "Subtotal"};
        cartTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(cartTable);
        panel.add(tableScroll, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Total
        cartTotalLabel = new JLabel("Total: $0.00");
        cartTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cartTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bottomPanel.add(cartTotalLabel, BorderLayout.NORTH);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> removeFromCart());
        buttonPanel.add(removeButton);
        
        JButton clearButton = new JButton("Clear Cart");
        clearButton.addActionListener(e -> clearCart());
        buttonPanel.add(clearButton);
        
        JButton checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setBackground(new Color(25, 118, 210));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> proceedToCheckout());
        buttonPanel.add(checkoutButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Creates the order/checkout panel
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Checkout - Complete Your Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // Customer Information Section
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel customerLabel = new JLabel("Customer Information");
        customerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(customerLabel, gbc);
        gbc.gridwidth = 1;
        
        // Customer Name
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        customerNameField = new JTextField(20);
        formPanel.add(customerNameField, gbc);
        row++;
        
        // Customer Email
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        customerEmailField = new JTextField(20);
        formPanel.add(customerEmailField, gbc);
        row++;
        
        // Customer Phone
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        customerPhoneField = new JTextField(20);
        formPanel.add(customerPhoneField, gbc);
        row++;
        
        // Shipping Address Section
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel shippingLabel = new JLabel("Shipping Address");
        shippingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(shippingLabel, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        shippingAddressArea = new JTextArea(3, 20);
        shippingAddressArea.setLineWrap(true);
        shippingAddressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(shippingAddressArea);
        formPanel.add(addressScroll, gbc);
        gbc.gridwidth = 1;
        row++;
        
        // Payment Information Section
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel paymentLabel = new JLabel("Payment Information");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(paymentLabel, gbc);
        gbc.gridwidth = 1;
        
        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        String[] paymentMethods = {"Credit Card", "Debit Card", "PayPal"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        formPanel.add(paymentMethodComboBox, gbc);
        row++;
        
        // Card Number
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        cardNumberField = new JTextField(20);
        formPanel.add(cardNumberField, gbc);
        row++;
        
        // Order Type Section
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel orderTypeLabel = new JLabel("Shipping Options");
        orderTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(orderTypeLabel, gbc);
        gbc.gridwidth = 1;
        
        // Order Type
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Shipping Type:"), gbc);
        gbc.gridx = 1;
        JPanel orderTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] orderTypes = {"Standard ($10 - 5-7 days)", "Express ($25 - 1-2 days)"};
        orderTypeComboBox = new JComboBox<>(orderTypes);
        orderTypePanel.add(orderTypeComboBox);
        formPanel.add(orderTypePanel, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        buttonPanel.add(cancelButton);
        
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBackground(new Color(76, 175, 80));
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.setFocusPainted(false);
        placeOrderButton.addActionListener(e -> placeOrder());
        buttonPanel.add(placeOrderButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Initializes sample product data using the Factory pattern
     */
    private void initializeSampleData() {
        allProducts = new ArrayList<>();
        
        // Create Electronics using ProductFactory
        allProducts.add(ProductFactory.createProduct("Electronics", "E001", "Laptop", 999.99, 
            "High-performance laptop", 10, "2 Years", "Dell"));
        allProducts.add(ProductFactory.createProduct("Electronics", "E002", "Smartphone", 699.99, 
            "Latest smartphone", 15, "1 Year", "Samsung"));
        allProducts.add(ProductFactory.createProduct("Electronics", "E003", "Headphones", 149.99, 
            "Wireless headphones", 20, "1 Year", "Sony"));
        allProducts.add(ProductFactory.createProduct("Electronics", "E004", "Tablet", 499.99, 
            "10-inch tablet", 12, "1 Year", "Apple"));
        
        // Create Clothing using ProductFactory
        allProducts.add(ProductFactory.createProduct("Clothing", "C001", "T-Shirt", 29.99, 
            "Cotton t-shirt", 50, "M", "Cotton"));
        allProducts.add(ProductFactory.createProduct("Clothing", "C002", "Jeans", 59.99, 
            "Denim jeans", 30, "32", "Denim"));
        allProducts.add(ProductFactory.createProduct("Clothing", "C003", "Jacket", 89.99, 
            "Winter jacket", 20, "L", "Polyester"));
        allProducts.add(ProductFactory.createProduct("Clothing", "C004", "Sneakers", 79.99, 
            "Running shoes", 25, "10", "Synthetic"));
        
        // Create Home Appliances using ProductFactory
        allProducts.add(ProductFactory.createProduct("Home Appliance", "H001", "Microwave", 199.99, 
            "1000W microwave", 8, "1000W", "A+"));
        allProducts.add(ProductFactory.createProduct("Home Appliance", "H002", "Coffee Maker", 79.99, 
            "12-cup coffee maker", 15, "900W", "A"));
        allProducts.add(ProductFactory.createProduct("Home Appliance", "H003", "Vacuum Cleaner", 249.99, 
            "Powerful vacuum", 10, "1500W", "A++"));
        allProducts.add(ProductFactory.createProduct("Home Appliance", "H004", "Blender", 59.99, 
            "High-speed blender", 18, "600W", "A+"));
        
        // Create a default customer
        currentCustomer = new Customer("CUST001", "John Doe", "john@example.com", 
            "555-0123", "123 Main St, City, State 12345");
    }

    /**
     * Filters products by selected category
     */
    private void filterProductsByCategory() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        productListModel.clear();
        
        for (Product product : allProducts) {
            if ("All".equals(selectedCategory) || product.getCategory().equals(selectedCategory)) {
                productListModel.addElement(product);
            }
        }
    }

    /**
     * Displays selected product details
     */
    private void displayProductDetails() {
        Product selected = productList.getSelectedValue();
        if (selected != null) {
            productDetailsArea.setText(selected.getDetails());
        }
    }

    /**
     * Adds selected product to cart using Singleton CartManager
     */
    private void addToCart() {
        Product selected = productList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a product first.", 
                "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantity = (Integer) quantitySpinner.getValue();
        
        // Check stock
        if (quantity > selected.getStockQuantity()) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient stock. Available: " + selected.getStockQuantity(), 
                "Stock Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Add to cart using Singleton CartManager
        cartManager.addProduct(selected, quantity);
        
        JOptionPane.showMessageDialog(this, 
            quantity + "x " + selected.getName() + " added to cart!", 
            "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
        
        // Switch to cart tab to show the update
        updateCartDisplay();
    }

    /**
     * Updates the cart display table
     */
    private void updateCartDisplay() {
        // Clear existing rows
        cartTableModel.setRowCount(0);
        
        // Get cart items from Singleton CartManager
        Map<Product, Integer> cartItems = cartManager.getCartItems();
        
        // Add rows
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double subtotal = product.getPrice() * quantity;
            
            cartTableModel.addRow(new Object[]{
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                quantity,
                String.format("$%.2f", subtotal)
            });
        }
        
        // Update total
        double total = cartManager.getTotalPrice();
        cartTotalLabel.setText(String.format("Total: $%.2f", total));
    }

    /**
     * Removes selected item from cart
     */
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", 
                "No Item Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get product from cart
        List<Product> products = cartManager.getProducts();
        if (selectedRow < products.size()) {
            Product product = products.get(selectedRow);
            cartManager.removeProduct(product);
            updateCartDisplay();
        }
    }

    /**
     * Clears the entire cart
     */
    private void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear the cart?", 
            "Clear Cart", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartManager.clear();
            updateCartDisplay();
        }
    }

    /**
     * Proceeds to checkout tab
     */
    private void proceedToCheckout() {
        if (cartManager.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", 
                "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Pre-fill customer information
        customerNameField.setText(currentCustomer.getName());
        customerEmailField.setText(currentCustomer.getEmail());
        customerPhoneField.setText(currentCustomer.getPhoneNumber());
        shippingAddressArea.setText(currentCustomer.getAddress());
        
        // Switch to checkout tab
        tabbedPane.setSelectedIndex(2);
    }

    /**
     * Places the order using Builder pattern and processes payment through Proxy
     */
    private void placeOrder() {
        try {
            // Validate form
            if (customerNameField.getText().trim().isEmpty() ||
                customerEmailField.getText().trim().isEmpty() ||
                customerPhoneField.getText().trim().isEmpty() ||
                shippingAddressArea.getText().trim().isEmpty() ||
                cardNumberField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all required fields.", 
                    "Incomplete Information", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cartManager.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!", 
                    "Empty Cart", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create/update customer
            Customer customer = new Customer(
                "CUST" + System.currentTimeMillis(),
                customerNameField.getText().trim(),
                customerEmailField.getText().trim(),
                customerPhoneField.getText().trim(),
                shippingAddressArea.getText().trim()
            );
            
            // Get order type
            String orderTypeSelection = (String) orderTypeComboBox.getSelectedItem();
            String orderType = orderTypeSelection.startsWith("Standard") ? "Standard" : "Express";
            
            // Build order using Builder pattern
            OrderBuilder builder = new OrderBuilder();
            Order order = builder
                .setCustomer(customer)
                .setProducts(cartManager.getProducts())
                .setShippingAddress(shippingAddressArea.getText().trim())
                .setPaymentMethod((String) paymentMethodComboBox.getSelectedItem())
                .setOrderType(orderType)
                .build();
            
            // Show confirmation dialog
            String message = String.format(
                "Order Summary:\n\n" +
                "Items: %d\n" +
                "Subtotal: $%.2f\n" +
                "Shipping (%s): $%.2f\n" +
                "Total: $%.2f\n\n" +
                "Estimated Delivery: %s\n\n" +
                "Proceed with payment?",
                cartManager.getItemCount(),
                cartManager.getTotalPrice(),
                orderType,
                order.getShippingCost(),
                order.getTotalAmount(),
                order.getEstimatedDelivery()
            );
            
            int confirm = JOptionPane.showConfirmDialog(this, message, 
                "Confirm Order", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Process payment through OrderProcessor (which uses PaymentProxy)
            String transactionId = orderProcessor.processOrder(order, cardNumberField.getText().trim());
            
            // Confirm order
            orderProcessor.confirmOrder(order, transactionId);
            
            // Clear cart
            orderProcessor.clearCart();
            updateCartDisplay();
            
            // Show success message
            String successMessage = String.format(
                "Order placed successfully!\n\n" +
                "Order ID: %s\n" +
                "Transaction ID: %s\n" +
                "Total Amount: $%.2f\n" +
                "Estimated Delivery: %s\n\n" +
                "Thank you for your order!",
                order.getOrderId(),
                transactionId,
                order.getTotalAmount(),
                order.getEstimatedDelivery()
            );
            
            JOptionPane.showMessageDialog(this, successMessage, 
                "Order Successful", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            clearOrderForm();
            
            // Go back to products tab
            tabbedPane.setSelectedIndex(0);
            
        } catch (PaymentException ex) {
            JOptionPane.showMessageDialog(this, 
                "Payment failed: " + ex.getMessage(), 
                "Payment Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error placing order: " + ex.getMessage(), 
                "Order Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Clears the order form
     */
    private void clearOrderForm() {
        cardNumberField.setText("");
        orderTypeComboBox.setSelectedIndex(0);
    }

    /**
     * Shows a welcome dialog explaining the design patterns
     */
    private void showWelcomeDialog() {
        String message = "Welcome to the E-commerce System!\n\n" +
            "This application demonstrates 5 Design Patterns:\n\n" +
            "1. Singleton Pattern:\n" +
            "   - CartManager: Single cart instance for the session\n" +
            "   - PaymentGateway: Single payment processor\n\n" +
            "2. Factory Pattern:\n" +
            "   - ProductFactory: Creates products by category\n" +
            "   - OrderFactory: Creates Standard/Express orders\n\n" +
            "3. Prototype Pattern:\n" +
            "   - Products can be cloned for variants\n\n" +
            "4. Builder Pattern:\n" +
            "   - OrderBuilder: Constructs complex orders step-by-step\n\n" +
            "5. Proxy Pattern:\n" +
            "   - PaymentProxy: Validates and logs payment transactions\n\n" +
            "Browse products, add to cart, and place orders!";
        
        JOptionPane.showMessageDialog(this, message, 
            "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }
}
