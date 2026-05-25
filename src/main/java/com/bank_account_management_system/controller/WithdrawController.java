package com.bank_account_management_system.controller;

import com.bank_account_management_system.model.BankAccount;
import com.bank_account_management_system.model.CheckingAccount;
import com.bank_account_management_system.service.AccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.bank_account_management_system.controller.Validation.isTextFieldValid;

public class WithdrawController {
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
    public void handleWithdraw(ActionEvent event) {
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
            if (!(account instanceof CheckingAccount)){
                Error.print(errorLabel,"can't withdraw from a non-checking account");
                return;

            }

            if(Validation.isAmountValid(amount)){
                Error.print(errorLabel,"can't withdraw less than .01");
                return;
            }
            // Calls your Checking-specific logic
            if (AccountService.withdraw(id, password,amount, Cache.getUser().getUsername())) {
                DashboardController.instance.loadAccountData();
                handleCancel(event);
            }

        }
        catch (NumberFormatException e) {
            Error.print(errorLabel,"Error: Please check numerical inputs.");
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