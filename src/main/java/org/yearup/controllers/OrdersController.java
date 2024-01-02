package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {
    private ProfileDao profileDao;
    private UserDao userDao;
    private ShoppingCartDao shoppingCartDao;


    @Autowired
    public OrdersController(
            ProfileDao profileDao,
            UserDao userDao,
            ShoppingCartDao shoppingCartDao
    ) {
        this.profileDao = profileDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    @PostMapping()
    public void checkout() {
        //retrieve the users shopping cart
        //create and insert a new order into orders tables
        //create a orderlineitem for each shopping cart item
        //add each item to the database
        //once created clear the shopping cart
    }
}
