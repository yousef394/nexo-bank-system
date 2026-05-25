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

import static com.bank_account_management_system.controller.Validation.isTextFieldValid;

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

    private ArrayList<TextField> activeFields = new ArrayList<>();

    @FXML
    public void initialize() {
        accountTypeBox.getItems().addAll("Checking Account", "Savings Account", "Home Loan", "Car Loan");
        Sanitize.applySanitizer(errorLabel, nameField, passwordField, balanceField);
    }

    @FXML
    public void handleCreateAccount(ActionEvent actionEvent) {
        try {
            // 1. DATA VALIDATION
            if(accountTypeBox.getValue() == null){
                Error.print(errorLabel,"Error: Please choose the account type.");
                return;

            }
            if ( isTextFieldValid(nameField)||isTextFieldValid(passwordField)) {
                Error.print(errorLabel, "Error: Please fill in all String fields.");
                return;
            }

            // 2. EXTRACT SHARED DATA
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            double balance = Double.parseDouble(balanceField.getText());
            String selectedType = accountTypeBox.getValue();

            BankAccount newAccount = null;

            // 3. OBJECT CREATION BASED ON TYPE (Using Array Indexes)
            switch (selectedType) {
                case "Checking Account":
                    // Index 0 is the Overdraft Limit (the first field added)
                    TextField overdraftLimitField = activeFields.getFirst();
                    double overdraft = Double.parseDouble(overdraftLimitField.getText());
                    newAccount = new CheckingAccount(password, name, balance, overdraft);
                    break;

                case "Savings Account":
                    // Index 0 is the Interest Rate (the first field added)
                    TextField interestRateField = activeFields.getFirst();
                    double rate = Double.parseDouble(interestRateField.getText());
                    newAccount = new SavingsAccount(password, name, balance, rate);
                    break;

                case "Home Loan":
                    // Order added: loanAmount (0), remainingAmount (1), propertyAddress (2)
                    TextField homeLoanAmtField = activeFields.get(0);
                    TextField homeRemAmtField = activeFields.get(1);
                    TextField propertyAddressField = activeFields.get(2);

                    if (isTextFieldValid(propertyAddressField)) {
                        Error.print(errorLabel,"Error: Please fill in all String fields.");
                        return;
                    }

                    double homeAmt = Double.parseDouble(homeLoanAmtField.getText());
                    double homeRem = Double.parseDouble(homeRemAmtField.getText());
                    String address = propertyAddressField.getText().trim();
                    if (homeRem > homeAmt){
                        Error.print(errorLabel,"Remaining amount can't be more than loan amount");
                        return;
                    }
                    newAccount = new HomeLoan(password, name, balance, homeAmt, homeRem, address);
                    break;

                case "Car Loan":
                    // Order added: loanAmount (0), remainingAmount (1), carModel (2)
                    TextField carLoanAmtField = activeFields.get(0);
                    TextField carRemAmtField = activeFields.get(1);
                    TextField carModelField = activeFields.get(2);

                    if (isTextFieldValid(carModelField)) {
                        Error.print(errorLabel,"Error: Please fill in all String fields.");
                        return;
                    }

                    double carAmt = Double.parseDouble(carLoanAmtField.getText());
                    double carRem = Double.parseDouble(carRemAmtField.getText());
                    String model = carModelField.getText().trim();
                    if (carRem > carAmt){
                        Error.print(errorLabel,"Remaining amount can't be more than loan amount");
                        return;
                    }
                    newAccount = new CarLoan( password, name, balance, carAmt, carRem, model);
                    break;
            }

            // 4. SAVE, RELOAD AND CLOSE
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
            if (newAccount == null){
                System.out.println("Failed to save the account to the database.");
                return;
            }
            boolean success = AccountService.createAccount(newAccount);

            if (!success){
                System.out.println("Failed to save the account to the database.");
                return;
            }
            if (DashboardController.instance != null) {
                DashboardController.instance.loadAccountData();
            }
            System.out.println("Created Account With Name: " + name);
            handleCancel(actionEvent);
        } catch (NumberFormatException e) {
            Error.print(errorLabel,"Input Error: Please check numerical inputs.");
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        Navigation.closePopup(actionEvent);
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
                addFields("Overdraft Limit");
                break;
            case "Savings Account":
                addFields("Interest Rate (e.g. 0.05)");
                break;
            case "Home Loan":
                addFields("Total Loan Amount","Remaining Amount","Property Address");
                break;
            case "Car Loan":
                addFields("Total Loan Amount","Remaining Amount","Car Model");
                break;
        }
    }

    private void addFields(String ...prompts) {
        for (String prompt : prompts) {
            TextField tf = new TextField();
            tf.setPromptText(prompt);
            tf.setStyle("-fx-background-radius:8;");
            Sanitize.applySanitizer(errorLabel, tf);
            dynamicFields.getChildren().add(tf);
            activeFields.add(tf); // Save to the ArrayList

        }
    }
}