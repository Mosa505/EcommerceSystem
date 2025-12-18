package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Order - Represents an order in the e-commerce system
 * 
 * This class is constructed using the Builder pattern (OrderBuilder).
 * It can be created using the Factory pattern (OrderFactory) for different order types.
 * 
 * ORDER TYPES:
 * - Standard Order: Regular shipping (5-7 days)
 * - Express Order: Fast shipping (1-2 days) with additional cost
 * 
 * @author E-commerce System Team
 */
public class Order {
    private String orderId;
    private Customer customer;
    private List<Product> products;
    private String shippingAddress;
    private String paymentMethod;
    private String orderType; // "Standard" or "Express"
    private double totalAmount;
    private Date orderDate;
    private String status;

    /**
     * Constructor for Order (typically used by OrderBuilder)
     * 
     * @param orderId Unique order identifier
     * @param customer Customer placing the order
     * @param products List of products in the order
     * @param shippingAddress Shipping address
     * @param paymentMethod Payment method used
     * @param orderType Type of order (Standard/Express)
     * @param totalAmount Total order amount
     */
    public Order(String orderId, Customer customer, List<Product> products, 
                String shippingAddress, String paymentMethod, String orderType, double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
        this.orderDate = new Date();
        this.status = "Pending";
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates shipping cost based on order type
     * 
     * @return Shipping cost
     */
    public double getShippingCost() {
        return "Express".equals(orderType) ? 25.0 : 10.0;
    }

    /**
     * Gets estimated delivery time based on order type
     * 
     * @return Estimated delivery time as a string
     */
    public String getEstimatedDelivery() {
        return "Express".equals(orderType) ? "1-2 business days" : "5-7 business days";
    }

    @Override
    public String toString() {
        return String.format("Order #%s - %s - $%.2f (%s)", 
                orderId, orderType, totalAmount, status);
    }
}
