package com.bank_account_management_system.controller;
import com.bank_account_management_system.model.*;
import com.bank_account_management_system.service.AccountService;
import com.bank_account_management_system.service.ReportService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class AddAccountController {
    @FXML
    private TextField nameField;
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> accountTypeBox;
    @FXML
    private TextField balanceField;
    @FXML
    private VBox dynamicFields;
    // Store dynamically created fields to read them during creation
    private Map<String, TextField> activeFields = new HashMap<>();

    @FXML
    public void initialize() {
        // Feed data to the ComboBox as requested
        accountTypeBox.getItems().addAll("Checking Account", "Savings Account", "Home Loan", "Car Loan");
        AccountService.applySanitizer(errorLabel, nameField, passwordField, balanceField);
    }

    @FXML
    public void handleCreateAccount(ActionEvent actionEvent) {
        try {
            // 1. DATA VALIDATION
            if(accountTypeBox.getValue() == null){
                System.out.println("Error: Please choose the account type.");
                errorLabel.setText("Error: Please choose the account type.");
                return;

            }
            if ( nameField== null ||
                nameField.getText().isBlank()||passwordField== null ||
                passwordField.getText().isBlank()  ) {
                System.out.println("Error: Please fill in all String fields.");
                errorLabel.setText("Error: Please fill in all String fields.");
                return;
            }

            // 2. EXTRACT SHARED DATA
            String name = nameField.getText();
            String password = passwordField.getText();
            double balance = Double.parseDouble(balanceField.getText());
            String selectedType = accountTypeBox.getValue();

            BankAccount newAccount = null;

            // 3. OBJECT CREATION BASED ON TYPE
            switch (selectedType) {
                case "Checking Account":
                    TextField overdraftLimitField = activeFields.get("overdraftLimit");

                    double overdraft = Double.parseDouble(overdraftLimitField.getText());

                    // Matches constructor: (id, password, name, balance, overdraft)
                    newAccount = new CheckingAccount(password, name, balance, overdraft);
                    break;

                case "Savings Account":
                    TextField interestRateField = activeFields.get("interestRate");

                    double rate = Double.parseDouble(interestRateField.getText());

                    // Matches constructor: (id, password, name, balance, interestRate)
                    newAccount = new SavingsAccount(password, name, balance, rate);
                    break;

                case "Home Loan":

                    TextField loanAmountField = activeFields.get("loanAmount");
                    TextField remainingAmountField = activeFields.get("remainingAmount");
                    TextField propertyAddressField = activeFields.get("propertyAddress");
                    if (propertyAddressField== null || propertyAddressField.getText().isBlank()) {
                        System.out.println("Error: Please fill in all String fields.");
                        errorLabel.setText("Error: Please fill in all String fields.");
                        return;
                    }

                    double homeAmt = Double.parseDouble(loanAmountField.getText());
                    double homeRem = Double.parseDouble(remainingAmountField.getText());
                    String address = propertyAddressField.getText();
                    if (homeRem>homeAmt){
                        errorLabel.setText("remaining amount can't be more than loan amount");
                        System.out.println("remaining amount can't be more than loan amount");
                    }
                    // Matches constructor: (id, password, name, balance, loanAmt, remainAmt, address)
                    newAccount = new HomeLoan(password, name, balance, homeAmt, homeRem, address);
                    break;

                case "Car Loan":
                     loanAmountField = activeFields.get("loanAmount");
                     remainingAmountField = activeFields.get("remainingAmount");
                    TextField carModelField = activeFields.get("carModel");
                    if (carModelField== null || carModelField.getText().isBlank()) {
                        System.out.println("Error: Please fill in all String fields.");
                        errorLabel.setText("Error: Please fill in all String fields.");
                        return;
                    }

                    double carAmt = Double.parseDouble(loanAmountField.getText());
                    double carRem = Double.parseDouble(remainingAmountField.getText());
                    String model = carModelField.getText();
                    if (carRem>carAmt){
                        errorLabel.setText("remaining amount can't be more than loan amount");
                        System.out.println("remaining amount can't be more than loan amount");
                        return;
                    }


                    // Matches constructor: (id, password, name, balance, loanAmt, remainAmt, model)
                    newAccount = new CarLoan( password, name, balance, carAmt, carRem, model);
                    break;
            }

            // 4. SAVE AND CLOSE
            if (newAccount != null) {
                boolean success = AccountService.createAccount(newAccount);
                if (success) {
                    System.out.println("Created Account With Name: " + name);

                    if (DashboardController.instance != null) {
                        DashboardController.instance.loadAccountData();
                    }
                    handleCancel(actionEvent); // Closes the popup
                } else {
                    System.out.println("Failed to save the account to the database.");
                }
            }

        } catch (NumberFormatException e) {
            System.err.println("Input Error: Please check numerical inputs.");
            errorLabel.setText("Input Error: Please check numerical inputs.");
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        ReportService.closePopup(actionEvent);
    }

    public void handleTypeChange() {
        // 1. Clear the old fields
        dynamicFields.getChildren().clear();
        activeFields.clear();

        String selectedType = accountTypeBox.getValue();
        if (selectedType == null) return;

        // 2. Inject fields based on Concrete Classes
        switch (selectedType) {
            case "Checking Account":
                addField("overdraftLimit", "Overdraft Limit"); //
                break;
            case "Savings Account":
                addField("interestRate", "Interest Rate (e.g. 0.05)"); //
                break;
            case "Home Loan":
                addField("loanAmount", "Total Loan Amount");
                addField("remainingAmount", "Remaining Balance");
                addField("propertyAddress", "Property Address"); //
                break;
            case "Car Loan":
                addField("loanAmount", "Total Loan Amount");
                addField("remainingAmount", "Remaining Balance");
                addField("carModel", "Car Model"); //
                break;
        }
    }
    private void addField(String id, String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius:8;");
        AccountService.applySanitizer(errorLabel,tf);
        dynamicFields.getChildren().add(tf);
        activeFields.put(id, tf); // Save reference to read later
    }
}
