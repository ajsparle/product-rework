package me.product.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestProductEndpoint
{
    private static final String BASE_URI = "http://localhost:4567";

    @Test
    public void testGetProducts() throws URISyntaxException, IOException, InterruptedException
    {
        HttpRequest request =
                HttpRequest.newBuilder(new URI(BASE_URI + "/products"))
                        .timeout(Duration.ofSeconds(10))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode(), is(200));

        ProductResponse productResponse = readProductResponse(response);
        System.out.println(productResponse);

        assertThat(productResponse.isSuccess(), is(true));
        assertThat(productResponse.getData(), is(notNullValue()));

        ArrayList<Product> list = readProductList(productResponse);
        list.forEach(System.out::println);
    }

    private static ArrayList<Product> readProductList(ProductResponse productResponse)
    {
        Gson gson = new Gson();
        JsonArray array = productResponse.getData().getAsJsonArray();

        ArrayList<Product> list = new ArrayList<>(array.size());
        array.forEach(elt -> list.add(gson.fromJson(elt, Product.class)));

        return list;
    }

    private static ProductResponse readProductResponse(HttpResponse<String> response)
    {
        return new Gson().fromJson(response.body(), ProductResponse.class);
    }

    @Test
    public void testAddProduct() throws URISyntaxException, IOException, InterruptedException
    {
//        addProduct("Chrome Toaster", 100_00)
//        addProduct("Copper Kettle", 49_00)
//        addProduct("Mixing Bowl", 20_00)
        addProduct("Foo", 13);
        addProduct("Bar", 1_000_00);
    }

    private void addProduct(String productName, int priceCents) throws URISyntaxException, IOException, InterruptedException
    {
        Product product = new Product(0, productName, priceCents);
        String body = new Gson().toJson(product);

        HttpRequest request =
                HttpRequest.newBuilder(new URI(BASE_URI + "/products"))
                        .timeout(Duration.ofSeconds(10))
                        .header("Accept", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());

        // Possible 201 if a new endpoint URI is created
        assertThat(response.statusCode(), is(200));
        //System.out.println(response.body());

        ProductResponse productResponse = readProductResponse(response);
        System.out.println(productResponse);

        assertThat(productResponse.isSuccess(), is(true));

        List<Product> list = readProductList(productResponse);
        assertThat(list.size(), is(1));

        Product returned = list.get(0);
        System.out.println("Created: " + returned);
        assertThat(returned.getProductName(), is(productName));
        assertThat(returned.getPriceCents(), is(priceCents));
        assertThat(returned.getId(), is(greaterThan(0)));
    }

    @Test
    public void testRequestSale() throws IOException, InterruptedException, URISyntaxException
    {
        SalesRecord salesRecord =
                createSalesRequest(
                        LineItem.request(1, 1),
                        LineItem.request(2, 2),
                        LineItem.request(3, 4));
        salesRecord.setDiscountCents(10_00);

        String body = new Gson().toJson(salesRecord);

        HttpRequest request =
                HttpRequest.newBuilder(new URI(BASE_URI + "/sales"))
                        .timeout(Duration.ofSeconds(10))
                        .header("Accept", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());

        // Possible 201 if a new endpoint URI is created, but not for sales
        assertThat(response.statusCode(), is(200));

        ProductResponse productResponse = readProductResponse(response);
        assertThat(productResponse.isSuccess(), is(true));
        assertThat(productResponse.getData(), is(notNullValue()));

        SalesRecord result = new Gson().fromJson(productResponse.getData(), SalesRecord.class);
        for (LineItem item : result.getLineItems())
        {
            System.out.println("   " + item);
        }
        System.out.println("Total cents = " + result.getTotalCents());
        System.out.println("Discount    = " + result.getDiscountCents());
    }

    private SalesRecord createSalesRequest(LineItem... items)
    {
        SalesRecord salesRecord = new SalesRecord();
        List<LineItem> itemList = new ArrayList<>();
        Collections.addAll(itemList, items);
        salesRecord.setLineItems(itemList);
        return salesRecord;
    }
}
