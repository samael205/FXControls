package com.davuskus.fxcontrols.videoplayer.media.controls.big;

import com.davuskus.fxcontrols.videoplayer.media.MediaModel;
import com.davuskus.fxcontrols.videoplayer.media.controls.MediaControl;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BigMediaControl extends MediaControl implements Initializable {

    @FXML
    private ImageView playImageView;

    @FXML
    private Button rewindButton;

    @FXML
    private Button forwardButton;

    private final int rewindMillis;

    private final int forwardMillis;

    private final Image pauseIcon;

    private final Image playIcon;

    private final Image replayIcon;

    public BigMediaControl() {
        rewindMillis = 10000;
        forwardMillis = 30000;
        pauseIcon = new Image("/resources/icons/pause/icon_pause.png");
        playIcon = new Image("/resources/icons/play/icon_play.png");
        replayIcon = new Image("/resources/icons/replay/icon_replay.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rewindButton.setDisable(true);
        forwardButton.setDisable(true);
    }

    @FXML
    private void playButtonOnAction(Event event) {
        controlModel.playSwitch();
    }

    @FXML
    private void rewindButtonOnAction(Event event) {
        controlModel.changeCurrentTime(-rewindMillis);
    }

    @FXML
    private void forwardButtonOnAction(Event event) {
        controlModel.changeCurrentTime(forwardMillis);
    }

    @FXML
    private void mediaControlOnMouseEntered(Event event) {
        isHoveredOn = true;
    }

    @FXML
    private void mediaControlOnMouseExited(Event event) {
        isHoveredOn = false;
    }

    @Override
    public void setMediaControlModel(MediaModel controlModel) {
        super.setMediaControlModel(controlModel);

        controlModel.addMediaControl(this);

        controlModel.addCurrentTimeListener((oldDuration, currentDuration) -> {

            if (controlModel.isPlaying()) {
                playImageView.setImage(pauseIcon);
            } else {
                playImageView.setImage(playIcon);
            }

            updateForwardButton();
            updateRewindButton();

        });

        controlModel.addStatusListener((oldStatus, newStatus) -> {

            switch (newStatus) {

                case PLAYING:
                    playImageView.setImage(pauseIcon);
                    break;

                case PAUSED:
                    playImageView.setImage(playIcon);
                    break;

                case STOPPED:
                    playImageView.setImage(playIcon);
                    break;

            }

        });

        controlModel.addOnEndOfMediaListener(() -> {
            updateRewindButton();
            playImageView.setImage(replayIcon);
        });

        controlModel.addMediaRestartListener(() -> playImageView.setImage(pauseIcon));

    }

    private void updateForwardButton() {
        double currentTime = controlModel.getCurrentTimeInMilliseconds();
        double totalTime = controlModel.getTotalTimeInMilliseconds();
        boolean canForward = currentTime + forwardMillis <= totalTime;
        forwardButton.setDisable(!canForward);
        forwardButton.setOpacity(canForward ? 1 : 0.5);
    }

    private void updateRewindButton() {
        double currentTime = controlModel.getCurrentTimeInMilliseconds();
        boolean canRewind = currentTime - rewindMillis >= 0;
        rewindButton.setDisable(!canRewind);
        rewindButton.setOpacity(canRewind ? 1 : 0.5);
    }

}
