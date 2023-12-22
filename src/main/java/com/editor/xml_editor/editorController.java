package com.editor.xml_editor;

import com.editor.Json.XML2JSON;
import com.editor.Json.XMLTree;
import com.editor.data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    public Button visualizeGraphButton;
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

    List<User> testUsers;
    List<String> xmlUsers;


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
        xmlUsers = parser.getUserList();

/*
        for (User user : users) {
            user.setFollowers(users);
        }

        System.out.println(users.get(0).getFollowers() );

*/


//        setUsersSample(); // setting the users list to test visualising


        Undo_Redo.puch_Stack(fileContent);
    }
    private void setUsersSample (){
        testUsers = new ArrayList<>();

        for(String userData : xmlUsers) {
            User user = new User();
            user.setUserXML(userData);
            user.parseData();
            testUsers.add(user);
        }

        for (User user : testUsers) {
            user.setFollowers(testUsers);
        }


        // Create a sample of users for visualization
//        User user1 = new User();
//        user1.setId(1);
//        user1.setUsername("User1");
//
//        User user2 = new User();
//        user2.setId(2);
//        user2.setUsername("User2");
//
//        User user3 = new User();
//        user3.setId(3);
//        user3.setUsername("User3");
//
//        User user4 = new User();
//        user4.setId(4);
//        user4.setUsername("User4");
//
//        User user5 = new User();
//        user5.setId(5);
//        user5.setUsername("User5");
//
//
//        User user6 = new User();
//        user6.setId(6);
//        user6.setUsername("User6");
//
//
//        User user7 = new User();
//        user7.setId(7);
//        user7.setUsername("User7");
//
//
//        User user8 = new User();
//        user8.setId(8);
//        user8.setUsername("User8");
//
//
//        // Establish some connections in the sample
//        user1.getFollowers().add(user2);
//        user2.getFollowers().add(user1);
//        user3.getFollowers().add(user2);
//        user3.getFollowers().add(user1);
//        user4.getFollowers().add(user2);
//
//        // Add the sample users to the users list
//        testUsers.add(user1);
//        testUsers.add(user2);
//      testUsers.add(user3);
//        testUsers.add(user4);
//        testUsers.add(user5);
      /*  testUsers.add(user6);
        testUsers.add(user7);
        testUsers.add(user8);
        for (int i=1 ; i<5 ; i++)
        testUsers.add(new User());
*/
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
        xmlUsers = new ArrayList<>();
        xmlUsers = parser.getUserList();

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
        return xmlBuilder.toString();
    }

    private Pane graphPane;
    public void VisualizeMyGraph(ActionEvent actionEvent) {
        setUsersSample();
        // Create a new stage for visualization
        Stage graphStage = new Stage();
        graphStage.setTitle("Social Network Graph Visualization");

        // Set dimensions for the stage (you can adjust these values)
        double sceneWidth = 800;
        double sceneHeight = 600;
            //graphStage.setMaximized(true);
        // Create a new Pane for the graph
        Pane graphScenePane = new Pane();

        // Set the scene for the stage
        Scene graphScene = new Scene(graphScenePane, sceneWidth, sceneHeight);

        // Set the scene for the stage
        graphStage.setScene(graphScene);

        // Show the stage
        graphStage.show();

        // Visualize the graph within the created stage
        visualizeGraph(testUsers, graphScenePane, sceneWidth, sceneHeight);
    }



    public void visualizeGraph(List<User> userList, Pane graphScenePane, double sceneWidth, double sceneHeight) {
        // Clear previous graph if any
        graphScenePane.getChildren().clear();

        // Draw nodes and connections
        double nodeRadius = 20;

        double centerX = sceneWidth / 2;
        double centerY = sceneHeight / 2;

        int numberOfUsers = userList.size();

        // Calculate the maximum number of nodes that can be accommodated in the scene
        double maxNodesHorizontal = sceneWidth / (2 * nodeRadius);
        double maxNodesVertical = sceneHeight / (2 * nodeRadius);
        double maxNodes = Math.min(maxNodesHorizontal, maxNodesVertical);

        // Set a default radius based on the maximum number of nodes
        double radiusMultiplier = Math.max(1, numberOfUsers / maxNodes);

        double angleBetweenUsers = 360.0 / numberOfUsers;
        double currentAngle = 0;

        // Calculate and set coordinates for each user
        for (User currentUser : userList) {
            double x = centerX + Math.cos(Math.toRadians(currentAngle)) * (sceneWidth / 4) * radiusMultiplier;
            double y = centerY + Math.sin(Math.toRadians(currentAngle)) * (sceneHeight / 4) * radiusMultiplier;

            currentUser.setCoordinates(x, y);

            Circle userNode = new Circle(x, y, nodeRadius);
            userNode.setCenterX(x);
            userNode.setCenterY(y);
            graphScenePane.getChildren().add(userNode);

            // Add user ID text in the middle of the circle
            Text userIdText = new Text(Integer.toString(currentUser.getId()));
            userIdText.setFill(Color.WHITE);
            userIdText.setX(x - 5); // Adjust the text position based on the circle's center
            userIdText.setY(y + 5);
            graphScenePane.getChildren().add(userIdText);

            currentAngle += angleBetweenUsers;
        }

        // Draw connections using stored coordinates
        for (User currentUser : userList) {
            double x = currentUser.getX();
            double y = currentUser.getY();

            for (User follower : currentUser.getFollowers()) {
                double followerX = follower.getX();
                double followerY = follower.getY();

                // Calculate the angle and distance to find the nearest point on the followed user's circle
                double angle = Math.atan2(y - followerY, x - followerX);
                double nearestXCurrentUser = x - nodeRadius * Math.cos(angle);
                double nearestYCurrentUser = y - nodeRadius * Math.sin(angle);

                // Calculate the angle and distance to find the nearest point on the follower's circle
                double angleFollower = Math.atan2(currentUser.getY() - followerY, currentUser.getX() - followerX);
                double nearestXFollower = followerX + nodeRadius * Math.cos(angleFollower);
                double nearestYFollower = followerY + nodeRadius * Math.sin(angleFollower);

                // Draw arrowed line from follower's nearest point to the current user's nearest point
                Line connection = new Line(nearestXFollower, nearestYFollower, nearestXCurrentUser, nearestYCurrentUser);
                connection.setStroke(Color.BLUE);
                graphScenePane.getChildren().add(connection);

                // Draw arrowhead in red at the nearest point of the current user
                addArrowHead(graphScenePane,  followerX, followerY,nearestXCurrentUser, nearestYCurrentUser, 10, Color.RED);
            }
        }
    }

    private void addArrowHead(Pane pane, double startX, double startY, double endX, double endY, double size, Color color) {
        double angle = Math.atan2(endY - startY, endX - startX);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // Create arrowhead polygon
        double arrowX1 = endX - size * (cos + sin);
        double arrowY1 = endY - size * (sin - cos);
        double arrowX2 = endX;
        double arrowY2 = endY;
        double arrowX3 = endX - size * (cos - sin);
        double arrowY3 = endY - size * (sin + cos);

        Polygon arrowhead = new Polygon(arrowX1, arrowY1, arrowX2, arrowY2, arrowX3, arrowY3);
        arrowhead.setFill(color);

        pane.getChildren().add(arrowhead);
    }


}

