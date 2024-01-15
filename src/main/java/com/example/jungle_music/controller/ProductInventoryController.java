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
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ProductInventoryController implements Initializable, Controller {
    private Database database;
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


    public void setData(Object data) {
        database = (Database) data;
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
    public void onAddClick() {
        String stockText = inputStock.getText();
        String name = inputName.getText();
        String category = inputCategory.getText();
        String priceText = inputPrice.getText();
        String description = inputDescription.getText();

        if (stockText.isEmpty() || name.isEmpty() || category.isEmpty() || priceText.isEmpty() || description.isEmpty()) {
            displayMessage("Please fill all the fields");
        } else {
            try {
                int stock = Integer.parseInt(stockText);
                double price = Double.parseDouble(priceText);

                Product product = new Product(stock, name, category, price, description);
                database.addProduct(product);
                observableProducts.add(product);
                clearTextFields();
                displayMessage("Product added successfully!");
            } catch (NumberFormatException e) {
                displayMessage("Invalid input format");
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



    private boolean isAnyFieldEmpty() {
        return inputStock.getText().isEmpty()
                || inputName.getText().isEmpty()
                || inputCategory.getText().isEmpty()
                || inputPrice.getText().isEmpty()
                || inputDescription.getText().isEmpty();
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

    public void onDeleteClick() {
        if (!tableInventory.getSelectionModel().getSelectedItems().isEmpty()) {
            try {

                deleteSelectedProducts();
            } catch (Exception e) {

                displayMessage("Product/s Deleting Failed!");
            }
        } else {

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

    public void OnImportClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            processCSVFile(selectedFile.toPath());
        }
    }

    private void processCSVFile(Path file) {
        try (Scanner scanner = new Scanner(file)) {
            scanner.nextLine(); // Skip the header line

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");

                if (parts.length == 5) {
                    try {
                        String name = parts[0].trim();
                        String category = parts[1].trim();
                        double price = Double.parseDouble(parts[2].trim());
                        String description = parts[3].trim();
                        int stock = Integer.parseInt(parts[4].trim());

                        Product product = new Product(stock, name, category, price, description);
                        database.addProduct(product);
                        observableProducts.add(product);
                    } catch (NumberFormatException e) {
                        displayMessage("Error parsing numeric values in the CSV file.");
                    }
                }
            }
        } catch (IOException e) {
            displayMessage("Error reading the CSV file.");
        }
    }
}
