package com.davuskus.fxcontrols.searchfield;

import com.davuskus.utils.interfaces.IAction;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchField implements Initializable {

    @FXML
    private Button searchButton;

    @FXML
    private ImageView searchButtonImageView;

    @FXML
    private TextField searchBox;

    private IAction<String> searchAction;

    private boolean isSearchingImmediately;

    @FXML
    private void onEnter(Event event) {
        makeSearch();
    }

    @FXML
    private void searchButtonOnAction(Event event) {
        makeSearch();
    }

    @FXML
    private void searchButtonOnMouseEntered() {
        // TODO Change search icon
    }

    @FXML
    private void searchButtonOnMousePressed() {
        // TODO Change search icon
    }

    @FXML
    private void searchButtonOnMouseExited() {
        // TODO Change search icon
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isSearchingImmediately)
                makeSearch();
        });
    }

    public void setSearchAction(IAction<String> searchAction) {
        this.searchAction = searchAction;
    }

    public void setSearchingImmediately(boolean searchingImmediately) {
        isSearchingImmediately = searchingImmediately;
    }

    public String getText() {
        return searchBox.getText();
    }

    private void makeSearch() {
        searchAction.execute(getText());
    }

}
