package com.davuskus.fxcontrols.slider.progress;

import com.davuskus.utils.interfaces.IAction;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProgressSlider implements Initializable {

    @FXML
    private ProgressBar progressBar;

    private boolean isProgressChanging;

    private final List<IAction<MouseEvent>> onMousePressedActions;

    private final List<IAction<MouseEvent>> onMouseReleasedActions;

    public ProgressSlider() {
        onMousePressedActions = new ArrayList<>(1);
        onMouseReleasedActions = new ArrayList<>(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        mouseEventAction(event);
        onMousePressedActions.forEach(action -> action.execute(event));
    }

    @FXML
    private void onMouseReleased(MouseEvent event) {
        isProgressChanging = false;
        onMouseReleasedActions.forEach(action -> action.execute(event));
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

    public void addOnMousePressedAction(IAction<MouseEvent> action) {
        onMousePressedActions.add(action);
    }

    public void addOnMouseReleasedAction(IAction<MouseEvent> action) {
        onMouseReleasedActions.add(action);
    }

    public DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }

    public boolean isProgressChanging() {
        return isProgressChanging;
    }

    public boolean isFocused() {
        return progressBar.isFocused();
    }

    public void setBarColor(String color) {
        progressBar.setStyle("-fx-accent: " + color);
    }

    public void setBackgroundColor(String color) {
        progressBar.setStyle("-fx-control-inner-background: " + color);
    }

    private void mouseEventAction(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {

            isProgressChanging = true;
            convertPositionToProgress(event);

        }

    }

    private void convertPositionToProgress(MouseEvent event) {

        double eventX = event.getX();
        if (eventX < 0) {
            eventX = 0;
        } else if (eventX > progressBar.getWidth()) {
            eventX = progressBar.getWidth();
        }

        setProgress(eventX / progressBar.getWidth());

    }

}
