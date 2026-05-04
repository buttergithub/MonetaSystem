package org.moneta.monetasys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/org/moneta/monetasys/moneta-view.fxml"));
        Scene scene = new Scene(loader.load(), 950, 650);

        stage.setTitle("Moneta - Secure Budget Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}