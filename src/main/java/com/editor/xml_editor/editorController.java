package com.editor.xml_editor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
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



}
