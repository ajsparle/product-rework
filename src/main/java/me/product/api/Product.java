package me.product.api;

import lombok.Data;

/**
 * Globally visible object describing a product.
 */
@Data
public class Product
{
    /**
     * Unique identifier of the product.
     */
    private int id;

    /**
     * Name of the product.
     */
    private String productName;

    /**
     * Price of the product in cents.
     */
    private int priceCents;

    /**
     * Default constructor
     */
    public Product()
    {
        // default
    }

    /**
     * Copy constructor.
     * @param product product to copy.
     */
    public Product(Product product)
    {
        this.id = product.id;
        this.productName = product.productName;
        this.priceCents = product.priceCents;
    }

    /**
     * Initialising constructor.
     */
    public Product(int id, String productName, int priceCents)
    {
        this.id = id;
        this.productName = productName;
        this.priceCents = priceCents;
    }
}
