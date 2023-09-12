package me.product.server;

import lombok.Data;
import me.product.api.Product;

@Data
public class ImmutableProduct
{
    /**
     * Unique identifier of the product.
     */
    private final int id;

    /**
     * Name of the product.
     */
    private final String productName;

    /**
     * Price of the product in cents.
     */
    private final int priceCents;

    public Product asProduct()
    {
        return new Product(id, productName, priceCents);
    }

}
