package com.bank_account_management_system.controller;

import com.bank_account_management_system.model.BankAccount;
import com.bank_account_management_system.service.AccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.bank_account_management_system.controller.Validation.isTextFieldValid;


public class DepositController {
    public Label errorLabel;
    public PasswordField passwordField;
    @FXML private TextField idField;
    @FXML private TextField amountField;

    @FXML
    public void initialize() {
        // Feed data to the ComboBox as requested
        Sanitize.applySanitizer(errorLabel, idField, amountField);
    }


    @FXML
    public void handleDeposit(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());
            String password = passwordField.getText().trim();
            if (isTextFieldValid(passwordField)) {
                Error.print(errorLabel,"Error: Please fill in the password.");
                return;
            }

            double amount = Double.parseDouble(amountField.getText());
            BankAccount account = AccountService.findByIdAndPassword(id, password);
            if (!Validation.isAccountFound(account)){
                Error.print(errorLabel,"Invalid Id or Password!");
                return;
            }
            if(Validation.isAmountValid(amount)){
                Error.print(errorLabel,"can't deposit less than .01");
                return;
            }
            boolean success = AccountService.deposit(id,password, amount, Cache.getUser().getUsername());
            if (success) {
                DashboardController.instance.loadAccountData(); // Refresh the table
                handleCancel(event);
            }


        }
        catch (NumberFormatException e) {
            Error.print(errorLabel,"Error: Please enter valid numbers for IDs, Amount... etc");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    // THE AUTOMATIC PART: Called by ReportService
    public void initData(BankAccount account) {
        if (account != null) {
            idField.setText(String.valueOf(account.getAccountId()));
        }
    }

}