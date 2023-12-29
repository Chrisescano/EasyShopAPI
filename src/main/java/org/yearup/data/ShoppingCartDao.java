package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCart add(int userId, int productId);
    ShoppingCartItem update(int userId, int quantity, int productId);
    ShoppingCart delete(int userId);
}
