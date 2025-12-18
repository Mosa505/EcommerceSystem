package models;

/**
 * Clothing - Product type for clothing items
 * 
 * This class extends Product and represents clothing products in the e-commerce system.
 * Created by ProductFactory using the Factory pattern.
 * 
 * SPECIFIC ATTRIBUTES:
 * - size: Size of the clothing (S, M, L, XL, etc.)
 * - material: Material/fabric of the clothing
 * 
 * @author E-commerce System Team
 */
public class Clothing extends Product {
    private String size;
    private String material;

    /**
     * Constructor for Clothing
     * 
     * @param productId Unique identifier
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Available stock
     * @param size Clothing size
     * @param material Fabric material
     */
    public Clothing(String productId, String name, double price, String description, 
                   int stockQuantity, String size, String material) {
        super(productId, name, price, description, "Clothing", stockQuantity);
        this.size = size;
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getDetails() {
        return String.format("Clothing: %s\nSize: %s\nMaterial: %s\nPrice: $%.2f\nDescription: %s\nStock: %d",
                name, size, material, price, description, stockQuantity);
    }

    @Override
    public Clothing clone() throws CloneNotSupportedException {
        return (Clothing) super.clone();
    }
}
