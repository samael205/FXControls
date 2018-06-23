package com.davuskus.fxcontrols.playback.videoplayer;

import com.davuskus.fxcontrols.playback.MediaControl;
import com.davuskus.fxcontrols.playback.MediaModel;
import com.davuskus.fxcontrols.playback.mediabar.video.VideoMediaBar;
import com.davuskus.utils.interfaces.ISelectionListener;
import com.davuskus.utils.javafx.AnimationCreator;
import com.davuskus.utils.runnable.DelayedRunnable;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class VideoPlayer implements Initializable, ISelectionListener<Media> {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private MediaView mediaView;

    @FXML
    private GridPane mediaControlsPane;

    @FXML
    private MediaControl mediaBarController;

    @FXML
    private MediaControl simpleControlController;

    private MediaModel controlModel;

    private boolean isAutoPlaying;

    private Timeline controlsFadeInAnimation;

    private Timeline controlsFadeOutAnimation;

    private DelayedRunnable controlsFadeOutDelayedAnimation;

    private final long fadeDurationMillis;

    private final long fadeOutDelayMillis;

    private boolean mouseHasExited;

    private boolean mediaControlsBgWasPressed;

    private final DelayedRunnable hideCursorDelayedRunnable;

    private final long hideCursorDelay;

    private boolean hasInitialized;

    public VideoPlayer() {

        fadeDurationMillis = 300;
        fadeOutDelayMillis = 2000;

        hideCursorDelay = 2000;
        hideCursorDelayedRunnable = new DelayedRunnable(() -> stackPane.setCursor(Cursor.NONE));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rootPane.widthProperty().addListener((observable, oldValue, newValue) ->
                rootPane.setClip(new Rectangle(newValue.doubleValue(), rootPane.getHeight()))
        );

        rootPane.heightProperty().addListener((observable, oldValue, newValue) ->
                rootPane.setClip(new Rectangle(rootPane.getWidth(), newValue.doubleValue()))
        );

        rootPane.setDisable(true);

        initControlsFadeAnimations();

        ((VideoMediaBar) mediaBarController).setFullscreenComponents(stackPane, rootPane);

        stackPane.setOnKeyPressed(event -> {

            if (!controlModel.isMediaControlFocused()) {
                controlModel.playSwitch();
            }

        });

        hasInitialized = true;

    }

    @FXML
    private void onMouseExited(Event event) {

        mouseHasExited = true;

        controlsFadeOutDelayedAnimation.runLater(fadeOutDelayMillis);

    }

    @FXML
    private void onMouseMoved(Event event) {

        hideCursorDelayedRunnable.stop();
        stackPane.setCursor(Cursor.DEFAULT);

        if (isInFront(mediaControlsPane)) {

            if (controlModel.isPlaying() && !controlModel.isMediaControlHoveredOn()) {

                controlsFadeOutDelayedAnimation.runLater(fadeOutDelayMillis);

            } else {

                controlsFadeOutDelayedAnimation.stop();

            }

        } else if (!mediaControlsBgWasPressed || mouseHasExited && mediaControlsBgWasPressed) {

            mediaControlsBgWasPressed = false;

            fadeInControls();

        } else if (mediaControlsBgWasPressed) {

            hideCursorDelayedRunnable.runLater(hideCursorDelay);

        }

        mouseHasExited = false;

    }

    @FXML
    private void mediaViewOnMousePressed(Event event) {

        controlModel.pauseMedia();

        fadeInControls();

    }

    @FXML
    private void mediaControlsPaneOnMousePressed(Event event) {

        mediaControlsBgWasPressed = true;

        controlsFadeOutAnimation.play();

    }

    @Override
    public void onSelection(Media media) {

        if (controlModel != null) {
            controlModel.disposeMedia();
        }

        setMedia(media);

    }

    public void setMedia(URL mediaURL) {
        setMedia(mediaURL.toExternalForm());
    }

    public void setMedia(String mediaSource) {
        setMedia(new Media(mediaSource));
    }

    public void setMedia(Media media) {

        if (controlModel != null) {
            controlModel.stopMedia();
        }

        controlModel = new MediaModel(media);

        controlModel.addStatusListener((oldStatus, newStatus) -> {

            switch (newStatus) {

                case READY:

                    controlModel.playMedia();
                    controlModel.pauseMedia();

                    rootPane.setDisable(false);

                    if (isAutoPlaying) {
                        controlModel.playSwitch();

                        if (controlsFadeOutDelayedAnimation != null) {
                            controlsFadeOutDelayedAnimation.runLater(fadeOutDelayMillis);
                        }

                    }

                    break;

                case PLAYING:

                    if (isInFront(mediaControlsPane)) {
                        controlsFadeOutDelayedAnimation.runLater(fadeOutDelayMillis);
                    }

                    break;

                case PAUSED:

                    if (isInFront(mediaView)) {
                        fadeInControls();
                    }

                    break;

            }

        });

        controlModel.addOnEndOfMediaListener(() -> {
            fadeInControls();
        });

        if (hasInitialized) {

            simpleControlController.setMediaControlModel(controlModel);
            mediaBarController.setMediaControlModel(controlModel);

            mediaView.setMediaPlayer(controlModel.getMediaPlayer());

            mediaControlsPane.setOpacity(1);
            switchView(mediaControlsPane);

        }

    }

    public void setAutoPlaying(boolean autoPlaying) {
        isAutoPlaying = autoPlaying;
    }

    public Duration getCurrentTime() {
        return new Duration(controlModel.getCurrentTime().toMillis());
    }

    public String getCurrentTimeInText() {
        return controlModel.getCurrentTimeInText();
    }

    public Media getMedia() {
        return new Media(controlModel.getMedia().getSource());
    }

    public boolean isFullscreen() {
        return ((VideoMediaBar) mediaBarController).isFullscreen();
    }

    private void initControlsFadeAnimations() {

        controlsFadeInAnimation = AnimationCreator.getOpacityChangeAnimation(
                mediaControlsPane,
                fadeDurationMillis,
                1,
                () -> {
                    mediaControlsBgWasPressed = false;
                }
        );

        controlsFadeOutAnimation = AnimationCreator.getOpacityChangeAnimation(
                mediaControlsPane,
                fadeDurationMillis,
                0,
                () -> {
                    mediaControlsPane.setDisable(true);
                    switchView(mediaView);
                    hideCursorDelayedRunnable.runLater(hideCursorDelay);
                }
        );

        controlsFadeOutDelayedAnimation = new DelayedRunnable(() -> {
            if (controlModel.isPlaying()
                    && !controlModel.isMediaControlHoveredOn()
                    && !controlModel.hasFinishedPlayingMedia()) {
                controlsFadeOutAnimation.play();
            }
        });

    }

    private void fadeInControls() {

        mediaControlsPane.setDisable(false);

        switchView(mediaControlsPane);

        controlsFadeInAnimation.play();

    }

    private void switchView(Node view) {
        int viewIndex = stackPane.getChildren().indexOf(view);
        stackPane.getChildren().get(viewIndex).toFront();
    }

    private boolean isInFront(Node node) {
        return stackPane.getChildren().get(stackPane.getChildren().size() - 1).equals(node);
    }

}
