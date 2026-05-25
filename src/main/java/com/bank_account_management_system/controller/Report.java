package com.bank_account_management_system.controller;

import com.bank_account_management_system.Repository.*;
import com.bank_account_management_system.model.Transaction;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class Report {
    private static final TransactionRepository transactionRepo = new TransactionRepository();
    private static final CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
    private static final SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
    private static final CarLoanRepository carLoanRepo = new CarLoanRepository();
    private static final HomeLoanRepository homeLoanRepo = new HomeLoanRepository();
    public static void showBarChart(BarChart<String, Number> reportBarChart, PieChart reportPieChart, TextArea reportArea) {
        // 1. Toggle visibility and layout management
        reportBarChart.setVisible(true);
        reportBarChart.setManaged(true);
        reportPieChart.setVisible(false);
        reportPieChart.setManaged(false);
        reportBarChart.setAnimated(true);

        // 2. Clear previous data to prevent overlapping
        reportBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Transactions");

        // 3. Get all transactions from the physical file
        ArrayList<Transaction> allTransactions = transactionRepo.getAll();

        // We use two parallel lists to keep track of counts
        ArrayList<Integer> uniqueAccountIds = new ArrayList<>();
        ArrayList<Integer> transactionCounts = new ArrayList<>();

        for (Transaction t : allTransactions) {
            int accId = t.getAccountId();
            // Check if we have seen this Account ID before
            boolean found = false;
            for (int i = 0; i < uniqueAccountIds.size(); i++) {
                if (uniqueAccountIds.get(i) == accId) {
                    // We found it! Increase its count in the parallel list
                    int currentCount = transactionCounts.get(i);
                    transactionCounts.set(i, currentCount + 1);
                    found = true;
                    break;
                }
            }

            // If it is a brand-new Account ID, add it and start its count at 1
            if (!found) {
                uniqueAccountIds.add(accId);
                transactionCounts.add(1);
            }
        }

        // Fill the series with data from our parallel lists
        for (int i = 0; i < uniqueAccountIds.size(); i++) {
            String accountIdStr = String.valueOf(uniqueAccountIds.get(i));
            int count = transactionCounts.get(i);
            series.getData().add(new XYChart.Data<>(accountIdStr, count));
        }

        // 4. Populate the Bar Chart and the History Details
        StringBuilder details = new StringBuilder("--- Recent Transaction History ---\n");
        details.append(String.format("%-10s | %-10s | %-10s | %-20s\n", "ID", "Type", "Amount", "Date"));
        details.append("------------------------------------------------------------\n");

        // Sort transactions so the latest ones appear first in the text area
        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        for (Transaction t : allTransactions) {
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
        reportPieChart.setAnimated(true);
        reportBarChart.getData().clear();
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

}
