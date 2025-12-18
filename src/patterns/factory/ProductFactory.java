package patterns.factory;

import models.*;

/**
 * ProductFactory - Factory pattern implementation for product creation
 * 
 * DESIGN PATTERN: Factory Pattern
 * PURPOSE: Provides a centralized way to create different types of products
 * 
 * JUSTIFICATION:
 * - In e-commerce, products have different categories with specific attributes
 * - Direct instantiation of product classes would scatter object creation logic
 * - Factory pattern centralizes product creation logic
 * - Makes it easy to add new product types without modifying existing code
 * - Encapsulates the complexity of deciding which product class to instantiate
 * - Promotes loose coupling - client code doesn't need to know specific product classes
 * 
 * BENEFITS:
 * - Single Responsibility: Product creation logic is in one place
 * - Open/Closed Principle: Easy to extend with new product types
 * - Simplifies client code - just specify the category
 * 
 * @author E-commerce System Team
 */
public class ProductFactory {
    
    /**
     * Creates a product based on category
     * 
     * @param category Product category ("Electronics", "Clothing", "Home Appliance")
     * @param productId Unique product identifier
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Available stock
     * @param attribute1 Category-specific attribute (warranty/size/powerConsumption)
     * @param attribute2 Category-specific attribute (brand/material/energyRating)
     * @return Created product instance
     * @throws IllegalArgumentException if category is unknown
     */
    public static Product createProduct(String category, String productId, String name, 
                                       double price, String description, int stockQuantity,
                                       String attribute1, String attribute2) {
        
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        // Factory decides which product type to create based on category
        switch (category.toLowerCase()) {
            case "electronics":
                // attribute1 = warranty, attribute2 = brand
                return new Electronics(productId, name, price, description, stockQuantity, 
                                     attribute1, attribute2);
            
            case "clothing":
                // attribute1 = size, attribute2 = material
                return new Clothing(productId, name, price, description, stockQuantity, 
                                  attribute1, attribute2);
            
            case "home appliance":
            case "homeappliance":
                // attribute1 = powerConsumption, attribute2 = energyRating
                return new HomeAppliance(productId, name, price, description, stockQuantity, 
                                       attribute1, attribute2);
            
            default:
                throw new IllegalArgumentException("Unknown product category: " + category);
        }
    }

    /**
     * Creates a basic electronics product with default attributes
     * Convenience method for quick product creation
     * 
     * @param productId Product ID
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Stock quantity
     * @return Electronics product
     */
    public static Electronics createElectronics(String productId, String name, double price, 
                                               String description, int stockQuantity) {
        return new Electronics(productId, name, price, description, stockQuantity, 
                             "1 Year", "Generic");
    }

    /**
     * Creates a basic clothing product with default attributes
     * 
     * @param productId Product ID
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Stock quantity
     * @return Clothing product
     */
    public static Clothing createClothing(String productId, String name, double price, 
                                         String description, int stockQuantity) {
        return new Clothing(productId, name, price, description, stockQuantity, 
                          "M", "Cotton");
    }

    /**
     * Creates a basic home appliance product with default attributes
     * 
     * @param productId Product ID
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Stock quantity
     * @return HomeAppliance product
     */
    public static HomeAppliance createHomeAppliance(String productId, String name, double price, 
                                                   String description, int stockQuantity) {
        return new HomeAppliance(productId, name, price, description, stockQuantity, 
                               "1000W", "A+");
    }
}
