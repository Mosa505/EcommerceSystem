package main;

import main.gui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main - Application entry point for the E-commerce System
 * 
 * This is a desktop GUI Java application demonstrating 5 design patterns:
 * 1. Singleton Pattern - CartManager and PaymentGateway
 * 2. Factory Pattern - ProductFactory and OrderFactory
 * 3. Prototype Pattern - Product cloning
 * 4. Builder Pattern - OrderBuilder for complex order construction
 * 5. Proxy Pattern - PaymentProxy for secure payment processing
 * 
 * The application provides a complete e-commerce workflow:
 * - Browse products by category
 * - Add products to shopping cart
 * - Manage cart items
 * - Create and place orders
 * - Process payments securely
 * 
 * @author E-commerce System Team
 */
public class Main {
    
    /**
     * Main method - application entry point
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Print startup message
        System.out.println("=".repeat(60));
        System.out.println("E-COMMERCE SYSTEM - DESIGN PATTERNS DEMONSTRATION");
        System.out.println("=".repeat(60));
        System.out.println("\nDesign Patterns Implemented:");
        System.out.println("1. Singleton Pattern - CartManager, PaymentGateway");
        System.out.println("2. Factory Pattern - ProductFactory, OrderFactory");
        System.out.println("3. Prototype Pattern - Product cloning");
        System.out.println("4. Builder Pattern - OrderBuilder");
        System.out.println("5. Proxy Pattern - PaymentProxy");
        System.out.println("\nStarting application...\n");
        
        // Set system look and feel for better UI appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel");
        }
        
        // Launch GUI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                System.out.println("Application launched successfully!");
                System.out.println("GUI is now running...\n");
            } catch (Exception e) {
                System.err.println("Error launching application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
