package com.bank_account_management_system.controller;

import com.bank_account_management_system.app.MainApplication;
import com.bank_account_management_system.model.BankAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {
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
    static public FXMLLoader openPopup(String toPage, String pageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/bank_account_management_system/view/"+toPage));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.setTitle(pageTitle);

        // Make it 'Modal' (blocks the main window)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root, 350, 400));
        popupStage.show();
        return loader;
    }

    public static void openActionPopup(String toPage, String pageTitle, BankAccount selectedAccount) throws IOException {
        FXMLLoader loader = openPopup(toPage,pageTitle);
        // Get the controller of the window we just loaded
        Object controller = loader.getController();

        // Push the selected row's data into the controller
        if (controller instanceof DepositController) {
            ((DepositController) controller).initData(selectedAccount);
        } else if (controller instanceof WithdrawController) {
            ((WithdrawController) controller).initData(selectedAccount);
        } else if (controller instanceof TransferController) {
            ((TransferController) controller).initData(selectedAccount);
        }

    }
    @FXML
    static public void closePopup(ActionEvent event) {
        // Get the stage from the 'Cancel' button and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
