package com.bank_account_management_system.controller;

import javafx.scene.control.Label;

public class Error {
    public static void print(Label errorLabel, String message){
        errorLabel.setText(message);
        System.err.println(message);
    }
}
