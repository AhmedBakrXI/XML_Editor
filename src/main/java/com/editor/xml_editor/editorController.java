package com.editor.xml_editor;

import com.editor.Compression.HuffmanCompression;
import com.editor.Json.XML2JSON;
import com.editor.Json.XMLTree;
import com.editor.data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
import java.util.LinkedList;
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

    List<String> xmlUsers;
    NetworkAnalysis networkAnalysis;
    private Pane graphPane;

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

        Undo_Redo.puch_Stack(fileContent);
    }

    private void setUsersSample() {
        users = new ArrayList<>();

        for (String userData : xmlUsers) {
            User user = new User();
            user.setUserXML(userData);
            user.parseData();
            users.add(user);
        }

        for (User user : users) {
            user.setFollowers(users);
        }
    }

    // Assuming you want to expose this TextArea


    // This TextArea is used internally

    private TextArea getTextArea() {
        return (TextArea) this.inputText.getParent().lookup("#inputText");
    }

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
        HuffmanCompression.compress(fileContent, new File("compressed.txt"));
        String encodedString = null;
        try {
            encodedString = FileHandler.readFile("compressed.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Text encodedText = new Text(encodedString);
        encodedText.setFill(Color.BLUEVIOLET);

        outputText.getChildren().clear();
        outputText.getChildren().add(encodedText);
    }

    public void decompressHandler() {
        List<String> list = parser.getCorrectedXML();
        String xml = Parser.listToString(list);

        HuffmanCompression.decompress(new File("compressed.txt"), new File("decompressed.txt"));
        String decodedString = null;
        try {
            decodedString = FileHandler.readFile("decompressed.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public void VisualizeMyGraph(ActionEvent actionEvent) {
        setUsersSample();
        // Create a new stage for visualization
        Stage graphStage = new Stage();
        graphStage.setTitle("Social Network Graph Visualization");

        // Set dimensions for the stage (you can adjust these values)
        double sceneWidth = 1000;
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
        visualizeGraph(users, graphScenePane, sceneWidth, sceneHeight);
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
                addArrowHead(graphScenePane, followerX, followerY, nearestXCurrentUser, nearestYCurrentUser, 10, Color.RED);
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


    public void graphHandler() {
        setUsersSample();
        Graph graph = new Graph(100);

        for (User user : users) {
            for (User follower : user.getFollowers()) {
                graph.addEdge(follower.getId(), user.getId());
            }
        }

        networkAnalysis = new NetworkAnalysis(graph);
        int mostActive = networkAnalysis.getMostActive() + 1;
        int mostInfluencer = networkAnalysis.getMostInfluencer();

        User mostActiveUser = getUserByID(mostActive);
        User mostInfluencerUser = getUserByID(mostInfluencer);

//        int mostInfluencer = networkAnalysis.getMostInfluencer();
//        User mostInfluencerUser = getUserByID(mostInfluencer);
//        System.out.println("Most Influencer: " + mostInfluencerUser.getUsername());

        // Graph window
        Stage stage = new Stage();
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Graph Window");
        stage.show();

        Font font = Font.font("Consolas", 18);
        Text mostActiveText = new Text("Most Active User: " + mostActiveUser.getUsername() + "\n");
        Text mostInfluencerText = new Text("Most Influencer: " + mostInfluencerUser.getUsername() + "\n");
        mostActiveText.setFill(Color.RED);
        mostInfluencerText.setFill(Color.GREEN);
        mostActiveText.setFont(font);
        mostInfluencerText.setFont(font);

        Text mutualText = new Text("Mutual Followers: \n" + mutualFollowersStringBuilder());
        mutualText.setFont(font);
        mutualText.setFill(Color.BLUEVIOLET);

        Text suggestText = new Text("Suggested Followers: \n" + suggestedFollowersStringBuilder());
        suggestText.setFont(font);
        suggestText.setFill(Color.ORANGE);

        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(mostActiveText,
                mostInfluencerText,
                mutualText,
                suggestText);
        pane.getChildren().add(textFlow);

    }

    private User getUserByID(int id) {
        User result = null;
        for (User user : users) {
            if (user.getId() == id) {
                result = user;
                break;
            }
        }
        return result;
    }

    private User getMostInfluencer() {
        int mostInfluencer = 0;
        User result = null;
        for (User user : users) {
            int followers = user.getFollowersID().size();
            if (mostInfluencer < followers) {
                mostInfluencer = followers;
                result = user;
            }
        }
        return result;
    }

    private String mutualFollowersStringBuilder() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                result.append(users.get(i).getUsername())
                        .append(", ")
                        .append(users.get(j).getUsername())
                        .append(": ")
                        .append(networkAnalysis.getMutualFollowers(
                                users.get(i).getId() - 1,
                                users.get(j).getId() - 1
                        ))
                        .append("\n");
            }
        }
        return result.toString();
    }

    private String suggestedFollowersStringBuilder() {
        StringBuilder result = new StringBuilder();
        for (User user : users) {
            result.append(user.getUsername())
                    .append(": ")
                    .append(networkAnalysis.getSuggestFollowers(user.getId() - 1))
                    .append("\n");
        }
        return result.toString();
    }

    public void searchPosts() {
        setUsersSample();
        Stage searchStage = new Stage();
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        row1.setSpacing(15);
        row1.setPadding(new Insets(15));
        row2.setSpacing(15);
        row2.setPadding(new Insets(15));

        Button searchTopic = new Button("Search by Topic");
        Button searchBody = new Button("Search by Body");
        row2.getChildren().addAll(searchBody, searchTopic);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(350);
        TextFlow textFlow = new TextFlow();

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(10);
        Text searchLabel = new Text("Search for: ");
        TextField searchField = new TextField("Enter body or topic");
        searchField.setPrefWidth(200);
        row1.getChildren().addAll(searchLabel, searchField);

        // Action handlers
        searchTopic.setOnAction(e -> {
            String result = "";
            String search = searchField.getText().trim();
            List<String> usersSearch = Post_search.searchForTopic(users, search);
            Text resultText = new Text();
            Color color = Color.WHITE;
            for (String r : usersSearch) {
                result += r;
                color = Color.GREEN;
            }

            if (usersSearch.size() == 0) {
                result = "Can't find any posts";
                color = Color.BLUE;
            }

            resultText.setText(result);
            resultText.setFill(color);


            textFlow.getChildren().clear();
            textFlow.getChildren().add(resultText);
        });

        searchBody.setOnAction(e -> {
            String result = "";
            String search = searchField.getText().trim();
            List<String> usersSearch = Post_search.searchForBody(users, search);
            Text resultText = new Text();
            Color color = Color.WHITE;
            for (String r : usersSearch) {
                result += r;
                color = Color.GREEN;
            }
            if (usersSearch.size() == 0) {
                result = "Can't find any posts";
                color = Color.BLUE;
            }

            resultText.setText(result);
            resultText.setFill(color);


            textFlow.getChildren().clear();
            textFlow.getChildren().add(resultText);
        });

        scrollPane.setContent(textFlow);
        vBox.getChildren().addAll(row1, row2, scrollPane);
        Scene scene = new Scene(vBox, 400, 600);
        searchStage.setScene(scene);
        searchStage.show();
    }
}
