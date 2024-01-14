package com.example.jungle_music.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private final Customer customer;

    private ArrayList<Product> products;

    private final String orderDate;

    public Order(Customer customer, ArrayList<Product> products, String orderDate) {
        this.customer = customer;
        this.products = products;
        this.orderDate = orderDate;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
