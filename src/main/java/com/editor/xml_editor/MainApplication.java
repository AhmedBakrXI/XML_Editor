package com.editor.xml_editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("app.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("XML Editor");
        stage.getIcons().add(new Image(getClass().getResource("/assets/icon.png").toExternalForm()));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}