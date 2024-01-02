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
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

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
    public void checkout(Principal principal) {
        //user credentials
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        Profile profile = profileDao.getByUserId(user.getId());

        //retrieve the users shopping cart
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(profile.getUserId());

        //create and insert a new order into orders tables
            //dao method to create new order

        //create a orderlineitem for each shopping cart item
            //create models, prolly in a list?

        //add each item to the database
            //dao method to create orderlineitem for each S.C. item

        //once created clear the shopping cart
        //shoppingCartDao.delete(userId);
    }
}
