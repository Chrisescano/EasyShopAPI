package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {
    private ProfileDao profileDao;
    private UserDao userDao;
    private ShoppingCartDao shoppingCartDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;


    @Autowired
    public OrdersController(
            ProfileDao profileDao,
            UserDao userDao,
            ShoppingCartDao shoppingCartDao,
            OrderDao orderDao,
            OrderLineItemDao orderLineItemDao
    ) {
        this.profileDao = profileDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @PostMapping()
    public List<OrderLineItem> checkout(Principal principal) {
        //user credentials
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        Profile profile = profileDao.getByUserId(user.getId());

        //retrieve the users shopping cart
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(profile.getUserId());

        //create and insert a new order into orders tables
        Order order = orderDao.create(profile, shoppingCart);

        //create a orderlineitem for each shopping cart item
        //add each item to the database
        for (ShoppingCartItem cartItem : shoppingCart.getItems().values()) {
            orderLineItemDao.create(order, cartItem);
        }
        List<OrderLineItem> orderLineItems = orderLineItemDao.getByOrderId(order.getOrderId());

        //once created clear the shopping cart
        shoppingCartDao.delete(profile.getUserId());

        return orderLineItems;
    }
}
