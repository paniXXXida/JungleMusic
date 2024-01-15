package com.example.jungle_music.controller;

import com.example.jungle_music.MusicApplication;
import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.Role;
import com.example.jungle_music.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final User user;
    private final Stage stage;
    @FXML
    private Button btnProductInventory;
    @FXML
    private Button btnCreateOrder;
    @FXML
    private BorderPane layout;
    private final Database database;

    @FXML
    private void onDashBoardClick() {
        loadScene("dashboard-view.fxml", user);
        stage.setTitle("Dashboard");
    }
    @FXML
    private void onCreateClick() {
        loadScene("create-order-view.fxml", database);
        stage.setTitle("Create Order");
    }
    @FXML
    private void onProductInvClick() {
        loadScene("product-inventory-view.fxml", database);
        stage.setTitle("Product Inventory");
    }
    @FXML
    private void onHistoryClick() {
        loadScene("order-history-view.fxml", database);
        stage.setTitle("Order History");
    }

    @FXML
    private void onLogoutClick() throws IOException {
        database.saveDatabase(database);
        Platform.exit();
    }


    public MainController(Database database, User user, Stage stage) {
        this.database = database;
        this.user = user;
        this.stage = stage;
    }
    private void limitAccess() {
        if(user.getRole() == Role.MANAGER){
            btnCreateOrder.setDisable(true);
        }
        else {
            btnProductInventory.setDisable(true);
        }
    }
    private void loadScene(String fxmlPath, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(MusicApplication.class.getResource(fxmlPath));
            loader.setControllerFactory(controllerClass -> {
                try {
                    Controller controller = (Controller) controllerClass.getConstructor().newInstance();
                    controller.setData(data);
                    return controller;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            layout.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        limitAccess();
        onDashBoardClick();
    }
}
