package com.example.riskmgmtlab3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        var rootPanel = new RootPane();
        var scene = new Scene(rootPanel);
        primaryStage.setTitle("Lab 3");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}