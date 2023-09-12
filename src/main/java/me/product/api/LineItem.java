package me.product.api;

import com.j256.ormlite.stmt.query.In;
import lombok.Data;

import java.util.Objects;

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
    private Integer totalCents;

    /**
     * Default constructor
     */
    public LineItem()
    {
        // default constructor required for JSON
    }

    /**
     * Convenience factory function to create a request.
     *
     * @param id       the item ID
     * @param quantity the item quantity
     */
    public static LineItem request(int id, int quantity)
    {
        if (id <= 0 || quantity <= 0)
        {
            throw new IllegalArgumentException("Invalid line item request");
        }

        LineItem lineItem = new LineItem();
        lineItem.id = id;
        lineItem.quantity = quantity;
        return lineItem;
    }
}
