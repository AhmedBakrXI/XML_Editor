package com.editor.xml_editor;

import com.editor.Json.XML2JSON;
import com.editor.Json.XMLTree;
import com.editor.data.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class editorController implements Initializable {
    public static String fileContent;
    static File inputFile;
    @FXML
    protected TextArea inputText;
    @FXML
    protected TextFlow outputText;
    @FXML
    protected MenuItem loadButton;
    Parser parser;
    List<Integer> errorList;
    List<String> xmlList;

    List<User> users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputText = getTextArea();
        // Initialize the TextArea with some default text if needed
        inputText.setText("Hello World");
        outputText.setPrefWidth(Region.USE_COMPUTED_SIZE);
        parser = new Parser();
        parser.parseXML(fileContent);
        errorList = parser.correctXML();
        users = new ArrayList<>();

        List<String> xmlUsers = parser.getUserList();
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setUserXML(xmlUsers.get(i));
        }

        Undo_Redo.puch_Stack(fileContent);
    }

    private TextArea getTextArea() {
        return (TextArea) this.inputText.getParent().lookup("#inputText");
    }

    // Assuming you want to expose this TextArea


    // This TextArea is used internally

    public void setInputText(String input) {
        // Set the text of the TextArea
        inputText.setText(input);
    }


    public void consistencyCheckHandler() {
        boolean check = parser.checkConsistency(parser.getXmlParsed());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Consistency Check");
        alert.setContentText(check ? "File is Consistent" : "File is not Consistent");
        alert.showAndWait();
    }

    public void correctHandler() {
        xmlList = parser.getCorrectedXML();

        outputText.getChildren().clear();
        List<String> indent = indentList(xmlList);

        for (int i = 0; i < xmlList.size(); i++) {
            int flag = 1;
            int j;
            for (j = 0; j < errorList.size(); j++) {
                if (i == errorList.get(j)) {
                    flag = 0;
                    break;
                }
            }

            outputText.getChildren().add(new Text(indent.get(i) + xmlList.get(i)));
            if (flag == 0) {
                Text sign = new Text("\t <--- Corrected\n");
                sign.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
                sign.setFill(Color.rgb(0, 200, 0));
                outputText.getChildren().add(sign);
            } else {
                outputText.getChildren().add(new Text("\n"));
            }
        }
    }

    public void formatHandler() {
        xmlList = parser.getCorrectedXML();

        outputText.getChildren().clear();
        List<String> indentation = indentList(xmlList);
        for (int i = 0; i < xmlList.size(); i++) {
            String s = xmlList.get(i);
            outputText.getChildren().add(new Text(indentation.get(i)));
            outputText.getChildren().add(colorText(s));
            outputText.getChildren().add(new Text("\n"));
        }
    }


    public void minifyHandler() {
        List<String> list = parser.getCorrectedXML();
        String xml = Parser.listToString(list);
        String minified = Formatting.minify(xml);

        Text minifiedText = new Text(minified);
        Font font = Font.font("Consolas", 12);
        minifiedText.setFont(font);
        minifiedText.setFill(Color.rgb(200, 0, 0));


        outputText.getChildren().clear();
        outputText.getChildren().add(minifiedText);
    }

    public void xml2jsonHandler() {
        String xmlModified = list2String();
        XMLTree xmlTree = new XMLTree(xmlModified);
        XML2JSON xml2JSON = new XML2JSON(xmlTree);
        String json = xml2JSON.getJsonString();
        outputText.getChildren().clear();
        outputText.getChildren().add(new Text(json));
    }

    public void compressHandler() {
        List<String> list = parser.getCorrectedXML();
        String xml = Parser.listToString(list);

        Hufmann hufmann = new Hufmann(xml);
        String encodedString = hufmann.get_xml_encode();

        Text encodedText = new Text(encodedString);
        encodedText.setFill(Color.BLUEVIOLET);

        outputText.getChildren().clear();
        outputText.getChildren().add(encodedText);
    }

    public void decompressHandler() {
        List<String> list = parser.getCorrectedXML();
        String xml = Parser.listToString(list);

        Hufmann hufmann = new Hufmann(xml);
        String encoded = hufmann.get_xml_encode();
        Decompress decompressor = new Decompress(hufmann.getRoot());
        String decodedString = decompressor.decode(encoded);

        Text decodedText = new Text(decodedString);
        decodedText.setFill(Color.rgb(152, 0, 152));

        outputText.getChildren().clear();
        outputText.getChildren().add(decodedText);
    }

    private Text colorText(String text) {
        Text colorText = new Text(text);
        colorText.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
        if (Parser.isTag(text)) {
            colorText.setFill(Color.rgb(0, 120, 215));
        }
        return colorText;
    }


    private List<String> indentList(List<String> xml) {
        List<String> result = new ArrayList<>();
        String indent = "";
        int level = -1;
        for (int i = 0; i < xml.size(); i++) {
            String s = xml.get(i);
            if (Parser.isTag(s)) {
                if (Parser.isOpeningTag(s)) {
                    level++;
                    indent = Formatting.generateSpaces(level);
                } else if (Parser.isClosingTag(s)) {
                    level--;
                    indent = Formatting.generateSpaces(level);
                    if ((i + 1) < xml.size()) {
                        if (Parser.isTag(xml.get(i + 1))) {
                            if (!Parser.isClosingTag(xml.get(i + 1))) {
                                level--;
                            }
                        }
                    }
                }
            } else {
                level++;
                indent = Formatting.generateSpaces(level);
            }
            result.add(indent);
        }
        return result;
    }

    public void saveHandler() throws IOException {
        fileContent = inputText.getText();
        Undo_Redo.puch_Stack(fileContent);
        parser = new Parser();
        parser.parseXML(fileContent);
        errorList = parser.correctXML();
        users = new ArrayList<>();

        List<String> xmlUsers = parser.getUserList();
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setUserXML(xmlUsers.get(i));
        }

        outputText.getChildren().clear();
        outputText.getChildren().add(new Text(fileContent));
        FileHandler.writeFile(inputFile.getPath(), fileContent);
    }

    public void undoHandler() {
        String undo = Undo_Redo.Undo_is_clicked();
        if (undo != null) {
            outputText.getChildren().clear();
            outputText.getChildren().add(new Text(undo));
        }
    }

    public void redoHandler() {
        String redo = Undo_Redo.Redo_is_clicked();
        if (redo != null) {
            outputText.getChildren().clear();
            outputText.getChildren().add(new Text(redo));
        }
    }

    public void loadHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(loadButton.getParentPopup().getScene().getWindow());
        editorController.inputFile = selectedFile;
        if (selectedFile != null) {
            try {
                String fileContent = Files.readString(selectedFile.toPath());
                editorController.fileContent = fileContent;
                inputText.setText(fileContent);
                outputText.getChildren().clear();
                outputText.getChildren().add(new Text(fileContent));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String list2String() {
        List<String> list = parser.getXmlParsed();
        StringBuilder xmlBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.size() > (i + 1)) {
                if (!Parser.isTag(list.get(i + 1))) {
                    xmlBuilder.append(list.get(i))
                            .append(list.get(i + 1))
                            .append("\n");
                } else {
                    xmlBuilder.append(list.get(i)).append("\n");
                }
            }
        }
        System.out.println(xmlBuilder);
        return xmlBuilder.toString();
    }
}

