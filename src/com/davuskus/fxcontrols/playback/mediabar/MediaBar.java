package com.davuskus.fxcontrols.playback.mediabar;

import com.davuskus.fxcontrols.playback.MediaModel;
import com.davuskus.fxcontrols.playback.MediaControl;
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

    private final Image speakerIcon;

    private final Image muteIcon;

    private double volumeBeforeMute;

    private boolean timeSliderValueIsChanging;

    private boolean volumeSliderValueIsChanging;

    public MediaBar() {
        String iconsPackagePath = "/com/davuskus/fxcontrols/resources/icons/";
        pauseIcon = new Image(iconsPackagePath + "pause/icon_pause.png");
        playIcon = new Image(iconsPackagePath + "play/icon_play.png");
        replayIcon = new Image(iconsPackagePath + "replay/icon_replay.png");
        speakerIcon = new Image(iconsPackagePath + "speaker/icon_speaker.png");
        muteIcon = new Image(iconsPackagePath + "speaker/icon_mute.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    private void volumeSliderOnMousePressed(Event event) {
        volumeSliderValueIsChanging = true;
    }

    @FXML
    private void volumeSliderOnMouseReleased(Event event) {

        controlModel.getMediaPlayer().setMute(volumeSlider.getValue() <= 0);
        controlModel.getMediaPlayer().setVolume(volumeSlider.getValue());

        volumeSliderValueIsChanging = false;
    }

    @FXML
    private void playButtonOnAction(Event event) {
        controlModel.playSwitch();
    }

    @FXML
    private void volumeButtonOnAction(Event event) {
        controlModel.muteSwitch();
        controlModel.getMediaPlayer().setVolume(volumeSlider.getValue());
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
                volumeImageView.setImage(muteIcon);
                volumeSlider.setValue(0);

            } else {

                volumeImageView.setImage(speakerIcon);

                if (!volumeSliderValueIsChanging) {

                    if (volumeBeforeMute <= 0) {
                        volumeSlider.setValue(1);
                    } else {
                        volumeSlider.setValue(volumeBeforeMute);
                    }

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

}
