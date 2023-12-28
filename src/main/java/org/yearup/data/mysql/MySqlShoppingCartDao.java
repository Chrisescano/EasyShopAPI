package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = """
                SELECT s.user_id, s.quantity, p.* FROM shopping_cart AS s
                JOIN products AS p ON s.product_id = p.product_id
                WHERE user_id = ?;
                """;
        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();

            while (row.next()) {
                shoppingCart.add(mapRow(row));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return shoppingCart;
    }

    @Override
    public ShoppingCart add(int userId, int productId) {
        String insertSql = """
                INSERT INTO shopping_cart (quantity, user_id, product_id)
                VALUES (?, ?, ?);
                """;
        String updateSql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?;
                """;

        ShoppingCart shoppingCart = getByUserId(userId);
        boolean update = shoppingCart.contains(productId);
        int quantity = 1;

        if (update) { //increment by 1 only if product exists
            quantity = shoppingCart.get(productId).getQuantity() + 1;
            shoppingCart.get(productId).setQuantity(quantity);
        }

        try (Connection connection = getConnection()) {
            PreparedStatement statement;
            statement = update ? connection.prepareStatement(updateSql) : connection.prepareStatement(insertSql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();

            return getByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public ShoppingCart delete(int userId) {
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ?;
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
            return getByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected static ShoppingCartItem mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        int quantity = row.getInt("quantity");

        Product product = new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setProduct(product);
        shoppingCartItem.setQuantity(quantity);
        return shoppingCartItem;
    }
}
