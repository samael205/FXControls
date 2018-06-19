package com.davuskus.fxcontrols.videocard;

import com.davuskus.fxcontrols.interfaces.Action;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class VideoCard implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private MediaView thumbnailMediaView;

    @FXML
    private Button playButton;

    @FXML
    private ImageView playImageView;

    private Media videoMedia;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Rectangle clip = new Rectangle(rootPane.getPrefWidth(), rootPane.getPrefHeight());
        rootPane.setClip(clip);

        if (videoMedia != null)
            setMediaPlayer(videoMedia);

    }

    @FXML
    private void onMouseEntered(Event event) {
        playImageView.setOpacity(1);
    }

    @FXML
    private void onMouseExited(Event event) {
        playImageView.setOpacity(0.75);
    }

    public void setPlayButtonOnAction(Action<Event> playButtonOnAction) {
        playButton.setOnAction(event -> playButtonOnAction.execute(event));
    }

    public void setVideoMedia(Media videoMedia) {
        this.videoMedia = new Media(videoMedia.getSource());

        if (thumbnailMediaView != null)
            setMediaPlayer(this.videoMedia);

    }

    public Media getVideoMedia() {
        return videoMedia;
    }

    private void setMediaPlayer(Media media) {
        thumbnailMediaView.setMediaPlayer(new MediaPlayer(media));
    }

}
