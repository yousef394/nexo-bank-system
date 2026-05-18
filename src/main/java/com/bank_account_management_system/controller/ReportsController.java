package com.bank_account_management_system.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import static com.bank_account_management_system.service.ReportService.*;

public class ReportsController {
    @FXML private BarChart<String, Number> reportBarChart;
    @FXML private PieChart reportPieChart;
    @FXML private ComboBox<String> reportTypeBox;
    @FXML private TextArea reportArea;//    @FXML

    @FXML
    public void initialize() {
        // Feed data to the ComboBox as requested
        reportTypeBox.getItems().addAll("Bar Chart: Transactions", "Pie Chart: Distribution");

        // Hide charts initially or show a default
        reportBarChart.setVisible(true);
        reportPieChart.setVisible(false);
    }


    public void handleGenerateReport() {
        String selected = reportTypeBox.getValue();

        if (selected == null) {
            reportArea.setText("Please select a chart type!");
            return;
        }

        // The "UI IF Condition" logic
        if (selected.equals("Bar Chart: Transactions")) {
            showBarChart(reportBarChart, reportPieChart, reportArea);
        } else if (selected.equals("Pie Chart: Distribution")) {
            showPieChart(reportBarChart, reportPieChart, reportArea);
        }
    }
}
