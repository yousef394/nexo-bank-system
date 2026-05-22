package com.bank_account_management_system.controller;
import com.bank_account_management_system.model.*;
import com.bank_account_management_system.service.AccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

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

    // THE CHANGE: Use a simple ArrayList instead of a HashMap
    private ArrayList<TextField> activeFields = new ArrayList<>();

    @FXML
    public void initialize() {
        accountTypeBox.getItems().addAll("Checking Account", "Savings Account", "Home Loan", "Car Loan");
        HelperClass.applySanitizer(errorLabel, nameField, passwordField, balanceField);
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

            // 3. OBJECT CREATION BASED ON TYPE (Using Array Indexes)
            switch (selectedType) {
                case "Checking Account":
                    // Index 0 is the Overdraft Limit (the first field added)
                    TextField overdraftLimitField = activeFields.get(0);
                    double overdraft = Double.parseDouble(overdraftLimitField.getText());
                    newAccount = new CheckingAccount(password, name, balance, overdraft);
                    break;

                case "Savings Account":
                    // Index 0 is the Interest Rate (the first field added)
                    TextField interestRateField = activeFields.get(0);
                    double rate = Double.parseDouble(interestRateField.getText());
                    newAccount = new SavingsAccount(password, name, balance, rate);
                    break;

                case "Home Loan":
                    // Order added: loanAmount (0), remainingAmount (1), propertyAddress (2)
                    TextField homeLoanAmtField = activeFields.get(0);
                    TextField homeRemAmtField = activeFields.get(1);
                    TextField propertyAddressField = activeFields.get(2);

                    if (propertyAddressField == null || propertyAddressField.getText().isBlank()) {
                        System.out.println("Error: Please fill in all String fields.");
                        errorLabel.setText("Error: Please fill in all String fields.");
                        return;
                    }

                    double homeAmt = Double.parseDouble(homeLoanAmtField.getText());
                    double homeRem = Double.parseDouble(homeRemAmtField.getText());
                    String address = propertyAddressField.getText();
                    if (homeRem > homeAmt){
                        errorLabel.setText("Remaining amount can't be more than loan amount");
                        System.out.println("Remaining amount can't be more than loan amount");
                        return;
                    }
                    newAccount = new HomeLoan(password, name, balance, homeAmt, homeRem, address);
                    break;

                case "Car Loan":
                    // Order added: loanAmount (0), remainingAmount (1), carModel (2)
                    TextField carLoanAmtField = activeFields.get(0);
                    TextField carRemAmtField = activeFields.get(1);
                    TextField carModelField = activeFields.get(2);

                    if (carModelField == null || carModelField.getText().isBlank()) {
                        System.out.println("Error: Please fill in all String fields.");
                        errorLabel.setText("Error: Please fill in all String fields.");
                        return;
                    }

                    double carAmt = Double.parseDouble(carLoanAmtField.getText());
                    double carRem = Double.parseDouble(carRemAmtField.getText());
                    String model = carModelField.getText();
                    if (carRem > carAmt){
                        errorLabel.setText("Remaining amount can't be more than loan amount");
                        System.out.println("Remaining amount can't be more than loan amount");
                        return;
                    }
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
                    handleCancel(actionEvent);
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
        HelperClass.closePopup(actionEvent);
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
                addField("Overdraft Limit");
                break;
            case "Savings Account":
                addField("Interest Rate (e.g. 0.05)");
                break;
            case "Home Loan":
                addField("Total Loan Amount");
                addField("Remaining Amount");
                addField("Property Address");
                break;
            case "Car Loan":
                addField("Total Loan Amount");
                addField("Remaining Amount");
                addField("Car Model");
                break;
        }
    }

    // THE CHANGE: Simplified addField method
    private void addField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius:8;");
        HelperClass.applySanitizer(errorLabel, tf);
        dynamicFields.getChildren().add(tf);
        activeFields.add(tf); // Save to the ArrayList
    }
}