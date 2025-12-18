package models;

import patterns.prototype.ProductPrototype;

/**
 * Product - Abstract base class for all product types
 * 
 * This class implements the ProductPrototype interface to support the Prototype pattern.
 * It serves as the base class for all product categories in the e-commerce system.
 * 
 * KEY FEATURES:
 * - Implements Prototype pattern for product cloning
 * - Provides common product attributes (ID, name, price, description, category)
 * - Serves as base for Factory pattern implementation
 * 
 * @author E-commerce System Team
 */
public abstract class Product implements ProductPrototype {
    protected String productId;
    protected String name;
    protected double price;
    protected String description;
    protected String category;
    protected int stockQuantity;

    /**
     * Constructor for Product
     * 
     * @param productId Unique identifier for the product
     * @param name Name of the product
     * @param price Price of the product
     * @param description Description of the product
     * @param category Category of the product
     * @param stockQuantity Available stock quantity
     */
    public Product(String productId, String name, double price, String description, 
                   String category, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    /**
     * Clone method implementation for Prototype pattern
     * Creates a deep copy of the product
     * 
     * @return A cloned copy of this product
     * @throws CloneNotSupportedException if cloning fails
     */
    @Override
    public Product clone() throws CloneNotSupportedException {
        return (Product) super.clone();
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Abstract method to get product details
     * Each product type implements this to return specific details
     * 
     * @return String containing product details
     */
    public abstract String getDetails();

    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s)", name, price, category);
    }
}
