package com.example.jungle_music.controller;

import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductInventoryController implements Initializable, Controller {
    @FXML
    public TextField inputStock;
    @FXML
    public TextField inputCategory;
    @FXML
    public TextField inputDescription;
    @FXML
    public TextField inputPrice;
    @FXML
    public TextField inputName;
    @FXML
    public Label labelMessage;
    @FXML
    private TableView<Product> tableInventory;
    @FXML
    private TableColumn<Product, Integer> columnStock;
    @FXML
    private TableColumn<Product, String> columnName;
    @FXML
    private TableColumn<Product, String> columnCategory;
    @FXML
    private TableColumn<Product, Double> columnPrice;
    @FXML
    private TableColumn<Product, String> columnDescription;
    private ObservableList<Product> observableProducts;
    private Database database;

    public void setData(Object data) {
        database = (Database) data;
    }

    public void onAddClick() {

        if(inputStock.getText().isEmpty() || columnName.getText().isEmpty() || columnCategory.getText().isEmpty() || columnPrice.getText().isEmpty() || columnDescription.getText().isEmpty()){
            displayMessage("Please Fill All The Fields");
        }
        else {
            try {
                int stock = Integer.parseInt(inputStock.getText());
                String productName = inputName.getText();
                String productCategory = inputCategory.getText();
                double productPrice = Double.parseDouble(inputPrice.getText());
                String productDescription = inputDescription.getText();

                Product product = new Product(stock,productName,productCategory,productPrice,productDescription);
                database.addProduct(product);
                observableProducts.add(product);
                clearTextFields();
                displayMessage("Product has been added Successfully!");
            } catch (NumberFormatException e) {
                displayMessage("The input is not correct");
            }
        }
    }

    private void clearTextFields() {
        inputStock.clear();
        inputName.clear();
        inputCategory.clear();
        inputPrice.clear();
        inputDescription.clear();
    }

    public void onEditClick() {
        ObservableList<Product> currentTableData = tableInventory.getItems();

        if (isAnyFieldEmpty() || tableInventory.getSelectionModel().isEmpty()) {
            displayMessage("Please Fill All Fields and Select a Product");
            return;
        }

        Product selectedProduct = tableInventory.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                selectedProduct.setStock(Integer.parseInt(inputStock.getText()));
                selectedProduct.setName(inputName.getText());
                selectedProduct.setCategory(inputCategory.getText());
                selectedProduct.setPrice(Double.parseDouble(inputPrice.getText()));
                selectedProduct.setDescription(inputDescription.getText());

                tableInventory.setItems(currentTableData);
                tableInventory.refresh();
                tableInventory.getSelectionModel().clearSelection();
                clearTextFields();
                displayMessage("Product was successfully changed");
            } catch (NumberFormatException e) {
                displayMessage("Please check correctness of your input");
            }
        } else {
            displayMessage("Product not found");
        }
    }

    private boolean isAnyFieldEmpty() {
        return inputStock.getText().isEmpty()
                || inputName.getText().isEmpty()
                || inputCategory.getText().isEmpty()
                || inputPrice.getText().isEmpty()
                || inputDescription.getText().isEmpty();
    }

    public void onDeleteClick() {
        if (!tableInventory.getSelectionModel().getSelectedItems().isEmpty()) {
            try {
                // Extracted the deletion logic into a separate method
                deleteSelectedProducts();
            } catch (Exception e) {
                // Display an error message in case of deletion failure
                displayMessage("Product/s Deleting Failed!");
            }
        } else {
            // Display a message if no product is selected
            displayMessage("Please select the product");
        }
    }

    private void deleteSelectedProducts() {
        ObservableList<Product> selectedProducts = tableInventory.getSelectionModel().getSelectedItems();

        // Remove products from the database
        for (Product product : selectedProducts) {
            database.removeProduct(product);
        }

        // Remove products from the observable list
        observableProducts.removeAll(selectedProducts);

        // Display success message and perform additional actions
        displayMessage("Product/s Successfully Deleted!");
        clearTextFields();
        tableInventory.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableProducts = FXCollections.observableArrayList(database.getProducts());
        tableInventory.setItems(observableProducts);

        columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void displayMessage(String message) {
        labelMessage.setText(message);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> labelMessage.setText(""))
        );
        timeline.play();
    }

    public void rowClicked() {
        Product product = tableInventory.getSelectionModel().getSelectedItem();
        if(product != null){
            inputPrice.setText(String.valueOf(product.getPrice()));
            inputStock.setText(String.valueOf(product.getStock()));
            inputName.setText(String.valueOf(product.getName()));
            inputCategory.setText(String.valueOf(product.getCategory()));
            inputDescription.setText(String.valueOf(product.getDescription()));
        }
        else {
            clearTextFields();
            displayMessage("There is no products");
        }
    }



}
