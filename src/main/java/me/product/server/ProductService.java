package me.product.server;

import me.product.api.Product;

import java.util.List;

/**
 * Interface for product storage.
 */
public interface ProductService
{
    /**
     * Retrieves a list of all products.
     *
     * @return a list of all products.
     */
    List<ImmutableProduct> getProducts();

    /**
     * Adds a new product. The ID field may be ignored depending on the implementation.
     *
     * @param product the product to add.
     * @return the product as it was added (may have a different ID).
     * @throws ProductException in case of error.
     */
    ImmutableProduct addProduct(Product product) throws ProductException;

    /**
     * Searches for a product by its id, returning null if not found.
     *
     * @param id the product id.
     * @return the product if known, null otherwise.
     */
    ImmutableProduct findProductById(int id);
}
