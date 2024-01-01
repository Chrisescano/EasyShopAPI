package org.yearup.data.util;

import org.springframework.stereotype.Component;

@Component
public class Quantity {
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
