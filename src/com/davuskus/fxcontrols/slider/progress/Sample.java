package com.davuskus.fxcontrols.slider.progress;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Sample extends Application implements Initializable {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static void main(String[] args) {
        launch(args);
    }

}
