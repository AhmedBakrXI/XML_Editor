package com.editor.xml_editor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class editorController implements Initializable {
    @FXML
    protected TextArea inputText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputText = getTextArea();
        // Initialize the TextArea with some default text if needed
        inputText.setText("Hello World");
    }

    private TextArea getTextArea() {
        return (TextArea) this.inputText.getParent().lookup("#inputText");
    }

    public void setInputText(String input) {
        // Set the text of the TextArea
        inputText.setText(input);
    }

    // Assuming you want to expose this TextArea


    // This TextArea is used internally


    @FXML
    MenuItem loadButton;

    @FXML
    private void handleLoadMenuItemAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        File selectedFile = fileChooser.showOpenDialog(loadButton.getParentPopup().getOwnerWindow());

        if (selectedFile != null) {
            try {
                String fileContent = Files.readString(selectedFile.toPath());
                setInputText(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
