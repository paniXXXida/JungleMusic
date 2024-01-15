package com.example.jungle_music.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private final Customer customer;

    private final String orderDate;

    private ArrayList<Product> products;


    public Order(Customer customer,String orderDate, ArrayList<Product> products) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.products = products;

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
