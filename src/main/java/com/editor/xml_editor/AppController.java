package com.editor.xml_editor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AppController  {

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
    private TextArea inputText;
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
/*
    private void openFullScreenStage(String fileContent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
            Parent root = loader.load();

            // Accessing the controller directly
            AppController controller = loader.getController();

            // Assuming that TextArea in editor.fxml has the fx:id "inputText"
            controller.inputText.setText(fileContent);


                Stage fullScreenStage = new Stage();
                fullScreenStage.setFullScreen(true);
                fullScreenStage.setScene(new Scene(root));
                fullScreenStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

*/

    private void openFullScreenStage(String fileContent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editor.fxml"));
            loader.setClassLoader(getClass().getClassLoader());
            Parent root = loader.load();

            // Accessing the controller directly
            AppController controller = loader.getController();

            // Assuming that TextArea in editor.fxml has the fx:id "inputText"
            TextArea textArea = controller.inputText;

            if (textArea != null) {
                textArea.setText(fileContent);

                Stage fullScreenStage = new Stage();
                fullScreenStage.setFullScreen(true);
                fullScreenStage.setScene(new Scene(root));
                fullScreenStage.show();
            } else {
                System.err.println("TextArea is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
