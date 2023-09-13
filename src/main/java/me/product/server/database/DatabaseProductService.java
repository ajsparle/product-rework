package me.product.server.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.product.api.Product;
import me.product.server.ImmutableProduct;
import me.product.server.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MYSQL implementation of a ProductService
 */
public class DatabaseProductService implements ProductService
{
    private final Dao<DatabaseProduct, String> productDao;

    public DatabaseProductService(String databaseUrl) throws SQLException
    {
        JdbcConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
        connectionSource.setUsername("test");
        connectionSource.setPassword("password");

        productDao = DaoManager.createDao(connectionSource, DatabaseProduct.class);

        TableUtils.createTableIfNotExists(connectionSource, DatabaseProduct.class);

    }

    @Override
    public List<ImmutableProduct> getProducts()
    {
        try
        {
            List<DatabaseProduct> list = productDao.queryForAll();
            return list.stream().map(DatabaseProduct::asImmutableProduct).collect(Collectors.toList());
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ImmutableProduct addProduct(Product product)
    {
        try
        {
            DatabaseProduct insert = new DatabaseProduct();
            insert.setProductName(product.getProductName());
            insert.setPriceCents(product.getPriceCents());

            // this updates insert to include the generated ID
            productDao.create(insert);
            return insert.asImmutableProduct();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ImmutableProduct findProductById(int id)
    {
        try
        {
            DatabaseProduct databaseProduct = productDao.queryForId(Integer.toString(id));
            if (databaseProduct == null)
            {
                return null;
            }
            return databaseProduct.asImmutableProduct();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
