package com.example.jungle_music.controller;

import com.example.jungle_music.MusicApplication;
import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.Customer;
import com.example.jungle_music.model.Order;
import com.example.jungle_music.model.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateOrderController implements Initializable, Controller {
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private Label labelMessage;
    @FXML
    private TextField inputPhone;
    @FXML
    private TextField inputEmail;
    @FXML
    private TextField inputSurname;
    @FXML
    private TextField inputName;
    private final ObservableList<Product> observableProducts = FXCollections.observableArrayList();
    private Database database;
    @FXML
    private TableColumn<Product, Integer> columnQuantity;
    @FXML
    private TableColumn<Product, String> columnName;
    @FXML
    private TableColumn<Product, String> columnCategory;
    @FXML
    private TableColumn<Product, Double> columnPrice;
    public void setData(Object database){
        this.database = (Database) database;
    }
    @FXML
    private void onAddProductClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(MusicApplication.class.getResource("add-product-dialog.fxml"));
        Parent addProductDialog = loader.load();
        AddProductController controller = loader.getController();
        controller.setDialog(database, this);
        Dialog<Product> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(addProductDialog);
        dialog.setTitle("Add Product");
        dialog.showAndWait();
    }

    @FXML
    private void onCreateOrderClick() {
        String firstName = inputName.getText().trim();
        String lastName = inputSurname.getText().trim();
        String emailAddress = inputEmail.getText().trim();
        String phoneNumber = inputPhone.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || phoneNumber.isEmpty()) {
            displayMessage("Please fill all the fields");
            return;
        }

        if (observableProducts.isEmpty()) {
            displayMessage("You haven't chosen any products");
            return;
        }

        try {
            Customer customer = new Customer(firstName, lastName, emailAddress, phoneNumber);
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
            Order order = new Order(customer, dateTime, new ArrayList<>(observableProducts));

            database.addOrder(order);
            cleanPage();
            displayMessage("Your order was placed");
        } catch (Exception ex) {
            displayMessage("Error occurred while creating order");
        }
    }

    @FXML
    private void onDeleteProductClick() {
        try {
            Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                observableProducts.remove(selectedProduct);
                database.increaseProductStock(selectedProduct.getName(), selectedProduct.getQuantity());
            }
        } catch (Exception e){
            displayMessage("Error Occurred While Deleting the Product, Please Try Again");
        }
    }

    public void getOrderedProduct(Product product) {
        observableProducts.add(product);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableProducts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableProducts.setItems(observableProducts);

        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    private void cleanPage(){
        inputName.clear();
        inputSurname.clear();
        inputEmail.clear();
        inputPhone.clear();
        tableProducts.setItems(FXCollections.observableArrayList());
        labelMessage.setText("");
    }

    private void displayMessage(String message) {
        labelMessage.setText(message);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> labelMessage.setText(""))
        );
        timeline.play();
    }}
