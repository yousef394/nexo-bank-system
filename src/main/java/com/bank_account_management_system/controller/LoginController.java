package com.bank_account_management_system.controller;

import com.bank_account_management_system.Repository.UserRepository;
import com.bank_account_management_system.model.User;
import com.bank_account_management_system.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.bank_account_management_system.controller.Validation.isTextFieldValid;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
    }
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String name = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        if ( isTextFieldValid(usernameField)||isTextFieldValid(passwordField)) {
            Error.print(errorLabel,"Error: Please fill in all String fields.");
            return;
        }

        UserRepository repo = new UserRepository();
        // Call the service
        User userAccount = UserService.findByUserNameAndPassword(name, pass);

        if (userAccount != null) {
            Cache.setUser(userAccount);
            Navigation.changeScene("dashboard.fxml", event);

            // Move to the Dashboard/Main Screen
            // You can store 'userAccount' in a static variable to know who is logged in
        } else {
            errorLabel.setText("Invalid Username or Password!");
            errorLabel.setVisible(true);
        }
    }}
