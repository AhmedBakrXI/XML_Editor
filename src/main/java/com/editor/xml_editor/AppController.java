package com.editor.xml_editor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
    private void handleBrowseButtonAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(BrowseButton.getScene().getWindow());
        editorController.inputFile = selectedFile;
        if (selectedFile != null) {
            try {
                String fileContent = Files.readString(selectedFile.toPath());
                editorController.fileContent = fileContent;
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
/*

    private void openFullScreenStage(String fileContent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
            Parent root = loader.load();

            // Accessing the controller of the editor.fxml
            editorController editorController1 = loader.getController();

            // Assuming that TextArea in editor.fxml has the fx:id "inputText"
            TextArea textArea = editorController1.InputText;

            if (textArea != null) {
                textArea.setText(fileContent);
                editorController1.setInputText(textArea);
                //System.out.println(fileContent);
                Stage fullScreenStage = new Stage();
                fullScreenStage.setFullScreen(true);
                fullScreenStage.setScene(new Scene(root));
                fullScreenStage.show();
            } else {
                System.err.println("TextArea in editor.fxml is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
 private void openFullScreenStage(String fileContent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
            Parent root = loader.load();

            // Accessing the controller of the editor.fxml
            editorController editorController1 = loader.getController();

            // Assuming TextArea in editor.fxml has the fx:id "inputText"
            /*TextArea textArea = editorController1.inputText;*/

            //editorController1.initialize();
            // editorController1.fakeinitialize();

            editorController1.setInputText(fileContent);



            Stage fullScreenStage = new Stage();
            fullScreenStage.getIcons().add(new Image(getClass().getResource("/assets/icon.png").toExternalForm()));
            fullScreenStage.setTitle("XML Editor");
            fullScreenStage.setFullScreen(false);
            fullScreenStage.setScene(new Scene(root));
            fullScreenStage.show();

            //System.err.println("TextArea in editor.fxml is null");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   



    @FXML
    void onCopyAndPasteButton(ActionEvent event) {
        String temp = "<users>\n\n</users>";
        editorController.fileContent = temp;
        openFullScreenStage(temp);
    }




}


