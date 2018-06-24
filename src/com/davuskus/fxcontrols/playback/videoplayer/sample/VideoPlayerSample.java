package com.davuskus.fxcontrols.playback.videoplayer.sample;

import com.davuskus.fxcontrols.playback.videoplayer.VideoPlayer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoPlayerSample extends Application implements Initializable {

    @FXML
    private AnchorPane videoPlayer;

    @FXML
    private VideoPlayer videoPlayerController;

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("video_player_sample.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        videoPlayerController.setTimeSliderBarColor("red");
        videoPlayerController.setAutoPlaying(true);
        videoPlayerController.setMedia(getClass().getResource("video_player_sample_video.mp4"));

    }

    public static void main(String[] args) {
        launch(args);
    }

}
