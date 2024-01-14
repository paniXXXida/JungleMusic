package com.example.jungle_music.database;

import com.example.jungle_music.model.Order;
import com.example.jungle_music.model.Product;
import com.example.jungle_music.model.Role;
import com.example.jungle_music.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Database implements Serializable {
    private final List<User> users;
    private final List<Product> products;
    private final List<Order> orders;

    private static final String DATABASE_FILE = "database.ser";

    public Database() {
        users = new ArrayList<>();
        this.products = new ArrayList<>();
        this.orders = new ArrayList<>();

        // Manager & Sales password
        users.add(new User("Manager", "Rienat", "Zhuravlov", "password1", Role.MANAGER));
        users.add(new User("Sales", "Bohdan", "Sovershenniy", "password2", Role.SALES));

        // Load existing database or create a new one
        loadDatabase();
    }

    private void loadDatabase() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            Database savedDatabase = (Database) in.readObject();
            users.clear();
            products.clear();
            orders.clear();
            users.addAll(savedDatabase.users);
            products.addAll(savedDatabase.products);
            orders.addAll(savedDatabase.orders);
            System.out.println("Database loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing database file found. Using default data.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading database.", e);
        }
    }

    public User authenticateUser(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public void saveDatabase(Database database) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            out.writeObject(this);
            System.out.println("Database is saved");
        } catch (IOException e) {
            throw new RuntimeException("Error saving database.", e);
        }
    }

    public Product getProductByName(String name) {
        return products.stream()
                .filter(product -> Objects.equals(product.getName(), name))
                .findFirst()
                .orElse(null);
    }

    public void increaseProductStock(String name, long toAdd) {
        Product product = getProductByName(name);
        long temp = product.getStock();
        product.setStock(temp+toAdd);
    }

    public void reduceProductStock(String name, long toDeduct) {
        Product product = getProductByName(name);
        long temp = product.getStock();
        product.setStock(temp-toDeduct);
    }

    public void addOrder(Order order){
        orders.add(order);
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Order> getOrders() {
        return orders;
    }


}
