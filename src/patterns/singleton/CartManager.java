package patterns.singleton;

import models.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CartManager - Singleton pattern implementation for shopping cart management
 * 
 * DESIGN PATTERN: Singleton Pattern
 * PURPOSE: Ensures only one shopping cart instance exists throughout the application session
 * 
 * JUSTIFICATION:
 * - A user should have only ONE shopping cart during their session
 * - Multiple cart instances would lead to data inconsistency
 * - Centralized cart management makes it easier to track cart state across different GUI panels
 * - Thread-safe implementation ensures proper behavior in multi-threaded environments
 * - Provides global access point to the cart from anywhere in the application
 * 
 * IMPLEMENTATION: Thread-safe lazy initialization with double-checked locking
 * 
 * @author E-commerce System Team
 */
public class CartManager {
    // Volatile keyword ensures visibility of changes across threads
    private static volatile CartManager instance;
    
    // Map to store products and their quantities
    private Map<Product, Integer> cartItems;

    /**
     * Private constructor to prevent instantiation from outside
     * This is a key element of the Singleton pattern
     */
    private CartManager() {
        cartItems = new HashMap<>();
    }

    /**
     * Thread-safe method to get the single instance of CartManager
     * Uses double-checked locking for performance optimization
     * 
     * @return The single instance of CartManager
     */
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

    /**
     * Adds a product to the cart
     * If product already exists, increases its quantity
     * 
     * @param product Product to add
     * @param quantity Quantity to add
     */
    public synchronized void addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }
        
        cartItems.put(product, cartItems.getOrDefault(product, 0) + quantity);
        System.out.println("Added " + quantity + "x " + product.getName() + " to cart");
    }

    /**
     * Removes a product from the cart completely
     * 
     * @param product Product to remove
     */
    public synchronized void removeProduct(Product product) {
        if (cartItems.remove(product) != null) {
            System.out.println("Removed " + product.getName() + " from cart");
        }
    }

    /**
     * Updates the quantity of a product in the cart
     * 
     * @param product Product to update
     * @param quantity New quantity
     */
    public synchronized void updateQuantity(Product product, int quantity) {
        if (quantity <= 0) {
            removeProduct(product);
        } else {
            cartItems.put(product, quantity);
        }
    }

    /**
     * Gets all items in the cart
     * 
     * @return Map of products and their quantities
     */
    public synchronized Map<Product, Integer> getCartItems() {
        return new HashMap<>(cartItems);
    }

    /**
     * Gets the list of all products in the cart
     * 
     * @return List of products
     */
    public synchronized List<Product> getProducts() {
        return new ArrayList<>(cartItems.keySet());
    }

    /**
     * Calculates the total price of all items in the cart
     * 
     * @return Total cart value
     */
    public synchronized double getTotalPrice() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    /**
     * Gets the total number of items in the cart
     * 
     * @return Total item count
     */
    public synchronized int getItemCount() {
        int count = 0;
        for (int quantity : cartItems.values()) {
            count += quantity;
        }
        return count;
    }

    /**
     * Clears all items from the cart
     * Typically called after successful order placement
     */
    public synchronized void clear() {
        cartItems.clear();
        System.out.println("Cart cleared");
    }

    /**
     * Checks if the cart is empty
     * 
     * @return true if cart is empty, false otherwise
     */
    public synchronized boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
