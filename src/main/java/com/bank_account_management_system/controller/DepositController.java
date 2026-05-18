package com.bank_account_management_system.controller;

import com.bank_account_management_system.model.BankAccount;
import com.bank_account_management_system.service.AccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class DepositController {
    public Label errorLabel;
    @FXML private TextField idField;
    @FXML private TextField amountField;

    @FXML
    public void initialize() {
        // Feed data to the ComboBox as requested
        AccountService.applySanitizer(errorLabel, idField, amountField);
    }

    // THE AUTOMATIC PART: Called by ReportService
    public void initData(BankAccount account) {
        if (account != null) {
            idField.setText(String.valueOf(account.getAccountId()));
        }
    }

    @FXML
    public void handleDeposit(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());
            double amount = Double.parseDouble(amountField.getText());
            BankAccount acc = AccountService.find(id);
            if (acc == null){
                System.out.println("id not found");
                errorLabel.setText("id not found");
                return;
            }
            if(amount <.01){
                System.out.println("can't deposit less than .01");
                errorLabel.setText("can't deposit less than .01");
                return;
            }
            boolean success = AccountService.deposit(id, amount);
            if (success) {
                DashboardController.instance.loadAccountData(); // Refresh the table
                handleCancel(event);
            }


        }
        catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for IDs, Amount... etc");
            errorLabel.setText("Error: Please enter valid numbers for IDs, Amount... etc");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

}