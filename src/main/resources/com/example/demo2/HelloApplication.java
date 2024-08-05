package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        // Créer la scène avec la racine chargée depuis le FXML
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Gestion des Contacts");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}