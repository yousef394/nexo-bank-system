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

public class WithdrawController {
    public Label errorLabel;
    public PasswordField passwordField;
    @FXML private TextField idField;
    @FXML private TextField amountField;

    @FXML
    public void initialize() {
        // Feed data to the ComboBox as requested
        HelperClass.applySanitizer(errorLabel, idField, amountField);

    }


    @FXML
    public void handleWithdraw(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());
            String password = passwordField.getText().trim();
            double amount = Double.parseDouble(amountField.getText());
            BankAccount acc = AccountService.findByIdAndPassword(id, password);
            if (acc == null){
                System.out.println("Invalid Id or Password!");
                errorLabel.setText("Invalid Id or Password!");
                return;
            }
            if (!(acc instanceof CheckingAccount)){
                System.out.println("can't withdraw from a non-checking account");
                errorLabel.setText("can't withdraw from a non-checking account");
                return;

            }

            if(amount <.01){
                System.out.println("can't withdraw less than .01");
                errorLabel.setText("can't withdraw less than .01");
                return;
            }
            // Calls your Checking-specific logic
            if (AccountService.withdraw(id, password,amount, HelperClass.getUser().getUsername())) {
                DashboardController.instance.loadAccountData();
                handleCancel(event);
            }

        }
        catch (NumberFormatException e) {
            System.out.println("Error: Please check numerical inputs.");
            errorLabel.setText("Error: Please check numerical inputs.");
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