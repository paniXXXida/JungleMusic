package com.example.jungle_music.controller;

import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class AddProductController {
    @FXML
    private TableColumn<Product, Integer> stockColumn;
    @FXML
    private TableColumn<Product, String>nameColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, String> descriptionColumn;
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private Spinner<Integer> inputQuantity;
    @FXML
    private Label labelMessage;
    @FXML
    private TextField searchField;
    private List<Product> products;
    private ObservableList<Product> observableProducts;
    private int currentQuantity;
    private Database database;
    private CreateOrderController orderController;
    public void setDialog(Database database, CreateOrderController orderController){
        this.database = database;
        this.orderController = orderController;
        setTableProducts();
        initializeSpinner();
    }

    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        valueFactory.setValue(1);
        inputQuantity.setValueFactory(valueFactory);
        currentQuantity = inputQuantity.getValue();
        inputQuantity.valueProperty().addListener((observableValue, integer, t1) -> currentQuantity = inputQuantity.getValue());
    }

    private void setTableProducts() {
        products = database.getProducts();
        observableProducts = FXCollections.observableArrayList(products);
        tableProducts.setItems(observableProducts);

        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void displayMessage(String message) {
        labelMessage.setText(message);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> labelMessage.setText(""))
        );
        timeline.play();
    }

    @FXML
    private void onOkClick(ActionEvent event) {
        try {
            Product selection = tableProducts.getSelectionModel().getSelectedItem();

            if (selection == null) {
                displayMessage("Please choose a Product");
                return;
            }

            if (currentQuantity <= selection.getStock()) {
                Product orderedProduct = new Product(currentQuantity, selection.getName(), selection.getCategory(), selection.getPrice());
                database.reduceProductStock(selection.getName(), currentQuantity);
                orderController.getOrderedProduct(orderedProduct);
                labelMessage.setText("");
                onCancelClick(event);
            } else {
                displayMessage("Out of Stock");
            }
        } catch (Exception e) {
            displayMessage("Error occurred while adding product");
        }
    }

    @FXML
    private void onCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }}

