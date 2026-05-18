package com.bank_account_management_system.service;

import com.bank_account_management_system.Repository.*;
import com.bank_account_management_system.app.MainApplication;
import com.bank_account_management_system.controller.DepositController;
import com.bank_account_management_system.controller.WithdrawController;
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
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReportService{
    // Repositories needed for real data
    private static final TransactionRepository transactionRepo = new TransactionRepository();
    private static final CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
    private static final SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
    private static final CarLoanRepository carLoanRepo = new CarLoanRepository();
    private static final HomeLoanRepository homeLoanRepo = new HomeLoanRepository();
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
        FXMLLoader loader = new FXMLLoader(ReportService.class.getResource("/com/bank_account_management_system/view/" + fxmlPath));
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

        // 3. Populate the chart with real IDs and their actual counts
        StringBuilder details = new StringBuilder("--- Transaction Activity Report ---\n");
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            series.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
            details.append("Account ID: ").append(entry.getKey())
                    .append(" | Activity: ").append(entry.getValue()).append(" transactions\n");
        }

        reportBarChart.getData().add(series);
        reportArea.setText(details.toString());    }

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

