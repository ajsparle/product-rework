package me.product.server.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import me.product.server.ImmutableProduct;

/**
 * Internal object used to map products to a database table.
 */
@DatabaseTable(tableName = "product")
public class DatabaseProduct
{

    @DatabaseField(generatedId = true)
    @Getter
    @Setter
    private int id;

    @DatabaseField
    @Getter
    @Setter
    private String productName;

    @DatabaseField
    @Getter
    @Setter
    private int priceCents;

    public DatabaseProduct()
    {
        // ORMLite needs a no-arg constructor
    }

    /**
     * Convenience function to return this object as an ImmutableProduct object.
     *
     * @return an immutable product.
     */
    public ImmutableProduct asImmutableProduct()
    {
        return new ImmutableProduct(id, productName, priceCents);
    }
}
