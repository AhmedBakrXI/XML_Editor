module com.editor.xml_editor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.editor.xml_editor to javafx.fxml;
    exports com.editor.xml_editor;
}