package com.example.dbapp;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        com.example.dbapp.util.DatabaseInitializer.initialize();

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("main-view.fxml"));
            javafx.scene.Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);

            primaryStage.setTitle("Autorzy i Utwory - CRUD Oracle");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}