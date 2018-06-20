package com.davuskus.fxcontrols.videoplayer.media.controls;

import com.davuskus.fxcontrols.videoplayer.media.MediaModel;
import javafx.util.Duration;

public abstract class MediaControl {

    protected MediaModel controlModel;

    protected boolean isHoveredOn;

    public void setMediaControlModel(MediaModel controlModel) {
        this.controlModel = controlModel;
    }

    public boolean isHoveredOn() {
        return isHoveredOn;
    }

    public Duration getCurrentTime() {
        return new Duration(controlModel.getCurrentTime().toMillis());
    }

    public String getCurrentTimeInText() {
        return controlModel.getCurrentTimeInText();
    }

}
