package com.bank_account_management_system.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public Stage stage;
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/bank_account_management_system/view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.stage=stage;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
