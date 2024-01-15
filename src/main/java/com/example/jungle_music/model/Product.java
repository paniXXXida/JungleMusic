package com.example.jungle_music.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private String category;
    private String description;
    private long quantity;
    private double price;
    private long stock;

    private double totalPrice;

    public Product(long quantity, String name, String category, double totalPrice){
        this(0,name,category,0,null);
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Product(long stock, String name, String  category, double price, String description){
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public long getStock() {
        return stock;
    }

    public double getTotalPrice() {
        return totalPrice*quantity;
    }

    public void decreaseStock(int quantity){
        if (stock> quantity){
            stock -= quantity;
        }
    }
}
