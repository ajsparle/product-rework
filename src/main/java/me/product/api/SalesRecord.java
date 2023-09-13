package me.product.api;

import lombok.Data;

import java.util.List;

/**
 * Record of a sale used across the API in both directions as a request and response.
 * The response populates the cents fields for each line item.
 */
@Data
public class SalesRecord
{
    /**
     * List of line items (input).
     */
    private List<LineItem> lineItems;

    /**
     * Sale total (output).
     */
    private Integer totalCents;

    /**
     * Discount to be applied (input).
     */
    private int discountCents;
}
