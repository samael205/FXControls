package com.davuskus.fxcontrols.playback;

import com.davuskus.utils.interfaces.IChangeListener;
import com.davuskus.utils.javafx.DurationUtils;
import com.davuskus.utils.javafx.MediaPlayerUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MediaModel {

    private MediaPlayer mediaPlayer;

    private boolean isPlaying;

    private final List<IChangeListener<Duration>> currentTimeListeners;

    private final List<IChangeListener<Boolean>> muteListeners;

    private final List<IChangeListener<MediaPlayer.Status>> statusListeners;

    private final List<Runnable> endOfMediaListeners;

    private final List<Runnable> mediaRestartListeners;

    private final List<MediaControl> mediaControls;

    public MediaModel(Media media) {
        createMediaPlayer(media);
        currentTimeListeners = new ArrayList<>(1);
        muteListeners = new ArrayList<>(1);
        statusListeners = new ArrayList<>(1);
        endOfMediaListeners = new ArrayList<>(1);
        mediaRestartListeners = new ArrayList<>(1);
        mediaControls = new ArrayList<>(1);
    }

    public void playSwitch() {

        boolean hasFinishedPlaying = hasFinishedPlayingMedia();

        if (isPlaying && !hasFinishedPlaying) {

            pauseMedia();

        } else if (hasFinishedPlaying) {

            restartMedia();

        } else {

            playMedia();

        }


    }

    public void playMedia() {
        mediaPlayer.play();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }

    public void restartMedia() {
        pauseMedia();
        MediaPlayerUtils.rewindToStart(mediaPlayer);
        mediaRestartListeners.forEach(Runnable::run);
        playMedia();
    }

    public void stopMedia() {
        mediaPlayer.stop();
    }

    public void muteSwitch() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
    }

    public boolean isMute() {
        return mediaPlayer.isMute();
    }

    public void disposeMedia() {
        mediaPlayer.dispose();
    }

    public void setCurrentTime(Duration duration) {
        mediaPlayer.seek(duration);
    }

    public void setCurrentTime(double milliseconds) {
        setCurrentTime(new Duration(milliseconds));
    }

    public void changeCurrentTime(double changeInMilliseconds) {
        setCurrentTime(mediaPlayer.getCurrentTime().toMillis() + changeInMilliseconds);
    }

    public String getCurrentTimeInText() {
        return DurationUtils.getDurationTimeInText(mediaPlayer.getCurrentTime());
    }

    public String getTotalTimeInText() {
        return DurationUtils.getDurationTimeInText(mediaPlayer.getTotalDuration());
    }

    public double getCurrentTimeInSeconds() {
        return mediaPlayer.getCurrentTime().toSeconds();
    }

    public double getTotalTimeInSeconds() {
        return mediaPlayer.getTotalDuration().toSeconds();
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public Duration getTotalTime() {
        return mediaPlayer.getTotalDuration();
    }

    public double getCurrentTimeInMilliseconds() {
        return mediaPlayer.getCurrentTime().toMillis();
    }

    public double getTotalTimeInMilliseconds() {
        return mediaPlayer.getTotalDuration().toMillis();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Media getMedia() {
        return mediaPlayer.getMedia();
    }

    public void addCurrentTimeListener(IChangeListener<Duration> currentTimeListener) {
        currentTimeListeners.add(currentTimeListener);
    }

    public void addMuteListener(IChangeListener<Boolean> muteListener) {
        muteListeners.add(muteListener);
    }

    public void addStatusListener(IChangeListener<MediaPlayer.Status> statusListener) {
        statusListeners.add(statusListener);
    }

    public void addOnEndOfMediaListener(Runnable onMediaEndListener) {
        endOfMediaListeners.add(onMediaEndListener);
    }

    public void addMediaRestartListener(Runnable mediaRestartListener) {
        mediaRestartListeners.add(mediaRestartListener);
    }

    public void addMediaControl(MediaControl mediaControl) {
        mediaControls.add(mediaControl);
    }

    public void removeCurrentTimeListener(IChangeListener<Duration> currentTimeListener) {
        currentTimeListeners.remove(currentTimeListener);
    }

    public void removeMuteListener(IChangeListener<Boolean> muteListener) {
        muteListeners.remove(muteListener);
    }

    public void removeStatusListener(IChangeListener<MediaPlayer.Status> statusListener) {
        statusListeners.remove(statusListener);
    }

    public void removeEndOfMediaListener(Runnable onMediaEndListener) {
        endOfMediaListeners.remove(onMediaEndListener);
    }

    public void removeMediaRestartListener(Runnable mediaRestartListener) {
        mediaRestartListeners.remove(mediaRestartListener);
    }

    public void removeMediaControl(MediaControl mediaControl) {
        mediaControls.remove(mediaControl);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isMediaControlHoveredOn() {

        for (MediaControl control : mediaControls) {
            if (control.isHoveredOn()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasFinishedPlayingMedia() {
        return MediaPlayerUtils.hasFinishedPlaying(mediaPlayer);
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
        mediaPlayer.setMute(volume <= 0);
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    private void createMediaPlayer(Media media) {

        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.currentTimeProperty().addListener((observable, oldDuration, currentDuration) -> {
            currentTimeListeners.forEach(listener -> listener.onChange(oldDuration, currentDuration));
        });

        mediaPlayer.statusProperty().addListener((observable, oldStatus, newStatus) -> {
            isPlaying = newStatus.equals(MediaPlayer.Status.PLAYING);
            statusListeners.forEach(listener -> listener.onChange(oldStatus, newStatus));
        });

        mediaPlayer.muteProperty().addListener((observable, wasMute, isMute) -> {
            muteListeners.forEach(listener -> listener.onChange(wasMute, isMute));
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            endOfMediaListeners.forEach(Runnable::run);
        });

    }

}
