package com.example.jungle_music.controller;

import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.Customer;
import com.example.jungle_music.model.Order;
import com.example.jungle_music.model.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable, Controller {

    //  Orders
    @FXML
    private TableView<Order> tableOrders;
    @FXML
    private TableColumn<Order, String> columnDate;
    @FXML
    private TableColumn<Order, String> columnCustomerName;
    @FXML
    private TableColumn<Order, Double> columnTotalPrice;

    //  Products
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product, Integer> columnQuantity;
    @FXML
    private TableColumn<Product, String> columnName;
    @FXML
    private TableColumn<Product, String> columnCategory;
    @FXML
    private TableColumn<Product, Double> columnPrice;

    private Database database;
    public void setData(Object data) {
        database = (Database) data;
    }

    private void loadProductData(Order order) {
        ObservableList<Product> products = FXCollections.observableArrayList(order.getProducts());
        tableProducts.setItems(products);

        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Order> orders = FXCollections.observableArrayList(database.getOrders());
        tableOrders.setItems(orders);

        columnDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        columnCustomerName.setCellValueFactory(cellDataFeatures -> {
            Order order = cellDataFeatures.getValue();
            Customer customer = order.getCustomer();
            return new SimpleStringProperty(customer != null ? customer.getName() : "");
        });

        columnTotalPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(
                cellData.getValue().getProducts().stream().mapToDouble(Product::getTotalPrice).sum()
        ).asObject());

        tableOrders.getSelectionModel().selectedItemProperty().addListener((observableValue, oldOrder, newOrder) -> {
            if (newOrder != null) {
                loadProductData(newOrder);
            }
        });
    }

}
