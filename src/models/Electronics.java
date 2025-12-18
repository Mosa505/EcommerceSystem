package models;

/**
 * Electronics - Product type for electronic items
 * 
 * This class extends Product and represents electronic products in the e-commerce system.
 * Created by ProductFactory using the Factory pattern.
 * 
 * SPECIFIC ATTRIBUTES:
 * - warranty: Warranty period for the electronic product
 * - brand: Brand/manufacturer of the electronic product
 * 
 * @author E-commerce System Team
 */
public class Electronics extends Product {
    private String warranty;
    private String brand;

    /**
     * Constructor for Electronics
     * 
     * @param productId Unique identifier
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Available stock
     * @param warranty Warranty period (e.g., "1 year", "2 years")
     * @param brand Brand name
     */
    public Electronics(String productId, String name, double price, String description, 
                      int stockQuantity, String warranty, String brand) {
        super(productId, name, price, description, "Electronics", stockQuantity);
        this.warranty = warranty;
        this.brand = brand;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String getDetails() {
        return String.format("Electronics: %s\nBrand: %s\nPrice: $%.2f\nWarranty: %s\nDescription: %s\nStock: %d",
                name, brand, price, warranty, description, stockQuantity);
    }

    @Override
    public Electronics clone() throws CloneNotSupportedException {
        return (Electronics) super.clone();
    }
}
