package com.bank_account_management_system.controller;

import com.bank_account_management_system.model.BankAccount;
import javafx.scene.control.TextField;

public class Validation {
    public static boolean isTextFieldValid(TextField textField){
        return textField == null && textField.getText().isBlank();
    };
    public static boolean isAccountFound(BankAccount account){
        return account != null;
    };
    public static boolean isAmountValid(double amount){
        return amount > .01;
    };

}
