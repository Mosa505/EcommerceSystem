package patterns.prototype;

/**
 * ProductPrototype Interface - Implements the Prototype Design Pattern
 * 
 * DESIGN PATTERN: Prototype Pattern
 * PURPOSE: Allows cloning of product objects to create new products based on existing templates
 * 
 * JUSTIFICATION:
 * - In an e-commerce system, products often have similar characteristics (e.g., same brand, 
 *   similar specifications but different colors/sizes)
 * - Creating products from scratch each time is inefficient
 * - The Prototype pattern allows quick duplication of products with minor variations
 * - This is especially useful for creating product variants without reinitializing all fields
 * 
 * @author E-commerce System Team
 */
public interface ProductPrototype extends Cloneable {
    /**
     * Creates and returns a copy of this product
     * 
     * @return A cloned copy of the product
     * @throws CloneNotSupportedException if cloning is not supported
     */
    ProductPrototype clone() throws CloneNotSupportedException;
}
