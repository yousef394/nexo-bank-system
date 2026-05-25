package com.bank_account_management_system.controller;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Sanitize {
    public static void applySanitizer(Label errorLabel, TextField... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.contains("#//#")) {
                    field.setText(oldValue); // Revert to what it was before the illegal char
                    errorLabel.setText("The sequence '#//#' is reserved for system use.");
                }
            });

        }
    }

}
