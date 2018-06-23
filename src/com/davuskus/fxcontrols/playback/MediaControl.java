package com.davuskus.fxcontrols.playback;

import com.davuskus.fxcontrols.playback.MediaModel;
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

    public abstract boolean isFocused();

}
