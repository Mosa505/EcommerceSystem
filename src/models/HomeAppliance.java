package models;

/**
 * HomeAppliance - Product type for home appliance items
 * 
 * This class extends Product and represents home appliance products in the e-commerce system.
 * Created by ProductFactory using the Factory pattern.
 * 
 * SPECIFIC ATTRIBUTES:
 * - powerConsumption: Power consumption rating (e.g., "1500W")
 * - energyRating: Energy efficiency rating (e.g., "A++", "A+", "A")
 * 
 * @author E-commerce System Team
 */
public class HomeAppliance extends Product {
    private String powerConsumption;
    private String energyRating;

    /**
     * Constructor for HomeAppliance
     * 
     * @param productId Unique identifier
     * @param name Product name
     * @param price Product price
     * @param description Product description
     * @param stockQuantity Available stock
     * @param powerConsumption Power consumption rating
     * @param energyRating Energy efficiency rating
     */
    public HomeAppliance(String productId, String name, double price, String description, 
                        int stockQuantity, String powerConsumption, String energyRating) {
        super(productId, name, price, description, "Home Appliance", stockQuantity);
        this.powerConsumption = powerConsumption;
        this.energyRating = energyRating;
    }

    public String getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(String powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public String getEnergyRating() {
        return energyRating;
    }

    public void setEnergyRating(String energyRating) {
        this.energyRating = energyRating;
    }

    @Override
    public String getDetails() {
        return String.format("Home Appliance: %s\nPower: %s\nEnergy Rating: %s\nPrice: $%.2f\nDescription: %s\nStock: %d",
                name, powerConsumption, energyRating, price, description, stockQuantity);
    }

    @Override
    public HomeAppliance clone() throws CloneNotSupportedException {
        return (HomeAppliance) super.clone();
    }
}
