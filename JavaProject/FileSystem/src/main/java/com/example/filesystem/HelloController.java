package com.example.filesystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class HelloController {
    @FXML
    private ImageView AboutUs;

    @FXML
    private ImageView FileStorage;

    @FXML
    private ImageView StartMenu;

    @FXML
    private Pane StoragePane;

    @FXML
    private ImageView Tips;

    @FXML
    private javafx.scene.control.TreeView<?> TreeView;

    @FXML
    private AnchorPane mainInterface;

    @FXML
    private ImageView main_BG;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}