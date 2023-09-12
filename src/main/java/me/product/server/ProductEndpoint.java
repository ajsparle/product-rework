package me.product.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.product.api.LineItem;
import me.product.api.Product;
import me.product.api.ProductResponse;
import me.product.api.SalesRecord;
import spark.Spark;

/**
 * Main class to create run API endpoint.
 */
public class ProductEndpoint
{
    private final ProductService productService;

    public static void main(String[] args) throws ProductException
    {
        ProductService productService = new ProductServiceMap();

        ProductEndpoint productEndpoint = new ProductEndpoint(productService);
        productEndpoint.initialiseApi();
    }

    /**
     * Create an endpoint using the supplied product service.
     *
     * @param productService the product service.
     */
    public ProductEndpoint(ProductService productService)
    {
        this.productService = productService;
    }

    public void initialiseApi()
    {

        Spark.get("/products", (request, response) ->
        {
            response.type("application/json");

            JsonArray data = new JsonArray();
            productService.getProducts().forEach(product -> data.add(new Gson().toJsonTree(product.asProduct())));

            return generateProductResponse(true, null, data);
        });

        Spark.post("/products", (request, response) ->
        {
            response.type("application/json");
            try
            {
                return addProduct(request.body());
            }
            catch (ProductException e)
            {
                return generateProductResponse(false, e.getMessage(), null);
            }
        });

        Spark.post("/sales", ((request, response) -> {
            response.type("application/json");
            try
            {
                return generateSale(request.body());
            }
            catch (ProductException e)
            {
                return generateProductResponse(false, e.getMessage(), null);
            }
        }));
    }

    private String addProduct(String request) throws ProductException
    {
        Product product = new Gson().fromJson(request, Product.class);
        ImmutableProduct added = productService.addProduct(product);

        JsonArray data = new JsonArray();
        data.add(new Gson().toJsonTree(added.asProduct()));
        return new Gson().toJson(new ProductResponse(true, null, data));
    }

    private String generateSale(String request) throws ProductException
    {
        SalesRecord salesRequest = new Gson().fromJson(request, SalesRecord.class);

        int totalCents = 0;
        for (LineItem item : salesRequest.getLineItems())
        {
            if (item.getQuantity() < 1)
            {
                throw new ProductException("Invalid quantity: " + item.getQuantity());
            }
            ImmutableProduct product = productService.findProductById(item.getId());
            if (product == null)
            {
                throw new ProductException("No such product: " + item.getId());
            }

            int itemCents = item.getQuantity() * product.getPriceCents();
            item.setTotalCents(itemCents);
            totalCents += itemCents;
        }

        salesRequest.setTotalCents(totalCents);

        JsonElement data = new Gson().toJsonTree(salesRequest);

        return new Gson().toJson(new ProductResponse(true, null, data));

    }

    private String generateProductResponse(boolean success, String message, JsonElement data)
    {
        return new Gson().toJson(new ProductResponse(success, message, data));
    }
}
