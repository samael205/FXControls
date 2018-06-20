package com.davuskus.fxcontrols.playback.mediabar.video;

import com.davuskus.fxcontrols.playback.MediaControl;
import com.davuskus.fxcontrols.playback.MediaModel;
import com.davuskus.fxcontrols.playback.mediabar.MediaBar;
import com.davuskus.utils.simulator.EventSimulator;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoMediaBar extends MediaControl implements Initializable {

    @FXML
    private MediaBar mediaBarController;

    @FXML
    private ImageView fullscreenImageView;

    private final Image fullscreenIcon;

    private final Image fullscreenMinimizeIcon;

    private Stage fullscreenStage;

    private Parent fullscreenContent;

    public VideoMediaBar() {
        String iconsPackagePath = "/com/davuskus/fxcontrols/resources/icons/";
        fullscreenIcon = new Image(iconsPackagePath + "fullscreen/icon_fullscreen_white.png");
        fullscreenMinimizeIcon = new Image(iconsPackagePath + "fullscreen/icon_fullscreen_minimize_white.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void fullscreenButtonOnAction(Event event) {

        if (fullscreenStage != null && fullscreenContent != null) {

            if (fullscreenStage.isFullScreen()) {

                EventSimulator.simulateKeyEvent(KeyEvent.VK_ESCAPE);

            } else {

                Pane root = (Pane) fullscreenStage.getScene().getRoot();
                root.getChildren().add(fullscreenContent);

                fullscreenStage.setFullScreen(true);
                fullscreenStage.show();

            }

        }

    }

    @Override
    public void setMediaControlModel(MediaModel controlModel) {
        super.setMediaControlModel(controlModel);
        mediaBarController.setMediaControlModel(controlModel);
    }

    public void setFullscreenComponents(Parent content, Pane minimizedContentParent) {

        if (fullscreenStage != null && fullscreenStage.isShowing()) {
            fullscreenStage.close();
        }

        fullscreenStage = new Stage();

        fullscreenContent = content;

        Pane root = new AnchorPane();

        Scene scene = new Scene(root);

        fullscreenStage.setScene(scene);

        fullscreenStage.fullScreenProperty().addListener((observable, wasFullscreen, isFullscreen) -> {

            if (isFullscreen) {
                fullscreenImageView.setImage(fullscreenMinimizeIcon);
            } else {
                fullscreenImageView.setImage(fullscreenIcon);
                fullscreenStage.close();
            }

        });

        fullscreenStage.setOnHiding(windowEvent -> {
            minimizedContentParent.getChildren().add(0, content);
        });

    }

    public boolean isFullscreen() {
        if (fullscreenStage != null) {
            return fullscreenStage.isFullScreen();
        }
        return false;
    }

}
