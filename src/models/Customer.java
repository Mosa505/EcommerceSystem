package models;

/**
 * Customer - Represents a customer in the e-commerce system
 * 
 * This class stores customer information including contact details and address.
 * Used in conjunction with the Order class to track customer orders.
 * 
 * @author E-commerce System Team
 */
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    /**
     * Constructor for Customer
     * 
     * @param customerId Unique customer identifier
     * @param name Customer's full name
     * @param email Customer's email address
     * @param phoneNumber Customer's phone number
     * @param address Customer's address
     */
    public Customer(String customerId, String name, String email, String phoneNumber, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, email);
    }
}
