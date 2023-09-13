package me.product.server.database;

import me.product.server.ProductEndpoint;
import me.product.server.ProductService;

import java.sql.SQLException;

/**
 * Main program for database version of product endpoint.
 */
public class DatabaseMain
{
    public static void main(String[] args) throws SQLException
    {
        ProductService productService = new DatabaseProductService("jdbc:mysql://localhost/spark");

        ProductEndpoint productEndpoint = new ProductEndpoint(productService);
        productEndpoint.initialiseApi();
    }
}
