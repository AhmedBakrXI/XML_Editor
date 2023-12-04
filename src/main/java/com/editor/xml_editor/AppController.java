package com.editor.xml_editor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AppController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    @FXML
    private Button BrowseButton;

    @FXML
    private Button CopyPasteButton;

    @FXML
    private void handleBrowseButtonAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        File selectedFile = fileChooser.showOpenDialog(BrowseButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                String fileContent = Files.readString(selectedFile.toPath());
                openFullScreenStage(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openFullScreenStage(String fileContent) {
        Stage fullScreenStage = new Stage();
        fullScreenStage.setFullScreen(true);

        TextField textField = new TextField(fileContent);
        textField.setEditable(false);

        fullScreenStage.setScene(new javafx.scene.Scene(new javafx.scene.layout.StackPane(textField)));
        fullScreenStage.show();
    }


}