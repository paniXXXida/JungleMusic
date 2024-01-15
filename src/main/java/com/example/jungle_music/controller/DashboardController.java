package com.example.jungle_music.controller;

import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.Product;
import com.example.jungle_music.model.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable, Controller {

    private User user;
    private Database database;
    private ObservableList<Product> products;
    private ObservableList<Product> lowStockProducts = FXCollections.observableArrayList();

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private TableView<Product> lowStockView;
    @FXML
    private TableColumn<Product, String> columnName;
    @FXML
    private TableColumn<Product, Integer> columnStock;

    @Override
    public void setData(Object user) {
        try {
            this.user = (User) user;
            this.database = new Database();
            this.products = FXCollections.observableArrayList(database.getProducts());
        } catch (Exception e) {
            handleInitializationError("Error initializing user and products.", e);
        }
    }

    private void handleInitializationError(String message, Exception e) {
        e.printStackTrace();
    }

    private void displayUserInfo() {
        welcomeLabel.setText("Welcome, " + user.getName() + " !");
        roleLabel.setText("Your role is: " + user.getRole().toString() + ".");
        displayTime();
    }

    private String setDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private void displayTime() {
        dateTimeLabel.setText("It is now: " + setDateTime());
    }

    private void displayLowStockProducts() {
        try {
            List<Product> newLowStockProducts = products.stream()
                    .filter(product -> product.getStock() < 5)
                    .collect(Collectors.toList());

            lowStockProducts.setAll(newLowStockProducts);

            if (!lowStockProducts.isEmpty()) {
                lowStockView.setItems(lowStockProducts);
                lowStockView.setVisible(true);
            } else {
                lowStockView.setVisible(false);
            }
        } catch (Exception e) {
            handleInitializationError("Error updating low stock products.", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

            lowStockView.setItems(lowStockProducts);

            displayUserInfo();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                displayTime();
                displayLowStockProducts(); // Update low stock products every second
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        } catch (Exception e) {
            handleInitializationError("Error initializing the dashboard.", e);
        }
    }
}
