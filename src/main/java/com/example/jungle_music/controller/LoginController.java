package com.example.jungle_music.controller;

import com.example.jungle_music.MusicApplication;
import com.example.jungle_music.database.Database;
import com.example.jungle_music.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private Label errorMessage;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;
    private Database database;

    public void setDatabase(Database database) {
        this.database = database;
    }

    @FXML
    void OnLoginClick(ActionEvent event) throws IOException {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showError("Please fill both fields");
        } else {
            try {
                User user = database.authenticateUser(enteredUsername, enteredPassword);

                if (user != null) {
                    createStage(user);
                    closeWindow(event);
                } else {
                    showError("Invalid username or password");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("An unexpected error occurred. Please try again.");
            }
        }
    }

    private void createStage(User authenticatedUser) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(MusicApplication.class.getResource("main-view.fxml"));
        Stage mainStage = new Stage();
        mainLoader.setControllerFactory(controllerClass -> new MainController(database, authenticatedUser, mainStage));
        mainStage.setScene(new Scene(mainLoader.load(),900, 700));
        mainStage.setResizable(true);
        mainStage.show();
    }

    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String error) {
        errorMessage.setText(error);
    }


}
