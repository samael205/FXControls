package com.davuskus.fxcontrols.playback.mediabar;

import com.davuskus.fxcontrols.playback.MediaControl;
import com.davuskus.fxcontrols.playback.MediaModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaBar extends MediaControl implements Initializable {

    @FXML
    private ImageView playImageView;

    @FXML
    private Slider timeSlider;

    @FXML
    private Label timeLabel;

    @FXML
    private ImageView volumeImageView;

    @FXML
    private Slider volumeSlider;

    private final Image pauseIcon;

    private final Image playIcon;

    private final Image replayIcon;

    private final Image maxVolumeIcon;

    private final Image mediumVolumeIcon;

    private final Image lowVolumeIcon;

    private final Image muteIcon;

    private double volumeBeforeMute;

    private boolean timeSliderValueIsChanging;

    public MediaBar() {
        String iconsPackagePath = "/com/davuskus/fxcontrols/resources/icons/";
        pauseIcon = new Image(iconsPackagePath + "pause/icon_pause.png");
        playIcon = new Image(iconsPackagePath + "play/icon_play.png");
        replayIcon = new Image(iconsPackagePath + "replay/icon_replay.png");
        maxVolumeIcon = new Image(iconsPackagePath + "volume/icon_volume_3.png");
        mediumVolumeIcon = new Image(iconsPackagePath + "volume/icon_volume_2.png");
        lowVolumeIcon = new Image(iconsPackagePath + "volume/icon_volume_1.png");
        muteIcon = new Image(iconsPackagePath + "volume/icon_volume_0.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        volumeSlider.valueProperty().addListener((observable, oldVolume, newVolume) -> {
            controlModel.setVolume(newVolume.doubleValue());
            updateVolumeIcon(newVolume.doubleValue());
        });

    }

    @FXML
    private void timeSliderOnMousePressed(Event event) {
        timeSliderValueIsChanging = true;
    }

    @FXML
    private void timeSliderOnMouseReleased(Event event) {

        controlModel.setCurrentTime(timeSlider.getValue() * 1000);

        if (controlModel.isPlaying()) {
            playImageView.setImage(pauseIcon);
        } else {
            playImageView.setImage(playIcon);
        }

        timeSliderValueIsChanging = false;

    }

    @FXML
    private void playButtonOnAction(Event event) {
        controlModel.playSwitch();
    }

    @FXML
    private void volumeButtonOnAction(Event event) {
        controlModel.muteSwitch();
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

            if (!timeSliderValueIsChanging) {
                timeSlider.setValue(currentDuration.toSeconds());
            }

            if (controlModel.isPlaying()) {
                playImageView.setImage(pauseIcon);
            } else {
                playImageView.setImage(playIcon);
            }

            updateTimeLabel();

        });

        controlModel.addStatusListener((oldStatus, newStatus) -> {

            switch (newStatus) {

                case READY:
                    timeSlider.setMin(0);
                    timeSlider.setMax(controlModel.getTotalTimeInSeconds());
                    timeSlider.setValue(0);
                    updateTimeLabel();
                    break;

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

        controlModel.addMuteListener((wasMute, isMute) -> {

            if (isMute) {

                volumeBeforeMute = volumeSlider.getValue();
                volumeSlider.setValue(0);

            } else if (!volumeSlider.isValueChanging()) {

                if (volumeBeforeMute <= 0) {
                    volumeSlider.setValue(1);
                } else {
                    volumeSlider.setValue(volumeBeforeMute);
                }

            }

        });

        controlModel.addOnEndOfMediaListener(() -> {
            timeSlider.setValue(timeSlider.getMax());
            playImageView.setImage(replayIcon);
        });

        controlModel.addMediaRestartListener(() -> playImageView.setImage(pauseIcon));

    }

    private void updateTimeLabel() {
        timeLabel.setText(controlModel.getCurrentTimeInText() + "/" + controlModel.getTotalTimeInText());
    }

    private void updateVolumeIcon(double volumeValue) {

        if (volumeValue <= 0) {
            volumeImageView.setImage(muteIcon);
        } else if (volumeValue < 0.3) {
            volumeImageView.setImage(lowVolumeIcon);
        } else if (volumeValue < 0.70) {
            volumeImageView.setImage(mediumVolumeIcon);
        } else {
            volumeImageView.setImage(maxVolumeIcon);
        }

    }

}
