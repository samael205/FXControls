package com.davuskus.fxcontrols.slider.progress;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressSlider implements Initializable {

    @FXML
    private ProgressBar progressBar;

    public ProgressSlider() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        mouseEventAction(event);
    }

    @FXML
    private void onMouseDragged(MouseEvent event) {
        mouseEventAction(event);
    }

    public void setProgress(double value) {
        progressBar.setProgress(value);
    }

    public double getProgress() {
        return progressBar.getProgress();
    }

    private void mouseEventAction(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY))
            convertPositionToProgress(event);
    }

    private void convertPositionToProgress(MouseEvent event) {

        double eventX = event.getX();
        if (eventX < 0) {
            eventX = 0;
        }

        setProgress(eventX / progressBar.getWidth());

    }


}
