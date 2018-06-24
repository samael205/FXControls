package com.davuskus.fxcontrols.playback.mediabar;

import com.davuskus.fxcontrols.playback.MediaControl;
import com.davuskus.fxcontrols.playback.MediaModel;
import com.davuskus.fxcontrols.slider.progress.ProgressSlider;
import com.davuskus.utils.time.TimeUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaBar extends MediaControl implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private ImageView playImageView;

    @FXML
    private Button volumeButton;
    @FXML
    private ProgressSlider timeSliderController;

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

    private boolean wasPlaying;

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

        timeSliderController.addOnMousePressedAction(event -> {

            if (controlModel.isPlaying()) {

                controlModel.pauseMedia();

                wasPlaying = true;

            }

        });

        timeSliderController.addOnMouseReleasedAction(event -> {

            double newTimeMillis = getTimeSliderProgressInMillis();
            controlModel.setCurrentTime(newTimeMillis);

            if (wasPlaying) {

                wasPlaying = false;

                controlModel.playMedia();

            }

            if (controlModel.isPlaying()) {
                playImageView.setImage(pauseIcon);
            } else {
                playImageView.setImage(playIcon);
            }

        });

        timeSliderController.progressProperty().addListener((observable, oldValue, newValue) -> updateTimeLabel());

        volumeSlider.valueProperty().addListener((observable, oldVolume, newVolume) -> {

            controlModel.setVolume(newVolume.doubleValue());

            updateVolumeIcon(newVolume.doubleValue());

        });

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

            if (!timeSliderController.isProgressChanging()) {
                double progress = currentDuration.toMillis() / controlModel.getTotalTimeInMilliseconds();
                timeSliderController.setProgress(progress);
            }

            updateTimeLabel();

        });

        controlModel.addStatusListener((oldStatus, newStatus) -> {

            switch (newStatus) {

                case READY:
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
            timeSliderController.setProgress(1);
            playImageView.setImage(replayIcon);
        });

        controlModel.addMediaRestartListener(() -> playImageView.setImage(pauseIcon));

    }

    @Override
    public boolean isFocused() {
        return timeSliderController.isFocused()
                || playButton.isFocused()
                || volumeButton.isFocused()
                || volumeSlider.isFocused();
    }

    public void setTimeSliderBarColor(String color) {
        timeSliderController.setBarColor(color);
    }

    public void setTimeSliderBackgroundColor(String color) {
        timeSliderController.setBackgroundColor(color);
    }

    private void updateTimeLabel() {
        String currentTime = TimeUtils.getTimeText((int) (0.5 + getTimeSliderProgressInSeconds()));
        timeLabel.setText(currentTime + "/" + controlModel.getTotalTimeInText());
    }

    private void updateVolumeIcon(double volumeValue) {

        if (volumeValue <= 0) {
            volumeImageView.setImage(muteIcon);
        } else if (volumeValue < 0.2) {
            volumeImageView.setImage(lowVolumeIcon);
        } else if (volumeValue < 0.70) {
            volumeImageView.setImage(mediumVolumeIcon);
        } else {
            volumeImageView.setImage(maxVolumeIcon);
        }

    }

    private double getTimeSliderProgressInSeconds() {
        return timeSliderController.getProgress() * controlModel.getTotalTimeInSeconds();
    }

    private double getTimeSliderProgressInMillis() {
        return timeSliderController.getProgress() * controlModel.getTotalTimeInMilliseconds();
    }

}
