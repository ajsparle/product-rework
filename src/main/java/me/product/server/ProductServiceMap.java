package me.product.server;

import me.product.api.Product;
import me.product.api.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Memory implementation of ProductService.
 * It uses copy on read since the map stores mutable objects.
 */
public class ProductServiceMap implements ProductService
{
    private final ConcurrentHashMap<Integer, ImmutableProduct> productMap = new ConcurrentHashMap<>();
    private final AtomicInteger lastId = new AtomicInteger();

    public ProductServiceMap() throws ProductException
    {
        addProduct(new Product(0, "Chrome Toaster", 100_00));
        addProduct(new Product(0, "Copper Kettle", 49_00));
        addProduct(new Product(0, "Mixing Bowl", 20_00));
    }

    @Override
    public synchronized List<ImmutableProduct> getProducts()
    {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public synchronized ImmutableProduct addProduct(Product product) throws ProductException
    {
        int key = product.getId();
        if (key <= 0)
        {
            // auto-assign key.
            key = lastId.incrementAndGet();
        }

        if (productMap.containsKey(key))
        {
            throw new ProductException("Product ID in use: " + key);
        }

        ImmutableProduct added = new ImmutableProduct(key, product.getProductName(), product.getPriceCents());

        productMap.put(key, added);

        return added;
    }
}
