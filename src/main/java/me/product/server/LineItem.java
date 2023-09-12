package me.product.server;

import lombok.Data;

@Data
public class LineItem
{
    /**
     * The product ID
     */
    private int id;

    /**
     * Quantity of the item
     */
    private int quantity;

    /**
     * Total cost for this item
     */
    private int totalCents;

}
