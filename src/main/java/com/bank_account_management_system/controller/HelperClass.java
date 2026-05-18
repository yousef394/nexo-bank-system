package com.bank_account_management_system.controller;

import com.bank_account_management_system.Repository.*;
import com.bank_account_management_system.app.MainApplication;
import com.bank_account_management_system.model.BankAccount;
import com.bank_account_management_system.model.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.bank_account_management_system.Repository.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

public class HelperClass {
    public static void applySanitizer(Label errorLabel, TextField... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.contains("#//#")) {
                    field.setText(oldValue); // Revert to what it was before the illegal char
                    errorLabel.setText("The sequence '#//#' is reserved for system use.");
                }
            });
        }
    }
    static public void changeScene(String toPage, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/bank_account_management_system/view/"+toPage));
        Parent root = loader.load();

        // Get the current Stage from the button click
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    static public void openPopup(String toPage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/bank_account_management_system/view/"+toPage));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.setTitle("Open New Account");

        // Make it 'Modal' (blocks the main window)
        popupStage.initModality(Modality.APPLICATION_MODAL);

        popupStage.setScene(new Scene(root, 350, 400));
        popupStage.show();
    }

    public static void openActionPopup(String fxmlPath, BankAccount selectedAccount) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/bank_account_management_system/view/" + fxmlPath));
        Parent root = loader.load();

        // Get the controller of the window we just loaded
        Object controller = loader.getController();

        // Push the selected row's data into the controller
        if (controller instanceof DepositController) {
            ((DepositController) controller).initData(selectedAccount);
        } else if (controller instanceof WithdrawController) {
            ((WithdrawController) controller).initData(selectedAccount);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


    @FXML
    static public void closePopup(ActionEvent event) {
        // Get the stage from the 'Cancel' button and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }





    // Repositories needed for real data
    private static final TransactionRepository transactionRepo = new TransactionRepository();
    private static final CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
    private static final SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
    private static final CarLoanRepository carLoanRepo = new CarLoanRepository();
    private static final HomeLoanRepository homeLoanRepo = new HomeLoanRepository();
    public static void showBarChart(BarChart reportBarChart, PieChart reportPieChart, TextArea reportArea) {
        // 1. Toggle visibility and layout management
        reportBarChart.setVisible(true);
        reportBarChart.setManaged(true);
        reportPieChart.setVisible(false);
        reportPieChart.setManaged(false);

        // 2. Clear previous data to prevent overlapping
        reportBarChart.getData().clear();

        // 3. Create a series and add data using standard JavaFX objects
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Transactions");

        // 1. Get all transactions from the physical file
        ArrayList<Transaction> allTransactions = transactionRepo.getAll();

        // 2. Count transactions per account using a Map
        Map<Integer, Integer> counts = new HashMap<>();
        for (Transaction t : allTransactions) {
            counts.put(t.getAccountId(), counts.getOrDefault(t.getAccountId(), 0) + 1);
        }
// 2. THIS WAS MISSING: Fill the series with data from the Map
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            // String.valueOf(entry.getKey()) handles the X-axis (Account ID)
            // entry.getValue() handles the Y-axis (Number of transactions)
            series.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
        }
// 3. Populate the Bar Chart and the History Details
        StringBuilder details = new StringBuilder("--- Recent Transaction History ---\n");
        details.append(String.format("%-10s | %-10s | %-10s | %-20s\n", "ID", "Type", "Amount", "Date"));
        details.append("------------------------------------------------------------\n");

// Sort transactions so the latest ones appear first in the text area
        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        for (Transaction t : allTransactions) {
            // Add data to the string details (The History)
            details.append(String.format("%-10d | %-10s | %-10.2f | %-20s\n",
                    t.getAccountId(),
                    t.getType(),
                    t.getAmount(),
                    t.getDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        }
        reportBarChart.getData().add(series);
        reportArea.setText(details.toString());
    }

    public static void showPieChart(BarChart reportBarChart, PieChart reportPieChart, TextArea reportArea) {
        reportPieChart.setVisible(true);
        reportPieChart.setManaged(true);
        reportBarChart.setVisible(false);
        reportBarChart.setManaged(false);

        reportPieChart.getData().clear();

// 1. Get actual counts from all account repositories
        int checking = checkingRepo.getAll().size();
        int savings = savingsRepo.getAll().size();
        int loans = carLoanRepo.getAll().size() + homeLoanRepo.getAll().size();

        // 2. Add to PieChart
        if (checking > 0) reportPieChart.getData().add(new PieChart.Data("Checking", checking));
        if (savings > 0) reportPieChart.getData().add(new PieChart.Data("Savings", savings));
        if (loans > 0) reportPieChart.getData().add(new PieChart.Data("Loans", loans));

        // 3. Update the report text area with totals
        reportArea.setText("--- Account Distribution Report ---\n" +
                "Checking Accounts: " + checking + "\n" +
                "Savings Accounts: " + savings + "\n" +
                "Loan Accounts: " + loans + "\n" +
                "Total Accounts Managed: " + (checking + savings + loans));
    }

    public static ArrayList<Transaction> generateAccountReport(int id)  {
        return transactionRepo.getTransactionsByAccountId(id);
    }

    public static ArrayList<Transaction> generateTransactionReport() {
        return transactionRepo.getAll();
    }
}

