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

public class TransferController {

    @FXML public PasswordField passwordField;
    @FXML private TextField fromAccountId;
    @FXML private TextField toAccountId;
    @FXML private TextField amountField;
    @FXML private Label errorLabel;


    @FXML
    public void initialize() {
        // Feed data to the ComboBox as requested
        Sanitize.applySanitizer(errorLabel, fromAccountId, toAccountId, amountField);

    }
    @FXML
    public void handleTransfer(ActionEvent event) {
        try {
            int fromId = Integer.parseInt(fromAccountId.getText());
            if (!Validation.isTextFieldValid(passwordField)){
                Error.print(errorLabel,"Error: Please fill in the password.");
                return;
            }
            String password = passwordField.getText().trim();
            int toId = Integer.parseInt(toAccountId.getText());
            double amount = Double.parseDouble(amountField.getText());
            BankAccount fromAccount = AccountService.findByIdAndPassword(fromId, password);
            if (!Validation.isAccountFound(fromAccount)){
                Error.print(errorLabel,"Invalid fromId or Password!");
                return;
            }
            if (!(fromAccount instanceof CheckingAccount)){
                Error.print(errorLabel,"can't withdraw from a non-checking account");
                return;

            }

            BankAccount toAccount = AccountService.findById(toId);
            if (!Validation.isAccountFound(toAccount)){
                Error.print(errorLabel,"toId not found");
                return;
            }
            if (toAccount == fromAccount){
                Error.print(errorLabel,"can't transfer into and from the same account");
                return;

            }
            if(Validation.isAmountValid(amount)){
                Error.print(errorLabel,"can't transfer less than .01");
                return;
            }
            // Call the transfer method in AccountService
            boolean success = AccountService.transfer(fromId, password,toId, amount, Cache.getUser().getUsername());

            if (success) {
                System.out.println("Transfer Successful!");

                // REFRESH the dashboard automatically using your new static instance
                if (DashboardController.instance != null) {
                    DashboardController.instance.loadAccountData();
                }

                handleCancel(event); // Close the popup
            } else {
                Error.print(errorLabel,"Transfer Failed: Check balance or IDs.");

            }

        }
        catch (NumberFormatException e) {
            Error.print(errorLabel,"Error: Please enter valid numbers for IDs, Amount.");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    // THE AUTOMATIC PART: Called by ReportService
    public void initData(BankAccount account) {
        if (account != null) {
            fromAccountId.setText(String.valueOf(account.getAccountId()));
        }
    }

}
