package me.product.api;

import lombok.Data;
import me.product.server.LineItem;

import java.util.ArrayList;

/**
 * Record of a sale used across the API in both directions as a request and response.
 * The response populates the cents fields for each line item.
 */
@Data
public class SalesRecord
{
    /**
     * List of line items.
     */
    private ArrayList<LineItem> lineItems;

    /**
     * Sale total.
     */
    private int totalCents;
}
